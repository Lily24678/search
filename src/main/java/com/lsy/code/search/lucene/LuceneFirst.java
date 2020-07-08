package com.lsy.code.search.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneFirst {
	private String indexlibDir="F:/Demo/temp/lucene/index";//索引库的位置
	private String sourceDir="F:/Demo/temp/file/file";//原始文档的位置
	
	/**
	 * 	创建索引库
	 * @throws IOException 
	 */
	@Test
	public void createIndexlib() throws IOException {
		//指定索引库的位置
		Directory dir = FSDirectory.open(new File(indexlibDir).toPath());
		//创建IndexWriter对象
		IndexWriter indexWriter = new IndexWriter(dir, new IndexWriterConfig());//IndexWriterConfig 若不指定Analyzer,默认是StandardAnalyzer
		//使用IndexWriter对象将Document对象写入索引库，此过程进行索引创建。并将索引和Document对象写入索引库。
		writeTOIndexlib(indexWriter);
		//关闭IndexWriter对象
		indexWriter.close();
	}
	
	/**
	 *	 查询索引
	 * @throws IOException 
	 */
	@Test
	public void searchIndex() throws IOException {
		//需知道索引库的位置
		Directory dir = FSDirectory.open(new File(indexlibDir).toPath());
		//创建IndexReader对象
		IndexReader indexReader = DirectoryReader.open(dir);
		//创建IndexSearcher对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//开始执行查询,获取查询结果并对结果进行处理
		executeQuery(indexSearcher);
		//关闭IndexReader对象
		indexReader.close();
	}
	
	/**
	 *	 查看各种分词器的分词效果
	 * @throws IOException 
	 */
	@Test
	public void analyzerResult() throws IOException {
		//创建分词器
		analyzerResult(new StandardAnalyzer());
		analyzerResult(new CJKAnalyzer());
		analyzerResult(new SmartChineseAnalyzer());
		analyzerResult(new IKAnalyzer());
	}

	/**
	 * 	查看分词器的分词效果
	 * @throws IOException 
	 */
	private void analyzerResult(Analyzer analyzer) throws IOException {
		//从分析器对象中获得TokenStream对象
		TokenStream tokenStream = analyzer.tokenStream("", "荷塘月色出自朱自清。朱自清（1898年11月22日—1948年8月12日），原名自华，号秋实 [1-4]  ，后改名自清，字佩弦。原籍浙江绍兴，出生于江苏省东海县（今连云港市东海县平明镇），后随父定居扬州。中国现代散文家、诗人、学者、民主战士 。");
		//设置一个引用，引用可以有多种类型，可以是关键词的引用、偏移量的引用
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		//调用TokenStream的reset方法
		tokenStream.reset();
		//循环变量单词列表
		while (tokenStream.incrementToken()) {
			System.out.print("start->"+offsetAttribute.startOffset()+"[ "+charTermAttribute+" ]"+"end->" + offsetAttribute.endOffset());
			System.out.print("[ "+charTermAttribute+" ]");
		}
		//关闭TokenStream对象
		tokenStream.close();
		
		System.out.println();
	}
	
	
	/**
	 *	 开始执行查询,获取查询结果并对结果进行处理
	 * @param indexSearcher
	 * @throws IOException
	 */
	private void executeQuery(IndexSearcher indexSearcher) throws IOException {
		Query query = new TermQuery(new Term("name", "背影"));
		TopDocs docs = indexSearcher.search(query, 10);
		System.out.println("查询结果总记录数："  + docs.totalHits);
		System.out.println("docID\t name\t path\t length\t content");
		for (ScoreDoc scoreDoc: docs.scoreDocs) {
			//获取文档ID
			int docID = scoreDoc.doc;
			//从索引库中取文档对象
			Document doc = indexSearcher.doc(docID);
			System.out.println(docID+"\t"+doc.get("name")+"\t"+doc.get("path")+"\t"+doc.get("length")+"\t"+doc.get("content"));
		}
	}
	
	/**
	 * 	使用IndexWriter对象将Document对象写入索引库，此过程进行索引创建。并将索引和Document对象写入索引库。
	 * @param indexWriter 
	 * @param doc
	 * @throws IOException 
	 */
	private void writeTOIndexlib(IndexWriter indexWriter) throws IOException {
		//创建Field对象,并将Field对象加入添加到Document对象中
		File file = new File(sourceDir);
		if (file.exists()&&file.isDirectory()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				Document doc = new Document();
				//文件名和文件内容需要被分词及索引但不需要展示,需要展示的是文件位置和文件长度
				Field content = new TextField("content", FileUtils.readFileToString(file2, "UTF-8"), Store.NO);
				Field name = new StringField("name", file2.getName().replace(".txt", ""), Store.YES);
				Field path = new StoredField("path", file2.getAbsolutePath());
				Field length = new StoredField("length", file2.length());
				doc.add(content);
				doc.add(name);
				doc.add(path);
				doc.add(length);
				//将索引和Document对象写入索引库
				indexWriter.addDocument(doc);
			}
		}
	}
}
