package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.Noise;

public class RippleFilter extends TransformFilter {
    public static final int NOISE = 3;
    public static final int SAWTOOTH = 1;
    public static final int SINE = 0;
    public static final int TRIANGLE = 2;
    private int waveType;
    private float xAmplitude = 5.0f;
    private float xWavelength = 16.0f;
    private float yAmplitude = 0.0f;
    private float yWavelength = 16.0f;

    public void setXAmplitude(float xAmplitude) {
        this.xAmplitude = xAmplitude;
    }

    public float getXAmplitude() {
        return this.xAmplitude;
    }

    public void setXWavelength(float xWavelength) {
        this.xWavelength = xWavelength;
    }

    public float getXWavelength() {
        return this.xWavelength;
    }

    public void setYAmplitude(float yAmplitude) {
        this.yAmplitude = yAmplitude;
    }

    public float getYAmplitude() {
        return this.yAmplitude;
    }

    public void setYWavelength(float yWavelength) {
        this.yWavelength = yWavelength;
    }

    public float getYWavelength() {
        return this.yWavelength;
    }

    public void setWaveType(int waveType) {
        this.waveType = waveType;
    }

    public int getWaveType() {
        return this.waveType;
    }

    protected void transformSpace(Rect r) {
        if (this.edgeAction == 0) {
            r.left -= (int) this.xAmplitude;
            r.right += (int) (this.xAmplitude * 2.0f);
            r.top -= (int) this.yAmplitude;
            r.bottom += (int) (this.yAmplitude * 2.0f);
        }
    }

    protected void transformInverse(int x, int y, float[] out) {
        float fx;
        float fy;
        float nx = ((float) y) / this.xWavelength;
        float ny = ((float) x) / this.yWavelength;
        switch (this.waveType) {
            case 1:
                fx = ImageMath.mod(nx, 1.0f);
                fy = ImageMath.mod(ny, 1.0f);
                break;
            case 2:
                fx = ImageMath.triangle(nx);
                fy = ImageMath.triangle(ny);
                break;
            case 3:
                fx = Noise.noise1(nx);
                fy = Noise.noise1(ny);
                break;
            default:
                fx = (float) Math.sin((double) nx);
                fy = (float) Math.sin((double) ny);
                break;
        }
        out[0] = ((float) x) + (this.xAmplitude * fx);
        out[1] = ((float) y) + (this.yAmplitude * fy);
    }

    public String toString() {
        return "Distort/Ripple...";
    }
}
