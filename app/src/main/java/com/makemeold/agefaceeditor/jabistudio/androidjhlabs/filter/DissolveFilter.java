package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.support.v4.view.ViewCompat;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import java.util.Random;

public class DissolveFilter extends PointFilter {
    private float density = 1.0f;
    private float maxDensity;
    private float minDensity;
    private Random randomNumbers;
    private float softness = 0.0f;

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return this.density;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public float getSoftness() {
        return this.softness;
    }

    public int[] filter(int[] src, int w, int h) {
        float d = (1.0f - this.density) * (this.softness + 1.0f);
        this.minDensity = d - this.softness;
        this.maxDensity = d;
        this.randomNumbers = new Random(0);
        return super.filter(src, w, h);
    }

    public int filterRGB(int x, int y, int rgb) {
        int a = (rgb >> 24) & 255;
        return (((int) (((float) a) * ImageMath.smoothStep(this.minDensity, this.maxDensity, this.randomNumbers.nextFloat()))) << 24) | (ViewCompat.MEASURED_SIZE_MASK & rgb);
    }

    public String toString() {
        return "Stylize/Dissolve...";
    }
}
