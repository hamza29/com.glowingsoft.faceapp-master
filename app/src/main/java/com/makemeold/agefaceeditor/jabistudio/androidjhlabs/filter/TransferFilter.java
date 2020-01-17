package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public abstract class TransferFilter extends PointFilter {
    protected int[] bTable;
    protected int[] gTable;
    protected boolean initialized;
    protected int[] rTable;

    public TransferFilter() {
        this.initialized = false;
        this.canFilterIndexColorModel = true;
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & -16777216;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        int r = this.rTable[(rgb >> 16) & 255];
        g = this.gTable[g];
        return (((r << 16) | a) | (g << 8)) | this.bTable[b];
    }

    public int[] filter(int[] src, int w, int h) {
        if (!this.initialized) {
            initialize();
        }
        return super.filter(src, w, h);
    }

    protected void initialize() {
        this.initialized = true;
        int[] makeTable = makeTable();
        this.bTable = makeTable;
        this.gTable = makeTable;
        this.rTable = makeTable;
    }

    protected int[] makeTable() {
        int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            table[i] = PixelUtils.clamp((int) (transferFunction(((float) i) / 255.0f) * 255.0f));
        }
        return table;
    }

    protected float transferFunction(float v) {
        return 0.0f;
    }

    public int[] getLUT() {
        if (!this.initialized) {
            initialize();
        }
        int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = filterRGB(0, 0, (((i << 24) | (i << 16)) | (i << 8)) | i);
        }
        return lut;
    }
}
