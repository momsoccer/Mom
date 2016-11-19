package com.mom.soccer.dto;


import java.io.Serializable;

public class Mission implements Serializable{

    private int missionid;
    private int categoryid;
    private int typeid;
    private String typename;
    private int sequence;
    private String missionname;
    private String description;
    private String precon;
    private String videoaddr;
    private String fullyoutubeaddr;
    private String youtubeaddr;
    private String enabled;
    private String feetype;
    private int grade;          //미션업로드점수
    private int passgrade;      //미션클리어점수
    private String creationdate;
    private String updatedate;
    private String change_creationdate;
    private String change_updatedate;
    private int opencount;
    private int getpoint;
    private int escapepoint;
    private int uid;
    private int uploadcount;
    private int missionpasscount;

    //영문 언어 추가
    private String enname;
    private String endescription;
    private String enprecon;

    //영문 중문
    private String cnname;
    private String cndescription;
    private String cnprecon;

    public Mission(){}

    public Mission(int missionid, String typename, String missionname, String description, String precon, int grade, int passgrade) {
        this.missionid = missionid;
        this.typename = typename;
        this.missionname = missionname;
        this.description = description;
        this.precon = precon;
        this.grade = grade;
        this.passgrade = passgrade;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getOpencount() {
        return opencount;
    }

    public void setOpencount(int opencount) {
        this.opencount = opencount;
    }

    public int getGetpoint() {
        return getpoint;
    }

    public void setGetpoint(int getpoint) {
        this.getpoint = getpoint;
    }

    public int getEscapepoint() {
        return escapepoint;
    }

    public void setEscapepoint(int escapepoint) {
        this.escapepoint = escapepoint;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getMissionid() {
        return missionid;
    }

    public void setMissionid(int missionid) {
        this.missionid = missionid;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
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

    public String getPrecon() {
        return precon;
    }

    public void setPrecon(String precon) {
        this.precon = precon;
    }

    public String getVideoaddr() {
        return videoaddr;
    }

    public void setVideoaddr(String videoaddr) {
        this.videoaddr = videoaddr;
    }

    public String getFullyoutubeaddr() {
        return fullyoutubeaddr;
    }

    public void setFullyoutubeaddr(String fullyoutubeaddr) {
        this.fullyoutubeaddr = fullyoutubeaddr;
    }

    public String getYoutubeaddr() {
        return youtubeaddr;
    }

    public void setYoutubeaddr(String youtubeaddr) {
        this.youtubeaddr = youtubeaddr;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getPassgrade() {
        return passgrade;
    }

    public void setPassgrade(int passgrade) {
        this.passgrade = passgrade;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
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

    public int getUploadcount() {
        return uploadcount;
    }

    public void setUploadcount(int uploadcount) {
        this.uploadcount = uploadcount;
    }

    public int getMissionpasscount() {
        return missionpasscount;
    }

    public void setMissionpasscount(int missionpasscount) {
        this.missionpasscount = missionpasscount;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getEndescription() {
        return endescription;
    }

    public void setEndescription(String endescription) {
        this.endescription = endescription;
    }

    public String getEnprecon() {
        return enprecon;
    }

    public void setEnprecon(String enprecon) {
        this.enprecon = enprecon;
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public String getCndescription() {
        return cndescription;
    }

    public void setCndescription(String cndescription) {
        this.cndescription = cndescription;
    }

    public String getCnprecon() {
        return cnprecon;
    }

    public void setCnprecon(String cnprecon) {
        this.cnprecon = cnprecon;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "missionid=" + missionid +
                ", categoryid=" + categoryid +
                ", typeid=" + typeid +
                ", typename='" + typename + '\'' +
                ", sequence=" + sequence +
                ", missionname='" + missionname + '\'' +
                ", description='" + description + '\'' +
                ", precon='" + precon + '\'' +
                ", videoaddr='" + videoaddr + '\'' +
                ", fullyoutubeaddr='" + fullyoutubeaddr + '\'' +
                ", youtubeaddr='" + youtubeaddr + '\'' +
                ", enabled='" + enabled + '\'' +
                ", feetype='" + feetype + '\'' +
                ", grade=" + grade +
                ", passgrade=" + passgrade +
                ", creationdate='" + creationdate + '\'' +
                ", updatedate='" + updatedate + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", change_updatedate='" + change_updatedate + '\'' +
                ", opencount=" + opencount +
                ", getpoint=" + getpoint +
                ", escapepoint=" + escapepoint +
                ", uid=" + uid +
                ", uploadcount=" + uploadcount +
                ", missionpasscount=" + missionpasscount +
                ", enname='" + enname + '\'' +
                ", endescription='" + endescription + '\'' +
                ", enprecon='" + enprecon + '\'' +
                ", cnname='" + cnname + '\'' +
                ", cndescription='" + cndescription + '\'' +
                ", cnprecon='" + cnprecon + '\'' +
                '}';
    }
}
