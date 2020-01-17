package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;
import java.util.Random;

public class NoiseFilter extends PointFilter {
    public static final int GAUSSIAN = 0;
    public static final int UNIFORM = 1;
    private int amount = 25;
    private float density = 1.0f;
    private int distribution = 1;
    private boolean monochrome = false;
    private Random randomNumbers = new Random();

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setDistribution(int distribution) {
        this.distribution = distribution;
    }

    public int getDistribution() {
        return this.distribution;
    }

    public void setMonochrome(boolean monochrome) {
        this.monochrome = monochrome;
    }

    public boolean getMonochrome() {
        return this.monochrome;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return this.density;
    }

    private int random(int x) {
        x += (int) ((this.distribution == 0 ? this.randomNumbers.nextGaussian() : (double) ((2.0f * this.randomNumbers.nextFloat()) - 1.0f)) * ((double) this.amount));
        if (x < 0) {
            return 0;
        }
        if (x > 255) {
            return 255;
        }
        return x;
    }

    public int filterRGB(int x, int y, int rgb) {
        if (this.randomNumbers.nextFloat() > this.density) {
            return rgb;
        }
        int a = rgb & -16777216;
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        if (this.monochrome) {
            int n = (int) ((this.distribution == 0 ? this.randomNumbers.nextGaussian() : (double) ((2.0f * this.randomNumbers.nextFloat()) - 1.0f)) * ((double) this.amount));
            r = PixelUtils.clamp(r + n);
            g = PixelUtils.clamp(g + n);
            b = PixelUtils.clamp(b + n);
        } else {
            r = random(r);
            g = random(g);
            b = random(b);
        }
        return (((r << 16) | a) | (g << 8)) | b;
    }

    public String toString() {
        return "Stylize/Add Noise...";
    }
}
