package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class SolarizeFilter extends TransferFilter {
    protected float transferFunction(float v) {
        return v > 0.5f ? (v - 0.5f) * 2.0f : (0.5f - v) * 2.0f;
    }

    public String toString() {
        return "Colors/Solarize";
    }
}
