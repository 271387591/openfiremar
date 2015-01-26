package com.ozstrategy.oz;

/**
 * Created by lihao on 1/13/15.
 */
public class SqlTest {
    public static void main(String[] s){
        User u=new User();
        u.setName("sdfqqqqq111");
        String sql=u.returnInsertSql();
        System.out.println(sql);
        sql=u.returnUpdateSql();
        System.out.println(sql);
        sql=u.returnDeleteSql();
        System.out.println(sql);
        sql=u.returnSelectByIdSql();
        System.out.println(sql);
        
        
        


        
        
        


    }
    
    
}
