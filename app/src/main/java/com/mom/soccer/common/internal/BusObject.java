package com.mom.soccer.common.internal;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sungbo on 2016-09-17.
 */
public class BusObject implements Serializable{

    private int position;
    private int keyid;
    private int lineCount;
    private String keyName;
    private Map<String,String>  map;

    public BusObject(){}

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "BusObject{" +
                "position=" + position +
                ", keyid=" + keyid +
                ", lineCount=" + lineCount +
                ", keyName='" + keyName + '\'' +
                ", map=" + map +
                '}';
    }
}
