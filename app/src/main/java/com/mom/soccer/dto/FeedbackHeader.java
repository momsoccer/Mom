package com.mom.soccer.dto;

import java.io.Serializable;

/**
 * Created by sungbo on 2016-06-20.
 */
public class FeedbackHeader implements Serializable{

    private int feedbackid;
    private String feedbacktype;
    private int missionid;
    private int usermissionid;
    private int uid;
    private int instructorid;
    private int cashpoint;
    private int Frequency;
    private String subject;
    private String pubstatus;
    private String instype;
    private String content;
    private String change_creationdate;
    private String insprofileimgurl;
    private String profileimgurl;
    private String teamname;
    private String change_updatedate;
    //DB Quer
    private String missionname;
    private String insname;
    private String username;
    private float   evalscore;
    private String description;
    private String typename;
    private String youtubeaddr;
    private int totalscore;
    private int level;
    private int replaycount;
    private String recontent;
    private String missiontype;

    //Line...
    private String type;
    private String videoaddr;
    private int    feedbacklineid;

    public String getMissiontype() {
        return missiontype;
    }

    public void setMissiontype(String missiontype) {
        this.missiontype = missiontype;
    }

    public String getRecontent() {
        return recontent;
    }

    public void setRecontent(String recontent) {
        this.recontent = recontent;
    }

    public String getChange_updatedate() {
        return change_updatedate;
    }

    public void setChange_updatedate(String change_updatedate) {
        this.change_updatedate = change_updatedate;
    }

    public int getReplaycount() {
        return replaycount;
    }

    public void setReplaycount(int replaycount) {
        this.replaycount = replaycount;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public FeedbackHeader(){

    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "FeedbackHeader{" +
                "feedbackid=" + feedbackid +
                ", feedbacktype='" + feedbacktype + '\'' +
                ", missionid=" + missionid +
                ", usermissionid=" + usermissionid +
                ", uid=" + uid +
                ", instructorid=" + instructorid +
                ", cashpoint=" + cashpoint +
                ", Frequency=" + Frequency +
                ", subject='" + subject + '\'' +
                ", pubstatus='" + pubstatus + '\'' +
                ", instype='" + instype + '\'' +
                ", content='" + content + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", insprofileimgurl='" + insprofileimgurl + '\'' +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", teamname='" + teamname + '\'' +
                ", missionname='" + missionname + '\'' +
                ", insname='" + insname + '\'' +
                ", username='" + username + '\'' +
                ", evalscore=" + evalscore +
                ", description='" + description + '\'' +
                ", typename='" + typename + '\'' +
                ", youtubeaddr='" + youtubeaddr + '\'' +
                ", type='" + type + '\'' +
                ", videoaddr='" + videoaddr + '\'' +
                ", feedbacklineid=" + feedbacklineid +
                '}';
    }

    public String getYoutubeaddr() {
        return youtubeaddr;
    }

    public void setYoutubeaddr(String youtubeaddr) {
        this.youtubeaddr = youtubeaddr;
    }

    public int getFeedbackid() {
        return feedbackid;
    }

    public void setFeedbackid(int feedbackid) {
        this.feedbackid = feedbackid;
    }

    public String getFeedbacktype() {
        return feedbacktype;
    }

    public void setFeedbacktype(String feedbacktype) {
        this.feedbacktype = feedbacktype;
    }

    public int getMissionid() {
        return missionid;
    }

    public void setMissionid(int missionid) {
        this.missionid = missionid;
    }

    public int getUsermissionid() {
        return usermissionid;
    }

    public void setUsermissionid(int usermissionid) {
        this.usermissionid = usermissionid;
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

    public int getCashpoint() {
        return cashpoint;
    }

    public void setCashpoint(int cashpoint) {
        this.cashpoint = cashpoint;
    }

    public int getFrequency() {
        return Frequency;
    }

    public void setFrequency(int frequency) {
        Frequency = frequency;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPubstatus() {
        return pubstatus;
    }

    public void setPubstatus(String pubstatus) {
        this.pubstatus = pubstatus;
    }

    public String getInstype() {
        return instype;
    }

    public void setInstype(String instype) {
        this.instype = instype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public String getInsprofileimgurl() {
        return insprofileimgurl;
    }

    public void setInsprofileimgurl(String insprofileimgurl) {
        this.insprofileimgurl = insprofileimgurl;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
    }

    public String getMissionname() {
        return missionname;
    }

    public void setMissionname(String missionname) {
        this.missionname = missionname;
    }

    public String getInsname() {
        return insname;
    }

    public void setInsname(String insname) {
        this.insname = insname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getEvalscore() {
        return evalscore;
    }

    public void setEvalscore(float evalscore) {
        this.evalscore = evalscore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoaddr() {
        return videoaddr;
    }

    public void setVideoaddr(String videoaddr) {
        this.videoaddr = videoaddr;
    }

    public int getFeedbacklineid() {
        return feedbacklineid;
    }

    public void setFeedbacklineid(int feedbacklineid) {
        this.feedbacklineid = feedbacklineid;
    }
}
