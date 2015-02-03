package com.ozstrategy.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;

import java.util.Date;

/**
 * Created by lihao on 1/30/15.
 */
public class DeleteIndexInstance {
    private volatile static DeleteIndexInstance instance=null;
    private DeleteIndexInstance(){}
    public static DeleteIndexInstance getInstance(){
        if(instance==null){
            synchronized (LuceneInstance.class){
                if(instance==null){
                    instance=new DeleteIndexInstance();
                }
            }
        }
        return instance;
    }    
    private LuceneInstance luceneInstance;
    synchronized public void deleteIndex(Date startDate, Date endDate, Long projectId){
        BooleanQuery query = new BooleanQuery();
        if (startDate != null) {
            if (endDate == null) {
                endDate = new Date();
            }
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("createDate", startDate.getTime(), endDate.getTime(), true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }

        if (projectId != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("toId", projectId, projectId, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        try{
            IndexWriter indexWriter = luceneInstance.getWriter();
            indexWriter.deleteDocuments(query);
            indexWriter.commit();
            
        }catch (Exception e){
            
        }finally {
            luceneInstance.closeAll();
        }
    }

    public LuceneInstance getLuceneInstance() {
        return luceneInstance;
    }

    public void setLuceneInstance(LuceneInstance luceneInstance) {
        this.luceneInstance = luceneInstance;
    }
}
