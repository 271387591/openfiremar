package com.ozstrategy.jdbc.message.impl;

import com.ozstrategy.Constants;
import com.ozstrategy.jdbc.message.HistoryMessageDao;
import com.ozstrategy.lucene.DeleteIndexInstance;
import com.ozstrategy.lucene.LuceneInstance;
import com.ozstrategy.model.openfire.MessageType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
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
import org.apache.lucene.search.TopDocs;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lihao on 1/15/15.
 */
@Repository("historyMessageDao")
public class HistoryMessageDaoImpl implements HistoryMessageDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Integer maxExportItem =20000;
    private Integer maxIndexItem = 200000;
    private Integer maxSelectSize=500000;
    @Autowired
    private LuceneInstance luceneInstance;
    @Autowired
    private DeleteIndexInstance deleteIndexInstance;
    @Autowired
    private Properties variable;
    

    public Long maxId() {
        String sql = "SELECT max(id) from ext_ofHistory";
        Long maxId= jdbcTemplate.queryForObject(sql, Long.class);
        if(maxId==null){
            maxId=0L;
        }
        return maxId;
    }

   

    public Map<String, Object> maxMinIdByTime(Date startTime, Date endTime, Long projectId) {
        String sql = "SELECT max(id) as max,min(id) as min from ext_ofHistory where toId=? and createDate>=? and createDate<=?";
        return jdbcTemplate.queryForMap(sql, projectId, startTime, endTime);
    }

    public void deleteByIds(List<Long> ids) {
        String sql = "DELETE FROM ext_ofHistory WHERE 1=1 ";
        String idsd=StringUtils.join(ids.iterator(),",");
        sql+=" and id in ("+idsd+")";
        jdbcTemplate.update(sql);
    }

    public List<Map<String, Object>> getIdByBetween(Long minId, Long maxId, Long projectId, Integer limit) {
        String sql = "SELECT id from ext_ofHistory WHERE id>? AND id<? and toId=? limit ?";
        return jdbcTemplate.queryForList(sql, minId, maxId, projectId, limit);
    }


    public void delete(Date startTime, Date endTime, Long projectId) {
        String sql = "DELETE FROM ext_ofHistory WHERE createDate>=? AND createDate<=? and toId=?";
        jdbcTemplate.update(sql, startTime, endTime, projectId);
    }

    public void exportMessage(Date startTime, Date endTime, File folder, Long projectId) throws Exception {
        String maxSql = "SELECT max(id) from ext_ofHistory where toId=? and createDate>=? and createDate<=? and type!=2";
        String minSql = "SELECT min(id) from ext_ofHistory where toId=? and createDate>=? and createDate<=? and type!=2";
        Long maxId = jdbcTemplate.queryForObject(maxSql, Long.class, projectId, startTime, endTime);
        if(maxId==null){
            maxId=0L;
        }
        maxId=maxId+1;
        Long minId = jdbcTemplate.queryForObject(minSql, Long.class, projectId, startTime, endTime);
        if(minId==null){
            minId=0L;
        }
        minId=minId-1;
        Long mId = minId;
        int fileIndex = 0;
        do {
            fileIndex++;
            mId = exportMessage(folder, mId, maxId, projectId, fileIndex);
        } while (mId != 0);
    }


    public void exportVoice(Date startTime, Date endTime, File folder, Long projectId) throws Exception {
        String maxSql = "SELECT max(id) from ext_ofHistory where toId=? and createDate>=? and createDate<=? and type=2";
        String minSql = "SELECT min(id) from ext_ofHistory where toId=? and createDate>=? and createDate<=? and type=2";
        Long maxId = jdbcTemplate.queryForObject(maxSql, Long.class, projectId, startTime, endTime);
        if(maxId==null){
            maxId=0L;
        }
        maxId=maxId+1;
        Long minId = jdbcTemplate.queryForObject(minSql, Long.class, projectId, startTime, endTime)-1;
        if(minId==null){
            minId=0L;
        }
        minId=minId-1;
        Long mId = minId;
        do {
            mId = exportVoice(folder, mId, maxId, projectId);
        } while (mId != 0);
    }

    private Long exportVoice(File folder, Long minId, Long maxId, Long projectId) throws Exception {
        final String sql = "select * from ext_ofHistory  where id >? and id<? and toId=? limit ?";
        maxExportItem= NumberUtils.toInt(variable.get("maxExportItem").toString());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long mId = 0L;
        try {
            con = jdbcTemplate.getDataSource().getConnection();
            statement = con.prepareStatement(sql);
            statement.setLong(1, minId);
            statement.setLong(2, maxId);
            statement.setLong(3, projectId);
            statement.setInt(4, maxExportItem);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                mId = 0L;
                return mId;
            }
            resultSet.close();
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                mId = id;
                Timestamp createDate = resultSet.getTimestamp("createDate");
                String fromNick = resultSet.getString("fromNick");
                int type = resultSet.getInt("type");
                String message = resultSet.getString("message");
                if (type == MessageType.voice.ordinal()) {
                    try {
                        byte[] bytes = IOUtils.toByteArray(new URI(message));
                        String ext = message.substring(message.lastIndexOf("."));
                        String fileName = fromNick + createDate.getTime() + ext;
                        File file = new File(folder, fileName);
                        FileOutputStream outputStream = new FileOutputStream(file);
                        IOUtils.write(bytes, outputStream);
                        outputStream.close();
                    } catch (Exception e) {
                    }
                }
            }
            return mId;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(con);
        }
    }

    private Long exportMessage(File folder, Long minId, Long maxId, Long projectId, Integer fileIndex) throws Exception {
        final String sql = "select * from ext_ofHistory  where id>? and id<? and toId=? limit ?";
        maxExportItem= NumberUtils.toInt(variable.get("maxExportItem").toString());
        final String sheetName = "聊天记录";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long mId = 0L;
        try {
            con = jdbcTemplate.getDataSource().getConnection();
            statement = con.prepareStatement(sql);
            statement.setLong(1, minId);
            statement.setLong(2, maxId);
            statement.setLong(3, projectId);
            statement.setInt(4, maxExportItem);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                mId = 0L;
                return mId;
            }
            resultSet.close();
            resultSet = statement.executeQuery();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            Map<String, CellStyle> styleMap = createStyles(wb);
            HSSFRow headerRow = sheet.createRow(2);
            headerRow.setHeight((short) (900));
            String[] headers = new String[]{"发送时间", "用户昵称", "工程", "聊天内容"};
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = headerRow.createCell(i);
                if (i == 3) {
                    sheet.setColumnWidth(i, 5000 << 3);
                } else if (i == 2) {
                    sheet.setColumnWidth(i, 5000 << 1);
                } else {
                    sheet.setColumnWidth(i, 5000);
                }
                cell.setCellStyle(styleMap.get("header"));
                cell.setCellValue(headers[i]);
            }
            int column = 3;
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                mId = id;
                int type = resultSet.getInt("type");
                if (type == MessageType.voice.ordinal()) {
                    continue;
                }
                HSSFRow sheetRow = sheet.createRow(column);
                Timestamp createDate = resultSet.getTimestamp("createDate");
                HSSFCell cell = sheetRow.createCell(0);
                cell.setCellStyle(styleMap.get("cell"));
                cell.setCellValue(DateFormatUtils.format(createDate, Constants.YMDHMS));

                String fromNick = resultSet.getString("fromNick");
                sheetRow.setHeight((short) (600));
                cell = sheetRow.createCell(1);
                cell.setCellStyle(styleMap.get("cell"));
                cell.setCellValue(fromNick);

                String toNick = resultSet.getString("toNick");
                cell = sheetRow.createCell(2);
                cell.setCellStyle(styleMap.get("cell"));
                cell.setCellValue(toNick);


                String message = resultSet.getString("message");
                if (type == MessageType.picture.ordinal()) {
                    try {
                        byte[] bytes = IOUtils.toByteArray(new URI(message));
                        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1022, 255, (short) 3, column, (short) 3, column + 6);
                        patriarch.createPicture(anchor, wb.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } catch (Exception e) {
                    }
                    column = column + 6;
                } else {
                    cell = sheetRow.createCell(3);
                    cell.setCellStyle(styleMap.get("cell"));
                    cell.setCellValue(message);
                }
                column++;
            }
            if (wb != null) {
                File file = new File(folder, sheetName + fileIndex + ".xls");
                FileOutputStream os1 = new FileOutputStream(file);
                wb.write(os1);
                os1.close();
                wb = null;
            }
            return mId;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(con);
        }
    }

    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setColor(IndexedColors.ROYAL_BLUE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short) 14);
//        monthFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
//        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        Font cellFont = wb.createFont();
//        cellFont.setFontHeightInPoints((short) 12);
//        cellFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(cellFont);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        styles.put("cell", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(cellFont);
//    style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        Font cellFont1 = wb.createFont();
        cellFont1.setFontHeightInPoints((short) 12);
        cellFont1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setLocked(false);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(cellFont1);
        styles.put("cellUnlocked", style);

        return styles;
    }


    public boolean checkExportDataExist(Date startTime, Date endTime, Long projectId) {
        String sql = "select id from ext_ofHistory where createDate>=? and createDate<=? and toId=? limit 1";
        List list = jdbcTemplate.queryForList(sql, startTime, endTime, projectId);
        return list != null && !list.isEmpty();
    }

    public void deleteMessage(Long projectId, String messageId) {
        String sql = "update ext_ofHistory set deleted=1 where toId=? and id=?";
        jdbcTemplate.update(sql, projectId, messageId);
    }

    public List<Map<String, String>> search(String message, Date startDate, Date endDate, Long fromId, Long projectId, Long manager,Long deleted,Long pillowTalk, Integer start, Integer limit) throws Exception {
        IndexReader indexReader = luceneInstance.getReader();
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        BooleanQuery query = search(message, fromId, projectId, manager, deleted,pillowTalk,startDate, endDate);
        BooleanClause[] clauses = query.getClauses();
        if (clauses != null && clauses.length < 1) {
            query.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);
        }
        Sort sort = new Sort(new SortField("id", SortField.Type.LONG, true));
        TopDocs paged=indexSearcher.search(query,1);

        int allTotal=paged.totalHits;
        if(start>=allTotal){
            return list;
        }
        
        int index=1;
        if(start>maxSelectSize){
            index=start/maxSelectSize;
        }
        if(start==0){
            index=0;
        }

        FieldDoc lastBottom=null;
        for(int i=0;i<index;i++){
            paged=indexSearcher.searchAfter(lastBottom, query, null, Math.min(start,maxSelectSize),sort);
            ScoreDoc[] scoreDocs = paged.scoreDocs;
            int len=scoreDocs.length;
            if(len>0){
                int  last = scoreDocs[len-1].doc;
                Document doc = indexSearcher.doc(last);
                Long lastId =NumberUtils.toLong(doc.get("id"));
                lastBottom=new FieldDoc(last,1.0f,new Object[]{lastId});
            }
            scoreDocs=null;
            paged=null;

        }
        if(start>maxSelectSize){
            paged=indexSearcher.searchAfter(lastBottom, query, null, start-index*maxSelectSize,sort);
            ScoreDoc[] scoreDocs = paged.scoreDocs;
            int len=scoreDocs.length;
            if(len>0){
                int  last = scoreDocs[len-1].doc;
                Document doc = indexSearcher.doc(last);
                Long lastId =NumberUtils.toLong(doc.get("id"));
                lastBottom=new FieldDoc(last,1.0f,new Object[]{lastId});
            }
        }
        paged=indexSearcher.searchAfter(lastBottom, query, null, limit,sort);
        
        if (paged == null) {
            return list;
        }
        ScoreDoc[] scoreDocs = paged.scoreDocs;
        int total = paged.totalHits;
        for (int i = 0; i < scoreDocs.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            Document doc = indexSearcher.doc(scoreDocs[i].doc);
            map.put("id", doc.get("id"));
            map.put("fromNick", doc.get("fromNick"));
            map.put("fromId", doc.get("fromId"));
            map.put("toId", doc.get("toId"));
            map.put("createDate", doc.get("createDate"));
            map.put("message", doc.get("message"));
            map.put("type", doc.get("type"));
            map.put("total", total + "");
            list.add(map);
        }
        return list;
    }

    public Long addIndex(Long minId) throws Exception {
        Long returnMaxId=null;
        try{
            IndexWriter indexWriter = luceneInstance.getWriter();
            Long maxId = minId;
            returnMaxId=maxId;
            do {
                returnMaxId=maxId;
                maxId = addIndex(indexWriter, maxId);
            } while (maxId != 0);
            indexWriter.commit();
        }finally {
            luceneInstance.closeAll(); 
        }
        return returnMaxId;
    }

    public void deleteIndex(Date startDate, Date endDate, Long projectId) throws Exception {
        deleteIndexInstance.deleteIndex(startDate, endDate, projectId);
    }

    public void deleteIndexById(Long id, Long projectId) throws Exception {
        deleteIndexInstance.deleteIndexById(id,projectId);
    }

    public List<Map<String, Object>> getHistory(Long projectId, Integer manager,Integer roleB,Integer start, Integer limit) throws Exception {
        String sql="select * from ext_ofHistory where toId=? and deleted=0 order by id desc limit ?,?";
        if(manager!=null){
            sql="select * from ext_ofHistory where toId=? and deleted=0 and manager=? order by id desc limit ?,?";
            return jdbcTemplate.queryForList(sql,projectId,manager,start,limit);
        }
        if(roleB!=null){
            sql="select * from ext_ofHistory where toId=? and deleted=0 and pillowTalk=0 order by id desc limit ?,?";
            return jdbcTemplate.queryForList(sql,projectId,start,limit);
        }
        return jdbcTemplate.queryForList(sql,projectId,start,limit);
    }

    public Integer getHistoryCount(Long projectId,Integer manager,Integer roleB) {
        String sql="select count(id) from ext_ofHistory where toId=? and deleted=0";
        if(manager!=null){
            sql="select count(id) from ext_ofHistory where toId=? and deleted=0 and manager=?";
            return jdbcTemplate.queryForObject(sql, Integer.class,projectId, manager);
        }
        if(roleB!=null){
            sql="select count(id) from ext_ofHistory where toId=? and deleted=0 and pillowTalk=0";
            return jdbcTemplate.queryForObject(sql, Integer.class, projectId);
        }
        return jdbcTemplate.queryForObject(sql, Integer.class,projectId);
    }

    public Long addIndex(IndexWriter indexWriter, Long minId) throws Exception {
        String sql = "select * from ext_ofHistory  where id>? limit ?";
        maxIndexItem=NumberUtils.toInt(variable.get("maxIndexItem").toString());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long maxId = minId;
        try {
            con = jdbcTemplate.getDataSource().getConnection();
            statement = con.prepareStatement(sql);
            statement.setLong(1, minId);
            statement.setInt(2, maxIndexItem);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                maxId = 0L;
                return maxId;
            }
            resultSet.close();

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Date createDate = resultSet.getTimestamp("createDate");
                Long fromId = resultSet.getLong("fromId");
                Long toId = resultSet.getLong("toId");
                Long manager = resultSet.getLong("manager");
                String message = resultSet.getString("message");
                String fromNick = resultSet.getString("fromNick");
                Integer deleted = resultSet.getInt("deleted");
                Integer pillowTalk = resultSet.getInt("pillowTalk");
                Integer type = resultSet.getInt("type");
                Document document = new Document();
                document.add(new LongField("id", id, Field.Store.YES));
                if (StringUtils.isNotEmpty(message)) {
                    document.add(new StringField("message", message, Field.Store.YES));
                }
                if (createDate != null) {
                    document.add(new LongField("createDate", createDate.getTime(), Field.Store.YES));
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
                if(pillowTalk!=null){
                    document.add(new LongField("pillowTalk", pillowTalk, Field.Store.YES));
                }
                if(type!=null){
                    document.add(new LongField("type", type, Field.Store.YES));
                }
                
                
                if (StringUtils.isNotEmpty(fromNick)) {
                    document.add(new StringField("fromNick", fromNick, Field.Store.YES));
                }
                indexWriter.addDocument(document);
                maxId = id;
            }
            return maxId;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(con);
        }
    }

    private static BooleanQuery search(String message, Long fromId, Long toId, Long manager,Long deleted,Long pillowTalk, Date startTime, Date endTime) throws Exception {
        BooleanQuery query = new BooleanQuery();
        if (StringUtils.isNotEmpty(message)) {
            RegexpQuery regexpQuery = new RegexpQuery(new Term("message", ".*" + message + ".*"));
            query.add(regexpQuery, BooleanClause.Occur.MUST);
        }
        if (fromId != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("fromId", fromId, fromId, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        if (toId != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("toId", toId, toId, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        if (manager != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("manager", toId, toId, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        if (deleted != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("deleted", deleted, deleted, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        if (pillowTalk != null) {
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("pillowTalk", 0L, 0L, true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        

        if (startTime != null) {
            if (endTime == null) {
                endTime = new Date();
            }
            NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("createDate", startTime.getTime(), endTime.getTime(), true, true);
            query.add(numericRangeQuery, BooleanClause.Occur.MUST);
        }
        if(endTime!=null){
            Long startLong=0L;
            if(startTime==null){
                NumericRangeQuery numericRangeQuery = NumericRangeQuery.newLongRange("createDate", startLong, endTime.getTime(), true, true);
                query.add(numericRangeQuery, BooleanClause.Occur.MUST);
            }
        }
        return query;
    }
}
