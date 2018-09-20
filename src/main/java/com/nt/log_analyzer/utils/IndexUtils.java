package com.nt.log_analyzer.utils;

import java.io.IOException;
import java.nio.file.Paths;


import org.apache.ibatis.annotations.Delete;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class IndexUtils {
	
	/**
	 * 根据域和Term值删除索引
	 * @param indexPath
	 * @param Field
	 * @param value
	 * @throws IOException
	 */
	public static void deleteByFieldAndTerm(String indexPath,String Field,String value) throws IOException {
		IndexWriter indexWriter = getIndexWriter(indexPath);
		Query query = new TermQuery(new Term(Field, value));
		indexWriter.deleteDocuments(query);
		indexWriter.close();
	}
	/**
	 * 获取IndexWriter对象
	 * @param indexPath  索引路径
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter getIndexWriter(String indexPath) throws IOException {
		Directory directory = FSDirectory.open(Paths.get(indexPath));
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new IKAnalyzer());
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		return new IndexWriter(directory, indexWriterConfig);
	}
	
	/**
	 * 获取IndexSearch对象 
	 * @param indexPath  索引路径
	 * @return
	 * @throws IOException
	 */
	public static IndexSearcher getIndexSearch(String indexPath) throws IOException {
		Directory directory = FSDirectory.open(Paths.get(indexPath));
		IndexReader indexReader = DirectoryReader.open(directory);
		return new IndexSearcher(indexReader);
		
	}
	
}
