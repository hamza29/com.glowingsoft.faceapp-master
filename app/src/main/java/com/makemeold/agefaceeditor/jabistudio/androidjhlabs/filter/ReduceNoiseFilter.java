package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;

public class ReduceNoiseFilter extends WholeImageFilter {
    private int smooth(int[] v) {
        int minindex = 0;
        int maxindex = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                if (v[i] < min) {
                    min = v[i];
                    minindex = i;
                }
                if (v[i] > max) {
                    max = v[i];
                    maxindex = i;
                }
            }
        }
        if (v[4] < min) {
            return v[minindex];
        }
        if (v[4] > max) {
            return v[maxindex];
        }
        return v[4];
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int index = 0;
        int[] r = new int[9];
        int[] g = new int[9];
        int[] b = new int[9];
        int[] outPixels = new int[(width * height)];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int k = 0;
                int irgb = inPixels[index];
                int ir = (irgb >> 16) & 255;
                int ig = (irgb >> 8) & 255;
                int ib = irgb & 255;
                for (int dy = -1; dy <= 1; dy++) {
                    int iy = y + dy;
                    int dx;
                    if (iy < 0 || iy >= height) {
                        for (dx = -1; dx <= 1; dx++) {
                            r[k] = ir;
                            g[k] = ig;
                            b[k] = ib;
                            k++;
                        }
                    } else {
                        int ioffset = iy * width;
                        for (dx = -1; dx <= 1; dx++) {
                            int ix = x + dx;
                            if (ix < 0 || ix >= width) {
                                r[k] = ir;
                                g[k] = ig;
                                b[k] = ib;
                            } else {
                                int rgb = inPixels[ioffset + ix];
                                r[k] = (rgb >> 16) & 255;
                                g[k] = (rgb >> 8) & 255;
                                b[k] = rgb & 255;
                            }
                            k++;
                        }
                    }
                }
                outPixels[index] = (((inPixels[index] & -16777216) | (smooth(r) << 16)) | (smooth(g) << 8)) | smooth(b);
                index++;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Blur/Smooth";
    }
}
