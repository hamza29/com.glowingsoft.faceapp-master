package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class SmartBlurFilter {
    private int hRadius = 5;
    private int threshold = 10;
    private int vRadius = 5;

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        int[] inPixels = new int[(width * height)];
        int[] outPixels = new int[(width * height)];
        inPixels = src;
        Kernel kernel = GaussianFilter.makeKernel((float) this.hRadius);
        thresholdBlur(kernel, inPixels, outPixels, width, height, true);
        thresholdBlur(kernel, outPixels, inPixels, height, width, true);
        return inPixels;
    }

    private void thresholdBlur(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha) {
        float[] matrix = kernel.getKernelData(null);
        int cols2 = kernel.getWidth() / 2;
        for (int y = 0; y < height; y++) {
            int ioffset = y * width;
            int outIndex = y;
            for (int x = 0; x < width; x++) {
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                float a = 0.0f;
                int moffset = cols2;
                int rgb1 = inPixels[ioffset + x];
                int a1 = (rgb1 >> 24) & 255;
                int r1 = (rgb1 >> 16) & 255;
                int g1 = (rgb1 >> 8) & 255;
                int b1 = rgb1 & 255;
                float af = 0.0f;
                float rf = 0.0f;
                float gf = 0.0f;
                float bf = 0.0f;
                for (int col = -cols2; col <= cols2; col++) {
                    float f = matrix[moffset + col];
                    if (f != 0.0f) {
                        int ix = x + col;
                        if (ix < 0 || ix >= width) {
                            ix = x;
                        }
                        int rgb2 = inPixels[ioffset + ix];
                        int a2 = (rgb2 >> 24) & 255;
                        int r2 = (rgb2 >> 16) & 255;
                        int g2 = (rgb2 >> 8) & 255;
                        int b2 = rgb2 & 255;
                        int d = a1 - a2;
                        if (d >= (-this.threshold) && d <= this.threshold) {
                            a += ((float) a2) * f;
                            af += f;
                        }
                        d = r1 - r2;
                        if (d >= (-this.threshold) && d <= this.threshold) {
                            r += ((float) r2) * f;
                            rf += f;
                        }
                        d = g1 - g2;
                        if (d >= (-this.threshold) && d <= this.threshold) {
                            g += ((float) g2) * f;
                            gf += f;
                        }
                        d = b1 - b2;
                        if (d >= (-this.threshold) && d <= this.threshold) {
                            b += ((float) b2) * f;
                            bf += f;
                        }
                    }
                }
                outPixels[outIndex] = ((((alpha ? PixelUtils.clamp((int) (((double) (af == 0.0f ? (float) a1 : a / af)) + 0.5d)) : 255) << 24) | (PixelUtils.clamp((int) (((double) (rf == 0.0f ? (float) r1 : r / rf)) + 0.5d)) << 16)) | (PixelUtils.clamp((int) (((double) (gf == 0.0f ? (float) g1 : g / gf)) + 0.5d)) << 8)) | PixelUtils.clamp((int) (((double) (bf == 0.0f ? (float) b1 : b / bf)) + 0.5d));
                outIndex += height;
            }
        }
    }

    public void setHRadius(int hRadius) {
        this.hRadius = hRadius;
    }

    public int getHRadius() {
        return this.hRadius;
    }

    public void setVRadius(int vRadius) {
        this.vRadius = vRadius;
    }

    public int getVRadius() {
        return this.vRadius;
    }

    public void setRadius(int radius) {
        this.vRadius = radius;
        this.hRadius = radius;
    }

    public int getRadius() {
        return this.hRadius;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public String toString() {
        return "Blur/Smart Blur...";
    }
}
