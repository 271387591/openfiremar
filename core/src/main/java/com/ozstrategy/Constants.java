package com.ozstrategy;

public interface Constants {
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String JdbcDriver_mysql = "com.mysql.jdbc.Driver";
    public static final String JdbcUrl_mysql = "jdbc:mysql://{0}:{1}/{2}?useUnicode=true&characterEncoding=utf-8";
    public static final String JdbcDriver_oracle = "oracle.jdbc.driver.OracleDriver";
    public static final String JdbcUrl_oralce = "jdbc:oracle:thin:@{0}:{1}:{2}";
    public final static String USER_NOT_Found="USER_NOT_Found";
    public final static String USER_NOT_Authentication="USER_NOT_Authentication";
    public final static String YMD="yyyy-MM-dd";
    public final static String YMDHMS="yyyy-MM-dd HH:mm:ss";
    public static final String imDataDir=System.getProperty("imdata");
    public static final String picFileDir="pictures";
    public static final String exportFileDir="export";
    public static final String appFileDir="export";
    public static final Integer LIMIT=25;
    
} 
