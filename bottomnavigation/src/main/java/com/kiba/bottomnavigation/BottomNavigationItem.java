package com.kiba.bottomnavigation;

/**
 * Created by KiBa-PC on 2016/6/21.
 */
public class BottomNavigationItem {

    private String label;

    private int drawableRes;
    // TODO: 2016/6/22 color to do. 
    private int color;

    private int badgeNumber = -1;

    public BottomNavigationItem(String label, int drawable, int color) {
        this.label = label;
        this.drawableRes = drawable;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int res) {
        this.drawableRes = res;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBadgeNumber() {
        return this.badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

}
