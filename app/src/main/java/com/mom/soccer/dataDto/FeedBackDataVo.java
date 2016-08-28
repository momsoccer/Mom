package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-08-28.
 * 강사 피드백 리스트
 */
public class FeedBackDataVo {

    private String datatype;
    private String profileimgurl;
    private int feedbackid;
    private int uid;
    private int instructorid;
    private String name;
    private String feedbacktype;
    private String missiontypename;
    private String missionname;
    private String description;
    private float evalscore;
    private String change_creationdate;
    private int level;
    private int totalscore;
    private String videoaddr;
    private String toname;
    private String teamname;

    //쿼리를 위한
    private int queryRow;
    private String orderbytype;

    public FeedBackDataVo() {
    }

    @Override
    public String toString() {
        return "FeedBackDataVo{" +
                "datatype='" + datatype + '\'' +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", feedbackid=" + feedbackid +
                ", uid=" + uid +
                ", instructorid=" + instructorid +
                ", name='" + name + '\'' +
                ", feedbacktype='" + feedbacktype + '\'' +
                ", missiontypename='" + missiontypename + '\'' +
                ", missionname='" + missionname + '\'' +
                ", description='" + description + '\'' +
                ", evalscore=" + evalscore +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", level=" + level +
                ", totalscore=" + totalscore +
                ", queryRow=" + queryRow +
                ", orderbytype='" + orderbytype + '\'' +
                '}';
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
    }

    public int getFeedbackid() {
        return feedbackid;
    }

    public void setFeedbackid(int feedbackid) {
        this.feedbackid = feedbackid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getInstructorid() {
        return instructorid;
    }

    public void setInstructorid(int instructorid) {
        this.instructorid = instructorid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedbacktype() {
        return feedbacktype;
    }

    public void setFeedbacktype(String feedbacktype) {
        this.feedbacktype = feedbacktype;
    }

    public String getMissiontypename() {
        return missiontypename;
    }

    public void setMissiontypename(String missiontypename) {
        this.missiontypename = missiontypename;
    }

    public String getMissionname() {
        return missionname;
    }

    public void setMissionname(String missionname) {
        this.missionname = missionname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getEvalscore() {
        return evalscore;
    }

    public void setEvalscore(float evalscore) {
        this.evalscore = evalscore;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
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

    public String getVideoaddr() {
        return videoaddr;
    }

    public void setVideoaddr(String videoaddr) {
        this.videoaddr = videoaddr;
    }
}