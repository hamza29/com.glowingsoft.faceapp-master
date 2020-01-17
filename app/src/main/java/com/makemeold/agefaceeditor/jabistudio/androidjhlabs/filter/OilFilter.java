package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;

public class OilFilter extends WholeImageFilter {
    private int levels = 256;
    private int range = 3;

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return this.range;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getLevels() {
        return this.levels;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int index = 0;
        int[] rHistogram = new int[this.levels];
        int[] gHistogram = new int[this.levels];
        int[] bHistogram = new int[this.levels];
        int[] rTotal = new int[this.levels];
        int[] gTotal = new int[this.levels];
        int[] bTotal = new int[this.levels];
        int[] outPixels = new int[(width * height)];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i;
                int r;
                int g;
                int b;
                for (i = 0; i < this.levels; i++) {
                    bTotal[i] = 0;
                    gTotal[i] = 0;
                    rTotal[i] = 0;
                    bHistogram[i] = 0;
                    gHistogram[i] = 0;
                    rHistogram[i] = 0;
                }
                for (int row = -this.range; row <= this.range; row++) {
                    int iy = y + row;
                    if (iy >= 0 && iy < height) {
                        int ioffset = iy * width;
                        for (int col = -this.range; col <= this.range; col++) {
                            int ix = x + col;
                            if (ix >= 0 && ix < width) {
                                int rgb = inPixels[ioffset + ix];
                                r = (rgb >> 16) & 255;
                                g = (rgb >> 8) & 255;
                                b = rgb & 255;
                                int ri = (this.levels * r) / 256;
                                int gi = (this.levels * g) / 256;
                                int bi = (this.levels * b) / 256;
                                rTotal[ri] = rTotal[ri] + r;
                                gTotal[gi] = gTotal[gi] + g;
                                bTotal[bi] = bTotal[bi] + b;
                                rHistogram[ri] = rHistogram[ri] + 1;
                                gHistogram[gi] = gHistogram[gi] + 1;
                                bHistogram[bi] = bHistogram[bi] + 1;
                            }
                        }
                    }
                }
                r = 0;
                g = 0;
                b = 0;
                for (i = 1; i < this.levels; i++) {
                    if (rHistogram[i] > rHistogram[r]) {
                        r = i;
                    }
                    if (gHistogram[i] > gHistogram[g]) {
                        g = i;
                    }
                    if (bHistogram[i] > bHistogram[b]) {
                        b = i;
                    }
                }
                b = bTotal[b] / bHistogram[b];
                outPixels[index] = (((inPixels[index] & -16777216) | ((rTotal[r] / rHistogram[r]) << 16)) | ((gTotal[g] / gHistogram[g]) << 8)) | b;
                index++;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Stylize/Oil...";
    }
}
