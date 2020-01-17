package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class PosterizeFilter extends PointFilter {
    private boolean initialized = false;
    private int[] levels;
    private int numLevels;

    public PosterizeFilter() {
        setNumLevels(6);
    }

    public void setNumLevels(int numLevels) {
        this.numLevels = numLevels;
        this.initialized = false;
    }

    public int getNumLevels() {
        return this.numLevels;
    }

    protected void initialize() {
        this.levels = new int[256];
        if (this.numLevels != 1) {
            for (int i = 0; i < 256; i++) {
                this.levels[i] = (((this.numLevels * i) / 256) * 255) / (this.numLevels - 1);
            }
        }
    }

    public int filterRGB(int x, int y, int rgb) {
        if (!this.initialized) {
            this.initialized = true;
            initialize();
        }
        int a = rgb & -16777216;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        int r = this.levels[(rgb >> 16) & 255];
        g = this.levels[g];
        return (((r << 16) | a) | (g << 8)) | this.levels[b];
    }

    public String toString() {
        return "Colors/Posterize...";
    }
}
