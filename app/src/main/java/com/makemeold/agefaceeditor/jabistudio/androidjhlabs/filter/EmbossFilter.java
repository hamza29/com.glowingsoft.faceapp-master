package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class EmbossFilter extends WholeImageFilter {
    private static final float pixelScale = 255.9f;
    private float azimuth = 2.3561945f;
    private float elevation = 0.5235988f;
    private boolean emboss = false;
    private float width45 = 3.0f;

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public float getAzimuth() {
        return this.azimuth;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public float getElevation() {
        return this.elevation;
    }

    public void setBumpHeight(float bumpHeight) {
        this.width45 = 3.0f * bumpHeight;
    }

    public float getBumpHeight() {
        return this.width45 / 3.0f;
    }

    public void setEmboss(boolean emboss) {
        this.emboss = emboss;
    }

    public boolean getEmboss() {
        return this.emboss;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int index = 0;
        int[] outPixels = new int[(width * height)];
        int bumpMapWidth = width;
        int[] bumpPixels = new int[(bumpMapWidth * height)];
        for (int i = 0; i < inPixels.length; i++) {
            bumpPixels[i] = PixelUtils.brightness(inPixels[i]);
        }
        int Lx = (int) ((Math.cos((double) this.azimuth) * Math.cos((double) this.elevation)) * 255.89999389648438d);
        int Ly = (int) ((Math.sin((double) this.azimuth) * Math.cos((double) this.elevation)) * 255.89999389648438d);
        int Lz = (int) (Math.sin((double) this.elevation) * 255.89999389648438d);
        int Nz = (int) (1530.0f / this.width45);
        int Nz2 = Nz * Nz;
        int NzLz = Nz * Lz;
        int background = Lz;
        int bumpIndex = 0;
        int y = 0;
        while (y < height) {
            int s1 = bumpIndex;
            int s2 = s1 + bumpMapWidth;
            int s3 = s2 + bumpMapWidth;
            int x = 0;
            int index2 = index;
            while (x < width) {
                int shade;
                if (y == 0 || y >= height - 2 || x == 0 || x >= width - 2) {
                    shade = background;
                } else {
                    int Nx = ((((bumpPixels[s1 - 1] + bumpPixels[s2 - 1]) + bumpPixels[s3 - 1]) - bumpPixels[s1 + 1]) - bumpPixels[s2 + 1]) - bumpPixels[s3 + 1];
                    int Ny = ((((bumpPixels[s3 - 1] + bumpPixels[s3]) + bumpPixels[s3 + 1]) - bumpPixels[s1 - 1]) - bumpPixels[s1]) - bumpPixels[s1 + 1];
                    if (Nx == 0 && Ny == 0) {
                        shade = background;
                    } else {
                        int NdotL = ((Nx * Lx) + (Ny * Ly)) + NzLz;
                        if (NdotL < 0) {
                            shade = 0;
                        } else {
                            shade = (int) (((double) NdotL) / Math.sqrt((double) (((Nx * Nx) + (Ny * Ny)) + Nz2)));
                        }
                    }
                }
                if (this.emboss) {
                    int rgb = inPixels[index2];
                    int b = ((rgb & 255) * shade) >> 8;
                    index = index2 + 1;
                    int i2 = ((((rgb >> 16) & 255) * shade) >> 8) << 16;
                    outPixels[index2] = ((i2 | (rgb & -16777216)) | (((((rgb >> 8) & 255) * shade) >> 8) << 8)) | b;
                } else {
                    index = index2 + 1;
                    outPixels[index2] = ((-16777216 | (shade << 16)) | (shade << 8)) | shade;
                }
                x++;
                s1++;
                s2++;
                s3++;
                index2 = index;
            }
            y++;
            bumpIndex += bumpMapWidth;
            index = index2;
        }
        return outPixels;
    }

    public String toString() {
        return "Stylize/Emboss...";
    }
}
