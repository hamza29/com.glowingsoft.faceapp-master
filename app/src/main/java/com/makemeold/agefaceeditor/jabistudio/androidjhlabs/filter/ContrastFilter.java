package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class ContrastFilter extends TransferFilter {
    private float brightness = 1.0f;
    private float contrast = 1.0f;

    protected float transferFunction(float f) {
        return (((f * this.brightness) - 0.5f) * this.contrast) + 0.5f;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        this.initialized = false;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
        this.initialized = false;
    }

    public float getContrast() {
        return this.contrast;
    }

    public String toString() {
        return "Colors/Contrast...";
    }
}
