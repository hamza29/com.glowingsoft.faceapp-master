package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class SharpenFilter extends ConvolveFilter {
    private static float[] sharpenMatrix = new float[]{0.0f, -0.2f, 0.0f, -0.2f, 1.8f, -0.2f, 0.0f, -0.2f, 0.0f};

    public SharpenFilter() {
        super(sharpenMatrix);
    }

    public String toString() {
        return "Blur/Sharpen";
    }
}
