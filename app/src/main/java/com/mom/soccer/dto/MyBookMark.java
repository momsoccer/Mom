package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-07-30.
 */
public class MyBookMark {

    private int bookid;
    private int uid;
    private int usermissionid;

    public MyBookMark(){}

    public MyBookMark(int bookid, int uid, int usermissionid) {
        this.bookid = bookid;
        this.uid = uid;
        this.usermissionid = usermissionid;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUsermissionid() {
        return usermissionid;
    }

    public void setUsermissionid(int usermissionid) {
        this.usermissionid = usermissionid;
    }

    @Override
    public String toString() {
        return "MyBookMark{" +
                "bookid=" + bookid +
                ", uid=" + uid +
                ", usermissionid=" + usermissionid +
                '}';
    }
}
