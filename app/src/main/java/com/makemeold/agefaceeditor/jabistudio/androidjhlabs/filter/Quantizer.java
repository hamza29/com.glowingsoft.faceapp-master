package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public interface Quantizer {
    void addPixels(int[] iArr, int i, int i2);

    int[] buildColorTable();

    int getIndexForColor(int i);

    void setup(int i);
}
