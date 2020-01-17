package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import java.lang.reflect.Array;

public class Histogram {
    public static final int BLUE = 2;
    public static final int GRAY = 3;
    public static final int GREEN = 1;
    public static final int RED = 0;
    protected int[][] histogram;
    protected boolean isGray;
    protected int[] maxFrequency;
    protected int[] maxValue;
    protected float[] mean;
    protected int[] minFrequency;
    protected int[] minValue;
    protected int numSamples;

    public Histogram() {
        this.histogram = null;
        this.numSamples = 0;
        this.isGray = true;
        this.minValue = null;
        this.maxValue = null;
        this.minFrequency = null;
        this.maxFrequency = null;
        this.mean = null;
    }

    public Histogram(int[] pixels, int w, int h, int offset, int stride) {
        this.histogram = (int[][]) Array.newInstance(Integer.TYPE, new int[]{3, 256});
        this.minValue = new int[4];
        this.maxValue = new int[4];
        this.minFrequency = new int[3];
        this.maxFrequency = new int[3];
        this.mean = new float[3];
        this.numSamples = w * h;
        this.isGray = true;
        int y = 0;
        while (y < h) {
            int index;
            int x = 0;
            int index2 = offset + (y * stride);
            while (x < w) {
                index = index2 + 1;
                int rgb = pixels[index2];
                int r = (rgb >> 16) & 255;
                int g = (rgb >> 8) & 255;
                int b = rgb & 255;
                int[] iArr = this.histogram[0];
                iArr[r] = iArr[r] + 1;
                iArr = this.histogram[1];
                iArr[g] = iArr[g] + 1;
                iArr = this.histogram[2];
                iArr[b] = iArr[b] + 1;
                x++;
                index2 = index;
            }
            y++;
            index = index2;
        }
        int i = 0;
        while (i < 256) {
            if (this.histogram[0][i] != this.histogram[1][i] || this.histogram[1][i] != this.histogram[2][i]) {
                this.isGray = false;
                break;
            }
            i++;
        }
        for (i = 0; i < 3; i++) {
            int j;
            float[] fArr;
            for (j = 0; j < 256; j++) {
                if (this.histogram[i][j] > 0) {
                    this.minValue[i] = j;
                    break;
                }
            }
            for (j = 255; j >= 0; j--) {
                if (this.histogram[i][j] > 0) {
                    this.maxValue[i] = j;
                    break;
                }
            }
            this.minFrequency[i] = Integer.MAX_VALUE;
            this.maxFrequency[i] = 0;
            for (j = 0; j < 256; j++) {
                this.minFrequency[i] = Math.min(this.minFrequency[i], this.histogram[i][j]);
                this.maxFrequency[i] = Math.max(this.maxFrequency[i], this.histogram[i][j]);
                fArr = this.mean;
                fArr[i] = fArr[i] + ((float) (this.histogram[i][j] * j));
            }
            fArr = this.mean;
            fArr[i] = fArr[i] / ((float) this.numSamples);
        }
        this.minValue[3] = Math.min(Math.min(this.minValue[0], this.minValue[1]), this.minValue[2]);
        this.maxValue[3] = Math.max(Math.max(this.maxValue[0], this.maxValue[1]), this.maxValue[2]);
    }

    public boolean isGray() {
        return this.isGray;
    }

    public int getNumSamples() {
        return this.numSamples;
    }

    public int getFrequency(int value) {
        if (this.numSamples <= 0 || !this.isGray || value < 0 || value > 255) {
            return -1;
        }
        return this.histogram[0][value];
    }

    public int getFrequency(int channel, int value) {
        if (this.numSamples < 1 || channel < 0 || channel > 2 || value < 0 || value > 255) {
            return -1;
        }
        return this.histogram[channel][value];
    }

    public int getMinFrequency() {
        if (this.numSamples <= 0 || !this.isGray) {
            return -1;
        }
        return this.minFrequency[0];
    }

    public int getMinFrequency(int channel) {
        if (this.numSamples < 1 || channel < 0 || channel > 2) {
            return -1;
        }
        return this.minFrequency[channel];
    }

    public int getMaxFrequency() {
        if (this.numSamples <= 0 || !this.isGray) {
            return -1;
        }
        return this.maxFrequency[0];
    }

    public int getMaxFrequency(int channel) {
        if (this.numSamples < 1 || channel < 0 || channel > 2) {
            return -1;
        }
        return this.maxFrequency[channel];
    }

    public int getMinValue() {
        if (this.numSamples <= 0 || !this.isGray) {
            return -1;
        }
        return this.minValue[0];
    }

    public int getMinValue(int channel) {
        return this.minValue[channel];
    }

    public int getMaxValue() {
        if (this.numSamples <= 0 || !this.isGray) {
            return -1;
        }
        return this.maxValue[0];
    }

    public int getMaxValue(int channel) {
        return this.maxValue[channel];
    }

    public float getMeanValue() {
        if (this.numSamples <= 0 || !this.isGray) {
            return -1.0f;
        }
        return this.mean[0];
    }

    public float getMeanValue(int channel) {
        if (this.numSamples <= 0 || channel < 0 || channel > 2) {
            return -1.0f;
        }
        return this.mean[channel];
    }
}
