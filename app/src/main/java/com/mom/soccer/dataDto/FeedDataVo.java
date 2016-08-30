package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-08-30.
 */
public class FeedDataVo {

    private int completecount;
    private int incompletecount;
    private int instructorid;

    public FeedDataVo(){}

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
                '}';
    }
}
