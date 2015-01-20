package com.ozstrategy.oz;

import com.ozstrategy.oz.sql.SqlBuilder;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseEntity implements Serializable {

    private static transient Map<Class<? extends BaseEntity>, List<String>> columnMap = null;

    static {
        columnMap = new ConcurrentHashMap<Class<? extends BaseEntity>, List<String>>();
    }
    abstract Long getId();
    abstract void setId(Long id);
    public Object[] insert(){
        caculationColumnList();
        List<String> list = columnMap.get(this.getClass());
        List<String> columnList=new ArrayList<String>();
        List<String> valueList=new ArrayList<String>();
        int len=list.size();
        Object[] objects=new Object[len];
        for(int i=0;i<len;i++){
            String fieldName=list.get(i);
            String columnName = fieldName;
            columnName = getColumnName(fieldName);
            columnList.add(columnName);
            valueList.add("?");
            try {
                objects[i]=ReflectHelper.getValueByFieldName(this,fieldName);
            } catch (Exception e) {
            }
        }
        String tableName=getTablename();
        String columnNames=StringUtils.join(columnList.iterator(),",");
        String values=StringUtils.join(valueList.iterator(),",");
        SqlBuilder.BEGIN();
        SqlBuilder.INSERT_INTO(tableName);
        SqlBuilder.VALUES(columnNames,values);
        String sql=SqlBuilder.SQL();
        Object[] o=new Object[2];
        o[0]=sql;
        o[1]=objects;
        return o;
    }
    public Object[] update(){
        caculationColumnList();
        List<String> list = columnMap.get(this.getClass());
        List<String> columnList=new ArrayList<String>();
        int len=list.size();
        int k=0;
        Object[] objects=new Object[len];
        for(int i=0;i<len;i++){
            String fieldName=list.get(i);
            String columnName = fieldName;
            if(isId(fieldName)){
                k=i;
                continue;
            }
            columnName = getColumnName(fieldName);
            columnList.add(columnName+"=?");
            try {
                objects[i]=ReflectHelper.getValueByFieldName(this,fieldName);
            } catch (Exception e) {
            }
        }
        
        
        Object[] objects1=ArrayUtils.remove(objects,k);
        objects1=ArrayUtils.add(objects1,getId());
        
        String tableName=getTablename();
        String columnNames=StringUtils.join(columnList.iterator(),",");
        String id=getIdName();
        SqlBuilder.BEGIN();
        SqlBuilder.UPDATE(tableName);
        SqlBuilder.SET(columnNames);
        SqlBuilder.WHERE(id+"=?");
        String sql=SqlBuilder.SQL();
        Object[] o=new Object[2];
        o[0]=sql;
        o[1]=objects1;
        return o;
    }
    public Object[] delete(){
        String tableName=getTablename();
        String id=getIdName();
        SqlBuilder.BEGIN();
        SqlBuilder.DELETE_FROM(tableName);
        SqlBuilder.WHERE(id+"=?");
        String sql=SqlBuilder.SQL();
        Object[] objects=new Object[1];
        objects[0]=getId();
        Object[] o=new Object[2];
        o[0]=sql;
        o[1]=objects;
        return o;
    }
    protected String getTablename() {
        Table table = this.getClass().getAnnotation(Table.class);
        if (table == null) {
            return this.getClass().getSimpleName().toUpperCase();
        } else {
            return table.name();
        }
    }
    public String getIdName() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return getColumnName(field);
            }
        }
        throw new RuntimeException("undefine POJO @Id");
    }
    public void caculationColumnList() {
        if (columnMap.containsKey(this.getClass())) {
            return;
        }
        Field[] fields = this.getClass().getDeclaredFields();
        List<String> columnList = new ArrayList<String>(fields.length);
        for (Field field : fields) {
            if(field.isAnnotationPresent(Transient.class)){
                continue;
            }
            columnList.add(field.getName());
        }
        columnMap.put(this.getClass(), columnList);
    }
    private String getColumnName(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            return getColumnName(field);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getColumnName(Field field) {
        try {
            String fieldName = field.getName();
            if (!field.isAnnotationPresent(Column.class)) {
                return fieldName;
            }
            Column column = field.getAnnotation(Column.class);
            String columnName = column.name().trim();
            if (columnName == null || columnName.isEmpty()) {
                return fieldName;
            } else {
                return columnName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
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
