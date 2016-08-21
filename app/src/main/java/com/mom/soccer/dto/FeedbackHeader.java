package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-06-20.
 */
public class FeedbackHeader {

    private int feedbackid;
    private String feedbacktpe;
    private int missionid;
    private int usermissionid;
    private int uid;
    private int instructorid;
    private int cashpoint;
    private int Frequency;
    private String pubstatus;
    private String subject;
    private String instype;
    private String change_creationdate;

    //라인글
    private String type;
    private String videoaddr;
    private String content;

    public FeedbackHeader(){}

    public FeedbackHeader(int uid) {
        this.uid = uid;
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

    public int getFeedbackid() {
        return feedbackid;
    }

    public void setFeedbackid(int feedbackid) {
        this.feedbackid = feedbackid;
    }

    public String getFeedbacktpe() {
        return feedbacktpe;
    }

    public void setFeedbacktpe(String feedbacktpe) {
        this.feedbacktpe = feedbacktpe;
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

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
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

    @Override
    public String toString() {
        return "FeedbackHeader{" +
                "feedbackid=" + feedbackid +
                ", feedbacktpe='" + feedbacktpe + '\'' +
                ", missionid=" + missionid +
                ", usermissionid=" + usermissionid +
                ", uid=" + uid +
                ", instructorid=" + instructorid +
                ", cashpoint=" + cashpoint +
                ", Frequency=" + Frequency +
                ", pubstatus='" + pubstatus + '\'' +
                ", subject='" + subject + '\'' +
                ", instype='" + instype + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", type='" + type + '\'' +
                ", videoaddr='" + videoaddr + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
