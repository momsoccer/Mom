package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-06-11.
 */
public class SpBalanceLine {

    private int lineid;
    private int headerid;
    private String type;
    private int previousamount;
    private int inamount;
    private int outamount;
    private int lastamount;
    private String description;
    private String change_creationdate;

    public SpBalanceLine(){}

    @Override
    public String toString() {
        return "SpBalanceLine{" +
                "lineid=" + lineid +
                ", headerid=" + headerid +
                ", type='" + type + '\'' +
                ", previousamount=" + previousamount +
                ", inamount=" + inamount +
                ", outamount=" + outamount +
                ", lastamount=" + lastamount +
                ", description='" + description + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                '}';
    }

    public int getLineid() {
        return lineid;
    }

    public void setLineid(int lineid) {
        this.lineid = lineid;
    }

    public int getHeaderid() {
        return headerid;
    }

    public void setHeaderid(int headerid) {
        this.headerid = headerid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPreviousamount() {
        return previousamount;
    }

    public void setPreviousamount(int previousamount) {
        this.previousamount = previousamount;
    }

    public int getInamount() {
        return inamount;
    }

    public void setInamount(int inamount) {
        this.inamount = inamount;
    }

    public int getOutamount() {
        return outamount;
    }

    public void setOutamount(int outamount) {
        this.outamount = outamount;
    }

    public int getLastamount() {
        return lastamount;
    }

    public void setLastamount(int lastamount) {
        this.lastamount = lastamount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }
}
