package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class MaskFilter extends PointFilter {
    private int mask;

    public MaskFilter() {
        this(-16711681);
    }

    public MaskFilter(int mask) {
        this.canFilterIndexColorModel = true;
        setMask(mask);
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return this.mask;
    }

    public int filterRGB(int x, int y, int rgb) {
        return this.mask & rgb;
    }

    public String toString() {
        return "Mask";
    }
}
