package com.lsy.code.search.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class LuceneSecond {
	private String indexlibDir="F:/Demo/temp/lucene/index";//索引库的位置
	private Analyzer analyzer= new SmartChineseAnalyzer();//索引和搜索时使用的分析器
	/**
	 *	 删除全部索引
	 * @throws IOException 
	 */
	@Test
	public void delAllIndex() throws IOException {
		//创建IndexWriter对象
		IndexWriter indexWriter = getIndexWriter();
		//删除全部索引
		indexWriter.deleteAll();//说明：将索引目录的索引信息全部删除，直接彻底删除，无法恢复。此方法慎用！！
		//关闭IndexWriter对象
		indexWriter.close();
	}
	
	/**
	 * 	根据查询条件删除索引
	 * @throws IOException 
	 */
	@Test
	public void delIndexByQuery() throws IOException {
		//创建IndexWriter对象
		IndexWriter indexWriter = getIndexWriter();
		//根据条件删除索引
		indexWriter.deleteDocuments(new Term("name", "荷塘月色"));
		//关闭IndexWriter对象
		indexWriter.close();
	}
	
	/**
	 * 	修改索引库
	 * @throws IOException 
	 */
	@Test
	public void updateIndexlib() throws IOException {
		//创建IndexWriter对象
		IndexWriter indexWriter = getIndexWriter();
		//修改索引库
		Document doc = new Document();
		doc.add(new StringField("name","father",Store.YES));
		doc.add(new TextField("content", "我与父亲不相见已二年余了，我最不能忘记的是他的背影。", Store.YES));
		indexWriter.updateDocument(new Term("content","父亲"), doc);
		//关闭IndexWriter对象
		indexWriter.close();
	}
	
	/**
	 * 	获取IndexWriter对象
	 * @return
	 * @throws IOException
	 */
	private IndexWriter getIndexWriter() throws IOException {
		//索引库的位置
		Directory dir = FSDirectory.open(new File(indexlibDir).toPath());
		//创建IndexWriter对象
		IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig(analyzer));//IndexWriterConfig 若不指定Analyzer,默认是StandardAnalyzer
		return indexWriter;
	}
}
