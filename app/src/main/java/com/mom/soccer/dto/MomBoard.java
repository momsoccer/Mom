package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-09-08.
 * 라인 및 헤더를 동시에 사용하는 Vo
 * */
public class MomBoard {

    //header
    private int boardid;
    private int uid;
    private String boardtype;
    private String subject;
    private String content;
    private int boardtypeid;
    private String pubtype;
    private String change_creationdate;
    private String change_updatedate;
    private String category;

    //line
    private int lineid;
    private int likeid;
    private int fileid;
    private String filename;
    private String fileaddr;

    //nonDb
    private String username;
    private String userimg;
    private int level;
    private int totalscore;
    private int teamid;
    private String teamname;

    private int commentcount;
    private int likecount;
    private String formatDataSign;

    public MomBoard(){}

    @Override
    public String toString() {
        return "MomBoard{" +
                "boardid=" + boardid +
                ", uid=" + uid +
                ", boardtype='" + boardtype + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", boardtypeid=" + boardtypeid +
                ", pubtype='" + pubtype + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", category='" + category + '\'' +
                ", lineid=" + lineid +
                ", likeid=" + likeid +
                ", fileid=" + fileid +
                ", filename='" + filename + '\'' +
                ", fileaddr='" + fileaddr + '\'' +
                ", username='" + username + '\'' +
                ", userimg='" + userimg + '\'' +
                ", level=" + level +
                ", totalscore=" + totalscore +
                ", teamid=" + teamid +
                ", teamname='" + teamname + '\'' +
                ", commentcount=" + commentcount +
                ", likecount=" + likecount +
                ", formatDataSign='" + formatDataSign + '\'' +
                '}';
    }

    public String getChange_updatedate() {
        return change_updatedate;
    }

    public void setChange_updatedate(String change_updatedate) {
        this.change_updatedate = change_updatedate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFormatDataSign() {
        return formatDataSign;
    }

    public void setFormatDataSign(String formatDataSign) {
        this.formatDataSign = formatDataSign;
    }

    public int getBoardid() {
        return boardid;
    }

    public void setBoardid(int boardid) {
        this.boardid = boardid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBoardtype() {
        return boardtype;
    }

    public void setBoardtype(String boardtype) {
        this.boardtype = boardtype;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBoardtypeid() {
        return boardtypeid;
    }

    public void setBoardtypeid(int boardtypeid) {
        this.boardtypeid = boardtypeid;
    }

    public String getPubtype() {
        return pubtype;
    }

    public void setPubtype(String pubtype) {
        this.pubtype = pubtype;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public int getLineid() {
        return lineid;
    }

    public void setLineid(int lineid) {
        this.lineid = lineid;
    }

    public int getLikeid() {
        return likeid;
    }

    public void setLikeid(int likeid) {
        this.likeid = likeid;
    }

    public int getFileid() {
        return fileid;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileaddr() {
        return fileaddr;
    }

    public void setFileaddr(String fileaddr) {
        this.fileaddr = fileaddr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
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

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }
}
