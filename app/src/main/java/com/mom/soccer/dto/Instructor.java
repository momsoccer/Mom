package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-06-01.
 */
public class Instructor {

    private int instructorid;
    private String email;
    private String password;
    private String name;
    private String profileimgurl;
    private String profile;
    private String description;
    private String phone;
    private int location;
    private int pointhistoryid;
    private String feedbackflag;
    private String apppushflag;
    private int commontokenid;
    private String serialnumber;
    private String fcmtoken;
    private String creationdate;
    private String change_creationdate;
    private int uid;

    public Instructor(){}

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
    }

    public int getCommontokenid() {
        return commontokenid;
    }

    public void setCommontokenid(int commontokenid) {
        this.commontokenid = commontokenid;
    }

    public int getInstructorid() {
        return instructorid;
    }

    public void setInstructorid(int instructorid) {
        this.instructorid = instructorid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getPointhistoryid() {
        return pointhistoryid;
    }

    public void setPointhistoryid(int pointhistoryid) {
        this.pointhistoryid = pointhistoryid;
    }

    public String getFeedbackflag() {
        return feedbackflag;
    }

    public void setFeedbackflag(String feedbackflag) {
        this.feedbackflag = feedbackflag;
    }

    public String getApppushflag() {
        return apppushflag;
    }

    public void setApppushflag(String apppushflag) {
        this.apppushflag = apppushflag;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getFcmtoken() {
        return fcmtoken;
    }

    public void setFcmtoken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "instructorid=" + instructorid +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", profile='" + profile + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", location=" + location +
                ", pointhistoryid=" + pointhistoryid +
                ", feedbackflag='" + feedbackflag + '\'' +
                ", apppushflag='" + apppushflag + '\'' +
                ", commontokenid=" + commontokenid +
                ", serialnumber='" + serialnumber + '\'' +
                ", fcmtoken='" + fcmtoken + '\'' +
                ", creationdate='" + creationdate + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", uid=" + uid +
                '}';
    }
}
