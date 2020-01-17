package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Matrix;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class MotionBlurFilter {
    private float angle = 0.0f;
    private float distance = 1.0f;
    private float falloff = 1.0f;
    private boolean premultiplyAlpha = true;
    private float rotation = 0.0f;
    private boolean wrapEdges = false;
    private float zoom = 0.0f;

    public MotionBlurFilter(float distance, float angle, float rotation, float zoom) {
        this.distance = distance;
        this.angle = angle;
        this.rotation = rotation;
        this.zoom = zoom;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getZoom() {
        return this.zoom;
    }

    public void setWrapEdges(boolean wrapEdges) {
        this.wrapEdges = wrapEdges;
    }

    public boolean getWrapEdges() {
        return this.wrapEdges;
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
        int[] inPixels = src;
        int[] outPixels = new int[(width * height)];
        int cx = width / 2;
        int cy = height / 2;
        int index = 0;
        float imageRadius = (float) Math.sqrt((double) ((cx * cx) + (cy * cy)));
        float translateX = (float) (((double) this.distance) * Math.cos((double) this.angle));
        float translateY = (float) (((double) this.distance) * (-Math.sin((double) this.angle)));
        int repetitions = (int) ((this.distance + Math.abs(this.rotation * imageRadius)) + (this.zoom * imageRadius));
        Matrix t = new Matrix();
        float[] p = new float[2];
        if (this.premultiplyAlpha) {
            ImageMath.premultiply(inPixels, 0, inPixels.length);
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = 0;
                int r = 0;
                int g = 0;
                int b = 0;
                int count = 0;
                for (int i = 0; i < repetitions; i++) {
                    int newX = x;
                    int newY = y;
                    float f = ((float) i) / ((float) repetitions);
                    p[0] = (float) x;
                    p[1] = (float) y;
                    t.reset();
                    t.preTranslate(((float) cx) + (f * translateX), ((float) cy) + (f * translateY));
                    float s = 1.0f - (this.zoom * f);
                    t.preScale(s, s);
                    if (this.rotation != 0.0f) {
                        t.preRotate((-this.rotation) * f);
                    }
                    t.preTranslate((float) (-cx), (float) (-cy));
                    t.mapPoints(p, p);
                    newX = (int) p[0];
                    newY = (int) p[1];
                    if (newX < 0 || newX >= width) {
                        if (!this.wrapEdges) {
                            break;
                        }
                        newX = ImageMath.mod(newX, width);
                    }
                    if (newY < 0 || newY >= height) {
                        if (!this.wrapEdges) {
                            break;
                        }
                        newY = ImageMath.mod(newY, height);
                    }
                    count++;
                    int rgb = inPixels[(newY * width) + newX];
                    a += (rgb >> 24) & 255;
                    r += (rgb >> 16) & 255;
                    g += (rgb >> 8) & 255;
                    b += rgb & 255;
                }
                if (count == 0) {
                    outPixels[index] = inPixels[index];
                } else {
                    a = PixelUtils.clamp(a / count);
                    r = PixelUtils.clamp(r / count);
                    g = PixelUtils.clamp(g / count);
                    outPixels[index] = (((a << 24) | (r << 16)) | (g << 8)) | PixelUtils.clamp(b / count);
                }
                index++;
            }
        }
        if (this.premultiplyAlpha) {
            ImageMath.unpremultiply(outPixels, 0, inPixels.length);
        }
        return outPixels;
    }

    public String toString() {
        return "Blur/Motion Blur...";
    }
}
