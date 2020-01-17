package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.Noise;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class MarbleFilter extends TransformFilter {
    private float amount = 1.0f;
    private float[] cosTable;
    private float[] sinTable;
    private float turbulence = 1.0f;
    private float xScale = 4.0f;
    private float yScale = 4.0f;

    public MarbleFilter() {
        setEdgeAction(1);
    }

    public void setXScale(float xScale) {
        this.xScale = xScale;
    }

    public float getXScale() {
        return this.xScale;
    }

    public void setYScale(float yScale) {
        this.yScale = yScale;
    }

    public float getYScale() {
        return this.yScale;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    public float getTurbulence() {
        return this.turbulence;
    }

    private void initialize() {
        this.sinTable = new float[256];
        this.cosTable = new float[256];
        for (int i = 0; i < 256; i++) {
            float angle = ((6.2831855f * ((float) i)) / 256.0f) * this.turbulence;
            this.sinTable[i] = (float) (((double) (-this.yScale)) * Math.sin((double) angle));
            this.cosTable[i] = (float) (((double) this.yScale) * Math.cos((double) angle));
        }
    }

    private int displacementMap(int x, int y) {
        return PixelUtils.clamp((int) (127.0f * (1.0f + Noise.noise2(((float) x) / this.xScale, ((float) y) / this.xScale))));
    }

    protected void transformInverse(int x, int y, float[] out) {
        int displacement = displacementMap(x, y);
        out[0] = ((float) x) + this.sinTable[displacement];
        out[1] = ((float) y) + this.cosTable[displacement];
    }

    public int[] filter(int[] src, int w, int h) {
        initialize();
        return super.filter(src, w, h);
    }

    public String toString() {
        return "Distort/Marble...";
    }
}
