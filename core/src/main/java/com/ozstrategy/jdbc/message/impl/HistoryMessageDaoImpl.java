package com.ozstrategy.jdbc.message.impl;

import com.ozstrategy.Constants;
import com.ozstrategy.jdbc.message.HistoryMessageDao;
import com.ozstrategy.model.export.ExportType;
import com.ozstrategy.util.LuceneUtils;
import org.apache.commons.io.IOUtils;
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
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private int maxItem=20000;

    public Long maxId() {
        String sql="SELECT max(id) from ext_ofHistory";
        return jdbcTemplate.queryForObject(sql,Long.class);
    }

    public void delete(Date startTime, Date endTime) {
        String sql="DELETE FROM ext_ofHistory WHERE createDate>=? AND createDate<=?";
        jdbcTemplate.update(sql,startTime,endTime);

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

    public void export(Date startTime, Date endTime,File folder) throws Exception {
        Long maxId=Long.MAX_VALUE;
        int index=0;
        export(startTime,endTime,folder,maxId,index);
    }
    private void export(final Date startTime, final Date endTime,File folder, Long maxId,int index){
        index++;
        final String sql="select * from ext_ofHistory where createDate>=? and createDate<=? and id<? order by id desc limit ?";
        final String sheetName="聊天记录";
        Long mId=0L;
        Connection con=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try{
            con = jdbcTemplate.getDataSource().getConnection();
            statement = con.prepareStatement(sql);
            statement.setDate(1,new java.sql.Date(startTime.getTime()));
            statement.setDate(2, new java.sql.Date(endTime.getTime()));
            statement.setLong(3, maxId);
            statement.setInt(4, maxItem);
            resultSet = statement.executeQuery();
            if(!resultSet.next()){
                return; 
            }
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
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                HSSFRow sheetRow = sheet.createRow(i);
                

                Date createDate=resultSet.getDate("createDate");
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
                
                
                String type=resultSet.getString("type");
                String message=resultSet.getString("message");
                if(StringUtils.equals(type, ExportType.MessagePicture.name())){
                    try{
                        byte[] bytes= IOUtils.toByteArray(new URI(message));
                        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,1022,255,(short) 2,i,(short)2,i+3);
                        patriarch.createPicture(anchor , wb.addPicture(bytes,HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }catch (IOException e){
                    }
                    i=i+3;
                }else if(StringUtils.equals(type, ExportType.Voice.name())){

                }else{
                    cell=sheetRow.createCell(3);
                    cell.setCellStyle(styleMap.get("cell"));
                    cell.setCellValue(message);
                }
                i++;
                maxId=id;
            }
            File file=new File(folder,"聊天记录"+index+".xls");
            FileOutputStream os1 = new FileOutputStream(file);
            wb.write(os1);
            
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(con);
        }
        mId=maxId;
        export(startTime,endTime,folder,mId,index);
        return;
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

    public boolean checkExportDataExist(Date startTime, Date endTime) {
        String sql="select id from ext_ofHistory where createDate>=? and createDate<=? limit 1";
        List list = jdbcTemplate.queryForList(sql, startTime, endTime);
        return list!=null && !list.isEmpty();
    }
}
