package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public abstract class PointFilter {
    protected boolean canFilterIndexColorModel = false;
    private int height;
    private int width;

    public abstract int filterRGB(int i, int i2, int i3);

    public int[] filter(int[] src, int w, int h) {
        this.width = w;
        this.height = h;
        setDimensions(this.width, this.height);
        int[] inPixels = new int[this.width];
        int[] outPixels = new int[(this.width * this.height)];
        for (int y = 0; y < this.height; y++) {
            int i;
            int index = 0;
            for (i = y * this.width; i < (this.width * y) + this.width; i++) {
                inPixels[index] = src[i];
                index++;
            }
            for (int x = 0; x < this.width; x++) {
                inPixels[x] = filterRGB(x, y, inPixels[x]);
            }
            index = 0;
            for (i = y * this.width; i < (this.width * y) + this.width; i++) {
                outPixels[i] = inPixels[index];
                index++;
            }
        }
        return outPixels;
    }

    public void setDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }
}
