package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class GainFilter extends TransferFilter {
    private float bias = 0.5f;
    private float gain = 0.5f;

    protected float transferFunction(float f) {
        return ImageMath.bias(ImageMath.gain(f, this.gain), this.bias);
    }

    public void setGain(float gain) {
        this.gain = gain;
        this.initialized = false;
    }

    public float getGain() {
        return this.gain;
    }

    public void setBias(float bias) {
        this.bias = bias;
        this.initialized = false;
    }

    public float getBias() {
        return this.bias;
    }

    public String toString() {
        return "Colors/Gain...";
    }
}
