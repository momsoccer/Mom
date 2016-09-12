package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-08-01.
 */
public class UserRangkinVo {

    private int uid;
    private String username;
    private String profileimgurl;
    private String teamname;
    private String totalscore;
    private int level;
    private int sequence;
    private int queryRow;
    private String orderbytype;  // 대량목록 조회시 sort 순서 해당 컬럼에 한해서 사용
    private int limit;
    private int offset;

    public UserRangkinVo(){};

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getQueryRow() {
        return queryRow;
    }

    public void setQueryRow(int queryRow) {
        this.queryRow = queryRow;
    }

    public String getOrderbytype() {
        return orderbytype;
    }

    public void setOrderbytype(String orderbytype) {
        this.orderbytype = orderbytype;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(String totalscore) {
        this.totalscore = totalscore;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "UserRangkinVo{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", teamname='" + teamname + '\'' +
                ", totalscore='" + totalscore + '\'' +
                ", level=" + level +
                ", sequence=" + sequence +
                ", queryRow=" + queryRow +
                ", orderbytype='" + orderbytype + '\'' +
                '}';
    }
}
