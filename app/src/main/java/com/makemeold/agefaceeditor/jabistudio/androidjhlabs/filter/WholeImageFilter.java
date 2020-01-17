package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;

public abstract class WholeImageFilter {
    protected Rect originalSpace;
    protected Rect transformedSpace;

    protected abstract int[] filterPixels(int i, int i2, int[] iArr, Rect rect);

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        this.originalSpace = new Rect(0, 0, width, height);
        this.transformedSpace = new Rect(0, 0, width, height);
        transformSpace(this.transformedSpace);
        return filterPixels(width, height, src, this.transformedSpace);
    }

    protected void transformSpace(Rect rect) {
    }
}
