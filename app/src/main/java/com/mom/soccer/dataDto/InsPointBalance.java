package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-09-05.
 * 강사의 포인트 적립내역 => 현금전환 가능 300,000원 단위
 */
public class InsPointBalance {

    private String balancetype;
    private String feedbacktype;
    private int cashpoint;
    private String username;
    private String userimge;
    private String missionname;
    private float evalscore;
    private String calculateflag;
    private String change_creationdate;

    public InsPointBalance(){

    }

    public String getBalancetype() {
        return balancetype;
    }

    public void setBalancetype(String balancetype) {
        this.balancetype = balancetype;
    }

    public String getFeedbacktype() {
        return feedbacktype;
    }

    public void setFeedbacktype(String feedbacktype) {
        this.feedbacktype = feedbacktype;
    }

    public int getCashpoint() {
        return cashpoint;
    }

    public void setCashpoint(int cashpoint) {
        this.cashpoint = cashpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimge() {
        return userimge;
    }

    public void setUserimge(String userimge) {
        this.userimge = userimge;
    }

    public String getMissionname() {
        return missionname;
    }

    public void setMissionname(String missionname) {
        this.missionname = missionname;
    }

    public float getEvalscore() {
        return evalscore;
    }

    public void setEvalscore(float evalscore) {
        this.evalscore = evalscore;
    }

    public String getCalculateflag() {
        return calculateflag;
    }

    public void setCalculateflag(String calculateflag) {
        this.calculateflag = calculateflag;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    @Override
    public String toString() {
        return "InsPointBalance{" +
                "balancetype='" + balancetype + '\'' +
                ", feedbacktype='" + feedbacktype + '\'' +
                ", cashpoint=" + cashpoint +
                ", username='" + username + '\'' +
                ", userimge='" + userimge + '\'' +
                ", missionname='" + missionname + '\'' +
                ", evalscore=" + evalscore +
                ", calculateflag='" + calculateflag + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                '}';
    }
}
