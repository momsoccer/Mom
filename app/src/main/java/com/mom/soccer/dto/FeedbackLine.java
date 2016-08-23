package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-08-21.
 */
public class FeedbackLine {

    private int feedbacklineid;
    private int feedbackid;
    private String type;
    private String videoaddr;
    private String content;
    private String image1;
    private String image2;
    private String image3;
    private float evalscore;
    private String change_creationdate;

    public FeedbackLine(){}

    public float getEvalscore() {
        return evalscore;
    }

    public void setEvalscore(float evalscore) {
        this.evalscore = evalscore;
    }

    public int getFeedbacklineid() {
        return feedbacklineid;
    }

    public void setFeedbacklineid(int feedbacklineid) {
        this.feedbacklineid = feedbacklineid;
    }

    public int getFeedbackid() {
        return feedbackid;
    }

    public void setFeedbackid(int feedbackid) {
        this.feedbackid = feedbackid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    @Override
    public String toString() {
        return "FeedbackLine{" +
                "feedbacklineid=" + feedbacklineid +
                ", feedbackid=" + feedbackid +
                ", type='" + type + '\'' +
                ", videoaddr='" + videoaddr + '\'' +
                ", content='" + content + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", evalscore=" + evalscore +
                ", change_creationdate='" + change_creationdate + '\'' +
                '}';
    }
}
