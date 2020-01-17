package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class RescaleFilter extends TransferFilter {
    private float scale = 1.0f;

    public RescaleFilter(float scale) {
        this.scale = scale;
    }

    public RescaleFilter() {

    }

    protected float transferFunction(float v) {
        return this.scale * v;
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.initialized = false;
    }

    public float getScale() {
        return this.scale;
    }

    public String toString() {
        return "Colors/Rescale...";
    }
}
