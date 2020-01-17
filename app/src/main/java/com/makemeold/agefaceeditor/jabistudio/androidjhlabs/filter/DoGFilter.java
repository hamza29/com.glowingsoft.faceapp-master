package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Color;

public class DoGFilter {
    private boolean invert;
    private boolean normalize = true;
    private float radius1 = 1.0f;
    private float radius2 = 2.0f;

    public void setRadius1(float radius1) {
        this.radius1 = radius1;
    }

    public float getRadius1() {
        return this.radius1;
    }

    public void setRadius2(float radius2) {
        this.radius2 = radius2;
    }

    public float getRadius2() {
        return this.radius2;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    public boolean getNormalize() {
        return this.normalize;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public boolean getInvert() {
        return this.invert;
    }

    public int[] filter(int[] src, int width, int height) {
        int[] originalSrc = new int[(width * height)];
        for (int i = 0; i < src.length; i++) {
            originalSrc[i] = src[i];
        }
        int[] image1 = new int[(width * height)];
        int[] image2 = new int[(width * height)];
        image2 = compose(width, height, new BoxBlurFilter(this.radius1, this.radius1, 3).filter(originalSrc, width, height), new BoxBlurFilter(this.radius2, this.radius2, 3).filter(src, width, height), 1.0f);
        if (this.normalize && this.radius1 != this.radius2) {
            int y;
            int x;
            int rgb;
            int r;
            int g;
            int b;
            int[] pixels = null;
            int max = 0;
            for (y = 0; y < height; y++) {
                pixels = getLineRGB(image2, y, width, pixels);
                for (x = 0; x < width; x++) {
                    rgb = pixels[x];
                    r = (rgb >> 16) & 255;
                    g = (rgb >> 8) & 255;
                    b = rgb & 255;
                    if (r > max) {
                        max = r;
                    }
                    if (g > max) {
                        max = g;
                    }
                    if (b > max) {
                        max = b;
                    }
                }
            }
            for (y = 0; y < height; y++) {
                pixels = getLineRGB(image2, y, width, pixels);
                for (x = 0; x < width; x++) {
                    rgb = pixels[x];
                    r = (rgb >> 16) & 255;
                    g = (rgb >> 8) & 255;
                    b = rgb & 255;
                    if (max != 0) {
                        b = (b * 255) / max;
                        pixels[x] = (((-16777216 & rgb) | (((r * 255) / max) << 16)) | (((g * 255) / max) << 8)) | b;
                    }
                }
                setLineRGB(image2, y, width, pixels);
            }
        }
        if (this.invert) {
            return new InvertFilter().filter(image2, width, height);
        }
        return image2;
    }

    private int[] getLineRGB(int[] src, int y, int width, int[] pixel) {
        pixel = new int[width];
        for (int i = 0; i < width; i++) {
            pixel[i] = src[(width * y) + i];
        }
        return pixel;
    }

    private void setLineRGB(int[] src, int y, int width, int[] pixel) {
        for (int i = 0; i < width; i++) {
            src[(width * y) + i] = pixel[i];
        }
    }

    private int[] getRasterLineRGB(int[] src, int y, int width, int[] pixel) {
        pixel = new int[(width * 4)];
        int j = width * y;
        for (int i = 0; i < width * 4; i += 4) {
            pixel[i] = Color.alpha(src[j]);
            pixel[i + 1] = Color.red(src[j]);
            pixel[i + 2] = Color.green(src[j]);
            pixel[i + 3] = Color.blue(src[j]);
            j++;
        }
        return pixel;
    }

    private void setRasterLineRGB(int[] dst, int y, int width, int[] pixel) {
        int j = width * y;
        for (int i = 0; i < width * 4; i += 4) {
            dst[j] = Color.argb(pixel[i], pixel[i + 1], pixel[i + 2], pixel[i + 3]);
            j++;
        }
    }

    public int[] compose(int w, int h, int[] src, int[] dst, float alpha) {
        int[] dstOut = new int[src.length];
        int y1 = 0 + h;
        int[] srcpixel = null;
        int[] dstpixel = null;
        for (int y = 0; y < y1; y++) {
            srcpixel = getRasterLineRGB(src, y, w, srcpixel);
            dstpixel = getRasterLineRGB(dst, y, w, dstpixel);
            composeRGB(srcpixel, dstpixel, alpha);
            setRasterLineRGB(dstOut, y, w, dstpixel);
        }
        return dstOut;
    }

    public void composeRGB(int[] src, int[] dst, float alpha) {
        int w = src.length;
        for (int i = 0; i < w; i += 4) {
            int sa = src[i];
            int dia = dst[i];
            int sr = src[i + 1];
            int dir = dst[i + 1];
            int sg = src[i + 2];
            int dig = dst[i + 2];
            int sb = src[i + 3];
            int dib = dst[i + 3];
            int dor = dir - sr;
            if (dor < 0) {
                dor = 0;
            }
            int dog = dig - sg;
            if (dog < 0) {
                dog = 0;
            }
            int dob = dib - sb;
            if (dob < 0) {
                dob = 0;
            }
            float a = (((float) sa) * alpha) / 255.0f;
            float ac = 1.0f - a;
            dst[i] = (int) ((((float) sa) * alpha) + (((float) dia) * ac));
            dst[i + 1] = (int) ((((float) dor) * a) + (((float) dir) * ac));
            dst[i + 2] = (int) ((((float) dog) * a) + (((float) dig) * ac));
            dst[i + 3] = (int) ((((float) dob) * a) + (((float) dib) * ac));
        }
    }

    public String toString() {
        return "Edges/Difference of Gaussians...";
    }
}
