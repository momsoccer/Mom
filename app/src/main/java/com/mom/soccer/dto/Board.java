package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-08-01.
 */
public class Board {

    private int boardid;
    private int usermissionid;
    private int uid;
    private int writeuid;
    private String username;
    private String profileimgurl;
    private String comment;
    private String change_creationdate;
    private String change_updatedate;

    public Board(){}

    public Board(int boardid, int usermissionid, int uid, int writeuid, String username, String profileimgurl, String comment, String change_creationdate, String change_updatedate) {
        this.boardid = boardid;
        this.usermissionid = usermissionid;
        this.uid = uid;
        this.writeuid = writeuid;
        this.username = username;
        this.profileimgurl = profileimgurl;
        this.comment = comment;
        this.change_creationdate = change_creationdate;
        this.change_updatedate = change_updatedate;
    }

    public int getBoardid() {
        return boardid;
    }

    public void setBoardid(int boardid) {
        this.boardid = boardid;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
    }

    public int getWriteuid() {
        return writeuid;
    }

    public void setWriteuid(int writeuid) {
        this.writeuid = writeuid;
    }

    @Override
    public String toString() {
        return "Board{" +
                "boardid=" + boardid +
                ", usermissionid=" + usermissionid +
                ", uid=" + uid +
                ", writeuid=" + writeuid +
                ", username='" + username + '\'' +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", comment='" + comment + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", change_updatedate='" + change_updatedate + '\'' +
                '}';
    }
}
