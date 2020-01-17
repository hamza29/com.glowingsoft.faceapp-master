package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;

public class MedianFilter extends WholeImageFilter {
    private int median(int[] array) {
        int i;
        int max;
        for (i = 0; i < 4; i++) {
            max = 0;
            int maxIndex = 0;
            for (int j = 0; j < 9; j++) {
                if (array[j] > max) {
                    max = array[j];
                    maxIndex = j;
                }
            }
            array[maxIndex] = 0;
        }
        max = 0;
        for (i = 0; i < 9; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private int rgbMedian(int[] r, int[] g, int[] b) {
        int index = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < 9; i++) {
            int sum = 0;
            for (int j = 0; j < 9; j++) {
                sum = ((sum + Math.abs(r[i] - r[j])) + Math.abs(g[i] - g[j])) + Math.abs(b[i] - b[j]);
            }
            if (sum < min) {
                min = sum;
                index = i;
            }
        }
        return index;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int index = 0;
        int[] argb = new int[9];
        int[] r = new int[9];
        int[] g = new int[9];
        int[] b = new int[9];
        int[] outPixels = new int[(width * height)];
        int y = 0;
        while (y < height) {
            int x = 0;
            int index2 = index;
            while (x < width) {
                int k = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    int iy = y + dy;
                    if (iy >= 0 && iy < height) {
                        int ioffset = iy * width;
                        for (int dx = -1; dx <= 1; dx++) {
                            int ix = x + dx;
                            if (ix >= 0 && ix < width) {
                                int rgb = inPixels[ioffset + ix];
                                argb[k] = rgb;
                                r[k] = (rgb >> 16) & 255;
                                g[k] = (rgb >> 8) & 255;
                                b[k] = rgb & 255;
                                k++;
                            }
                        }
                    }
                }
                while (k < 9) {
                    argb[k] = -16777216;
                    b[k] = 0;
                    g[k] = 0;
                    r[k] = 0;
                    k++;
                }
                index = index2 + 1;
                outPixels[index2] = argb[rgbMedian(r, g, b)];
                x++;
                index2 = index;
            }
            y++;
            index = index2;
        }
        return outPixels;
    }

    public String toString() {
        return "Blur/Median";
    }
}
