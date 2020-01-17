package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class GlowFilter extends GaussianFilter {
    private float amount;

    public GlowFilter() {
        this.amount = 0.5f;
        this.radius = 2.0f;
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
                inPixels[index] = (((-16777216 & rgb1) | (PixelUtils.clamp((int) (((float) r1) + (((float) ((rgb2 >> 16) & 255)) * a))) << 16)) | (PixelUtils.clamp((int) (((float) g1) + (((float) ((rgb2 >> 8) & 255)) * a))) << 8)) | PixelUtils.clamp((int) (((float) b1) + (((float) (rgb2 & 255)) * a)));
                index++;
            }
        }
        return inPixels;
    }

    public String toString() {
        return "Blur/Glow...";
    }
}
