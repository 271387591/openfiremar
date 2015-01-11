package com.ozstrategy.util;

import com.ozstrategy.model.openfire.HistoryMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LuceneUtils {
    private static String INDEX_DIR = System.getProperty("index") + "/index";
    private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
    private static Integer MAX=10000000;
    private static Log log= LogFactory.getLog(LuceneUtils.class);

    synchronized public static Long addIndex(List<HistoryMessage> historyMessages) throws Exception{
        Long maxId=0L;
        File indexFile = new File(INDEX_DIR);
        if (!indexFile.exists()) {
            indexFile.mkdirs();
        }
        Directory directory = null;
        IndexWriter indexWriter = null;
        directory = FSDirectory.open(indexFile);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        if (IndexWriter.isLocked(directory)) {
            IndexWriter.unlock(directory);
        }
        indexWriter = new IndexWriter(directory, config);
        if(historyMessages!=null&&!historyMessages.isEmpty()){
            for(HistoryMessage history:historyMessages){
                Document document = new Document();
                Long id = history.getId();
                document.add(new LongField("id", history.getId(), Field.Store.YES));
                String message = history.getMessage();
                if (StringUtils.isNotEmpty(message)) {
                    document.add(new StringField("message", message, Field.Store.YES));
                }
                Date createDate=history.getCreateDate();
                if(createDate!=null){
                    document.add(new LongField("createDate",createDate.getTime(),Field.Store.YES));
                }
                String fromNick=history.getFromNick();
                if(StringUtils.isNotEmpty(fromNick)){
                    document.add(new StringField("fromNick",fromNick,Field.Store.YES));
                }
                String toNick=history.getToNick();
                if(StringUtils.isNotEmpty(toNick)){
                    document.add(new StringField("toNick",toNick,Field.Store.YES));
                }
                indexWriter.addDocument(document);
                maxId=id;
            }
        }
        try {
        } finally {
            closeAll(directory, indexWriter);
        }
        return maxId;
    }

    public static void closeAll(Directory directory, IndexWriter indexWriter) {
        try {
            if (indexWriter != null) {
                indexWriter.close();
            }
            if (directory != null) {
                directory.close();
            }
        } catch (IOException e) {
        }
    }
    private  static BooleanQuery search(String message,String fromNick,String toNick,Long startTime,Long endTime) throws Exception{
        BooleanQuery query = new BooleanQuery();
        if(StringUtils.isNotEmpty(message)){
            RegexpQuery regexpQuery=new RegexpQuery(new Term("message",".*"+message+".*"));
            query.add(regexpQuery, BooleanClause.Occur.MUST);
        }
        if(StringUtils.isNotEmpty(fromNick)){
            TermQuery termQuery=new TermQuery(new Term("fromNick",fromNick));
            query.add(termQuery, BooleanClause.Occur.MUST);
        }
        if(StringUtils.isNotEmpty(toNick)){
            TermQuery termQuery=new TermQuery(new Term("toNick",toNick));
            query.add(termQuery, BooleanClause.Occur.MUST);
        }
        if(startTime!=null){
            if(endTime==null){
                endTime=new Date().getTime();
            }
            NumericRangeQuery numericRangeQuery=NumericRangeQuery.newLongRange("createDate", startTime, endTime, true, false);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        
        return query;
    }

    public static List<HistoryMessage> search(String message,String fromNick,String toNick,Long startTime,Long endTime, int pageSize, int curPage) throws Exception {
        Directory directory = FSDirectory.open(new File(INDEX_DIR));
        IndexReader ir = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(ir);
        BooleanQuery query=search(message,fromNick,toNick,startTime,endTime);
        BooleanClause[] clauses = query.getClauses();
        if(clauses!=null && clauses.length<1){
            query.add(new MatchAllDocsQuery(),BooleanClause.Occur.MUST);
        }
        Sort sort = new Sort(new SortField("createDate", SortField.Type.LONG,true));
        
        TopDocs topDocs = indexSearcher.search(query, curPage * pageSize,sort);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        int begin = pageSize * (curPage - 1);
        int end = Math.min(begin + pageSize, scoreDocs.length);
        List<HistoryMessage> historyMessages=new ArrayList<HistoryMessage>();
        for(int i=begin;i<end;i++){
            HistoryMessage historyMessage=new HistoryMessage();
            Document doc = indexSearcher.doc(scoreDocs[i].doc);
            String string = doc.get("id");
            Long id= NumberUtils.toLong(string);
            historyMessage.setId(id);
            historyMessage.setFromNick(doc.get("fromNick"));
            historyMessage.setToNick(doc.get("toNick"));
            Long createDate=NumberUtils.toLong(doc.get("createDate"));
            Date date=new Date(createDate);
            historyMessage.setCreateDate(date);
            historyMessage.setMessage(doc.get("message"));
            historyMessages.add(historyMessage);
        }
        try {
        } finally {
            closeAll(directory, null);
        }
        return historyMessages;
    }
    public static Integer count(String message,String fromNick,String toNick,Long startTime,Long endTime) throws Exception{
        Directory directory = FSDirectory.open(new File(INDEX_DIR));
        IndexReader ir = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(ir);
        BooleanQuery query=search(message, fromNick, toNick, startTime, endTime);
        BooleanClause[] clauses = query.getClauses();
        if(clauses!=null && clauses.length<1){
            query.add(new MatchAllDocsQuery(),BooleanClause.Occur.MUST);
        }
        TopDocs topDocs = indexSearcher.search(query, MAX);
        return topDocs.totalHits;
    }
    public static void deleteIndex(Long startTime,Long endTime) throws Exception{
        Directory dir = FSDirectory.open(new File(INDEX_DIR));
        BooleanQuery query = new BooleanQuery();
        NumericRangeQuery numericRangeQuery=NumericRangeQuery.newLongRange("createDate", startTime, endTime, true, true);
        query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        if (IndexWriter.isLocked(dir)) {
            IndexWriter.unlock(dir);
        }
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteDocuments(query);
        try {
        } finally {
            closeAll(dir, indexWriter);
        }
    }


    public static void main(String[] args) {
        try {
            List<HistoryMessage> historyMessages=search(null,null,null,null,null,25,1);
            for(HistoryMessage historyMessage:historyMessages){
                System.out.println(historyMessage.getCreateDate());
            }
            
            
//            LuceneUtils.index();
//            LuceneUtils.search("离开", 50, 1);
            System.out.println(INDEX_DIR);
            System.out.println(Integer.MAX_VALUE);
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}