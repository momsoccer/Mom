package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-09-11.
 */
public class TeamRankingVo {

    private String teamname;
    private int teamid;
    private int instructorid;
    private String emblem;
    private String insimg;
    private String insname;
    private int totalscore;
    private String change_creationdate;
    private int querycount;

    public TeamRankingVo(){}

    @Override
    public String toString() {
        return "TeamRankingVo{" +
                "teamname='" + teamname + '\'' +
                ", teamid=" + teamid +
                ", instructorid=" + instructorid +
                ", emblem='" + emblem + '\'' +
                ", insimg='" + insimg + '\'' +
                ", insname='" + insname + '\'' +
                ", totalscore=" + totalscore +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", querycount=" + querycount +
                '}';
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public int getInstructorid() {
        return instructorid;
    }

    public void setInstructorid(int instructorid) {
        this.instructorid = instructorid;
    }

    public String getEmblem() {
        return emblem;
    }

    public void setEmblem(String emblem) {
        this.emblem = emblem;
    }

    public String getInsimg() {
        return insimg;
    }

    public void setInsimg(String insimg) {
        this.insimg = insimg;
    }

    public String getInsname() {
        return insname;
    }

    public void setInsname(String insname) {
        this.insname = insname;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public int getQuerycount() {
        return querycount;
    }

    public void setQuerycount(int querycount) {
        this.querycount = querycount;
    }
}
