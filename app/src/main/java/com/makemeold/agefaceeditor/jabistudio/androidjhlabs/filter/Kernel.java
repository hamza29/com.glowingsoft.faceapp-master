package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class Kernel {
    private int mHeight;
    private float[] mMatrix;
    private int mWidth;

    public Kernel(int w, int h, float[] matrix) {
        this.mWidth = w;
        this.mHeight = h;
        this.mMatrix = matrix;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public float[] getKernelData(float[] data) {
        return this.mMatrix;
    }
}
