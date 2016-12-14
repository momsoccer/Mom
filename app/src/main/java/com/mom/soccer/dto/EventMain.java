package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-12-14.
 */

public class EventMain {

    private int mainid;
    private String subject;
    private String description;
    private String location;
    private String img;
    private String img2;
    private String img3;
    private String changeTime;

    private String endflag;
    private String youtube;
    private int agefrom;
    private int ageto;
    private String skill;

    private String insname;
    private String inscareer;
    private int fee;
    private int entrance;
    private int minentrance;
    private String age;
    private int reqcount;
    private String conditions;
    private String locdesc;
    private String insimg;
    private String appsubject;
    private String appdisp;
    private int boardcount;
    private String apptime;

    public EventMain(){}

    public String getApptime() {
        return apptime;
    }

    public void setApptime(String apptime) {
        this.apptime = apptime;
    }

    public String getAppsubject() {
        return appsubject;
    }

    public void setAppsubject(String appsubject) {
        this.appsubject = appsubject;
    }

    public String getAppdisp() {
        return appdisp;
    }

    public void setAppdisp(String appdisp) {
        this.appdisp = appdisp;
    }

    public int getMainid() {
        return mainid;
    }

    public void setMainid(int mainid) {
        this.mainid = mainid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getEndflag() {
        return endflag;
    }

    public void setEndflag(String endflag) {
        this.endflag = endflag;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public int getAgefrom() {
        return agefrom;
    }

    public void setAgefrom(int agefrom) {
        this.agefrom = agefrom;
    }

    public int getAgeto() {
        return ageto;
    }

    public void setAgeto(int ageto) {
        this.ageto = ageto;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getInsname() {
        return insname;
    }

    public void setInsname(String insname) {
        this.insname = insname;
    }

    public String getInscareer() {
        return inscareer;
    }

    public void setInscareer(String inscareer) {
        this.inscareer = inscareer;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getEntrance() {
        return entrance;
    }

    public void setEntrance(int entrance) {
        this.entrance = entrance;
    }

    public int getMinentrance() {
        return minentrance;
    }

    public void setMinentrance(int minentrance) {
        this.minentrance = minentrance;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getReqcount() {
        return reqcount;
    }

    public void setReqcount(int reqcount) {
        this.reqcount = reqcount;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getLocdesc() {
        return locdesc;
    }

    public void setLocdesc(String locdesc) {
        this.locdesc = locdesc;
    }

    public String getInsimg() {
        return insimg;
    }

    public void setInsimg(String insimg) {
        this.insimg = insimg;
    }

    public int getBoardcount() {
        return boardcount;
    }

    public void setBoardcount(int boardcount) {
        this.boardcount = boardcount;
    }


    @Override
    public String toString() {
        return "EventMain{" +
                "mainid=" + mainid +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", img='" + img + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                ", changeTime='" + changeTime + '\'' +
                ", endflag='" + endflag + '\'' +
                ", youtube='" + youtube + '\'' +
                ", agefrom=" + agefrom +
                ", ageto=" + ageto +
                ", skill='" + skill + '\'' +
                ", insname='" + insname + '\'' +
                ", inscareer='" + inscareer + '\'' +
                ", fee=" + fee +
                ", entrance=" + entrance +
                ", minentrance=" + minentrance +
                ", age='" + age + '\'' +
                ", reqcount=" + reqcount +
                ", conditions='" + conditions + '\'' +
                ", locdesc='" + locdesc + '\'' +
                ", insimg='" + insimg + '\'' +
                ", boardcount=" + boardcount +
                '}';
    }
}
