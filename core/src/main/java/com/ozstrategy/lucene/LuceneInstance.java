package com.ozstrategy.lucene;

import com.ozstrategy.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Created by lihao on 1/28/15.
 */
public class LuceneInstance {
    private Object lock_w=new Object();
    private Object lock_r=new Object();
    private IndexWriter writer;
    private IndexReader reader;
    
    private static String INDEX_DIR = Constants.imDataDir + "/index";
    private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
    private static Log log= LogFactory.getLog(LuceneInstance.class);
    
    private volatile static LuceneInstance instance=null;
    private LuceneInstance(){} 
    public static LuceneInstance getInstance(){
        if(instance==null){
            synchronized (LuceneInstance.class){
                if(instance==null){
                    instance=new LuceneInstance();
                }
            }
        }
        return instance;
    }
    public IndexWriter getWriter() throws CorruptIndexException, LockObtainFailedException, IOException{
        synchronized (lock_w){
            if(writer==null){
                File file=new File(INDEX_DIR);
                if(!file.exists()){
                    file.mkdirs();
                }
                FSDirectory directory= FSDirectory.open(file);
                if(IndexWriter.isLocked(directory)){
                    IndexWriter.unlock(directory);
                }
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
                config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                writer = new IndexWriter(directory, config);
                System.out.println("创建IndexWriter");
            }
        }
        return writer;
    } 
    
    public IndexReader getReader() throws CorruptIndexException, IOException{
        synchronized (lock_r){
            if(reader==null){
                System.out.println("创建IndexReader");
                File file=new File(INDEX_DIR);
                if(!file.exists()){
                    file.mkdirs();
                }
                FSDirectory directory= FSDirectory.open(file);
                reader = DirectoryReader.open(directory);
            }
        }
        return reader;
    }
    
    public void closeWriter(){
        if(writer!=null){
            try {
                writer.close();
                writer=null;
                lock_w=new Object();
            } catch (IOException e) {
            }
        }
    }
    public void closeReader(){
        if(reader!=null){
            try {
                reader.close();
                reader=null;
                lock_r=new Object();
            } catch (IOException e) {
            }
        }
    }
    public void closeAll(){
        this.closeReader();
        this.closeWriter();
    }
    
}
