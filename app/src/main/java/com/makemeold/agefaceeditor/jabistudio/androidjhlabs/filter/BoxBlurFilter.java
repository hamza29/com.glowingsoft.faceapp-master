package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.support.v4.view.MotionEventCompat;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class BoxBlurFilter {
    private float hRadius;
    private int iterations = 1;
    private boolean premultiplyAlpha = true;
    private float vRadius;

    public BoxBlurFilter(float hRadius, float vRadius, int iterations) {
        this.hRadius = hRadius;
        this.vRadius = vRadius;
        this.iterations = iterations;
    }

    public BoxBlurFilter() {

    }

    public void setPremultiplyAlpha(boolean premultiplyAlpha) {
        this.premultiplyAlpha = premultiplyAlpha;
    }

    public boolean getPremultiplyAlpha() {
        return this.premultiplyAlpha;
    }

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        int[] inPixels = new int[(width * height)];
        int[] outPixels = new int[(width * height)];
        inPixels = src;
        if (this.premultiplyAlpha) {
            ImageMath.premultiply(inPixels, 0, inPixels.length);
        }
        for (int i = 0; i < this.iterations; i++) {
            blur(inPixels, outPixels, width, height, this.hRadius);
            blur(outPixels, inPixels, height, width, this.vRadius);
        }
        blurFractional(inPixels, outPixels, width, height, this.hRadius);
        blurFractional(outPixels, inPixels, height, width, this.vRadius);
        if (this.premultiplyAlpha) {
            ImageMath.unpremultiply(inPixels, 0, inPixels.length);
        }
        return inPixels;
    }

    public static void blur(int[] in, int[] out, int width, int height, float radius) {
        int i;
        int widthMinus1 = width - 1;
        int r = (int) radius;
        int tableSize = (r * 2) + 1;
        int[] divide = new int[(tableSize * 256)];
        for (i = 0; i < tableSize * 256; i++) {
            divide[i] = i / tableSize;
        }
        int inIndex = 0;
        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0;
            int tr = 0;
            int tg = 0;
            int tb = 0;
            for (i = -r; i <= r; i++) {
                int rgb = in[ImageMath.clamp(i, 0, width - 1) + inIndex];
                ta += (rgb >> 24) & 255;
                tr += (rgb >> 16) & 255;
                tg += (rgb >> 8) & 255;
                tb += rgb & 255;
            }
            for (int x = 0; x < width; x++) {
                out[outIndex] = (((divide[ta] << 24) | (divide[tr] << 16)) | (divide[tg] << 8)) | divide[tb];
                int i1 = (x + r) + 1;
                if (i1 > widthMinus1) {
                    i1 = widthMinus1;
                }
                int i2 = x - r;
                if (i2 < 0) {
                    i2 = 0;
                }
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];
                ta += ((rgb1 >> 24) & 255) - ((rgb2 >> 24) & 255);
                tr += ((16711680 & rgb1) - (16711680 & rgb2)) >> 16;
                tg += ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & rgb1) - (MotionEventCompat.ACTION_POINTER_INDEX_MASK & rgb2)) >> 8;
                tb += (rgb1 & 255) - (rgb2 & 255);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    public static void blurFractional(int[] in, int[] out, int width, int height, float radius) {
        radius -= (float) ((int) radius);
        float f = 1.0f / (1.0f + (2.0f * radius));
        int inIndex = 0;
        for (int y = 0; y < height; y++) {
            int outIndex = y;
            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; x++) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];
                int a2 = (rgb2 >> 24) & 255;
                int r2 = (rgb2 >> 16) & 255;
                int g2 = (rgb2 >> 8) & 255;
                int b2 = rgb2 & 255;
                int i2 = ((((int) (((float) (a2 + ((int) (((float) (((rgb1 >> 24) & 255) + ((rgb3 >> 24) & 255))) * radius)))) * f)) << 24) | (((int) (((float) (r2 + ((int) (((float) (((rgb1 >> 16) & 255) + ((rgb3 >> 16) & 255))) * radius)))) * f)) << 16)) | (((int) (((float) (g2 + ((int) (((float) (((rgb1 >> 8) & 255) + ((rgb3 >> 8) & 255))) * radius)))) * f)) << 8);
                out[outIndex] = i2 | ((int) (((float) (b2 + ((int) (((float) ((rgb1 & 255) + (rgb3 & 255))) * radius)))) * f));
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    public void setHRadius(float hRadius) {
        this.hRadius = hRadius;
    }

    public float getHRadius() {
        return this.hRadius;
    }

    public void setVRadius(float vRadius) {
        this.vRadius = vRadius;
    }

    public float getVRadius() {
        return this.vRadius;
    }

    public void setRadius(float radius) {
        this.vRadius = radius;
        this.hRadius = radius;
    }

    public float getRadius() {
        return this.hRadius;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return this.iterations;
    }

    public String toString() {
        return "Blur/Box Blur...";
    }
}
