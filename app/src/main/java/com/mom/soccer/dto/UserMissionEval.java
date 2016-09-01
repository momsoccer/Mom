package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-06-23.
 */
public class UserMissionEval {

    private int evalid;
    private int usermissionid;
    private int uid;
    private int instructorid;
    private String coment;
    private int score;
    private String change_creationdate;

    //dbflag
    private String queryflag;

    public UserMissionEval(){}

    @Override
    public String toString() {
        return "UserMissionEval{" +
                "evalid=" + evalid +
                ", usermissionid=" + usermissionid +
                ", uid=" + uid +
                ", instructorid=" + instructorid +
                ", coment='" + coment + '\'' +
                ", score=" + score +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", queryflag='" + queryflag + '\'' +
                '}';
    }

    public int getEvalid() {
        return evalid;
    }

    public void setEvalid(int evalid) {
        this.evalid = evalid;
    }

    public int getUsermissionid() {
        return usermissionid;
    }

    public void setUsermissionid(int usermissionid) {
        this.usermissionid = usermissionid;
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

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public String getQueryflag() {
        return queryflag;
    }

    public void setQueryflag(String queryflag) {
        this.queryflag = queryflag;
    }
}
