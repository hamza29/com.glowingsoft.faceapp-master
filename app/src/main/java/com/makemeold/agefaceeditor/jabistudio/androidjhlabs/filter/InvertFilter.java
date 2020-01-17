package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.support.v4.view.ViewCompat;

public class InvertFilter extends PointFilter {
    public InvertFilter() {
        this.canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        return ((rgb ^ -1) & ViewCompat.MEASURED_SIZE_MASK) | (rgb & -16777216);
    }

    public String toString() {
        return "Colors/Invert";
    }
}
