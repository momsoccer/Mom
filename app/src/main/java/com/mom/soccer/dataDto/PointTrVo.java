package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-08-09.
 */
public class PointTrVo {

    private String typecode;
    private String enabled;
    private int point;
    private String sign;
    private int missionid;
    private int uid;
    private int getpoint;
    private int escapepoint;
    private String trType;

    public PointTrVo(){}

    public String getTrType() {
        return trType;
    }

    public void setTrType(String trType) {
        this.trType = trType;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getMissionid() {
        return missionid;
    }

    public void setMissionid(int missionid) {
        this.missionid = missionid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGetpoint() {
        return getpoint;
    }

    public void setGetpoint(int getpoint) {
        this.getpoint = getpoint;
    }

    public int getEscapepoint() {
        return escapepoint;
    }

    public void setEscapepoint(int escapepoint) {
        this.escapepoint = escapepoint;
    }


}
