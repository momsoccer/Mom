package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-07-21.
 */
public class MainTypeListVo {
    private int typeId;
    private String name;
    private String description;
    private String backGraoundImage;
    private String enabled;

    public MainTypeListVo(){}

    public MainTypeListVo(int typeId, String name, String description, String backGraoundImage, String enabled) {
        this.typeId = typeId;
        this.name = name;
        this.description = description;
        this.backGraoundImage = backGraoundImage;
        this.enabled = enabled;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackGraoundImage() {
        return backGraoundImage;
    }

    public void setBackGraoundImage(String backGraoundImage) {
        this.backGraoundImage = backGraoundImage;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
