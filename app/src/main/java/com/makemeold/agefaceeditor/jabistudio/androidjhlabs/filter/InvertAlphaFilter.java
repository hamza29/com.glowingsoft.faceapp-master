package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class InvertAlphaFilter extends PointFilter {
    public InvertAlphaFilter() {
        this.canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        return -16777216 ^ rgb;
    }

    public String toString() {
        return "Alpha/Invert";
    }
}
