package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class GrayscaleFilter extends PointFilter {
    public GrayscaleFilter() {
        this.canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & -16777216;
        rgb = (((((rgb >> 16) & 255) * 77) + (((rgb >> 8) & 255) * 151)) + ((rgb & 255) * 28)) >> 8;
        return (((rgb << 16) | a) | (rgb << 8)) | rgb;
    }

    public String toString() {
        return "Colors/Grayscale";
    }
}
