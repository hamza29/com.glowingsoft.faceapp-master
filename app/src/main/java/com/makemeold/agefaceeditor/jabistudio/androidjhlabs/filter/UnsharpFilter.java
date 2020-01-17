package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class UnsharpFilter extends GaussianFilter {
    private float amount;
    private int threshold;

    public UnsharpFilter() {
        this.amount = 0.5f;
        this.threshold = 1;
        this.radius = 2.0f;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        int[] inPixels = new int[(width * height)];
        int[] outPixels = new int[(width * height)];
        inPixels = src;
        if (this.radius > 0.0f) {
            Kernel kernel = this.kernel;
            boolean z = this.alpha;
            boolean z2 = this.alpha && this.premultiplyAlpha;
            GaussianFilter.convolveAndTranspose(kernel, inPixels, outPixels, width, height, z, z2, false, CLAMP_EDGES);
            Kernel kernel2 = this.kernel;
            boolean z3 = this.alpha;
            boolean z4 = this.alpha && this.premultiplyAlpha;
            GaussianFilter.convolveAndTranspose(kernel2, outPixels, inPixels, height, width, z3, false, z4, CLAMP_EDGES);
        }
        outPixels = src;
        float a = 4.0f * this.amount;
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = outPixels[index];
                int r1 = (rgb1 >> 16) & 255;
                int g1 = (rgb1 >> 8) & 255;
                int b1 = rgb1 & 255;
                int rgb2 = inPixels[index];
                int r2 = (rgb2 >> 16) & 255;
                int g2 = (rgb2 >> 8) & 255;
                int b2 = rgb2 & 255;
                if (Math.abs(r1 - r2) >= this.threshold) {
                    r1 = PixelUtils.clamp((int) (((1.0f + a) * ((float) (r1 - r2))) + ((float) r2)));
                }
                if (Math.abs(g1 - g2) >= this.threshold) {
                    g1 = PixelUtils.clamp((int) (((1.0f + a) * ((float) (g1 - g2))) + ((float) g2)));
                }
                if (Math.abs(b1 - b2) >= this.threshold) {
                    b1 = PixelUtils.clamp((int) (((1.0f + a) * ((float) (b1 - b2))) + ((float) b2)));
                }
                inPixels[index] = (((-16777216 & rgb1) | (r1 << 16)) | (g1 << 8)) | b1;
                index++;
            }
        }
        return inPixels;
    }

    public String toString() {
        return "Blur/Unsharp Mask...";
    }
}
