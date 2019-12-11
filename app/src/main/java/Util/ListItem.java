package Util;

import android.graphics.drawable.Drawable;

public class ListItem {
    String firstLine;
    String secondLine;
    Drawable icon;
    String id;

    public ListItem(String firstLine, String secondLine, Drawable icon, String id) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.icon = icon;
        this.id = id;
    }

    public String getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(String firstLine) {
        this.firstLine = firstLine;
    }

    public String getSecondLine() {
        return secondLine;
    }

    public void setSecondLine(String secondLine) {
        this.secondLine = secondLine;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
