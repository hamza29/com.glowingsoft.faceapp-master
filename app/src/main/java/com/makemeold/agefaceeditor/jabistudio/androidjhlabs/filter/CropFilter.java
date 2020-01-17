package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class CropFilter {
    private int height;
    private int width;
    /* renamed from: x */
    private int f448x;
    /* renamed from: y */
    private int f449y;

    public CropFilter() {
        this(0, 0, 32, 32);
    }

    public CropFilter(int x, int y, int width, int height) {
        this.f448x = x;
        this.f449y = y;
        this.width = width;
        this.height = height;
    }

    public void setX(int x) {
        this.f448x = x;
    }

    public int getX() {
        return this.f448x;
    }

    public void setY(int y) {
        this.f449y = y;
    }

    public int getY() {
        return this.f449y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public int[] filter(int[] src, int w, int h) {
        int[] dst = new int[(this.width * this.height)];
        int[] iArr = dst;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        Bitmap.createScaledBitmap(Bitmap.createBitmap(Bitmap.createBitmap(src, 0, w, w, h, Config.ARGB_8888), this.f448x, this.f449y, w - this.f448x, h - this.f449y), this.width, this.height, false).getPixels(iArr, i, this.width, i2, i3, this.width, this.height);
        return dst;
    }

    public String toString() {
        return "Distort/Crop";
    }
}
