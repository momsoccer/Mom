package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-08-29.
 */
public class MissionPass {

    private int passid;
    private int seq;
    private int uid;
    private int instructorid;

    private int missionid;
    private int usermissionid;

    private String status;
    private String passflag;
    private String failuredisp;
    private String inscomment;

    private String change_creationdate;
    private String change_updatedate;

    private String evaltype;
    private int cashpoint;
    private String missiontype;

    //non DB
    private int level;
    private String insname;
    private String username;
    private String insimge;
    private String userimge;
    private String passgrade;
    private String missionPassFlag;
    private String teamname;
    private String missionname;
    private String uservideo;
    private String missionvideo;
    private int totalscore;

    public MissionPass() {
    }

    public String getMissiontype() {
        return missiontype;
    }

    public void setMissiontype(String missiontype) {
        this.missiontype = missiontype;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
    }

    public String getUservideo() {
        return uservideo;
    }

    public void setUservideo(String uservideo) {
        this.uservideo = uservideo;
    }

    public String getMissionvideo() {
        return missionvideo;
    }

    public void setMissionvideo(String missionvideo) {
        this.missionvideo = missionvideo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMissionPassFlag() {
        return missionPassFlag;
    }

    public void setMissionPassFlag(String missionPassFlag) {
        this.missionPassFlag = missionPassFlag;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getMissionname() {
        return missionname;
    }

    public void setMissionname(String missionname) {
        this.missionname = missionname;
    }

    public int getCashpoint() {
        return cashpoint;
    }

    public void setCashpoint(int cashpoint) {
        this.cashpoint = cashpoint;
    }

    public String getEvaltype() {
        return evaltype;
    }

    public void setEvaltype(String evaltype) {
        this.evaltype = evaltype;
    }

    public String getPassgrade() {
        return passgrade;
    }

    public void setPassgrade(String passgrade) {
        this.passgrade = passgrade;
    }

    public int getPassid() {
        return passid;
    }

    public void setPassid(int passid) {
        this.passid = passid;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

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

    public int getMissionid() {
        return missionid;
    }

    public void setMissionid(int missionid) {
        this.missionid = missionid;
    }

    public int getUsermissionid() {
        return usermissionid;
    }

    public void setUsermissionid(int usermissionid) {
        this.usermissionid = usermissionid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassflag() {
        return passflag;
    }

    public void setPassflag(String passflag) {
        this.passflag = passflag;
    }

    public String getFailuredisp() {
        return failuredisp;
    }

    public void setFailuredisp(String failuredisp) {
        this.failuredisp = failuredisp;
    }

    public String getInscomment() {
        return inscomment;
    }

    public void setInscomment(String inscomment) {
        this.inscomment = inscomment;
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

    public String getInsname() {
        return insname;
    }

    public void setInsname(String insname) {
        this.insname = insname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInsimge() {
        return insimge;
    }

    public void setInsimge(String insimge) {
        this.insimge = insimge;
    }

    public String getUserimge() {
        return userimge;
    }

    public void setUserimge(String userimge) {
        this.userimge = userimge;
    }



    @Override
    public String toString() {
        return "MissionPass{" +
                "passid=" + passid +
                ", seq=" + seq +
                ", uid=" + uid +
                ", instructorid=" + instructorid +
                ", missionid=" + missionid +
                ", usermissionid=" + usermissionid +
                ", status='" + status + '\'' +
                ", passflag='" + passflag + '\'' +
                ", failuredisp='" + failuredisp + '\'' +
                ", inscomment='" + inscomment + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", change_updatedate='" + change_updatedate + '\'' +
                ", evaltype='" + evaltype + '\'' +
                ", cashpoint=" + cashpoint +
                ", level=" + level +
                ", insname='" + insname + '\'' +
                ", username='" + username + '\'' +
                ", insimge='" + insimge + '\'' +
                ", userimge='" + userimge + '\'' +
                ", passgrade='" + passgrade + '\'' +
                ", missionPassFlag='" + missionPassFlag + '\'' +
                ", teamname='" + teamname + '\'' +
                ", missionname='" + missionname + '\'' +
                ", uservideo='" + uservideo + '\'' +
                ", missionvideo='" + missionvideo + '\'' +
                ", totalscore=" + totalscore +
                '}';
    }
}