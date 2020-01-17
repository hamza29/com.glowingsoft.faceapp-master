package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Color;
import android.support.v4.view.ViewCompat;

public class HSBAdjustFilter extends PointFilter {
    private float bFactor;
    private float hFactor;
    private float[] hsb;
    private float sFactor;

    public HSBAdjustFilter() {
        this(0.0f, 0.0f, 0.0f);
    }

    public HSBAdjustFilter(float r, float g, float b) {
        this.hsb = new float[3];
        this.hFactor = r;
        this.sFactor = g;
        this.bFactor = b;
        this.canFilterIndexColorModel = true;
    }

    public void setHFactor(float hFactor) {
        this.hFactor = hFactor;
    }

    public float getHFactor() {
        return this.hFactor;
    }

    public void setSFactor(float sFactor) {
        this.sFactor = sFactor;
    }

    public float getSFactor() {
        return this.sFactor;
    }

    public void setBFactor(float bFactor) {
        this.bFactor = bFactor;
    }

    public float getBFactor() {
        return this.bFactor;
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & -16777216;
        Color.RGBToHSV((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255, this.hsb);
        float[] fArr = this.hsb;
        fArr[0] = fArr[0] + this.hFactor;
        while (this.hsb[0] < 0.0f) {
            fArr = this.hsb;
            fArr[0] = (float) (((double) fArr[0]) + 6.283185307179586d);
        }
        fArr = this.hsb;
        fArr[1] = fArr[1] + this.sFactor;
        if (this.hsb[1] < 0.0f) {
            this.hsb[1] = 0.0f;
        } else if (((double) this.hsb[1]) > 1.0d) {
            this.hsb[1] = 1.0f;
        }
        fArr = this.hsb;
        fArr[2] = fArr[2] + this.bFactor;
        if (this.hsb[2] < 0.0f) {
            this.hsb[2] = 0.0f;
        } else if (((double) this.hsb[2]) > 1.0d) {
            this.hsb[2] = 1.0f;
        }
        return (ViewCompat.MEASURED_SIZE_MASK & Color.HSVToColor(this.hsb)) | a;
    }

    public String toString() {
        return "Colors/Adjust HSB...";
    }
}
