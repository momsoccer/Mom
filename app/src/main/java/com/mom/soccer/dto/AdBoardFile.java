package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-09-19.
 */
public class AdBoardFile {

    private int fileid;
    private int advid;
    private String filename;
    private String fileaddr;

    public AdBoardFile(){}

    public int getFileid() {
        return fileid;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }

    public int getAdvid() {
        return advid;
    }

    public void setAdvid(int advid) {
        this.advid = advid;
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
