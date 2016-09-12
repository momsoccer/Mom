package com.mom.soccer.dto;


import java.io.Serializable;

public class User implements Serializable{

	private int uid;
	private String useremail;
	private String username;
	private String snsid;
	private String snsname;
	private String snstype;
	private String phone;
	private String password;
	private String profileimgurl;
	private String googleemail;
	private int location;
	private String apppushflag;
	private String teampushflag;
	private int commontokenid;
	private String serialnumber;
	private String creationdate;
	private String change_creationdate;
	private String teamname;
	private int teamid;
	private String wifi;
	private String backimage;

	private int totalscore;
	private int level;

	//DB
	private String fcmToken;
	private int queryRow;
	private String orderbytype;

	public User() {
	}

	public int getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getBackimage() {
		return backimage;
	}

	public void setBackimage(String backimage) {
		this.backimage = backimage;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public User(String useremail) {
		this.useremail = useremail;
	}

	public int getTeamid() {
		return teamid;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}

	public int getQueryRow() {
		return queryRow;
	}

	public void setQueryRow(int queryRow) {
		this.queryRow = queryRow;
	}

	public String getOrderbytype() {
		return orderbytype;
	}

	public void setOrderbytype(String orderbytype) {
		this.orderbytype = orderbytype;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSnsid() {
		return snsid;
	}

	public void setSnsid(String snsid) {
		this.snsid = snsid;
	}

	public String getSnsname() {
		return snsname;
	}

	public void setSnsname(String snsname) {
		this.snsname = snsname;
	}

	public String getSnstype() {
		return snstype;
	}

	public void setSnstype(String snstype) {
		this.snstype = snstype;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfileimgurl() {
		return profileimgurl;
	}

	public void setProfileimgurl(String profileimgurl) {
		this.profileimgurl = profileimgurl;
	}

	public String getGoogleemail() {
		return googleemail;
	}

	public void setGoogleemail(String googleemail) {
		this.googleemail = googleemail;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getApppushflag() {
		return apppushflag;
	}

	public void setApppushflag(String apppushflag) {
		this.apppushflag = apppushflag;
	}

	public String getTeampushflag() {
		return teampushflag;
	}

	public void setTeampushflag(String teampushflag) {
		this.teampushflag = teampushflag;
	}

	public String getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
	}

	public String getChange_creationdate() {
		return change_creationdate;
	}

	public void setChange_creationdate(String change_creationdate) {
		this.change_creationdate = change_creationdate;
	}

	public int getCommontokenid() {
		return commontokenid;
	}

	public void setCommontokenid(int commontokenid) {
		this.commontokenid = commontokenid;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	@Override
	public String toString() {
		return "User{" +
				"uid=" + uid +
				", useremail='" + useremail + '\'' +
				", username='" + username + '\'' +
				", snsid='" + snsid + '\'' +
				", snsname='" + snsname + '\'' +
				", snstype='" + snstype + '\'' +
				", phone='" + phone + '\'' +
				", password='" + password + '\'' +
				", profileimgurl='" + profileimgurl + '\'' +
				", googleemail='" + googleemail + '\'' +
				", location=" + location +
				", apppushflag='" + apppushflag + '\'' +
				", teampushflag='" + teampushflag + '\'' +
				", commontokenid=" + commontokenid +
				", serialnumber='" + serialnumber + '\'' +
				", creationdate='" + creationdate + '\'' +
				", change_creationdate='" + change_creationdate + '\'' +
				", teamname='" + teamname + '\'' +
				", teamid=" + teamid +
				", wifi='" + wifi + '\'' +
				", backimage='" + backimage + '\'' +
				", totalscore=" + totalscore +
				", level=" + level +
				", fcmToken='" + fcmToken + '\'' +
				", queryRow=" + queryRow +
				", orderbytype='" + orderbytype + '\'' +
				'}';
	}
}