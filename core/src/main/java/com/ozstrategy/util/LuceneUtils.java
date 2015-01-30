package com.ozstrategy.util;

import com.ozstrategy.Constants;
import com.ozstrategy.lucene.LuceneInstance;
import com.ozstrategy.model.openfire.HistoryMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FieldDoc;
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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuceneUtils {
    private static String INDEX_DIR = Constants.imDataDir + "/index";
    private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
    private static Integer MAX=80000;
    private static Log log= LogFactory.getLog(LuceneUtils.class);
    
    synchronized public static Long addIndex(ResultSet resultSet) throws Exception{
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
        while (resultSet.next()) {
            Document document = new Document();
            Long id = resultSet.getLong("id");
            document.add(new LongField("id", id, Field.Store.YES));
            String message = resultSet.getString("message");
            if (StringUtils.isNotEmpty(message)) {
                document.add(new StringField("message", message, Field.Store.YES));
            }
            Date createDate=resultSet.getDate("createDate");
            if(createDate!=null){
                document.add(new LongField("createDate",createDate.getTime(),Field.Store.YES));
            }
            String fromNick=resultSet.getString("fromNick");
            if(StringUtils.isNotEmpty(fromNick)){
                document.add(new StringField("fromNick",fromNick,Field.Store.YES));
            }
            String toNick=resultSet.getString("toNick");
            if(StringUtils.isNotEmpty(toNick)){
                document.add(new StringField("toNick",toNick,Field.Store.YES));
            }
            indexWriter.addDocument(document);
            maxId=id;
        }
        try {
        } finally {
            closeAll(directory, indexWriter);
        }
        return maxId;
    }

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
            IndexWriter.unlock(directory);
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
    private  static BooleanQuery search(Long id,String message,String fromNick,String toNick,Long toId,Long startTime,Long endTime,Long formId) throws Exception{
        BooleanQuery query = new BooleanQuery();

//        NumericRangeQuery idQuery=NumericRangeQuery.newLongRange("id",0L,minId,false,false);
//        query.add(idQuery,BooleanClause.Occur.MUST);
        
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
        if (toId != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("toId", toId, toId, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        if (id != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("id", id, id, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        if (formId != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("fromId", formId, formId, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
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

    public static List<Map<String,String>> search(Long id,String message,String fromNick,String toNick,Long toId,Long startTime,Long endTime, int pageSize, int start,Long formId) throws Exception {
        Directory directory = FSDirectory.open(new File(INDEX_DIR));
        IndexReader ir = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(ir);
        List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        int maxDoc=ir.maxDoc();
        System.out.println(maxDoc);
        BooleanQuery query=search(id,message,fromNick,toNick,toId,startTime,endTime,formId);
        BooleanClause[] clauses = query.getClauses();
        if(clauses!=null && clauses.length<1){
            query.add(new MatchAllDocsQuery(),BooleanClause.Occur.MUST);
        }
        Sort sort = new Sort(new SortField("id", SortField.Type.LONG,true));
        TopDocs paged=null;
        if(start==0){
            FieldDoc lastBottom=new FieldDoc(maxDoc-(start+1),1.0f,new Object[]{Long.MAX_VALUE});
            paged=indexSearcher.searchAfter(lastBottom, query, null, pageSize,sort);
        }else{
            FieldDoc lastBottom=new FieldDoc(maxDoc-(start+1),1.0f,new Object[]{new Long(maxDoc-(start+1))});
            paged=indexSearcher.searchAfter(lastBottom, query, null, pageSize,sort);
        }
        if(paged==null){
            return list;
        }
        
        ScoreDoc[] scoreDocs = paged.scoreDocs;
        int total=paged.totalHits;
        
        for(int i=0;i<scoreDocs.length;i++){
            Map<String,String> map=new HashMap<String, String>();
            Document doc = indexSearcher.doc(scoreDocs[i].doc);
            map.put("id",doc.get("id"));
            map.put("fromNick",doc.get("fromNick"));
            map.put("toNick",doc.get("toNick"));
            map.put("createDate",doc.get("createDate"));
            map.put("message",doc.get("message"));
            map.put("deleted",doc.get("deleted"));
            map.put("total",total+"");
            list.add(map);
        }
        try {
        } finally {
            ir.close();
            closeAll(directory, null);
        }
        return list;
    }
    public static void deleteIndex(Long id,Long startTime,Long endTime) throws Exception{
        Directory dir = FSDirectory.open(new File(INDEX_DIR));
        BooleanQuery query = new BooleanQuery();
        if(id!=null){
            NumericRangeQuery numericRangeQuery=NumericRangeQuery.newLongRange("id", id, id, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        if(startTime!=null){
            if(endTime==null){
                endTime=new Date().getTime();
            }
            NumericRangeQuery numericRangeQuery=NumericRangeQuery.newLongRange("createDate", startTime, endTime, true, false);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        if (IndexWriter.isLocked(dir)) {
            IndexWriter.unlock(dir);
        }
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteDocuments(query);
        indexWriter.commit();
        try {
        } finally {
            closeAll(dir, indexWriter);
        }
    }
    public static void updateIndex(Long id,Long deleted) throws Exception{
        Directory dir = FSDirectory.open(new File(INDEX_DIR));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        if (IndexWriter.isLocked(dir)) {
            IndexWriter.unlock(dir);
        }

        Term term=new Term("id",id.toString());
        Document document=null;

        List<Map<String,String>> list=search(id,null,null,null,null,null,null,1,0,1L);
        if(list!=null && list.size()>0){
            document=new Document();
            Map<String,String> map=list.get(0);
            Long createDate = NumberUtils.toLong(map.get("createDate"));
            Long fromId = NumberUtils.toLong(map.get("fromId"));
            Long toId = NumberUtils.toLong(map.get("toId"));
            Long manager = NumberUtils.toLong(map.get("manager"));
            String message = map.get("message");
            String fromNick = map.get("fromNick");


            document.add(new LongField("id", id, Field.Store.YES));
            if (message!=null) {
                document.add(new StringField("message", message, Field.Store.YES));
            }
            if (createDate != null) {
                document.add(new LongField("createDate", createDate, Field.Store.YES));
            }
            if (fromId != null) {
                document.add(new LongField("fromId", fromId, Field.Store.YES));
            }
            if (toId != null) {
                document.add(new LongField("toId", toId, Field.Store.YES));
            }
            if (manager != null) {
                document.add(new LongField("manager", manager, Field.Store.YES));
            }
            if(deleted!=null){
                document.add(new LongField("deleted", deleted, Field.Store.YES));
            }
            if (fromNick!=null) {
                document.add(new StringField("fromNick", fromNick, Field.Store.YES));
            }
            LongField longField=new LongField("id",id, Field.Store.YES);
            document.add(longField);
        }
        IndexWriter indexWriter=null;
        if(document!=null){
            indexWriter = new IndexWriter(dir, config);
            BooleanQuery query = new BooleanQuery();
            if(id!=null){
                NumericRangeQuery numericRangeQuery=NumericRangeQuery.newLongRange("id", id, id, true, true);
                query.add(numericRangeQuery, BooleanClause.Occur.MUST);
            }
            indexWriter.deleteDocuments(query);
            indexWriter.updateDocument(term, document);
            indexWriter.commit();
        }
        
        try {
        } finally {
            closeAll(dir, indexWriter);
        }
    }


    public static void main(String[] args) {
        try {
            
//            updateIndex(1L,1L);
//            Long sTime= DateUtils.parseDate("2015-01-01 16:09:43",new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime();
//            Long eTime= DateUtils.parseDate("2015-01-27 23:09:44", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime();
//            List<Map<String,String>> historyMessages=search(null,null,null,null,7L,null,null,25,0,1L);
//            for(Map<String,String> map:historyMessages){                   
//                System.out.print("id==" + map.get("id"));
//                System.out.print("----");
//                System.out.print("message==" + map.get("message"));
//                System.out.print("----");
//                System.out.print("deleted==" + map.get("deleted"));
//                System.out.print("----");
//                
//                System.out.print("total==" + map.get("total"));
//                System.out.print("----");
//                
//                System.out.print("date==" + DateFormatUtils.format(new Date(NumberUtils.toLong(map.get("createDate"))), "yyyy-MM-dd HH:mm:ss"));
//                System.out.println();
//            }

//            LuceneUtils.deleteIndex(sTime,eTime);
            
//            LuceneUtils.index();
//            LuceneUtils.search("离开", 50, 1);
            final LuceneInstance instance = LuceneInstance.getInstance();
            for(int i=0;i<5;i++){
                final int index=i;
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            
                            IndexWriter indexWriter = instance.getIndexWriter1();
                            for(int j=0;j<index;j++){
                                BooleanQuery query = new BooleanQuery();
                                NumericRangeQuery numericRangeQuery=NumericRangeQuery.newIntRange("j", j, j, true, true);
                                query.add(numericRangeQuery, BooleanClause.Occur.MUST);
                                indexWriter.deleteDocuments(query);
                                indexWriter.commit();
                                Document document = new Document();
                                document.add(new IntField("j",j,Field.Store.YES));
                                indexWriter.addDocument(document);
                            }
                            indexWriter.commit();
                            
//                            instance.close(indexWriter);
                            
                            IndexReader reader=instance.getIndexReader1();
//                            
                            System.out.println(Thread.currentThread().getName()+"-----max=="+reader.maxDoc());
                            instance.close(reader);
                            
                            
                            
                            
                            
                            
                            
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                
                System.out.println("线程===="+thread.getName());
                thread.start();
                thread.sleep(2000);
                
            }
            IndexReader reader=instance.getIndexReader1();
//                            
            System.out.println(Thread.currentThread().getName()+"--all---max=="+reader.maxDoc());

//            IndexWriter indexWriter = instance.getIndexWriter();
//            BooleanQuery query = new BooleanQuery();
//            NumericRangeQuery numericRangeQuery=NumericRangeQuery.newIntRange("j", 4, 4, true, true);
//            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
//            indexWriter.deleteDocuments(query);
//            indexWriter.commit();
//        
//
//            IndexReader reader=instance.getIndexReader();
//            Document document = reader.document(reader.maxDoc()-1);
////                            
//            System.out.println(document.get("j"));
//            System.out.println(Thread.currentThread().getName()+"-----max=="+reader.maxDoc());
//            instance.close(reader);
            
            
            
            
            
            System.out.println(INDEX_DIR);
            System.out.println(Integer.MAX_VALUE);
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}