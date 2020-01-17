package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class HighPassFilter extends GaussianFilter {
    public HighPassFilter() {
        this.radius = 10.0f;
    }

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        int[] inPixels = new int[(width * height)];
        int[] originalPixels = new int[(width * height)];
        int[] outPixels = new int[(width * height)];
        inPixels = src;
        for (int i = 0; i < src.length; i++) {
            originalPixels[i] = src[i];
        }
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
        outPixels = originalPixels;
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = outPixels[index];
                int r1 = (rgb1 >> 16) & 255;
                int g1 = (rgb1 >> 8) & 255;
                int b1 = rgb1 & 255;
                int rgb2 = inPixels[index];
                inPixels[index] = (((-16777216 & rgb1) | ((((r1 + 255) - ((rgb2 >> 16) & 255)) / 2) << 16)) | ((((g1 + 255) - ((rgb2 >> 8) & 255)) / 2) << 8)) | (((b1 + 255) - (rgb2 & 255)) / 2);
                index++;
            }
        }
        return inPixels;
    }

    public String toString() {
        return "Blur/High Pass...";
    }
}
