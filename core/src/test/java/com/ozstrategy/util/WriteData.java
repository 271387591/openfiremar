package com.ozstrategy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

/**
 * Created by lihao on 1/29/15.
 */
public class WriteData {
    public static void main(String[] s) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/openfiremar" ;
        String username = "root" ;
        String password = "Passw0rd" ;
        String string="a拉斯克奖的法律框架阿萨德路附近拉萨的就分了家拉屎大家浪费就爱死拉到附近拉萨的弗拉三季度分了就撒旦法律收代理费拉萨的减肥里的水浪费教室里的房间里是大家发链接阿萨德路附近的酸辣粉就";
        String historyInsertSql ="insert into ext_history (fromId,toId, type,createDate,manager,deleted,message,fromNick,toNick) values (?,?,?,?,?,?,?,?,?)";
        for (int j=0;j<5;j++){
            Connection connection = DriverManager.getConnection(url, username, password);
            for(int i=0;i<1000000;i++){
                PreparedStatement pstmt = connection.prepareStatement(historyInsertSql);
                pstmt.setLong(1, Math.max(new Random().nextInt(4),1));
                pstmt.setLong(2, 7L);
                pstmt.setInt(3, 0);
                pstmt.setTimestamp(4, new Timestamp(new Date().getTime()));
                pstmt.setInt(5, 0);
                pstmt.setInt(6, 0);
                pstmt.setString(7,string.substring(new Random().nextInt(string.length() - 2)));
                pstmt.setString(8, "李浩" + Math.max(new Random().nextInt(3),0));
                pstmt.setString(9, "工程一");
                pstmt.executeUpdate();
                pstmt.close();
                System.out.println("index====="+i);
            }
            connection.close();
        }
    }
}
