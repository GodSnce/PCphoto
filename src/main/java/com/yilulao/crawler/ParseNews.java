package com.yilulao.crawler;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.oahzuw.po.NewsEntity;
import com.oahzuw.utils.*;
import com.qcloud.cosapi.TencCosUtil;
import com.yilulao.pojo.NewsList;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by oahzuw@gmail.com on 2016/4/25.
 */
public class ParseNews {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseNews.class);

    public static void parse(NewsList news, String url, DBCollection dbCollection, DBCollection dbCollectionUrl, String baseFilePath) {
        //判断是否已抓取
        boolean inDb = OuMongoDBUtil.isInDb("url", url, dbCollectionUrl);
        if (!inDb) {
            LOGGER.info("It has crawled !!!");
            return;
        }

        try {
            String pageSource = OuNet.getPageSource(url, OuNet.HTMLUNITDRIVER, "", "", 30);
            if (!StringUtils.isNoneBlank(pageSource)) {
                try {
                    Thread.sleep(1000 * 60 * 1);
                } catch (InterruptedException e) {
                }
//                pageSource = OuNet.getPageSource(url, OuNet.HTTPCLIENT, "", "", 30);

                try {
                    Thread.sleep(1000 * 60 * 1);
                } catch (InterruptedException e) {
                }
                String drivePath = null;
                String jsPath = null;
                if (OuRuntimeUtil.isNix()) {
                    drivePath = "/1TB/wuzhao/phantomjs/phantomjs";
                    jsPath = "/1TB/wuzhao/phantomjs/exec_getPageSource.js";
                } else if (OuRuntimeUtil.isWindows()) {
                    drivePath = "D:/Vbox/phantomjs.exe";
                    jsPath = "D:/Vbox/exec_getPageSource.js";
                }
                if (StringUtils.isNoneBlank(drivePath)) {
                    pageSource = OuNet.getPageSource(url, OuNet.PHANTOMJS, drivePath, jsPath, 1f);
                }
                if (!StringUtils.isNoneBlank(pageSource)) {
                    return;
                }
            }
            Document pageDoc = Jsoup.parse(pageSource);

            //去除脚本 开始
            try {
                pageDoc.select("script").remove();
            } catch (Exception e) {
            }
            try {
                pageDoc.select("noscript").remove();
            } catch (Exception e) {
            }

            //去除脚本 结束


            NewsEntity newsEntity = new NewsEntity();

            String title = null;
            try {
                title = pageDoc.select("div.pic-title h1").text();
            } catch (Exception e) {
            }
            if (!StringUtils.isNoneBlank(title)) {
                LOGGER.info("=================== title not get url: " + url);
                return;
            }

            newsEntity.setM_Title(title);

            Element body = null;
            try {
                body = pageDoc.select("ul.pic-ul").get(0);
            } catch (Exception e) {
                LOGGER.info("=================== img body not get url: " + url);
                return;
            }

            String m_ImageUrl = ""; //图片相对路径 本地
            String m_ImageUrlAbs = ""; // 图片绝对路径 internet
            Elements img = body.select("img");
            List<String> imgList = new ArrayList<String>();
            int x = 1;
            String m_Summary = null;

            for (Element e : img) {


                InputStream inputStream = null;
                try {
                    String imgurl = e.attr("src");

                    if (!StringUtils.isNoneBlank(imgurl)) {
                        continue;
                    }

                    if (!imgList.contains(imgurl)) {
                        imgList.add(imgurl);
                    } else {
                        continue;
                    }
                    if (1 == x) {
                        m_Summary = e.attr("alt");
                        x++;
                    }

                    String filePath = OuDownFile.getFilePath("", "/7y7", OuDownFile.getFileSuffix(imgurl)).replace(".Jpeg", ".jpg");
                    inputStream = OuDownFile.getInputStream(1.4f, 1.8f, imgurl, 60f);
                    byte[] bytes = OuDownFile.inputStream2ByteArray(inputStream);

                    String result = TencCosUtil.uploadFile(2, filePath, bytes);

                    String code = JSONObject.parseObject(result).get("code").toString();
//                String resource_path = OuRegexpUtils.doMatch1(result, "\"resource_path\":\"(.*?)\",", Pattern.DOTALL);
                    if ("0".equals(code)) {
                        System.out.println(result);
                        Map<String, String> map = OuDownFile.getImgWidthAndHeight(inputStream);
                        m_ImageUrl += "/images" + filePath + "?w=" + map.get("width") + "&h=" + map.get("height") + "\t";
                        m_ImageUrlAbs += imgurl + "\t";
                    } else {
                        imgList.remove(imgurl);
                    }
                    try {
                        Thread.sleep(1000 + new Random().nextInt(9000));
                    } catch (InterruptedException e1) {
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    OuDownFile.close(inputStream, null);
                }

//                }
            }

            if (StringUtils.isNoneBlank(m_Summary)) {
                newsEntity.setM_Summary(m_Summary);
            }

            if (StringUtils.isNoneBlank(m_ImageUrl)) {
                newsEntity.setM_ImageUrl(m_ImageUrl.trim());
            } else {
                LOGGER.info("没有符合的图片 返回 进行下一个链接");
                //用于 使用 url去重
                BasicDBObject dbObject = new BasicDBObject();
                dbObject.put("url", url);
                dbCollectionUrl.insert(dbObject);
                return;
            }
            if (StringUtils.isNoneBlank(m_ImageUrlAbs)) {
                newsEntity.setM_ImageUrlAbs(m_ImageUrlAbs.trim());
            } else {
                return;
            }

            newsEntity.setM_firstclass(news.getFirstclassCN());
            newsEntity.setM_secondclass(news.getSecondclassCN());
            newsEntity.setM_ImageCount(imgList.size());

            newsEntity.setM_Url(url);
            newsEntity.setM_Source("七丽时尚");
            long timeMillis = System.currentTimeMillis();
            newsEntity.setM_timestamp(timeMillis / 1000);
            newsEntity.setM_crawlTime(OuDateUtils.dateToStr(timeMillis));

            OuMongoDBUtil.add(newsEntity, dbCollection);

            //用于 使用 url去重
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.put("url", url);
            dbCollectionUrl.insert(dbObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void tParse() { //用于 DEBUG
        String url = "";

        String baseFilePath = null;
        if (OuRuntimeUtil.isWindows()) {
            baseFilePath = ResourceBundle.getBundle("file").getString("baseFilePathWin");
        } else if (OuRuntimeUtil.isNix()) {
            baseFilePath = ResourceBundle.getBundle("file").getString("baseFilePathLinux");
        }
        // mysql

        //获取mongodb连接
        DBCollection collection = OuMongoDBUtil.getCollection("20151216", "7v7");
        OuMongoDBUtil mongoDBUtilUrl = new OuMongoDBUtil();
        DBCollection dbCollectionUrl = mongoDBUtilUrl.GetCollection("URL", "urlT");

        NewsList msn = new NewsList();
        msn.setCountryXX("namibia");
        try {
            ParseNews.parse(msn, url, collection, dbCollectionUrl, baseFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OuMongoDBUtil.close(collection);
            OuMongoDBUtil.close(dbCollectionUrl);
        }
    }
}
