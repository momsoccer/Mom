package com.mom.soccer.dataDto;

import java.io.Serializable;

/**
 * Created by sungbo on 2016-08-15.
 * 강사 정보를 복합으로 담은 객체
 */
public class InsInfoVo implements Serializable{

    private int instructorid;
    private int uid;
    private String name;
    private String profileimgurl;
    private String teamname;
    private String emblem;
    private int teamvideopoint;
    private int teamwordpoint;
    private int pubvideopoint;
    private int pubwordpoint;
    private int teamjoinpoint;
    private int teammembercount;
    private int questioncount;
    private int answercount;
    private int estimation;
    private int insvidecount;
    private int teampasspoint;
    private int pubpasspoint;
    private String change_teamcreationdate;
    private int teamid;

    //강사정보 추가
    private int age;
    private int playeryear;
    private int instructoryear;

    private String address;
    private String currentteamname;
    private String career1;
    private String career2;
    private String career3;
    private String career4;
    private String career5;
    private String resume;

    //search object
    private String searchname;
    private int queryRow;
    private String orderbytype;

    public InsInfoVo(){}

    @Override
    public String toString() {
        return "InsInfoVo{" +
                "instructorid=" + instructorid +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", teamname='" + teamname + '\'' +
                ", emblem='" + emblem + '\'' +
                ", teamvideopoint=" + teamvideopoint +
                ", teamwordpoint=" + teamwordpoint +
                ", pubvideopoint=" + pubvideopoint +
                ", pubwordpoint=" + pubwordpoint +
                ", teamjoinpoint=" + teamjoinpoint +
                ", teammembercount=" + teammembercount +
                ", questioncount=" + questioncount +
                ", answercount=" + answercount +
                ", estimation=" + estimation +
                ", insvidecount=" + insvidecount +
                ", teampasspoint=" + teampasspoint +
                ", pubpasspoint=" + pubpasspoint +
                ", change_teamcreationdate='" + change_teamcreationdate + '\'' +
                ", teamid=" + teamid +
                ", age=" + age +
                ", playeryear=" + playeryear +
                ", instructoryear=" + instructoryear +
                ", address='" + address + '\'' +
                ", currentteamname='" + currentteamname + '\'' +
                ", career1='" + career1 + '\'' +
                ", career2='" + career2 + '\'' +
                ", career3='" + career3 + '\'' +
                ", career4='" + career4 + '\'' +
                ", career5='" + career5 + '\'' +
                ", resume='" + resume + '\'' +
                ", searchname='" + searchname + '\'' +
                ", queryRow=" + queryRow +
                ", orderbytype='" + orderbytype + '\'' +
                '}';
    }

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public int getInstructorid() {
        return instructorid;
    }

    public void setInstructorid(int instructorid) {
        this.instructorid = instructorid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getEmblem() {
        return emblem;
    }

    public void setEmblem(String emblem) {
        this.emblem = emblem;
    }

    public int getTeamvideopoint() {
        return teamvideopoint;
    }

    public void setTeamvideopoint(int teamvideopoint) {
        this.teamvideopoint = teamvideopoint;
    }

    public int getTeamwordpoint() {
        return teamwordpoint;
    }

    public void setTeamwordpoint(int teamwordpoint) {
        this.teamwordpoint = teamwordpoint;
    }

    public int getPubvideopoint() {
        return pubvideopoint;
    }

    public void setPubvideopoint(int pubvideopoint) {
        this.pubvideopoint = pubvideopoint;
    }

    public int getPubwordpoint() {
        return pubwordpoint;
    }

    public void setPubwordpoint(int pubwordpoint) {
        this.pubwordpoint = pubwordpoint;
    }

    public int getTeamjoinpoint() {
        return teamjoinpoint;
    }

    public void setTeamjoinpoint(int teamjoinpoint) {
        this.teamjoinpoint = teamjoinpoint;
    }

    public int getTeammembercount() {
        return teammembercount;
    }

    public void setTeammembercount(int teammembercount) {
        this.teammembercount = teammembercount;
    }

    public int getQuestioncount() {
        return questioncount;
    }

    public void setQuestioncount(int questioncount) {
        this.questioncount = questioncount;
    }

    public int getAnswercount() {
        return answercount;
    }

    public void setAnswercount(int answercount) {
        this.answercount = answercount;
    }

    public int getEstimation() {
        return estimation;
    }

    public void setEstimation(int estimation) {
        this.estimation = estimation;
    }

    public int getInsvidecount() {
        return insvidecount;
    }

    public void setInsvidecount(int insvidecount) {
        this.insvidecount = insvidecount;
    }

    public int getTeampasspoint() {
        return teampasspoint;
    }

    public void setTeampasspoint(int teampasspoint) {
        this.teampasspoint = teampasspoint;
    }

    public int getPubpasspoint() {
        return pubpasspoint;
    }

    public void setPubpasspoint(int pubpasspoint) {
        this.pubpasspoint = pubpasspoint;
    }

    public String getChange_teamcreationdate() {
        return change_teamcreationdate;
    }

    public void setChange_teamcreationdate(String change_teamcreationdate) {
        this.change_teamcreationdate = change_teamcreationdate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPlayeryear() {
        return playeryear;
    }

    public void setPlayeryear(int playeryear) {
        this.playeryear = playeryear;
    }

    public int getInstructoryear() {
        return instructoryear;
    }

    public void setInstructoryear(int instructoryear) {
        this.instructoryear = instructoryear;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrentteamname() {
        return currentteamname;
    }

    public void setCurrentteamname(String currentteamname) {
        this.currentteamname = currentteamname;
    }

    public String getCareer1() {
        return career1;
    }

    public void setCareer1(String career1) {
        this.career1 = career1;
    }

    public String getCareer2() {
        return career2;
    }

    public void setCareer2(String career2) {
        this.career2 = career2;
    }

    public String getCareer3() {
        return career3;
    }

    public void setCareer3(String career3) {
        this.career3 = career3;
    }

    public String getCareer4() {
        return career4;
    }

    public void setCareer4(String career4) {
        this.career4 = career4;
    }

    public String getCareer5() {
        return career5;
    }

    public void setCareer5(String career5) {
        this.career5 = career5;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getSearchname() {
        return searchname;
    }

    public void setSearchname(String searchname) {
        this.searchname = searchname;
    }

    public int getQueryRow() {
        return queryRow;
    }

    public void setQueryRow(int queryRow) {
        this.queryRow = queryRow;
    }

    public String getOrderbytype() {
        return orderbytype;
    }

    public void setOrderbytype(String orderbytype) {
        this.orderbytype = orderbytype;
    }
}
