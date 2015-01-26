package com.ozstrategy.jdbc.message.impl;

import com.ozstrategy.Constants;
import com.ozstrategy.jdbc.message.HistoryMessageDao;
import com.ozstrategy.model.export.ExportType;
import com.ozstrategy.model.openfire.HistoryMessage;
import com.ozstrategy.util.LuceneUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/15/15.
 */
@Repository("historyMessageDao")
public class HistoryMessageDaoImpl implements HistoryMessageDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Long maxItem=2L;

    public Long maxId() {
        String sql="SELECT max(id) from ext_ofHistory";
        return jdbcTemplate.queryForObject(sql,Long.class);
    }

    public void delete(Date startTime, Date endTime,Long projectId) {
        String sql="DELETE FROM ext_ofHistory WHERE createDate>=? AND createDate<=? and toId=?";
        jdbcTemplate.update(sql,startTime,endTime,projectId);

    }

    public Long addIndex(final Long index_max_id)  throws Exception{
        final String sql="SELECT * FROM  ext_ofHistory WHERE id>?";
        return jdbcTemplate.execute(new ConnectionCallback<Long>(){
            public Long doInConnection(Connection con) throws SQLException, DataAccessException {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setLong(1,index_max_id);
                ResultSet resultSet = statement.executeQuery();
                try{
                Long id= LuceneUtils.addIndex(resultSet);
                return id;
                }catch (Exception e) {
                    e.printStackTrace();
                    throw new SQLException("");
                }finally {
                    resultSet.close();
                    statement.close();
                }
            }
        });
    }

    public void exportMessage(Date startTime, Date endTime,File folder,Long projectId) throws Exception {
        int index=0;
        String sql="SELECT max(id) from ext_ofHistory where toId=?";
        Long maxId = jdbcTemplate.queryForObject(sql,Long.class,projectId)+1;
        do{
            index++;
            maxId=exportMessage(startTime, endTime, folder, maxId, index, projectId);
        }while (maxId!=0);
       
    }

    public void exportVoice(Date startTime, Date endTime, File folder, Long projectId) throws Exception {
        String sql="SELECT max(id) from ext_ofHistory where type=2 and toId=?";
        Long maxId = jdbcTemplate.queryForObject(sql,Long.class,projectId)+1;
        do{
            maxId=exportVoice(startTime, endTime, folder, projectId,maxId);
        }while (maxId!=0);

    }
    private Long exportVoice(Date startTime, Date endTime, File folder, Long projectId,Long maxId) throws Exception{
        final String sql="select * from ext_ofHistory where  type=2 and createDate>=? and createDate<=? and id<? and toId=? order by id desc limit ?";
        Long mId=0L;
        Connection con=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try{
            con = jdbcTemplate.getDataSource().getConnection();
            statement = con.prepareStatement(sql);
            statement.setTimestamp(1, new Timestamp(startTime.getTime()));
            statement.setTimestamp(2, new Timestamp(endTime.getTime()));
            statement.setLong(3, maxId);
            statement.setLong(4, projectId);
            statement.setInt(5, maxItem.intValue());
            resultSet = statement.executeQuery();
            
            if(!resultSet.next()){
                mId=0L;
                if(mId<=0){
                    return mId;
                }
            }
            resultSet.close();
            resultSet=statement.executeQuery();
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                Timestamp createDate=resultSet.getTimestamp("createDate");
                String fromNick=resultSet.getString("fromNick");
                int type=resultSet.getInt("type");
                String message=resultSet.getString("message");
                if(type==(ExportType.Voice.ordinal()+1)){
                    try{
                        byte[] bytes= IOUtils.toByteArray(new URI(message));
                        String ext=message.substring(message.lastIndexOf("."));
                        String fileName=fromNick+createDate.getTime()+ext;
                        File file=new File(folder,fileName);
                        FileOutputStream outputStream=new FileOutputStream(file);
                        IOUtils.write(bytes,outputStream);
                        outputStream.close();
                    }catch (Exception e){
                    }
                }
                maxId=id;
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(con);
        }
        mId=maxId;
        return mId;
    }

    private Long exportMessage(final Date startTime, final Date endTime,File folder, Long maxId,int index,Long projectId) throws Exception{
        final String sql="select * from ext_ofHistory where type!=2 and createDate>=? and createDate<=? and id<? and toId=? order by id desc limit ?";
        final String sheetName="聊天记录";
        Long mId=0L;
        Connection con=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try{
            con = jdbcTemplate.getDataSource().getConnection();
            statement = con.prepareStatement(sql);
            statement.setTimestamp(1, new Timestamp(startTime.getTime()));
            statement.setTimestamp(2, new Timestamp(endTime.getTime()));
            statement.setLong(3, maxId);
            statement.setLong(4, projectId);
            statement.setInt(5, maxItem.intValue());
            resultSet = statement.executeQuery();
            if(!resultSet.next()){
                mId=0L;
                if(mId<=0){
                    return mId;
                }
            }
            resultSet.close();
            resultSet=statement.executeQuery();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            Map<String,CellStyle> styleMap=createStyles(wb);
            HSSFRow headerRow = sheet.createRow(2);
            headerRow.setHeight((short)(900));
            String[] headers=new String[]{"发送时间","用户昵称","工程","聊天内容"};
            for(int i=0;i<headers.length;i++){
                HSSFCell cell  = headerRow.createCell(i);
                if(i==3){
                    sheet.setColumnWidth(i, 5000<<3);
                }else if(i==2){
                    sheet.setColumnWidth(i, 5000<<1);
                }else{
                    sheet.setColumnWidth(i, 5000);
                }
                cell.setCellStyle(styleMap.get("header"));
                cell.setCellValue(headers[i]);
            }
            int i=3;
            boolean hasMessage=false;
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                int type=resultSet.getInt("type");
                if(type==(ExportType.Voice.ordinal()+1)){
                    
                }else{
                    HSSFRow sheetRow = sheet.createRow(i);
                    Timestamp createDate=resultSet.getTimestamp("createDate");
                    HSSFCell cell=sheetRow.createCell(0);
                    cell.setCellStyle(styleMap.get("cell"));
                    cell.setCellValue(DateFormatUtils.format(createDate, Constants.YMDHMS));

                    String fromNick=resultSet.getString("fromNick");
                    sheetRow.setHeight((short)(600));
                    cell=sheetRow.createCell(1);
                    cell.setCellStyle(styleMap.get("cell"));
                    cell.setCellValue(fromNick);


                    String toNick=resultSet.getString("toNick");
                    cell=sheetRow.createCell(2);
                    cell.setCellStyle(styleMap.get("cell"));
                    cell.setCellValue(toNick);
                    String message=resultSet.getString("message");
                    if(type==(ExportType.MessagePicture.ordinal()+1)){
                        try{
                            byte[] bytes= IOUtils.toByteArray(new URI(message));
                            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                            HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,1022,255,(short) 2,i,(short)2,i+3);
                            patriarch.createPicture(anchor , wb.addPicture(bytes,HSSFWorkbook.PICTURE_TYPE_JPEG));
                            hasMessage=true;
                        }catch (Exception e){
                        }
                        i=i+3;
                    }else{
                        cell=sheetRow.createCell(3);
                        cell.setCellStyle(styleMap.get("cell"));
                        cell.setCellValue(message);
                        hasMessage=true;
                    }
                }
                i++;
                maxId=id;
            }
            if(hasMessage){
                File file=new File(folder,sheetName+index+".xls");
                FileOutputStream os1 = new FileOutputStream(file);
                wb.write(os1);
                os1.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(con);
        }
        mId=maxId;
//        export(startTime,endTime,folder,mId,index,projectId);
        return mId;
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

    public Integer countTime(Date startTime, Date endTime) throws Exception {
        String sql="select count(id) from ext_ofHistory where createDate>=? and createDate<=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, startTime, endTime);
        return count;
    }

    public boolean checkExportDataExist(Date startTime, Date endTime,Long projectId) {
        String sql="select id from ext_ofHistory where createDate>=? and createDate<=? and toId=? limit 1";
        List list = jdbcTemplate.queryForList(sql, startTime, endTime,projectId);
        return list!=null && !list.isEmpty();
    }

    public List<HistoryMessage> listHistoryMessagesFromDb(Map<String, Object> map, Integer start, Integer limit) throws Exception {
        List<Map<String,Object>> list=null;
        String sql="select * from ext_ofHistory where toId=? and deleted!=?  order by createDate desc limit ?,?";
        String toId= ObjectUtils.toString(map.get("projectId"));
        String deleted=ObjectUtils.toString(map.get("deleted"));
        if(StringUtils.isEmpty(deleted)){
            deleted="1";
        }
        list=jdbcTemplate.queryForList(sql,toId,deleted,start,limit);
        List<HistoryMessage> historyMessages=new ArrayList<HistoryMessage>();
        if(list!=null && list.size()>0){
            for(Map<String,Object> map1 :list){
                HistoryMessage historyMessage=new HistoryMessage();
                BeanUtils.populate(historyMessage,map1);
                historyMessages.add(historyMessage);
            }
        }
        return historyMessages;
    }

    public Integer listHistoryMessagesFromDbCount(Map<String, Object> map) throws Exception {
        String sql="select count(id) as count from ext_ofHistory WHERE toId=? and deleted!=?";
        String toId= ObjectUtils.toString(map.get("projectId"));
        String deleted=ObjectUtils.toString(map.get("deleted"));
        if(StringUtils.isEmpty(deleted)){
            deleted="1";
        }
        Integer count=jdbcTemplate.queryForObject(sql, Integer.class,toId,deleted);
        return count;
    }

    public List<HistoryMessage> listHistoryMessagesStore(Map<String, Object> map, Integer start, Integer limit) throws Exception {
        String sql="select * from ext_ofHistory where 1=1";
        String projectId=ObjectUtils.toString(map.get("projectId"));
        String message=ObjectUtils.toString(map.get("message"));
        String startTime=ObjectUtils.toString(map.get("startTime"));
        String endTime=ObjectUtils.toString(map.get("endTime"));
        String fromNick=ObjectUtils.toString(map.get("fromNick"));
        if(StringUtils.isNotEmpty(projectId)){
            sql+=" and toId='"+projectId+"'";
        }
        if(StringUtils.isNotEmpty(message)){
            message="%"+message+"%";
            sql+=" and message like '"+message+"'";
        }
        if(StringUtils.isNotEmpty(fromNick)){
            sql+=" and fromNick = '"+fromNick+"'";
        }
        if(StringUtils.isNotEmpty(startTime)){
            sql+=" and createDate >= '"+startTime+"'";
        }
        if(StringUtils.isNotEmpty(endTime)){
            sql+=" and createDate <= '"+endTime+"'";
        }
        sql+=" order by createDate desc limit ?,?";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql, start, limit);
        List<HistoryMessage> historyMessages=new ArrayList<HistoryMessage>();
        if(list!=null && list.size()>0){
            for(Map<String,Object> map1 :list){
                HistoryMessage historyMessage=new HistoryMessage();
                BeanUtils.populate(historyMessage,map1);
                historyMessages.add(historyMessage);
            }
        }
        
        return historyMessages;
    }

    public Integer listHistoryMessagesStoreCount(Map<String, Object> map) throws Exception {
        String sql="select count(id) from ext_ofHistory where 1=1";
        String projectId=ObjectUtils.toString(map.get("projectId"));
        String message=ObjectUtils.toString(map.get("message"));
        String startTime=ObjectUtils.toString(map.get("startTime"));
        String endTime=ObjectUtils.toString(map.get("endTime"));
        String fromNick=ObjectUtils.toString(map.get("fromNick"));
        if(StringUtils.isNotEmpty(projectId)){
            sql+=" and toId='"+projectId+"'";
        }
        if(StringUtils.isNotEmpty(message)){
            message="%"+message+"%";
            sql+=" and message like '"+message+"'";
        }
        if(StringUtils.isNotEmpty(fromNick)){
            sql+=" and fromNick = '"+fromNick+"'";
        }
        if(StringUtils.isNotEmpty(startTime)){
            sql+=" and createDate >= '"+startTime+"'";
        }
        if(StringUtils.isNotEmpty(endTime)){
            sql+=" and createDate <= '"+endTime+"'";
        }
        Integer count=jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }

    public List<HistoryMessage> listManagerMessages(Map<String, Object> map, Integer start, Integer limit) throws Exception {
        List<Map<String,Object>> list=null;
        String sql="select * from ext_ofHistory where toId=? and manager=?   order by createDate desc limit ?,?";
        String toId= ObjectUtils.toString(map.get("projectId"));
        String manager=ObjectUtils.toString(map.get("manager"));
        if(StringUtils.isEmpty(manager)){
            manager="1";
        }
        list=jdbcTemplate.queryForList(sql,toId,manager,start,limit);
        List<HistoryMessage> historyMessages=new ArrayList<HistoryMessage>();
        if(list!=null && list.size()>0){
            for(Map<String,Object> map1 :list){
                HistoryMessage historyMessage=new HistoryMessage();
                BeanUtils.populate(historyMessage,map1);
                historyMessages.add(historyMessage);
            }
        }
        return historyMessages;
    }

    public Integer listManagerMessagesCount(Map<String, Object> map) throws Exception {
        String sql="select count(id) from ext_ofHistory where toId=? and manager=?   order by createDate desc limit ?,?";
        String toId= ObjectUtils.toString(map.get("projectId"));
        String manager=ObjectUtils.toString(map.get("manager"));
        if(StringUtils.isEmpty(manager)){
            manager="1";
        }
        Integer list=jdbcTemplate.queryForObject(sql, Integer.class,toId, manager);
        return list;
    }

    public void deleteMessage(Long projectId, String messageId) {
        String sql="update ext_ofHistory set deleted=1 where projectId=? and messageId=?";
        jdbcTemplate.update(sql, projectId, messageId);
    }
}
