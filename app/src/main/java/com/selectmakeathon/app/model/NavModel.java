package com.selectmakeathon.app.model;

public class NavModel {

    private int iconId;
    private String text;

    public NavModel(int iconId, String text) {
        this.iconId = iconId;
        this.text = text;
    }

    public NavModel() {

    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
