package com.nt.log_analyzer.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.search.BooleanClause.Occur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.nt.log_analyzer.dao.FileDao;
import com.nt.log_analyzer.dao.LogModelDao;
import com.nt.log_analyzer.model.FileModel;
import com.nt.log_analyzer.model.LogModel;
import com.nt.log_analyzer.model.config.Config;
import com.nt.log_analyzer.model.config.MyConfig;
import com.nt.log_analyzer.service.IndexService;
import com.nt.log_analyzer.utils.FileUtil;
import com.nt.log_analyzer.utils.IndexUtils;
import com.nt.log_analyzer.utils.JsonUtil;
import com.nt.log_analyzer.utils.YmlUtil;

@Service
public class IndexServiceImpl implements IndexService{

	private final static Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);
	@Autowired
	private LogModelDao logModelDao;
	
	
	@Autowired
	private Config config;
	
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
		//=============================================
		String indexPath = config.getIndexpath();
		List<Map<String,String[]>> configs = config.getConfigs();
		for (Map<String, String[]> map : configs) {
			File logFiles = new File(map.get("logfilePath")[0]);
			File[] listFiles = logFiles.listFiles();
			String regex =YmlUtil.ymlArrayToString(map.get("logregex"));
			String dateFormat = map.get("datePattern")[0];
			String[] filteWorlds = map.get("filterwords");
			String[] logModelFieldNameArray = map.get("logparameter");
			
			for(File file : listFiles) {
				
				int count =  FileUtil.getFileRowNumber(file);
				
				//判断文件状态
				//从数据库中查有没有该文件
				FileModel model = fileDao.selectByFileAbsolutePath(file.getAbsolutePath());
				
				//新增文件(数据库中没有该文件)
				if (model == null) { 
					logger.info("新增文件"+file.getAbsolutePath());
					FileModel fileModel = new FileModel();
					fileModel.setFileAbsolutePath(file.getAbsolutePath());
					fileModel.setLastLine(count);
					fileModel.setLastModified(file.lastModified());
					fileDao.insertFileModel(fileModel);
					
					//格式化
					
					logFileFormatToDBAndIndex(indexPath,file, 1, count, regex, logModelFieldNameArray, dateFormat,filteWorlds);
					
				}else if (file.lastModified() == model.getLastModified() ) { //文件未修改
					logger.info("当前文件未被修改"+file.getAbsolutePath());
					continue;
				}else {//文件被修改
					//FileModel fileModel = fileDao.selectByFileAbsolutePath(file.getAbsolutePath());
					int startRow = model.getLastLine()+1; 
					logger.info("当前文件被修改，"+model.getFileAbsolutePath()+"  startRow="+startRow);
					//更新lastLine
					model.setLastLine(count);
					model.setLastModified(file.lastModified());
					fileDao.updateById(model);
					
					logFileFormatToDBAndIndex(indexPath,file, startRow, count, regex, logModelFieldNameArray, dateFormat,filteWorlds);
				
				
				}
			}
			
			
			
		}
		
		//=============================================
		/*File sourcePath = new File(myConfig.getLogFilePath());
		File[] listFiles = sourcePath.listFiles();
		
		String regex = myConfig.getLogRegex();
		String dateFormat = myConfig.getDatePattern();
		String[] logModelFieldNameArray = JsonUtil.jsonStrToStringArray(myConfig.getLogParameter());*/

		
	
		
		
	}
	
	/**
	 *将logModel插入数据库并创建索引
	 */
	@Override
	public void creatIndex(List<LogModel> logModels,String indexPath){
		
		IndexWriter indexWriter = null;
		try {
			indexWriter = IndexUtils.getIndexWriter(indexPath);
				
		 		//logModelDao.insertLogModel(logModel);
				for (LogModel logModel : logModels) {
					Document document = new Document();
					//文件相关
					//document.add(new StringField("filePath", fileModel.getFileAbsolutePath(), Store.YES));
					//document.add(new LongField("lastModified", fileModel.getLastModified(),Store.YES));
					//日志相关
				
					document.add(new IntField("logModelId", logModel.getId(),Store.YES));
					document.add(new IntField("rowNumber", logModel.getRowNumber(), Store.YES));
					//==========================================================
					if (StringUtils.isNotBlank(logModel.getFileName())) {
						document.add(new TextField("fileName", logModel.getFileName(),Store.YES));
					}
					if (logModel.getTimeStamp() != null) {
						document.add(new LongField("timeStamp", logModel.getTimeStamp().getTime(), Store.YES));
					}
					
					if (logModel.getMilliSecond()!=null) {
						document.add(new StoredField("milliSecond", logModel.getMilliSecond()));
					}
					if (StringUtils.isNotBlank(logModel.getPriority())) {
						document.add(new TextField("priority", logModel.getPriority(),Store.YES));
					}
					
					//============================================================
					
					if (StringUtils.isNotBlank(logModel.getThreadName())) {
						document.add(new TextField("threadName", logModel.getThreadName(),Store.YES));
					}
					if (StringUtils.isNotBlank(logModel.getClassName())) {
						document.add(new TextField("className",logModel.getClassName(),Store.YES));
					}
					if (StringUtils.isNotBlank(logModel.getMessage())) {
						document.add(new TextField("message", logModel.getMessage(), Store.YES));
					}
					
					indexWriter.addDocument(document);
				}
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				indexWriter.commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				indexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
	}
	
	/**
	 * 通过lucene索引查
	 */
	@Override
	public Map<String,Object> selectByIndex(String indexPath,int pageIndex, int pageSize,String fileName,Date timeStamp_from, Date timeStamp_to,String priority,String threadName,String className,String message,String relatedType) throws Exception {
		IndexSearcher indexSearcher = IndexUtils.getIndexSearch(indexPath);
		BooleanQuery booleanQuery = new BooleanQuery();
	
		Occur occur = Occur.MUST;
		
		if ("or".equals(relatedType)) {
			occur = Occur.SHOULD;
		}
		if ("and".equals(relatedType)) {
			occur = Occur.MUST;
		}
		
		if (StringUtils.isNotBlank(fileName)) {
			QueryParser queryParser = new QueryParser("fileName", new IKAnalyzer());
			Query query = queryParser.parse(fileName);
			booleanQuery.add(query,occur);
		}
		
		Query timeStampQuery = NumericRangeQuery.newLongRange("timeStamp",timeStamp_from==null?null:timeStamp_from.getTime(),timeStamp_to==null?null:timeStamp_to.getTime(),true, true);
		booleanQuery.add(timeStampQuery,occur);
		
		if(StringUtils.isNotBlank(priority)) {
			QueryParser queryParser = new QueryParser("priority", new IKAnalyzer());
			Query query = queryParser.parse(priority);
			booleanQuery.add(query,occur);
		}
		
		if (StringUtils.isNotBlank(threadName)) {
			QueryParser queryParser = new QueryParser("threadName", new IKAnalyzer());
			Query query = queryParser.parse(threadName);
			booleanQuery.add(query,occur);
		}
		if (StringUtils.isNotBlank(className)) {
			QueryParser queryParser = new QueryParser("className", new IKAnalyzer());
			Query query = queryParser.parse(className);
				booleanQuery.add(query,occur);
		}
		if (StringUtils.isNotBlank(message)) {
			QueryParser queryParser = new QueryParser("message", new IKAnalyzer());
			Query query = queryParser.parse(message);
				booleanQuery.add(query,occur);
		}
		
		ScoreDoc lastSd = getLastScoreDoc(pageIndex, pageSize, booleanQuery, indexSearcher);
		TopDocs topDocs = indexSearcher.searchAfter(lastSd, booleanQuery, pageSize);
		
		Map<String,Object> map = new HashMap<>();
		List<LogModel> logModels = new LinkedList<>();
		
		if (topDocs.scoreDocs.length==0) {
			map.put("total", 0);
			map.put("rows", "");
			return map;
		}
		
		for (ScoreDoc scoreDoc:topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			LogModel logModel = new LogModel();
			logModel.setId(Integer.parseInt(document.get("logModelId")));
			logModel.setRowNumber(Integer.parseInt(document.get("rowNumber")));
			logModel.setFileName(document.get("fileName"));
			logModel.setTimeStamp(new Date(Long.parseLong(document.get("timeStamp"))));
			logModel.setMilliSecond(Integer.valueOf(document.get("milliSecond")));
			logModel.setPriority(document.get("priority"));
			logModel.setThreadName(document.get("threadName"));
			logModel.setClassName(document.get("className"));
			logModel.setMessage(document.get("message"));
			logModels.add(logModel);
		}
		map.put("total", topDocs.totalHits);
		map.put("rows", logModels);
		
		indexSearcher.getIndexReader().close();
		
		return map;
	}
	
	
	
	//==============================================
	/**
	 * 按行读取log文件，将其格式化为LogModel，并插入数据库、创建索引
	 * @param file 要格式化的文件
	 * @param startRow 开始读取的行号
	 * @param count 当前文件的总行数
	 * @param regex 日志每行的正则表达式
	 * @param logModelFieldNameArray LogModel的属性数组(顺序与正则表达式中的每个分组顺序一致)
	 * @param dateFormat LogModel的属性数组中Date类型的日期格式(如果有的话)
	 * @return
	 * @throws Exception
	 */
	@Transactional
	private void logFileFormatToDBAndIndex(String indexPath,File file,int startRow,long count,String regex,String[] logModelFieldNameArray,
			String dateFormat,String[] filteWorlds){

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(file.getAbsolutePath());
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			String line = null;
			
			
			//LogModel logModel = new LogModel();
			
			Deque<LogModel> deque = new LinkedBlockingDeque<>(11000);
			
			
			
			for (int i = 1; (line = br.readLine()) != null; i++) {
				
				if (i < startRow) {
					continue;
				}
				
				if (StringUtils.isBlank(line)) {
					continue; 
				}
				
				//日志过滤
				if (FileUtil.logFilter(line,filteWorlds)) {
					continue;
				}
				
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(line);
				
				if (matcher.matches()) {//匹配成功
					
					List<LogModel> logModels = new LinkedList<>();
					
					if (deque.size() > 10002) { //匹配成功，且队列元素大于100002
						for(int j=0;j<10000;j++) {
							LogModel firstLogModel = deque.pollFirst();
							logModels.add(firstLogModel);
						
						}
						//批量入库并创建索引
						int z = logModelDao.insertBatch(logModels);
						logger.info("批量插入了："+z+"条数据");
						creatIndex(logModels,indexPath);
			
					}
					
					LogModel logModel = new LogModel(); //新建LogModel并赋值
					logModel.setFileName(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1));
					logModel.setRowNumber(i);
					setLogModel(logModel, matcher, logModelFieldNameArray, dateFormat);
					if (!deque.offerLast(logModel)) { //匹配成功，,从队尾插入队列
						logger.debug("匹配成功，从队尾插入失败，当前行数："+i);
					}
					
				} else { //匹配不成功
					if (deque.isEmpty()) { //第一行匹配不成功
						LogModel logModel = new LogModel();
						logModel.setRowNumber(i);
						logModel.setFileName(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1));
						logModel.setMessage(line);
						//新建LogModel,从队尾插入队列
						if (!deque.offerLast(logModel)) {
							logger.debug("第一行匹配不成功，从队尾插入失败");
						}
						
						
					}else { // 非第一行匹配不成功
						LogModel lastLogModel = deque.peekLast(); //获取队尾元素，修改message
						if (lastLogModel != null) {
							String str = lastLogModel.getMessage();
							lastLogModel.setMessage(str==null?line:str+"\n"+line);
						}else {
							logger.debug("匹配不成功，获取队尾失败，当前行数："+i);
						}
						 
					}
				}
			}
			logger.info("文件读取完成，队列中还有 "+deque.size()+" 个元素");
			List<LogModel> logModels = new LinkedList<>();
			int size = deque.size();
			for(int j=0;j<size;j++) {
				LogModel firstLogModel = deque.pollFirst();
				logModels.add(firstLogModel);
			
			}
			//批量入库并创建索引
			int z = logModelDao.insertBatch(logModels);
			logger.info("批量插入了："+z+"条数据");
			creatIndex(logModels,indexPath);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				isr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
	/**
	 * 根据logModelFieldNameArray给logModel属性赋值
	 * @param logModel
	 * @param matcher
	 * @param logModelFieldNameArray
	 * @param dateFormat
	 * @throws Exception
	 */
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
	
	
	
	
	//=======================================================
	
	
	
	
	
	

	
	
	/**
	 * 根据页码和分页大小获取上一次的最后一个scoredocs
	 */
	private ScoreDoc getLastScoreDoc(int pageIndex,int pageSize,Query query,IndexSearcher searcher) throws IOException {
		if(pageIndex==1)return null;//如果是第一页就返回空
		int num = pageSize*(pageIndex-1);//获取上一页的最后数量
		TopDocs tds = searcher.search(query, num);
		return tds.scoreDocs[num-1];
	}
	
	
	/*public List<String> selectByTermName(int count,String key,String termName) throws Exception {
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
}*/
	
	/**
	 * 高亮显示查询结果XXXXXX
	 */
	/*@Override
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

	*/


	
	
	
}
