package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class DiffuseFilter extends TransformFilter {
    private float[] cosTable;
    private float scale = 4.0f;
    private float[] sinTable;

    public DiffuseFilter() {
        setEdgeAction(1);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    protected void transformInverse(int x, int y, float[] out) {
        int angle = (int) (Math.random() * 255.0d);
        float distance = (float) Math.random();
        out[0] = ((float) x) + (this.sinTable[angle] * distance);
        out[1] = ((float) y) + (this.cosTable[angle] * distance);
    }

    public int[] filter(int[] src, int w, int h) {
        this.sinTable = new float[256];
        this.cosTable = new float[256];
        for (int i = 0; i < 256; i++) {
            float angle = (6.2831855f * ((float) i)) / 256.0f;
            this.sinTable[i] = (float) (((double) this.scale) * Math.sin((double) angle));
            this.cosTable[i] = (float) (((double) this.scale) * Math.cos((double) angle));
        }
        return super.filter(src, w, h);
    }

    public String toString() {
        return "Distort/Diffuse...";
    }
}
