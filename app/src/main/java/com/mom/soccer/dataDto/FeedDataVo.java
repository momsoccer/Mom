package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-08-30.
 */
public class FeedDataVo {

    private int completecount;
    private int incompletecount;
    private int instructorid;
    private int uid;
    private int countattribute1;
    private int countattribute2;
    private int countattribute3;

    public int getCountattribute1() {
        return countattribute1;
    }

    public void setCountattribute1(int countattribute1) {
        this.countattribute1 = countattribute1;
    }

    public int getCountattribute2() {
        return countattribute2;
    }

    public void setCountattribute2(int countattribute2) {
        this.countattribute2 = countattribute2;
    }

    public int getCountattribute3() {
        return countattribute3;
    }

    public void setCountattribute3(int countattribute3) {
        this.countattribute3 = countattribute3;
    }

    public FeedDataVo(){}

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

    public int getCompletecount() {
        return completecount;
    }

    public void setCompletecount(int completecount) {
        this.completecount = completecount;
    }

    public int getIncompletecount() {
        return incompletecount;
    }

    public void setIncompletecount(int incompletecount) {
        this.incompletecount = incompletecount;
    }

    @Override
    public String toString() {
        return "FeedDataVo{" +
                "completecount=" + completecount +
                ", incompletecount=" + incompletecount +
                ", instructorid=" + instructorid +
                ", uid=" + uid +
                ", countattribute1=" + countattribute1 +
                ", countattribute2=" + countattribute2 +
                ", countattribute3=" + countattribute3 +
                '}';
    }
}
