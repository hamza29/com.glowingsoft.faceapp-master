package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.support.v4.view.ViewCompat;

public class OpacityFilter extends PointFilter {
    private int opacity;
    private int opacity24;

    public OpacityFilter() {
        this(136);
    }

    public OpacityFilter(int opacity) {
        setOpacity(opacity);
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
        this.opacity24 = opacity << 24;
    }

    public int getOpacity() {
        return this.opacity;
    }

    public int filterRGB(int x, int y, int rgb) {
        if ((-16777216 & rgb) != 0) {
            return (ViewCompat.MEASURED_SIZE_MASK & rgb) | this.opacity24;
        }
        return rgb;
    }

    public String toString() {
        return "Colors/Transparency...";
    }
}
