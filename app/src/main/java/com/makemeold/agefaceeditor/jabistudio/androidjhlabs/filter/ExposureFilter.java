package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class ExposureFilter extends TransferFilter {
    private float exposure = 1.0f;

    protected float transferFunction(float f) {
        return 1.0f - ((float) Math.exp((double) ((-f) * this.exposure)));
    }

    public void setExposure(float exposure) {
        this.exposure = exposure;
        this.initialized = false;
    }

    public float getExposure() {
        return this.exposure;
    }

    public String toString() {
        return "Colors/Exposure...";
    }
}
