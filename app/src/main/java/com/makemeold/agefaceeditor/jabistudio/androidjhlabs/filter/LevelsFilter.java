package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;
import java.lang.reflect.Array;

public class LevelsFilter extends WholeImageFilter {
    private float highLevel = 1.0f;
    private float highOutputLevel = 1.0f;
    private float lowLevel = 0.0f;
    private float lowOutputLevel = 0.0f;
    private int[][] lut;

    public void setLowLevel(float lowLevel) {
        this.lowLevel = lowLevel;
    }

    public float getLowLevel() {
        return this.lowLevel;
    }

    public void setHighLevel(float highLevel) {
        this.highLevel = highLevel;
    }

    public float getHighLevel() {
        return this.highLevel;
    }

    public void setLowOutputLevel(float lowOutputLevel) {
        this.lowOutputLevel = lowOutputLevel;
    }

    public float getLowOutputLevel() {
        return this.lowOutputLevel;
    }

    public void setHighOutputLevel(float highOutputLevel) {
        this.highOutputLevel = highOutputLevel;
    }

    public float getHighOutputLevel() {
        return this.highOutputLevel;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int i;
        if (new Histogram(inPixels, width, height, 0, width).getNumSamples() > 0) {
            this.lut = (int[][]) Array.newInstance(Integer.TYPE, new int[]{3, 256});
            float low = this.lowLevel * 255.0f;
            float high = this.highLevel * 255.0f;
            if (low == high) {
                high += 1.0f;
            }
            for (i = 0; i < 3; i++) {
                for (int j = 0; j < 256; j++) {
                    this.lut[i][j] = PixelUtils.clamp((int) (255.0f * (this.lowOutputLevel + (((this.highOutputLevel - this.lowOutputLevel) * (((float) j) - low)) / (high - low)))));
                }
            }
        } else {
            this.lut = null;
        }
        i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                inPixels[i] = filterRGB(x, y, inPixels[i]);
                i++;
            }
        }
        this.lut = null;
        return inPixels;
    }

    public int filterRGB(int x, int y, int rgb) {
        if (this.lut == null) {
            return rgb;
        }
        int a = rgb & -16777216;
        int r = this.lut[0][(rgb >> 16) & 255];
        int g = this.lut[1][(rgb >> 8) & 255];
        return (((r << 16) | a) | (g << 8)) | this.lut[2][rgb & 255];
    }

    public String toString() {
        return "Colors/Levels...";
    }
}
