package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class BicubicScaleFilter {
    private int height;
    private int width;

    public BicubicScaleFilter() {
        this(32, 32);
    }

    public BicubicScaleFilter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int[] filter(int[] src, int w, int h) {
        int[] dst = new int[(this.width * this.height)];
        Bitmap srcBitmap = Bitmap.createBitmap(src, w, h, Config.ARGB_8888);
        Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, this.width, this.height, true);
        dstBitmap.getPixels(dst, 0, this.width, 0, 0, this.width, this.height);
        srcBitmap.recycle();
        dstBitmap.recycle();
        return dst;
    }

    public String toString() {
        return "Distort/Bicubic Scale";
    }
}
