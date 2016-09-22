package com.mom.soccer.dto;

import java.util.List;

/**
 * Created by sungbo on 2016-09-19.
 */
public class AdBoardVo{

    private int advid;
    private String addr;
    private String introduce;
    private String subcontent1;
    private String subcontent2;
    private String subcontent3;
    private String phone;
    private String change_creationdate;
    private String formatDataSign;
    private int filecount;
    private int limit;
    private int offset;
    private String youtubeaddr;

    private List<AdBoardFile> adBoardFiles;

    public AdBoardVo(){}


    public String getYoutubeaddr() {
        return youtubeaddr;
    }

    public void setYoutubeaddr(String youtubeaddr) {
        this.youtubeaddr = youtubeaddr;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getFilecount() {
        return filecount;
    }

    public void setFilecount(int filecount) {
        this.filecount = filecount;
    }

    public int getAdvid() {
        return advid;
    }

    public void setAdvid(int advid) {
        this.advid = advid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getSubcontent1() {
        return subcontent1;
    }

    public void setSubcontent1(String subcontent1) {
        this.subcontent1 = subcontent1;
    }

    public String getSubcontent2() {
        return subcontent2;
    }

    public void setSubcontent2(String subcontent2) {
        this.subcontent2 = subcontent2;
    }

    public String getSubcontent3() {
        return subcontent3;
    }

    public void setSubcontent3(String subcontent3) {
        this.subcontent3 = subcontent3;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public String getFormatDataSign() {
        return formatDataSign;
    }

    public void setFormatDataSign(String formatDataSign) {
        this.formatDataSign = formatDataSign;
    }

    public List<AdBoardFile> getAdBoardFiles() {
        return adBoardFiles;
    }

    public void setAdBoardFiles(List<AdBoardFile> adBoardFiles) {
        this.adBoardFiles = adBoardFiles;
    }

    @Override
    public String toString() {
        return "AdBoardVo{" +
                "advid=" + advid +
                ", addr='" + addr + '\'' +
                ", introduce='" + introduce + '\'' +
                ", subcontent1='" + subcontent1 + '\'' +
                ", subcontent2='" + subcontent2 + '\'' +
                ", subcontent3='" + subcontent3 + '\'' +
                ", phone='" + phone + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", formatDataSign='" + formatDataSign + '\'' +
                ", filecount=" + filecount +
                ", limit=" + limit +
                ", offset=" + offset +
                ", youtubeaddr='" + youtubeaddr + '\'' +
                ", adBoardFiles=" + adBoardFiles +
                '}';
    }
}
