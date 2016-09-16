package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-09-16.
 */
public class MomBoardFile {

    private int fileid;
    private int boardid;
    private String filename;
    private String fileaddr;

    public MomBoardFile(){}

    @Override
    public String toString() {
        return "MomBoardFile{" +
                "fileid=" + fileid +
                ", boardid=" + boardid +
                ", filename='" + filename + '\'' +
                ", fileaddr='" + fileaddr + '\'' +
                '}';
    }

    public int getFileid() {
        return fileid;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }

    public int getBoardid() {
        return boardid;
    }

    public void setBoardid(int boardid) {
        this.boardid = boardid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileaddr() {
        return fileaddr;
    }

    public void setFileaddr(String fileaddr) {
        this.fileaddr = fileaddr;
    }
}
