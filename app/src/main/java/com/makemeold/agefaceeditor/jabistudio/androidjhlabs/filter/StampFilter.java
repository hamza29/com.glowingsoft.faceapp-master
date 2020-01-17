package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class StampFilter extends PointFilter {
    private int black;
    private float lowerThreshold3;
    private float radius;
    private float softness;
    private float threshold;
    private float upperThreshold3;
    private int white;

    public StampFilter() {
        this(0.5f);
    }

    public StampFilter(float threshold) {
        this.softness = 0.0f;
        this.radius = 5.0f;
        this.white = -1;
        this.black = -16777216;
        setThreshold(threshold);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getThreshold() {
        return this.threshold;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public float getSoftness() {
        return this.softness;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    public int getWhite() {
        return this.white;
    }

    public void setBlack(int black) {
        this.black = black;
    }

    public int getBlack() {
        return this.black;
    }

    public int[] filter(int[] src, int w, int h) {
        int[] dst = new int[(w * h)];
        dst = new GaussianFilter((float) ((int) this.radius)).filter(src, w, h);
        this.lowerThreshold3 = (this.threshold - (this.softness * 0.5f)) * 765.0f;
        this.upperThreshold3 = (this.threshold + (this.softness * 0.5f)) * 765.0f;
        return super.filter(dst, w, h);
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & -16777216;
        return ImageMath.mixColors(ImageMath.smoothStep(this.lowerThreshold3, this.upperThreshold3, (float) ((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + (rgb & 255))), this.black, this.white);
    }

    public String toString() {
        return "Stylize/Stamp...";
    }
}
