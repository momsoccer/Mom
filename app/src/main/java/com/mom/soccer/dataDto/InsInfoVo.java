package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-08-15.
 * 강사 정보를 복합으로 담은 객체
 */
public class InsInfoVo {

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

    //search object
    private String searchname;
    private int queryRow;
    private String orderbytype;

    public InsInfoVo(){}

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

    public String getChange_teamcreationdate() {
        return change_teamcreationdate;
    }

    public void setChange_teamcreationdate(String change_teamcreationdate) {
        this.change_teamcreationdate = change_teamcreationdate;
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
                ", change_teamcreationdate='" + change_teamcreationdate + '\'' +
                ", searchname='" + searchname + '\'' +
                ", queryRow=" + queryRow +
                ", orderbytype='" + orderbytype + '\'' +
                '}';
    }
}
