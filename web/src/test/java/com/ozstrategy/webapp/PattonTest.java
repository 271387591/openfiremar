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
import java.util.UUID;
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
        String uid = UUID.randomUUID().toString();
        System.out.println(uid);
        
    }

    /***
     * 接口名称：注册
     *接口参数: 
     * 1、username：用户名，请验证用户名规则如上的testUsername
     * 2、password：用户密码
     * 3、nickName：用户昵称
     * 3、projectId：工程ID
     * 3、activationCode：工程激活码
     * 返回参数：
      
     * 
     * 具体格式请参照下面得junit测试
     * 
     * 
     * 
     * @throws Exception
     */
    @Test
    public void testRegister() throws Exception{
        String url="http://120.24.234.71/im/app/register";
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("username", "admin"));
        nvps.add(new BasicNameValuePair("password", "tomcat"));
        nvps.add(new BasicNameValuePair("nickName", "李浩"));
        nvps.add(new BasicNameValuePair("projectId", "8"));
        nvps.add(new BasicNameValuePair("activationCode", "111"));

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

    /***
     * 接口名称：登陆             d
     * 接口参数：
     * 1、username：用户名
     * 2、password：密码
     * 3、projectId：工程ID
     * 
     * 返回参数：
     * 1、id，用户ID
     * 2、username，用户名
     * 3、openfireUsername，登陆openfire的用户名
     * 4、nickName，昵称
     * 5、authentication是否认证
     * 6、sessionId，用户登陆唯一回话，该参数文件上传时需要
     * 7、projectId，用户所属工程ID
     * 8、projectName，工程名称
     * 9、manager是否为工程管理员
     * 10、roles用户角色
     * 11、features用户权限
     *
     * 
     * @throws Exception
     */
    @Test
    public void testLogin() throws Exception{
        String url="http://120.24.234.71/im/app/login";
//        String url="http://localhost:9095/openfiremar/app/login";
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("username", "admin"));
        nvps.add(new BasicNameValuePair("password", "111111"));
        nvps.add(new BasicNameValuePair("projectId", "7"));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        body = EntityUtils.toString(entity);
        System.out.println(body);
        httpclient.getConnectionManager().shutdown();
        
    }

    /***
     * 接口名称：获取工程列表
     * 接口参数：无
     * 返回参数：
     * 1、id，工程ID
     * 2、name，工程名称
     * 3、serialNumber，工程序号
     * 4、description，工程描述
     * 5、activationCode，工程激活码
     * @throws Exception
     */
    @Test
    public void testGetProjects() throws Exception{
        String url="http://120.24.234.71/im/app/getProjects";
//        String url="http://localhost:9095/openfiremar/app/getProjects";
        HttpPost httpost = new HttpPost(url);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        body = EntityUtils.toString(entity);
        System.out.println(body);
        httpclient.getConnectionManager().shutdown();
        
    }
    /**
     * 接口参数：
     * start:数据起始量，比如：从第0条数据开始，start=0,从第34条数据开始：start=34 （必须，并且为数字）
     * limit:每次获取的数据量,默认每次25条，（可以不传，默认25条）
     * projectId:工程ID（必须）
     *
     * 参数示例：比如每页显示30条数据，参数传递为：
     * 第一页：start=0&limit=30
     * 第二页：start=31&limit=30
     * 第三页：start=61&limit=30
     * .......
     *
     * 请求方式：POST/GET
     * 返回字段说明：
     * id:消息ID
     * fromId：发送者用户名
     * fromNick：发送者昵称
     * toId:工程ID
     * toNick：工程名称
     * type：消息类型，0表示图片，1表示音频
     * message：消息类容
     * createDate：发送时间
     * manager：是否为管理员消息，1表示管理员，0表示普通用户
     * deleted：表示是否为删除的数据，1表示是，0表示否
     * @throws Exception
     */
    
     @Test
    public void testGetHistory() throws Exception{
        String url="http://120.24.234.71/im/app/getHistory";
//        String url="http://localhost:9095/openfiremar/app/getHistory";
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("start", "0"));
        nvps.add(new BasicNameValuePair("limit", "25"));
        nvps.add(new BasicNameValuePair("projectId", "2"));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        body = EntityUtils.toString(entity);
        System.out.println(body);
        httpclient.getConnectionManager().shutdown();
        
    }
     /**
     * 接口参数：
     * start:数据起始量，比如：从第0条数据开始，start=0,从第34条数据开始：start=34 （必须，并且为数字）
     * limit:每次获取的数据量,默认每次25条，（可以不传，默认25条）
     * projectId:工程ID（必须）
     *
     * 参数示例：比如每页显示30条数据，参数传递为：
     * 第一页：start=0&limit=30
     * 第二页：start=31&limit=30
     * 第三页：start=61&limit=30
     * .......
     *
     * 请求方式：POST/GET
     * 返回字段说明：
     * id:消息ID
     * fromId：发送者用户名
     * fromNick：发送者昵称
     * toId:工程ID
     * toNick：工程名称
     * type：消息类型，0表示图片，1表示音频
     * message：消息类容
     * createDate：发送时间
     * manager：是否为管理员消息，1表示管理员，0表示普通用户
     * deleted：表示是否为删除的数据，1表示是，0表示否
     * messageId：openfire每次发送消息时的id,该参数在管理员删除信息时需要
     * @throws Exception
     */
    
     @Test
    public void testGetManagerMessage() throws Exception{
        String url="http://120.24.234.71/im/app/getManagerMessage";
//        String url="http://localhost:9095/openfiremar/app/getHistory";
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("start", "0"));
        nvps.add(new BasicNameValuePair("limit", "25"));
        nvps.add(new BasicNameValuePair("projectId", "2"));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        body = EntityUtils.toString(entity);
        System.out.println(body);
        httpclient.getConnectionManager().shutdown();
        
    }
    /**
     * 接口参数：
     * projectId:工程ID（必须）
     * messageId:openfire每次发送消息时的id,
     *
     *
     * 请求方式：POST
     * @throws Exception
     */
    @Test
    public void testDeleteMessage() throws Exception{
        String url="http://120.24.234.71/im/app/deleteMessage";
//        String url="http://localhost:9095/openfiremar/app/getHistory";
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("projectId", "2"));
        nvps.add(new BasicNameValuePair("messageId", "SSSSS"));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        body = EntityUtils.toString(entity);
        System.out.println(body);
        httpclient.getConnectionManager().shutdown();
        
    }

    /***
     * 接口名称：搜索
     * 接口参数：
     * 1、startTime（可选），开始时间，格式：yyyy-MM-dd HH:mm:ss
     * 2、endTime（可选），结束时间，格式：yyyy-MM-dd HH:mm:ss
     * 3、start（必须），搜索起始位置，详细说明请参照获取历史消息或者其他类似接口
     * 4、limit（可选，默认25条），搜索的条数
     * 5、fromNick（可选），用户昵称
     * 6、message（可选），消息关键字
     * 7、projectId(必须)，工程ID
     * 返回参数：
     * 详细请参照请求历史消息接口
     * 
     * 
     * 
     * @throws Exception
     */
    
    
    @Test
    public void testSearch() throws Exception{
//        String url="http://120.24.234.71/im/app/search";
        String url="http://localhost:9095/openfiremar/app/search";
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

//        nvps.add(new BasicNameValuePair("message", "admin"));
        nvps.add(new BasicNameValuePair("projectId", "7"));
        nvps.add(new BasicNameValuePair("startTime", "2014-12-23 12:23:34"));
        nvps.add(new BasicNameValuePair("endTime", "2015-12-23 12:23:34"));
        nvps.add(new BasicNameValuePair("start", "0"));
        nvps.add(new BasicNameValuePair("limit", "2"));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();

        String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        body = EntityUtils.toString(entity);
        System.out.println(body);
        httpclient.getConnectionManager().shutdown();
        
    }
    
    
    
    
    @Test
    public void testUpload() throws Exception{
        String url="http://120.24.234.71/im/app/upload";
//        String url="http://localhost:9095/openfiremar/app/upload";
        HttpPost httppost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        File file=new File("/Users/lihao/Downloads/codes.xml");
        FileBody bin = new FileBody(file);
        StringBody username = new StringBody("admin");
        StringBody sessionId = new StringBody("72c37e99-06fd-4f8a-91fd-c41c93ca1fde\"");
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
