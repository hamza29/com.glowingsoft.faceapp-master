package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class BlurFilter extends ConvolveFilter {
    protected static float[] blurMatrix = new float[]{0.071428575f, 0.14285715f, 0.071428575f, 0.14285715f, 0.14285715f, 0.14285715f, 0.071428575f, 0.14285715f, 0.071428575f};

    public BlurFilter() {
        super(blurMatrix);
    }

    public String toString() {
        return "Blur/Simple Blur";
    }
}
