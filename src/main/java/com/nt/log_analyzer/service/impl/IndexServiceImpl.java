package com.nt.log_analyzer.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.nt.log_analyzer.dao.FileDao;
import com.nt.log_analyzer.dao.LogModelDao;
import com.nt.log_analyzer.model.FileModel;
import com.nt.log_analyzer.model.LogModel;
import com.nt.log_analyzer.model.config.MyConfig;
import com.nt.log_analyzer.service.IndexService;
import com.nt.log_analyzer.utils.IndexUtils;
import com.nt.log_analyzer.utils.JsonUtil;
@Service
public class IndexServiceImpl implements IndexService{

	
	@Autowired
	private LogModelDao logModelDao;
	
	@Autowired
	private MyConfig myConfig;
	
	@Autowired
	private FileDao fileDao;
	//索引目录
	//private String indexPath = myConfig.getIndexPath();
	//public  String filePath = myConfig.getLogFilePath();
	
	/**
	 * 轮询日志文件
	 */
	@Override
	@Transactional
	public void poll() throws Exception {

		File sourcePath = new File(myConfig.getLogFilePath());
		File indexPath = new File(myConfig.getIndexPath());
		File[] listFiles = sourcePath.listFiles();
		for(File file : listFiles) {
			/*
			 TODO 
			 判断文件状态，
			  若新增
			   	读完文件，获取最后一行的行号，将file保存到数据库中并返回id，
			   	新建索引
			   		读文件，格式化，建索引
			  若修改
				从数据库中获取lastLine+1，从该行开始按行读，匹配，获取logModels,插入数据库，更新索引
			若未修改
				继续循环
			*/
			
			
			
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			int count = 0;
			while(br.readLine()!=null) {
				count++;
			}
			br.close();
			isr.close();
			fis.close();
			
			//判断文件状态
			//从数据库中查有没有该文件
			FileModel model = fileDao.selectByFileAbsolutePath(file.getAbsolutePath());
			
			//新增文件(数据库中没有该文件)
			if (model == null) { 
				System.out.println("新增文件"+file.getAbsolutePath());
				FileModel fileModel = new FileModel();
				fileModel.setFileAbsolutePath(file.getAbsolutePath());
				fileModel.setLastLine(count);
				fileModel.setLastModified(file.lastModified());
				fileDao.insertFileModel(fileModel);
				
				//格式化
				String[] logModelFieldNameArray = JsonUtil.jsonStrToStringArray(myConfig.getLogParameter());
				List<LogModel> logModels =logList(fileModel.getFileAbsolutePath().substring(fileModel.getFileAbsolutePath().lastIndexOf('\\')+1),1, myConfig.getLogRegex(), file.getAbsolutePath(), logModelFieldNameArray, myConfig.getDatePattern());
				//插入数据库并创建索引 
				creatIndex(logModels, fileModel);
			}else if (file.lastModified() == model.getLastModified() ) { //文件未修改
				System.out.println("当前文件未被修改"+file.getAbsolutePath());
				continue;
			}else {//文件被修改
				//FileModel fileModel = fileDao.selectByFileAbsolutePath(file.getAbsolutePath());
				int startRow = model.getLastLine()+1; 
				System.out.println("当前文件被修改，"+model.getFileAbsolutePath()+"  startRow="+startRow);
				//更新lastLine
				model.setLastLine(count);
				model.setLastModified(file.lastModified());
				fileDao.updateById(model);
				
				//格式化
				String[] logModelFieldNameArray = JsonUtil.jsonStrToStringArray(myConfig.getLogParameter());
				List<LogModel> logModels = logList(model.getFileAbsolutePath().substring(model.getFileAbsolutePath().lastIndexOf('\\')+1),startRow, myConfig.getLogRegex(), file.getAbsolutePath(), logModelFieldNameArray, myConfig.getDatePattern());
				//插入数据库并创建索引
				creatIndex(logModels, model);
			}
		}
		
		
	}
	
	/**
	 *插入数据库并创建索引
	 */
	@Override
	@Transactional
	public void creatIndex(List<LogModel> logModels,FileModel fileModel) throws Exception {
		
		IndexWriter indexWriter = IndexUtils.getIndexWriter(myConfig.getIndexPath());
	
		for (LogModel logModel : logModels) {
			
			logModelDao.insertLogModel(logModel);
			
			Document document = new Document();
			//文件相关
			//document.add(new StringField("filePath", fileModel.getFileAbsolutePath(), Store.YES));
			//document.add(new LongField("lastModified", fileModel.getLastModified(),Store.YES));
			//日志相关
			//TODO threadName、className、message 的Store.YES之后改为NO
			document.add(new IntField("logModelId", logModel.getId(),Store.YES));
			document.add(new TextField("threadName", logModel.getThreadName(),Store.YES));
			document.add(new TextField("className",logModel.getClassName(),Store.YES));
			document.add(new TextField("message", logModel.getMessage(), Store.YES));
			indexWriter.addDocument(document);
		}
		indexWriter.commit();
		indexWriter.close();
	}
	
	/**
	 * 根据threadName, className, message查
	 */
	@Override
	public List<Integer> getIdsByIndex(int resultCount,String threadName,String className,String message,String relatedType) throws Exception {
		IndexSearcher indexSearcher = IndexUtils.getIndexSearch(myConfig.getIndexPath());
		BooleanQuery booleanQuery = new BooleanQuery();
		
		
		
		if (StringUtils.isNotBlank(threadName)) {
			QueryParser queryParser = new QueryParser("threadName", new IKAnalyzer());
			Query query = queryParser.parse(threadName);
			
			//Query query = new TermQuery(new Term("threadName", threadName));
			if ("or".equals(relatedType)) {
				booleanQuery.add(query, Occur.SHOULD);
			}else if ("and".equals(relatedType)) {
				booleanQuery.add(query, Occur.MUST);
			}
			
		}
		if (StringUtils.isNotBlank(className)) {
			QueryParser queryParser = new QueryParser("className", new IKAnalyzer());
			Query query = queryParser.parse(className);
			//Query query = new TermQuery(new Term("className", className));
			if ("or".equals(relatedType)) {
				booleanQuery.add(query, Occur.SHOULD);
			}else if ("and".equals(relatedType)) {
				booleanQuery.add(query, Occur.MUST);
			}
		}
		
		if (StringUtils.isNotBlank(message)) {
			QueryParser queryParser = new QueryParser("message", new IKAnalyzer());
			Query query = queryParser.parse(message);
			//Query query = new TermQuery(new Term("message", message));
			if ("or".equals(relatedType)) {
				booleanQuery.add(query, Occur.SHOULD);
			}else if ("and".equals(relatedType)) {
				booleanQuery.add(query, Occur.MUST);
			}
		}
		
		TopDocs topDocs = indexSearcher.search(booleanQuery, resultCount);
		
		List<Integer> logModelIds = new ArrayList<>();
		
		for (ScoreDoc scoreDoc:topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			Integer logModelId = Integer.valueOf(document.get("logModelId"));
			//根据logModelId 查
			//LogModel logModel = logModelDao.selectLogModelById(logModelId);
			logModelIds.add(logModelId);
		}
		return logModelIds;
	}
	
	/**
	 * 判断文件状态
	 * @param file
	 * @return 0:新增文件(索引中没有该文件)，1：文件被修改，-1：文件未修改
	 * @throws Exception
	 */
	/*private  byte checkFileStatus(File indexPath,File file) throws Exception{
		
		if (indexPath.list().length == 0) {
			return 0;
		}
		//file
		List<String> list = selectByTermName(1, file.getAbsolutePath(), "lastModified");
		if (list.size() == 0) {
			return 0;
		}
		
		if (file.lastModified() != Long.parseLong(list.get(0))) {
			return 1;
		}else {
			return -1;
		}
	}*/
	
	//==============================================
	/**
	 * 按行读取log文件，将其格式化为LogModel类型的List
	 * @param fileName 文件名
	 * @param startRow 开始读取的行号
	 * @param regex 日志每行的正则表达式
	 * @param filePathAndName 日志文件路径+文件名
	 * @param logModelFieldNameArray LogModel的属性数组(顺序与正则表达式中的每个分组顺序一致)
	 * @param dateFormat LogModel的属性数组中Date类型的日期格式(如果有的话)
	 * @return
	 * @throws Exception
	 */
	private List<LogModel> logList(String fileName,int startRow,String regex, String filePathAndName, String[] logModelFieldNameArray,
			String dateFormat) throws Exception {

		FileInputStream fis = new FileInputStream(filePathAndName);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line = null;

		List<LogModel> logModels = new ArrayList<>();

		for (int i = 1; (line = br.readLine()) != null; i++) {
			
			if (i < startRow) {
				continue;
			}
			
			if (StringUtils.isBlank(line)) {
				continue;
			}
			
			//日志过滤
			if (logFilter(line)) {
				continue;
			}
			
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(line);

			if (matcher.matches()) {
				LogModel logModel = new LogModel();
				setLogModel(logModel, matcher, logModelFieldNameArray, dateFormat);
				logModel.setRowNumber(i);
				logModel.setFileName(fileName);
				logModels.add(logModel);
			} else {

				if (logModels.size() == 0) {
					// 新建LogModel并插入
					LogModel logModel = new LogModel();
					logModel.setRowNumber(i);
					logModel.setFileName(fileName);
					logModel.setMessage(line);
					logModels.add(logModel);
				} else {
					// 获取LOGModels最后一个元素的message 添加内容
					LogModel logModel = logModels.get(logModels.size() - 1);
					String message = logModel.getMessage();
					logModel.setMessage(message + "\n" + line);
				}
			}
		}

		br.close();
		isr.close();
		fis.close();
		return logModels;
	}
	
	private  void setLogModel(LogModel logModel,Matcher matcher, String[] logModelFieldNameArray, String dateFormat) throws Exception{
		Class clazz = logModel.getClass();
		
		for(int j = 1;j<=matcher.groupCount();j++ ) {
			String fieldValue = matcher.group(j);
			
			Field field = clazz.getDeclaredField(logModelFieldNameArray[j-1]);
			field.setAccessible(true);
			String typeName = field.getType().getSimpleName();
			
			if ("Date".equals(typeName)) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
				Date date = simpleDateFormat.parse(fieldValue);
				
				field.set(logModel, date);
				continue;
			}
			
			if ("long".equals(typeName) || "Long".equals(typeName)) {
				long l = Long.parseLong(fieldValue);
				field.set(logModel, l);
				continue;
			}
			if ("int".equals(typeName) || "Integer".equals(typeName)) {
				int z = Integer.parseInt(fieldValue);
				field.set(logModel, z);
				continue;
			}
			field.set(logModel, fieldValue);
			
		}
	}
	
	
	private boolean logFilter(String str) {
		String[] filteWorlds = JsonUtil.jsonStrToStringArray(myConfig.getFilterWords());
		for (String s : filteWorlds) {
			if(str.contains(s)) {
				return true;
			}
		}
		
		return false;
	}
	
	//=======================================================
	
	
	
	
	
	
	public List<String> selectByTermName(int count,String key,String termName) throws Exception {
		IndexSearcher indexSearcher = IndexUtils.getIndexSearch(myConfig.getIndexPath());
		Query query = new TermQuery(new Term(termName,key));
		TopDocs topDocs = indexSearcher.search(query, count);
		List<String> list = new ArrayList<>();
		for (ScoreDoc scoreDoc:topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			String str = document.get(termName);
			list.add(str);
		}
		return list;
	}
	
	/**
	 * 高亮显示查询结果XXXXXX
	 */
	@Override
	public Map<String,Object> getTopDocs(int resultCount,String key) throws Exception {
		IndexSearcher indexSearcher = IndexUtils.getIndexSearch(myConfig.getIndexPath());
		//默认查询的域
		String[] fields = {"fileName","className","message"};
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new IKAnalyzer());
		
		Query query = queryParser.parse(key);
		
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style=\"color: red;\">", "</span>");
		
		QueryScorer fileNameScorer = new QueryScorer(query);
		Highlighter fileNameHL = new Highlighter(formatter, fileNameScorer);
		
		QueryScorer messageScorer = new QueryScorer(query);
		Highlighter messageHL = new Highlighter(formatter, messageScorer);
		
		TopDocs topDocs = indexSearcher.search(query, resultCount);
		
		List<LogModel> logModels = new LinkedList<>();
		Map<String, Object> map = new HashMap<>(); 
		
		for (ScoreDoc scoreDoc:topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			
			//TODO获取查询结果logModel的ID
			int logModelId = Integer.parseInt(document.get("logModelId"));
			
			String name = document.get("fileName");
			String content = document.get("message");
			TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), scoreDoc.doc, fields[0], new IKAnalyzer());
			Fragmenter fragmenter = new SimpleSpanFragmenter(fileNameScorer);
			fileNameHL.setTextFragmenter(fragmenter);
			String hl_name = fileNameHL.getBestFragment(tokenStream, name);
			
			
			tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), scoreDoc.doc, fields[1], new IKAnalyzer());
			fragmenter = new SimpleSpanFragmenter(messageScorer);
			messageHL.setTextFragmenter(fragmenter);
			String hl_content = messageHL.getBestFragment(tokenStream, content);
			
			LogModel logModel = new LogModel();
			FileModel fileModel = new FileModel();
			
			logModel.setRowNumber(Integer.parseInt(document.get("rowNumber")));
			logModel.setTimeStamp(new Date(Long.parseLong(document.get("timeStamp"))));
			logModel.setMilliSecond(Integer.parseInt("milliSecond"));;
			logModel.setThreadName(document.get("threadName"));
			logModel.setPriority(document.get("priority"));
			logModel.setClassName(document.get("className"));
			logModel.setMessage(document.get("message"));
			
			
			
			//hl_title!=null?hl_title:title, hl_content!=null?hl_content:content
			//fileModel.setFileName(hl_name!=null?hl_name:name);
			//fileModel.setFileContent(hl_content!=null?hl_content:content);
			//logModels.add(logModel);
			
		}
		map.put("logModels", logModels);
		return map;
		
	}

	


	
	
	
}
