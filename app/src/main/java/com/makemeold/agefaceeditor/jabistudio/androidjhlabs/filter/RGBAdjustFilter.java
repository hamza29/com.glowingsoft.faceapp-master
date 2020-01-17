package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class RGBAdjustFilter extends PointFilter {
    public float bFactor;
    public float gFactor;
    public float rFactor;

    public RGBAdjustFilter() {
        this(0.0f, 0.0f, 0.0f);
    }

    public RGBAdjustFilter(float r, float g, float b) {
        this.rFactor = 1.0f + r;
        this.gFactor = 1.0f + g;
        this.bFactor = 1.0f + b;
        this.canFilterIndexColorModel = true;
    }

    public void setRFactor(float rFactor) {
        this.rFactor = 1.0f + rFactor;
    }

    public float getRFactor() {
        return this.rFactor - 1.0f;
    }

    public void setGFactor(float gFactor) {
        this.gFactor = 1.0f + gFactor;
    }

    public float getGFactor() {
        return this.gFactor - 1.0f;
    }

    public void setBFactor(float bFactor) {
        this.bFactor = 1.0f + bFactor;
    }

    public float getBFactor() {
        return this.bFactor - 1.0f;
    }

    public int[] getLUT() {
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = filterRGB(0, 0, (((i << 24) | (i << 16)) | (i << 8)) | i);
        }
        return lut;
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & -16777216;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        int r = PixelUtils.clamp((int) (((float) ((rgb >> 16) & 255)) * this.rFactor));
        g = PixelUtils.clamp((int) (((float) g) * this.gFactor));
        return (((r << 16) | a) | (g << 8)) | PixelUtils.clamp((int) (((float) b) * this.bFactor));
    }

    public String toString() {
        return "Colors/Adjust RGB...";
    }
}
