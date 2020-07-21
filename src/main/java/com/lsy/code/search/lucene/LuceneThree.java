package com.lsy.code.search.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
/**
 * Lucene的两种查询方法
 */
public class LuceneThree {
	private String indexlibDir="F:/Demo/temp/lucene/index";//索引库的位置
	private Analyzer analyzer= new SmartChineseAnalyzer();//索引和搜索时使用的分析器
	
	/**
	 * 	使用Query的子类进行查询
	 * @throws IOException 
	 */
	@Test
	public void searchIndex1() throws IOException {
		//TermQuery 不使用分析器，精确查询
		executeQuery(getIndexSearcher(), new TermQuery(new Term("content", "父亲")));
		//MatchAllDocsQuery查询索引目录中的所有文档; 查询所有的文档， 语法： *:*
		executeQuery(getIndexSearcher(), new MatchAllDocsQuery());
		//模糊查询FuzzyQuery;能够矫正的次数 只能在 0到2 之间
		executeQuery(getIndexSearcher(), new FuzzyQuery(new Term("content", "父亲"),2));
		//通配符查询WildcardQuery;*匹配多个字符,?匹配单个字符
		executeQuery(getIndexSearcher(), new WildcardQuery(new Term("content", "*父亲")));
		//根据数值范围查询
		executeQuery(getIndexSearcher(), LongPoint.newRangeQuery("size", 2000l, 4500l));
		//组合查询条件
		Builder builder = new BooleanQuery.Builder();
		builder.add(new TermQuery(new Term("content", "父亲")),Occur.MUST);
		builder.add(LongPoint.newRangeQuery("size", 2000l, 4009l),Occur.MUST);
		executeQuery(getIndexSearcher(), builder.build());
		//打印Query的查询语法
		System.out.println(builder.build());
	}
	
	/**
	 *	 使用QueryParser查询
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Test
	public void searchIndex2() throws ParseException, IOException {
		//创建一个QueryParser对象。参数1：默认搜索域 参数2：分析器对象。
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("content:父亲 name:匆匆");
		executeQuery(getIndexSearcher(), query);
		//打印Query的查询语法
		System.out.println(query);
		
		
		//指定多个默认搜索域
		String[] fields= {"name","content"};
		//创建一个QueryParser对象。参数1：默认搜索域 参数2：分析器对象
		MultiFieldQueryParser queryParser2 = new MultiFieldQueryParser(fields, analyzer);
		Query query2 = queryParser2.parse("name:父亲荷塘月色匆匆");
		executeQuery(getIndexSearcher(), query2);
		//打印Query的查询语法
		System.out.println(query2);
	}
	
	
	/**
	 * 	获取IndexSearcher对象
	 * @return
	 * @throws IOException 
	 */
	private IndexSearcher getIndexSearcher() throws IOException {
		//索引库位置
		Directory dir = FSDirectory.open(new File(indexlibDir).toPath());
		//创建IndexReader对象
		IndexReader indexReader = DirectoryReader.open(dir);
		//创建IndexSearcher对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		return indexSearcher;
		
	}
	
	/**
	 *	 开始执行查询,获取查询结果并对结果进行处理
	 * @param indexSearcher
	 * @throws IOException
	 */
	private void executeQuery(IndexSearcher indexSearcher,Query query) throws IOException {
		TopDocs docs = indexSearcher.search(query, 20);
		System.out.println("查询结果总记录数："  + docs.totalHits);
		System.out.println("docID\t name\t path\t size\t content");
		for (ScoreDoc scoreDoc: docs.scoreDocs) {
			//获取文档ID
			int docID = scoreDoc.doc;
			//从索引库中取文档对象
			Document doc = indexSearcher.doc(docID);
			System.out.println(docID+"\t"+doc.get("name")+"\t"+doc.get("path")+"\t"+doc.get("size")+"\t"+doc.get("content"));
		}
		//关闭索引库IndexReader对象
		indexSearcher.getIndexReader().close();
	}
}
