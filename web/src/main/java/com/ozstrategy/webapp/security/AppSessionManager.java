package com.ozstrategy.webapp.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihao on 1/1/15.
 */
public class AppSessionManager {
    private static Map<String,String> map=new ConcurrentHashMap<String, String>();
    public static void put(String username,String sessionId){
        map.put(username,sessionId);
    }
    public static String get(String username){
        return map.get(username);
    }
    public static void clear(){
        map.clear();
    }
}
