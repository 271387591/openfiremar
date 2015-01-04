package com.ozstrategy.webapp;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lihao on 12/30/14.
 */
public class PattonTest {
    @Test
    public void testUsername(){
        Pattern pattern=Pattern.compile("^[a-zA-Z0-9_]{3,16}$");
        Matcher matcher = pattern.matcher("2233");
        System.out.println(matcher.matches());
    }
    @Test
    public void testRegister() throws Exception{
        String url="http://localhost:9095/openfiremar/app/register";
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("username", "admins"));
        nvps.add(new BasicNameValuePair("password", "tomcat"));
        nvps.add(new BasicNameValuePair("nickName", "李浩"));
        nvps.add(new BasicNameValuePair("projectId", "1"));
        nvps.add(new BasicNameValuePair("activationCode", "111111"));

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;

        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();

        String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(body);
        httpclient.getConnectionManager().shutdown();
    }
    @Test
    public void testUpload() throws Exception{
        String url="http://localhost:9095/openfiremar/app/upload";
        HttpPost httppost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("username", "admin1"));
        nvps.add(new BasicNameValuePair("password", ""));
        nvps.add(new BasicNameValuePair("nickName", "2222"));
        nvps.add(new BasicNameValuePair("projectId", "1"));
        nvps.add(new BasicNameValuePair("activationCode", "111111"));
        File file=new File("/Users/lihao/Downloads/codes.xml");
        FileBody bin = new FileBody(file);
        StringBody username = new StringBody("admin");
        StringBody password = new StringBody("13696900475");
        StringBody sessionId = new StringBody("5ia7zimnhl36");
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("username", username);
        reqEntity.addPart("sessionId", sessionId);
        reqEntity.addPart("data", bin);
        httppost.setEntity(reqEntity);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(httppost);
        System.out.println("statusCode is " + response.getStatusLine().getStatusCode());
        HttpEntity resEntity = response.getEntity();
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            System.out.println("返回长度: " + resEntity.getContentLength());
            System.out.println("返回类型: " + resEntity.getContentType());
            InputStream in = resEntity.getContent();
            System.out.println("in is " + in);
            System.out.println(IOUtils.toString(in));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }


    }
}