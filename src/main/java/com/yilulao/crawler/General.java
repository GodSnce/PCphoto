package com.yilulao.crawler;

import com.mongodb.*;
import com.oahzuw.utils.OuDownFile;
import com.oahzuw.utils.OuMongoDBUtil;
import com.oahzuw.utils.OuMybatisHelper;
import com.oahzuw.utils.OuRuntimeUtil;
import com.yilulao.mapper.MeinvtuMapper;
import com.yilulao.pojo.Meinvtu;
import com.yilulao.pojo.NewsList;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.InputStream;
import java.util.*;

/**
 * Created by Administrator on 2016/4/27.
 */
public class General {
    public static void main(String[] args) {
        String baseFilePath = null;
        if (OuRuntimeUtil.isWindows()) {
            baseFilePath = ResourceBundle.getBundle("file").getString("baseFilePathWin");
        } else if (OuRuntimeUtil.isNix()) {
            baseFilePath = ResourceBundle.getBundle("file").getString("baseFilePathLinux");
        }
        // mysql
        SqlSession sqlSession = OuMybatisHelper.getSqlSession();

//        NewsListMapper nlMapper = sqlSession.getMapper(NewsListMapper.class);
//
//        Example example = new Example(NewsList.class);
//        example.createCriteria().andLike("url", "%irrawaddy.com%").andEqualTo("remark", "11").andEqualTo("countryCN", "缅甸");
//
//        List<NewsList> newsLists = nlMapper.selectByExample(example);
//        System.out.println(newsLists.size());

        NewsList news0 = new NewsList();
//        news0.setCountryCN("中国");
//        news0.setCountryXX("7v7");
        news0.setFirstclassCN("图库");
//        news0.setFirstclassXX("");
        news0.setSecondclassCN("性感尤物");
//        news0.setSecondclassXX("");
        news0.setUrl("http://www.7y7.com/pic/youwu/");

        NewsList news1 = new NewsList();
//        news1.setCountryCN("中国");
//        news1.setCountryXX("7v7");
        news1.setFirstclassCN("图库");
//        news1.setFirstclassXX("");
        news1.setSecondclassCN("清纯美女");
//        news1.setSecondclassXX("");
        news1.setUrl("http://www.7y7.com/pic/meinv/");

        NewsList news2 = new NewsList();
//        news2.setCountryCN("中国");
//        news2.setCountryXX("7v7");
        news2.setFirstclassCN("图库");
//        news2.setFirstclassXX("");
        news2.setSecondclassCN("明星写真");
//        news2.setSecondclassXX("");
        news2.setUrl("http://www.7y7.com/pic/xiezhen/");

        List<NewsList> newsLists = new ArrayList<NewsList>();

        newsLists.add(news0);
        newsLists.add(news1);
        newsLists.add(news2);

        //
        OuMongoDBUtil mongoDBUtilUrl = new OuMongoDBUtil();

        DBCollection dbCollection = OuMongoDBUtil.getCollection("IMAGE", "meinvtu");
        DBCollection dbCollectionUrl = mongoDBUtilUrl.GetCollection("URL", "url");

        try {

            for (NewsList news : newsLists) {
                String url = news.getUrl();
                List<String> newsList = GetNewsList.get(url);
                System.out.println(url + "   " + newsList.size());
                System.out.println(newsList);

//                String m_srcType = newsList.get(newsList.size() - 1);
//                int z = 1;
                for (int i = 0; i < newsList.size() - 1; i++) {
//                    if (z > 2) {
//                        continue;
//                    }
                    String newsurl = newsList.get(i);
                    ParseNews.parse(news, newsurl, dbCollection, dbCollectionUrl, baseFilePath);
                    Thread.sleep(5000 + new Random().nextInt(10000));
//                    z++;
                }
            }
        } catch (Exception e) {
        } finally {
            OuMongoDBUtil.close(dbCollection);
            OuMongoDBUtil.close(dbCollectionUrl);
            sqlSession.close();
        }
    }

    @Test
    public void cleanMongo() {
        DBCollection dbCollection = OuMongoDBUtil.getCollection("IMAGE", "meinvtu");
        try {
            DBCursor dbCursor = dbCollection.find();
            System.out.println(dbCursor.size());
            for (DBObject dbo : dbCursor) {

                String m_imageUrl = dbo.get("m_ImageUrl").toString();
                String[] split = m_imageUrl.split("\t");
                String m_imageUrlNew = "";
                for (String str : split) {
                    if (str.startsWith("/images")) {
                        m_imageUrlNew += str + "\t";
                        continue;
                    }
                    InputStream inputStream = null;
                    try {
                        inputStream = OuDownFile.getInputStream("http://img.ononnews.com/images" + str, 60f);
                        Map<String, String> map = OuDownFile.getImgWidthAndHeight(inputStream);
                        m_imageUrlNew += "/images" + str + "?w=" + map.get("width") + "&h=" + map.get("height") + "\t";
                    } catch (Exception e) {
                        System.out.println(dbo.get("_id"));
                        System.out.println(str);
                        e.printStackTrace();
                        m_imageUrlNew += str + "\t";
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                }
                if (StringUtils.isNoneBlank(m_imageUrlNew)) {
                    BasicDBObject res = new BasicDBObject();
                    BasicDBObject doc = new BasicDBObject();
                    res.put("m_ImageUrl", m_imageUrlNew.trim());
                    doc.put("$set", res);
                    dbCollection.update(dbo, doc);
                    System.out.println("update");
                }
            }
        } catch (Exception e) {
        } finally {
            OuMongoDBUtil.close(dbCollection);
        }
    }

    @Test
    public void cleanMongoImgCount() {
        DBCollection dbCollection = OuMongoDBUtil.getCollection("IMAGE", "meinvtu");
        try {
            DBCursor dbCursor = dbCollection.find();
            System.out.println(dbCursor.size());
            for (DBObject dbo : dbCursor) {

                String m_imageUrl = dbo.get("m_ImageUrl").toString();
                String[] split = m_imageUrl.split("\t");
                Integer imageCount = (Integer) dbo.get("m_ImageCount");
                if (imageCount != split.length) {
                    BasicDBObject res = new BasicDBObject();
                    BasicDBObject doc = new BasicDBObject();
                    res.put("m_ImageCount", split.length);
                    doc.put("$set", res);
                    dbCollection.update(dbo, doc);
                    System.out.println("update");
                }
            }
        } catch (Exception e) {
        } finally {
            OuMongoDBUtil.close(dbCollection);
        }
    }

    @Test
    public void toMysql() {
        DBCollection dbCollection = OuMongoDBUtil.getCollection("IMAGE", "meinvtu");
        SqlSession sqlSession = OuMybatisHelper.getSqlSession();
        try {
            MeinvtuMapper imagesMapper = sqlSession.getMapper(MeinvtuMapper.class);
            DBCursor dbCursor = dbCollection.find();
            System.out.println(dbCursor.size());
            for (DBObject dbo : dbCursor) {
                try {
                    Meinvtu images = new Meinvtu();
                    images.setM_language("zh_CN");
                    images.setM_Title(dbo.get("m_Title").toString());
                    images.setM_Url(dbo.get("m_Url").toString());
                    images.setM_Source(dbo.get("m_Source").toString());
                    try {
                        images.setM_Summary((String) dbo.get("m_Summary"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    images.setM_firstclass(dbo.get("m_firstclass").toString());
                    images.setM_secondclass(dbo.get("m_secondclass").toString());
                    images.setM_ImageUrl(dbo.get("m_ImageUrl").toString());
                    images.setM_ImageUrlAbs(dbo.get("m_ImageUrlAbs").toString());
                    images.setM_ImageCount((Integer) dbo.get("m_ImageCount"));
                    images.setM_crawlTime(dbo.get("m_crawlTime").toString());
                    images.setM_timestamp((Long) dbo.get("m_timestamp"));
                    imagesMapper.insert(images);
                    sqlSession.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        } finally {
            OuMongoDBUtil.close(dbCollection);
            sqlSession.close();
        }
    }

}
