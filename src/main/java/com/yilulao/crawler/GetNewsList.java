package com.yilulao.crawler;

import com.oahzuw.utils.OuNet;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oahzuw@gmail.com on 2016/4/25.
 */
public class GetNewsList {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetNewsList.class);

    public static List<String> get(String url) {

        String reg = "^http://www.7y7.com/.*?.html$";
        List<String> list = new ArrayList<String>();
        Document pageDoc = null;
        int i = 1;
        while (i <= 159) { // 使用while 是由于分页
            String pageSource = OuNet.getPageSource(url + "index_" + i + ".html", OuNet.HTTPCLIENT, "", "", 30f);
            if (!StringUtils.isNoneBlank(pageSource)) {
                pageSource = OuNet.getPageSource(url, OuNet.HTMLUNITDRIVER, "", "", 30f);
                if (!StringUtils.isNoneBlank(pageSource)) {
                    LOGGER.info("===================== !!! ERROR !!! GET PAGESOURECE FAIL ! =====================");
                }
            }
            pageDoc = Jsoup.parse(pageSource);
            if (pageDoc.title().contains("Page Not Found")) {
                i = 255;
            }

            Elements tagA = pageDoc.select("a");
            System.out.println(tagA.size());
            if (tagA.size() < 80) {
                i = 255;
            }
            for (Element a : tagA) {
                String href = "http://www.7y7.com" + a.attr("href");
                if (href.matches(reg)) {
                    if (!list.contains(href)) {
                        list.add(href);
                    }
                }
            }
            i++;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }

        return list;
    }

    @Test
    public void testGet() { //用于 DEBUG
//        String url = "http://www.irrawaddy.com/election";
        String url = "http://www.irrawaddy.com/education ";
        List<String> list = get(url);
        System.out.println(list.size() + "   " + list);
    }
}
