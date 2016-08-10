package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-08-10.
 * 재 업로드 방지를 위한 객체
 * dbtable : user_video_history
 */
public class UserVideoHistoryVo {

    private int id;
    private int uid;
    private int missionid;
    private String filename;
    private String change_date;
    public UserVideoHistoryVo(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMissionid() {
        return missionid;
    }

    public void setMissionid(int missionid) {
        this.missionid = missionid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    @Override
    public String toString() {
        return "UserVideoHistoryVo{" +
                "id=" + id +
                ", uid=" + uid +
                ", missionid=" + missionid +
                ", filename='" + filename + '\'' +
                ", change_date='" + change_date + '\'' +
                '}';
    }
}
