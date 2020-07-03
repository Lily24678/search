package com.lsy.code.search.lucene;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;

public class LuceneFirst {

	//创建索引库
	@Test
	public void createIndex() throws Exception {
		//1、指定索引库存放的位置，可以是内存也可以是磁盘
		Directory directory = FSDirectory.open(new File("F:/temp/lucene/index").toPath());
		//2、创建一个IndexWriter对象。需要一个分析器对象。
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);//此时在F:/temp/lucene/index下会创建文件write.lock
		//参数1：索引库存放的路径 参数2：配置信息，其中包含分析器对象
		IndexWriter indexWriter = new IndexWriter(directory, conf);
		//3、创建文档
		Document doc = new Document();
		//4、向文档中添加域
		String fileContent = FileUtils.readFileToString(new File("F:/temp/template/春.txt"), "UTF-8");
		TextField textField = new TextField("content", fileContent, Store.YES);
		doc.add(textField);
		//5、把文档对象写入索引库
		indexWriter.addDocument(doc);
		//6、关闭IndexWriter对象
		indexWriter.close();

	}
	
	//查询索引
	@Test
	public void searchIndex1() throws Exception {
		//1、知道索引库的位置
		FSDirectory directory = FSDirectory.open(new File("F:/temp/lucene/index").toPath());
		//2、使用IndexReader对象打开索引库
		IndexReader indexReader = DirectoryReader.open(directory);
		//3、创建IndexSearcher对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//4、获取查询结果
		Query query = new MatchAllDocsQuery();
		TopDocs topDocs = indexSearcher.search(query, 10);
		//5、处理查询的结果
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			//取文档id
			int id = scoreDoc.doc;
			//从索引库中取文档对象
			Document document = indexSearcher.doc(id);
			System.out.println(document.get("content"));
		}
		//6、关闭IndexReader对象
		indexReader.close();
	}
	
	//查询索引
	@Test
	public void searchIndex() throws Exception {
		//1指定索引库存放的位置
		Directory directory = FSDirectory.open(new File("F:/Demo/temp/lucene/index").toPath());
		//2使用IndexReader对象打开索引库
		IndexReader indexReader = DirectoryReader.open(directory);
		//3创建一个IndexSearcher对象，构造方法需要一个indexReader对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//4创建一个查询对象,需要指定查询域及要查询的关键字。
		//term的参数1：要搜索的域 参数2：搜索的关键字
		Query query = new TermQuery(new Term("content", "spring"));
		System.out.println(query);
		//参数1：查询条件 参数2：查询结果返回的最大值
		//5取查询结果
		TopDocs topDocs = indexSearcher.search(query, 10);
		//取查询结果总记录数
		System.out.println("查询结果总记录数："  + topDocs.totalHits);
		//6遍历查询结果并打印.
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			//取文档id
			int id = scoreDoc.doc;
			//从索引库中取文档对象
			Document document = indexSearcher.doc(id);
			//取属性
			System.out.println(document.get("name"));
			System.out.println(document.get("size"));
			System.out.println(document.get("content"));
			System.out.println(document.get("path"));
		}
		//7关闭IndexReader对象
		indexReader.close();
	}
	
	//查看分析器的分词效果
	@Test
	public void testAnanlyzer() throws Exception {
		//创建一个分析器对象
		Analyzer analyzer = new StandardAnalyzer();
		//Analyzer analyzer = new CJKAnalyzer();
//		Analyzer analyzer = new SmartChineseAnalyzer();
		//从分析器对象中获得tokenStream对象
		//参数1：域的名称，可以为null或者""
		//参数2：要分析的文本内容
		TokenStream tokenStream = analyzer.tokenStream("", "高富帅白富美蒋泽明数据库中存储的数据是高富帅结构化数据传智播客，即行数据java，可以用二维表结构来逻辑表达实现的数据。");
		//设置一个引用，引用可以有多种类型，可以是关键词的引用、偏移量的引用
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		//偏移量
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		//调用tokenStream的reset方法
		tokenStream.reset();
		//使用while循环变量单词列表
		while (tokenStream.incrementToken()) {
			System.out.println("start->" + offsetAttribute.startOffset());
			//打印单词
			System.out.println(charTermAttribute);
			System.out.println("end->" + offsetAttribute.endOffset());
		}
		//关闭tokenStream
		tokenStream.close();
	}
}

