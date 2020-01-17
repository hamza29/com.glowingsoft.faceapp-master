package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class BumpFilter extends ConvolveFilter {
    private static float[] embossMatrix = new float[]{-1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};

    public BumpFilter() {
        super(embossMatrix);
    }

    public String toString() {
        return "Blur/Emboss Edges";
    }
}
