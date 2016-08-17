package com.mom.soccer.ins;

/**
 * Created by sungbo on 2016-08-17.
 */
public class InstructorVideoVo {

    private int videoid;
    private int instructorid;
    private String youtubeaddr;
    private String videoaddr;
    private String subject;
    private String description;
    private String relationmission;
    private String change_creationdate;
    private String change_updatedate;

    public InstructorVideoVo(){}

    @Override
    public String toString() {
        return "InstructorVideoVo{" +
                "videoid=" + videoid +
                ", instructorid=" + instructorid +
                ", youtubeaddr='" + youtubeaddr + '\'' +
                ", videoaddr='" + videoaddr + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", relationmission='" + relationmission + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", change_updatedate='" + change_updatedate + '\'' +
                '}';
    }

    public int getVideoid() {
        return videoid;
    }

    public void setVideoid(int videoid) {
        this.videoid = videoid;
    }

    public int getInstructorid() {
        return instructorid;
    }

    public void setInstructorid(int instructorid) {
        this.instructorid = instructorid;
    }

    public String getYoutubeaddr() {
        return youtubeaddr;
    }

    public void setYoutubeaddr(String youtubeaddr) {
        this.youtubeaddr = youtubeaddr;
    }

    public String getVideoaddr() {
        return videoaddr;
    }

    public void setVideoaddr(String videoaddr) {
        this.videoaddr = videoaddr;
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

    public String getRelationmission() {
        return relationmission;
    }

    public void setRelationmission(String relationmission) {
        this.relationmission = relationmission;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public String getChange_updatedate() {
        return change_updatedate;
    }

    public void setChange_updatedate(String change_updatedate) {
        this.change_updatedate = change_updatedate;
    }
}
