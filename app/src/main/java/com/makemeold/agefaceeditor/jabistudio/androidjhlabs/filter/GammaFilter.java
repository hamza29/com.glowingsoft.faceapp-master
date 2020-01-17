package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class GammaFilter extends TransferFilter {
    private float bGamma;
    private float gGamma;
    private float rGamma;

    public GammaFilter() {
        this(1.0f);
    }

    public GammaFilter(float gamma) {
        this(gamma, gamma, gamma);
    }

    public GammaFilter(float rGamma, float gGamma, float bGamma) {
        setGamma(rGamma, gGamma, bGamma);
    }

    public void setGamma(float rGamma, float gGamma, float bGamma) {
        this.rGamma = rGamma;
        this.gGamma = gGamma;
        this.bGamma = bGamma;
        this.initialized = false;
    }

    public void setGamma(float gamma) {
        setGamma(gamma, gamma, gamma);
    }

    public float getGamma() {
        return this.rGamma;
    }

    protected void initialize() {
        this.rTable = makeTable(this.rGamma);
        if (this.gGamma == this.rGamma) {
            this.gTable = this.rTable;
        } else {
            this.gTable = makeTable(this.gGamma);
        }
        if (this.bGamma == this.rGamma) {
            this.bTable = this.rTable;
        } else if (this.bGamma == this.gGamma) {
            this.bTable = this.gTable;
        } else {
            this.bTable = makeTable(this.bGamma);
        }
    }

    private int[] makeTable(float gamma) {
        int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            int v = (int) ((Math.pow(((double) i) / 255.0d, 1.0d / ((double) gamma)) * 255.0d) + 0.5d);
            if (v > 255) {
                v = 255;
            }
            table[i] = v;
        }
        return table;
    }

    public String toString() {
        return "Colors/Gamma...";
    }
}
