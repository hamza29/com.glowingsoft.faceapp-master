package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class TritoneFilter extends PointFilter {
    private int highColor = -1;
    private int[] lut;
    private int midColor = -7829368;
    private int shadowColor = -16777216;

    public int[] filter(int[] src, int w, int h) {
        int i;
        this.lut = new int[256];
        for (i = 0; i < 128; i++) {
            this.lut[i] = ImageMath.mixColors(((float) i) / 127.0f, this.shadowColor, this.midColor);
        }
        for (i = 128; i < 256; i++) {
            this.lut[i] = ImageMath.mixColors(((float) (i - 127)) / 128.0f, this.midColor, this.highColor);
        }
        src = super.filter(src, w, h);
        this.lut = null;
        return src;
    }

    public int filterRGB(int x, int y, int rgb) {
        return this.lut[PixelUtils.brightness(rgb)];
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getShadowColor() {
        return this.shadowColor;
    }

    public void setMidColor(int midColor) {
        this.midColor = midColor;
    }

    public int getMidColor() {
        return this.midColor;
    }

    public void setHighColor(int highColor) {
        this.highColor = highColor;
    }

    public int getHighColor() {
        return this.highColor;
    }

    public String toString() {
        return "Colors/Tritone...";
    }
}
