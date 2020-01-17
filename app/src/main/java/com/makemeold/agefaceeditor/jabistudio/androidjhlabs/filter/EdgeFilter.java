package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class EdgeFilter extends WholeImageFilter {
    public static final float R2 = ((float) Math.sqrt(2.0d));
    public static float[] FREI_CHEN_H = new float[]{-1.0f, -R2, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, R2, 1.0f};
    public static final float[] FREI_CHEN_V = new float[]{-1.0f, 0.0f, 1.0f, -R2, 0.0f, R2, -1.0f, 0.0f, 1.0f};
    public static final float[] PREWITT_H = new float[]{-1.0f, -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
    public static final float[] PREWITT_V = new float[]{-1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f};

    public static final float[] ROBERTS_H = new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public static final float[] ROBERTS_V = new float[]{0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public static float[] SOBEL_H = new float[]{-1.0f, -2.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f};
    public static final float[] SOBEL_V = new float[]{-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f};
    protected float[] hEdgeMatrix = SOBEL_H;
    protected float[] vEdgeMatrix = SOBEL_V;

    public void setVEdgeMatrix(float[] vEdgeMatrix) {
        this.vEdgeMatrix = vEdgeMatrix;
    }

    public float[] getVEdgeMatrix() {
        return this.vEdgeMatrix;
    }

    public void setHEdgeMatrix(float[] hEdgeMatrix) {
        this.hEdgeMatrix = hEdgeMatrix;
    }

    public float[] getHEdgeMatrix() {
        return this.hEdgeMatrix;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int index = 0;
        int[] outPixels = new int[(width * height)];
        int y = 0;
        while (y < height) {
            int x = 0;
            int index2 = index;
            while (x < width) {
                int g;
                int b;
                int rh = 0;
                int gh = 0;
                int bh = 0;
                int rv = 0;
                int gv = 0;
                int bv = 0;
                int a = inPixels[(y * width) + x] & -16777216;
                for (int row = -1; row <= 1; row++) {
                    int ioffset;
                    int iy = y + row;
                    if (iy < 0 || iy >= height) {
                        ioffset = y * width;
                    } else {
                        ioffset = iy * width;
                    }
                    int moffset = ((row + 1) * 3) + 1;
                    for (int col = -1; col <= 1; col++) {
                        int ix = x + col;
                        if (ix < 0 || ix >= width) {
                            ix = x;
                        }
                        int rgb = inPixels[ioffset + ix];
                        float h = this.hEdgeMatrix[moffset + col];
                        float v = this.vEdgeMatrix[moffset + col];
                        int r = (16711680 & rgb) >> 16;
                        g = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & rgb) >> 8;
                        b = rgb & 255;
                        rh += (int) (((float) r) * h);
                        gh += (int) (((float) g) * h);
                        bh += (int) (((float) b) * h);
                        rv += (int) (((float) r) * v);
                        gv += (int) (((float) g) * v);
                        bv += (int) (((float) b) * v);
                    }
                }
                g = (int) (Math.sqrt((double) ((gh * gh) + (gv * gv))) / 1.8d);
                b = (int) (Math.sqrt((double) ((bh * bh) + (bv * bv))) / 1.8d);
                index = index2 + 1;
                outPixels[index2] = (((PixelUtils.clamp((int) (Math.sqrt((double) ((rh * rh) + (rv * rv))) / 1.8d)) << 16) | a) | (PixelUtils.clamp(g) << 8)) | PixelUtils.clamp(b);
                x++;
                index2 = index;
            }
            y++;
            index = index2;
        }
        return outPixels;
    }

    public String toString() {
        return "Edges/Detect Edges";
    }
}
