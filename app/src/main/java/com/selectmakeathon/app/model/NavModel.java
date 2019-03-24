package com.selectmakeathon.app.model;

import com.selectmakeathon.app.ui.main.MainActivity;

public class NavModel {

    private int iconId;
    private String text;
    private MainActivity.NavItem navItem;

    public NavModel(int iconId, String text, MainActivity.NavItem navItem) {
        this.iconId = iconId;
        this.text = text;
        this.navItem = navItem;
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

    public MainActivity.NavItem getNavItem() {
        return navItem;
    }

    public void setNavItem(MainActivity.NavItem navItem) {
        this.navItem = navItem;
    }
}
