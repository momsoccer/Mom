package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-09-23.
 */
public class QuestionVo {

    private int id;
    private String content;
    private int uid;

    public QuestionVo(){}

    @Override
    public String toString() {
        return "QuestionVo{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", uid=" + uid +
                '}';
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
