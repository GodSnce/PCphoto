package com.yilulao.pojo;

import javax.persistence.*;

/**
 * Created by oahzuw@gmail.com on 2016/1/6.
 */
@Table(name = "en107")
public class NewsList {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    Integer id;
    @Column(name = "countryCN")
    String countryCN;

    @Column(name = "countryXX")
    String countryXX;

    @Column(name = "firstclassCN")
    String firstclassCN;
    @Column(name = "firstclassXX")
    String firstclassXX;
    @Column(name = "secondclassCN")
    String secondclassCN;
    @Column(name = "secondclassXX")
    String secondclassXX;
    @Column(name = "url")
    String url;
    @Column(name = "remark")
    String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstclassCN() {
        return firstclassCN;
    }

    public void setFirstclassCN(String firstclassCN) {
        this.firstclassCN = firstclassCN;
    }

    public String getFirstclassXX() {
        return firstclassXX;
    }

    public void setFirstclassXX(String firstclassXX) {
        this.firstclassXX = firstclassXX;
    }

    public String getSecondclassCN() {
        return secondclassCN;
    }

    public void setSecondclassCN(String secondclassCN) {
        this.secondclassCN = secondclassCN;
    }

    public String getSecondclassXX() {
        return secondclassXX;
    }

    public void setSecondclassXX(String secondclassXX) {
        this.secondclassXX = secondclassXX;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCountryCN() {
        return countryCN;
    }

    public void setCountryCN(String countryCN) {
        this.countryCN = countryCN;
    }

    public String getCountryXX() {
        return countryXX;
    }

    public void setCountryXX(String countryXX) {
        this.countryXX = countryXX;
    }
}
