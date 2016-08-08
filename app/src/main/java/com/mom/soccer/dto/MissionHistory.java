package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-08-08.
 */
public class MissionHistory {

    private int historyid;
    private int uid;
    private int missionid;
    private int escapepoint;
    private String change_date;
    private String opentype;

    public MissionHistory() {
    }

    public MissionHistory(int uid, int missionid, int escapepoint, String opentype) {
        this.uid = uid;
        this.missionid = missionid;
        this.escapepoint = escapepoint;
        this.opentype = opentype;
    }

    public int getHistoryid() {
        return historyid;
    }

    public void setHistoryid(int historyid) {
        this.historyid = historyid;
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

    public int getEscapepoint() {
        return escapepoint;
    }

    public void setEscapepoint(int escapepoint) {
        this.escapepoint = escapepoint;
    }



    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    public String getOpentype() {
        return opentype;
    }

    public void setOpentype(String opentype) {
        this.opentype = opentype;
    }

    @Override
    public String toString() {
        return "MissionHistory{" +
                "historyid=" + historyid +
                ", uid=" + uid +
                ", missionid=" + missionid +
                ", escapepoint=" + escapepoint +
                ", change_date='" + change_date + '\'' +
                ", opentype='" + opentype + '\'' +
                '}';
    }
}
