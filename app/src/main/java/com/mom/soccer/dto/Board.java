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
    private String teamname;
    private String change_creationdate;
    private String change_updatedate;
    private int totalscore;
    private int level;
    private String formatDataSign;
    private String missiontype;

    public Board(){}

    public String getMissiontype() {
        return missiontype;
    }

    public void setMissiontype(String missiontype) {
        this.missiontype = missiontype;
    }

    public String getFormatDataSign() {
        return formatDataSign;
    }

    public void setFormatDataSign(String formatDataSign) {
        this.formatDataSign = formatDataSign;
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

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
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
                ", teamname='" + teamname + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", change_updatedate='" + change_updatedate + '\'' +
                '}';
    }
}
