package com.ozstrategy.model;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SqlBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseEntity implements Serializable {
    @Transient
    private static transient Map<Class<? extends BaseEntity>, Map<String,String>> columnMap = null;
    static {
        columnMap = new ConcurrentHashMap<Class<? extends BaseEntity>, Map<String,String>>();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Column(name = "createDate")
    protected Date createDate;
    @Column(name = "lastUpdateDate")
    protected Date lastUpdateDate;

    public BaseEntity() {
        this.createDate = new Date();
        this.lastUpdateDate = createDate;
        caculationColumnList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String returnInsertSql(){
        Map<String,String> map = columnMap.get(this.getClass());
        List<String> columnList=new ArrayList<String>();
        List<String> valueList=new ArrayList<String>();
        Map<String,String> idMap=getIdMap(this.getClass());
        for(Map.Entry<String,String> entry:idMap.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            columnList.add(key);
            valueList.add("#{"+value+"}");
        }
        for(Map.Entry<String,String> entry:map.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            columnList.add(key);
            valueList.add("#{"+value+"}");
        }

        String tableName=getTablename();
        String columnNames=StringUtils.join(columnList.iterator(),",");
        String values=StringUtils.join(valueList.iterator(),",");
        SqlBuilder.BEGIN();
        SqlBuilder.INSERT_INTO(tableName);
        SqlBuilder.VALUES(columnNames, values);
        String sql= SqlBuilder.SQL();
        return sql;
    }
    public String returnUpdateSql(){
        Map<String,String> map = columnMap.get(this.getClass());
        List<String> columnList=new ArrayList<String>();
        Map<String,String> idMap=getIdMap(this.getClass());
        for(Map.Entry<String,String> entry:map.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            columnList.add(key+"=#{"+value+"}");
        }
        List<String> whereList=new ArrayList<String>();
        for(Map.Entry<String,String> entry:idMap.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            whereList.add(key+"=#{"+value+"}");
        }
        String tableName=getTablename();
        String columnNames=StringUtils.join(columnList.iterator(), ",");
        String wheres=StringUtils.join(whereList.iterator(),",");
        SqlBuilder.BEGIN();
        SqlBuilder.UPDATE(tableName);
        SqlBuilder.SET(columnNames);
        SqlBuilder.WHERE(wheres);
        String sql= SqlBuilder.SQL();
        return sql;
    }
    public String returnDeleteSql(){
        List<String> columnList=new ArrayList<String>();
        Map<String,String> idMap=getIdMap(this.getClass());
        for(Map.Entry<String,String> entry:idMap.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            columnList.add(key+"=#{"+value+"}");
        }
        String tableName=getTablename();
        String columnNames=StringUtils.join(columnList.iterator(), ",");
        SqlBuilder.BEGIN();
        SqlBuilder.DELETE_FROM(tableName);
        SqlBuilder.WHERE(columnNames);
        String sql= SqlBuilder.SQL();
        return sql;
    }

    private String getTablename() {
        Table table = this.getClass().getAnnotation(Table.class);
        if (table == null) {
            return this.getClass().getSimpleName().toUpperCase();
        } else {
            return table.name();
        }
    }
    private Map<String,String> getIdMap(Class<? extends BaseEntity> cl) {
        Map<String,String> map=new HashMap<String, String>();
        List<Field> fields=new ArrayList<Field>();
        fields = caculationColumn(cl,fields);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if(field.isAnnotationPresent(Column.class)){
                    String idName=getColumnName(field);
                    map.put(idName,field.getName());
                }else{
                    String idName=field.getName();
                    map.put(idName,field.getName());
                }
                break;
            }
        }
        if(map.isEmpty()){
            throw new RuntimeException("undefine POJO @Id");
        }
        return map;
    }


    private void caculationColumnList() {
        if (columnMap.containsKey(this.getClass())) {
            return;
        }
        List<Field> list=new ArrayList<Field>();
        list=caculationColumn(this.getClass(),list);
        Map<String,String> map=new HashMap<String, String>();
        for (Field field : list) {
            if(field.isAnnotationPresent(Transient.class)){
                continue;
            }
            if(field.isAnnotationPresent(Column.class)){
                String columnName=getColumnName(field);
                if(columnName!=null){
                    map.put(columnName, field.getName());
                }
            }
        }
        columnMap.put(this.getClass(), map);
    }
    private List<Field> caculationColumn(Class cl,List<Field> list){
        Field[] fields = cl.getDeclaredFields();
        list.addAll(Arrays.asList(fields));
        Class supercl= cl.getSuperclass();
        if(supercl.getName().equals(Object.class.getName())){
            return list;
        }
        return caculationColumn(supercl,list);
    }

    private String getColumnName(Field field) {
        String columnName=null;
        Column column = field.getAnnotation(Column.class);
        if(column==null){
            columnName=field.getName();
        }else{
            if(StringUtils.isNotEmpty(column.name())){
                columnName=column.name();
            }else{
                columnName=field.getName();
            }
        }
        return columnName;
    }

    private boolean isTransient(String fieldName){
        Field field = null;
        try {
            field = this.getClass().getDeclaredField(fieldName);
            return field.isAnnotationPresent(Transient.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    private boolean isId(String fieldName){
        Field field = null;
        try {
            field = this.getClass().getDeclaredField(fieldName);
            return field.isAnnotationPresent(Id.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isNull(String fieldname, boolean isMust) {
        try {
            Field field = this.getClass().getDeclaredField(fieldname);
            return isNull(field, isMust);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isNull(Field field, boolean isMust) {
        try {
            field.setAccessible(true);
            Object val = field.get(this);
            boolean isnull = (val == null);
            if (!field.isAnnotationPresent(Column.class)) {
                return isnull;
            }
            Column column = field.getAnnotation(Column.class);
            boolean nullable = column.nullable();
            if (!nullable && isnull && isMust) {
                throw new Exception(field.getName() + " is not null.");
            } else {
                return isnull;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
