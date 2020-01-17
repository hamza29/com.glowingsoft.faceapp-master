package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class GrayFilter extends PointFilter {
    public GrayFilter() {
        this.canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        return (((((((rgb >> 16) & 255) + 255) / 2) << 16) | (rgb & -16777216)) | (((((rgb >> 8) & 255) + 255) / 2) << 8)) | (((rgb & 255) + 255) / 2);
    }

    public String toString() {
        return "Colors/Gray Out";
    }
}
