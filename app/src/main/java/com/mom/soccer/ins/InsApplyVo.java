package com.mom.soccer.ins;

import java.io.Serializable;

/**
 * Created by sungbo on 2016-08-11.
 * 강사 신청 VO
 * instructor_apply
 */
public class InsApplyVo implements Serializable {

    private int insid;
    private String name;
    private int age;
    private int playeryear;
    private int instructoryear;
    private String bankname;
    private String bankaccount;
    private String address;
    private String currentteamname;
    private String career1;
    private String career2;
    private String career3;
    private String career4;
    private String career5;
    private int uid;
    private String phonenumber;
    private String applystatus;
    private String change_creationdate;
    private String change_updatedate;
    private String momappteamname;
    private String teamimg;
    private String email;
    private String resume;

    public InsApplyVo(){}

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getInsid() {
        return insid;
    }

    public void setInsid(int insid) {
        this.insid = insid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPlayeryear() {
        return playeryear;
    }

    public void setPlayeryear(int playeryear) {
        this.playeryear = playeryear;
    }

    public int getInstructoryear() {
        return instructoryear;
    }

    public void setInstructoryear(int instructoryear) {
        this.instructoryear = instructoryear;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrentteamname() {
        return currentteamname;
    }

    public void setCurrentteamname(String currentteamname) {
        this.currentteamname = currentteamname;
    }

    public String getCareer1() {
        return career1;
    }

    public void setCareer1(String career1) {
        this.career1 = career1;
    }

    public String getCareer2() {
        return career2;
    }

    public void setCareer2(String career2) {
        this.career2 = career2;
    }

    public String getCareer3() {
        return career3;
    }

    public void setCareer3(String career3) {
        this.career3 = career3;
    }

    public String getCareer4() {
        return career4;
    }

    public void setCareer4(String career4) {
        this.career4 = career4;
    }

    public String getCareer5() {
        return career5;
    }

    public void setCareer5(String career5) {
        this.career5 = career5;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getApplystatus() {
        return applystatus;
    }

    public void setApplystatus(String applystatus) {
        this.applystatus = applystatus;
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

    public String getMomappteamname() {
        return momappteamname;
    }

    public void setMomappteamname(String momappteamname) {
        this.momappteamname = momappteamname;
    }

    public String getTeamimg() {
        return teamimg;
    }

    public void setTeamimg(String teamimg) {
        this.teamimg = teamimg;
    }

    @Override
    public String toString() {
        return "InsApplyVo{" +
                "insid=" + insid +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", playeryear=" + playeryear +
                ", instructoryear=" + instructoryear +
                ", bankname='" + bankname + '\'' +
                ", bankaccount='" + bankaccount + '\'' +
                ", address='" + address + '\'' +
                ", currentteamname='" + currentteamname + '\'' +
                ", career1='" + career1 + '\'' +
                ", career2='" + career2 + '\'' +
                ", career3='" + career3 + '\'' +
                ", career4='" + career4 + '\'' +
                ", career5='" + career5 + '\'' +
                ", uid=" + uid +
                ", phonenumber='" + phonenumber + '\'' +
                ", applystatus='" + applystatus + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", change_updatedate='" + change_updatedate + '\'' +
                ", momappteamname='" + momappteamname + '\'' +
                ", teamimg='" + teamimg + '\'' +
                ", email='" + email + '\'' +
                ", resume='" + resume + '\'' +
                '}';
    }
}
