package com.ozstrategy.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.dom.ParentNode;
import junit.framework.TestCase;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.w3c.dom.Node;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OzDateUtilTest extends TestCase {
    //~ Instance fields ========================================================

    private final Log log = LogFactory.getLog(OzDateUtilTest.class);

    //~ Constructors ===========================================================

    public OzDateUtilTest(String name) {
        super(name);
    }
    
//    @Test
    public void testJsoup() throws Exception{
        String path=OzDateUtilTest.class.getClassLoader().getResource("jsoup.txt").getPath();
        String html= FileUtils.readFileToString(new File(path));
        Document document = Jsoup.parse(html);
//        Elements elements =  document.select("table[xtype=table]").select("textarea[xtype=textareafield]").not("table[xtype=detailGrid]:has(textarea[xtype=textareafield])");
//        Elements elements =  document.select("table[xtype=detailGrid]:not(textarea[xtype=textareafield])");//.select("textarea[xtype=textareafield]").not("table[xtype=detailGrid]:has(textarea[xtype=textareafield])");
        Elements tables= document.select("table[xtype=table]");
        Iterator<Element> tableIterator=tables.iterator();
        while (tableIterator.hasNext()){
            Element table=tableIterator.next();
            Elements elements =  table.select("textarea[xtype=textareafield]");//.select("textarea[xtype=textareafield]").not("table[xtype=detailGrid]:has(textarea[xtype=textareafield])");
            Iterator<Element> iterator =  elements.iterator();
            while (iterator.hasNext()){
                Element element=iterator.next();
                System.out.println("name=="+element.attributes().get("name"));
                System.out.println("txtlabel=="+element.attributes().get("txtlabel"));
                System.out.println("xtype==="+element.attributes().get("xtype"));
                System.out.println("html=="+element.outerHtml());
            }
        }
    }
    @Test
    public void testProcessElementType() throws Exception{
//        String path=OzDateUtilTest.class.getClassLoader().getResource("jsoup2.json").getPath();
//        String html= FileUtils.readFileToString(new File(path));
//        ObjectMapper objectMapper=new ObjectMapper();
//        List<Map<String,Object>> fieldList = objectMapper.readValue(html, List.class);
//        parseEl(fieldList,null);
        System.out.println(System.getProperty("java.io.tmpdir"));
//        
    }
    
}
