package com.yilulao.pojo;

import javax.persistence.*;

/**
 * Created by oahzuw@gmail.com on 2016/6/1.
 * <p/>
 * <p/>
 * CREATE TABLE `images` (
 * `id` int(11) NOT NULL AUTO_INCREMENT,
 * `m_language` varchar(255) DEFAULT NULL,
 * `m_Title` varchar(255) DEFAULT NULL,
 * `m_Url` varchar(255) DEFAULT NULL,
 * `m_Source` varchar(255) DEFAULT NULL,
 * `m_Summary` text,
 * `m_firstclass` varchar(255) DEFAULT NULL,
 * `m_secondclass` varchar(255) DEFAULT NULL,
 * `m_ImageUrl` text,
 * `m_ImageUrlAbs` text,
 * `m_ImageCount` int(10) DEFAULT NULL,
 * `m_crawlTime` varchar(255) DEFAULT NULL,
 * `m_timestamp` int(15) DEFAULT NULL,
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@Table(name = "meinvtu")
public class Meinvtu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "m_language")
    private String m_language;
    @Column(name = "m_Title")
    private String m_Title;
    @Column(name = "m_Url")
    private String m_Url;
    @Column(name = "m_Source")
    private String m_Source;
    @Column(name = "m_Summary")
    private String m_Summary;
    @Column(name = "m_firstclass")
    private String m_firstclass;
    @Column(name = "m_secondclass")
    private String m_secondclass;
    @Column(name = "m_ImageUrl")
    private String m_ImageUrl;
    @Column(name = "m_ImageUrlAbs")
    private String m_ImageUrlAbs;
    @Column(name = "m_ImageCount")
    private Integer m_ImageCount;
    @Column(name = "m_crawlTime")
    private String m_crawlTime;
    @Column(name = "m_timestamp")
    private Long m_timestamp;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getM_language() {
        return m_language;
    }

    public void setM_language(String m_language) {
        this.m_language = m_language;
    }

    public String getM_Title() {
        return m_Title;
    }

    public void setM_Title(String m_Title) {
        this.m_Title = m_Title;
    }

    public String getM_Url() {
        return m_Url;
    }

    public void setM_Url(String m_Url) {
        this.m_Url = m_Url;
    }

    public String getM_Source() {
        return m_Source;
    }

    public void setM_Source(String m_Source) {
        this.m_Source = m_Source;
    }

    public String getM_Summary() {
        return m_Summary;
    }

    public void setM_Summary(String m_Summary) {
        this.m_Summary = m_Summary;
    }

    public String getM_firstclass() {
        return m_firstclass;
    }

    public void setM_firstclass(String m_firstclass) {
        this.m_firstclass = m_firstclass;
    }

    public String getM_secondclass() {
        return m_secondclass;
    }

    public void setM_secondclass(String m_secondclass) {
        this.m_secondclass = m_secondclass;
    }

    public String getM_ImageUrl() {
        return m_ImageUrl;
    }

    public void setM_ImageUrl(String m_ImageUrl) {
        this.m_ImageUrl = m_ImageUrl;
    }

    public String getM_ImageUrlAbs() {
        return m_ImageUrlAbs;
    }

    public void setM_ImageUrlAbs(String m_ImageUrlAbs) {
        this.m_ImageUrlAbs = m_ImageUrlAbs;
    }

    public Integer getM_ImageCount() {
        return m_ImageCount;
    }

    public void setM_ImageCount(Integer m_ImageCount) {
        this.m_ImageCount = m_ImageCount;
    }

    public String getM_crawlTime() {
        return m_crawlTime;
    }

    public void setM_crawlTime(String m_crawlTime) {
        this.m_crawlTime = m_crawlTime;
    }

    public Long getM_timestamp() {
        return m_timestamp;
    }

    public void setM_timestamp(Long m_timestamp) {
        this.m_timestamp = m_timestamp;
    }
}
