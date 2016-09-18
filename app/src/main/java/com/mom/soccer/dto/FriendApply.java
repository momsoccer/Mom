package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-07-12.
 */
public class FriendApply {

    private int applyid;
    private int requid;
    private int resuid;
    private String flag;
    private String requestmessage;
    private String creationdate;
    private String updatedate;

    private String requsername;

    //non DB
    private String change_creationdate;
    private String change_updatedate;

    public FriendApply(){}

    public String getRequsername() {
        return requsername;
    }

    public void setRequsername(String requsername) {
        this.requsername = requsername;
    }

    public int getApplyid() {
        return applyid;
    }

    public void setApplyid(int applyid) {
        this.applyid = applyid;
    }

    public int getRequid() {
        return requid;
    }

    public void setRequid(int requid) {
        this.requid = requid;
    }

    public int getResuid() {
        return resuid;
    }

    public void setResuid(int resuid) {
        this.resuid = resuid;
    }


    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "FriendApply{" +
                "applyid=" + applyid +
                ", requid=" + requid +
                ", resuid=" + resuid +
                ", flag='" + flag + '\'' +
                ", requestmessage='" + requestmessage + '\'' +
                ", creationdate='" + creationdate + '\'' +
                ", updatedate='" + updatedate + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", change_updatedate='" + change_updatedate + '\'' +
                '}';
    }
}
