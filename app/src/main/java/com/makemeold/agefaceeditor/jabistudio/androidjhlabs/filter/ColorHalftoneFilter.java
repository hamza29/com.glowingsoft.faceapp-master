package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.support.v4.view.ViewCompat;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class ColorHalftoneFilter {
    private float cyanScreenAngle = ((float) Math.toRadians(108.0d));
    private float dotRadius = 2.0f;
    private float magentaScreenAngle = ((float) Math.toRadians(162.0d));
    private float yellowScreenAngle = ((float) Math.toRadians(90.0d));

    public void setdotRadius(float dotRadius) {
        this.dotRadius = dotRadius;
    }

    public float getdotRadius() {
        return this.dotRadius;
    }

    public float getCyanScreenAngle() {
        return this.cyanScreenAngle;
    }

    public void setCyanScreenAngle(float cyanScreenAngle) {
        this.cyanScreenAngle = cyanScreenAngle;
    }

    public float getMagentaScreenAngle() {
        return this.magentaScreenAngle;
    }

    public void setMagentaScreenAngle(float magentaScreenAngle) {
        this.magentaScreenAngle = magentaScreenAngle;
    }

    public float getYellowScreenAngle() {
        return this.yellowScreenAngle;
    }

    public void setYellowScreenAngle(float yellowScreenAngle) {
        this.yellowScreenAngle = yellowScreenAngle;
    }

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        float gridSize = (2.0f * this.dotRadius) * 1.414f;
        float[] angles = new float[]{this.cyanScreenAngle, this.magentaScreenAngle, this.yellowScreenAngle};
        float[] fArr = new float[5];
        fArr = new float[]{0.0f, -1.0f, 1.0f, 0.0f, 0.0f};
        float[] fArr2 = new float[5];
        fArr2 = new float[]{0.0f, 0.0f, 0.0f, -1.0f, 1.0f};
        float halfGridSize = gridSize / 2.0f;
        int[] outPixels = new int[width];
        int[] inPixels = src;
        int[] dst = new int[(w * h)];
        for (int y = 0; y < height; y++) {
            int x = 0;
            int ix = y * width;
            while (x < width) {
                outPixels[x] = (inPixels[ix] & -16777216) | ViewCompat.MEASURED_SIZE_MASK;
                x++;
                ix++;
            }
            for (int channel = 0; channel < 3; channel++) {
                int shift = 16 - (channel * 8);
                int mask = 255 << shift;
                float angle = angles[channel];
                float sin = (float) Math.sin((double) angle);
                float cos = (float) Math.cos((double) angle);
                for (x = 0; x < width; x++) {
                    int i;
                    float tx = (((float) x) * cos) + (((float) y) * sin);
                    float ty = (((float) (-x)) * sin) + (((float) y) * cos);
                    tx = (tx - ImageMath.mod(tx - halfGridSize, gridSize)) + halfGridSize;
                    ty = (ty - ImageMath.mod(ty - halfGridSize, gridSize)) + halfGridSize;
                    float f = 1.0f;
                    for (i = 0; i < 5; i++) {
                        float ttx = tx + (fArr[i] * gridSize);
                        float tty = ty + (fArr2[i] * gridSize);
                        float ntx = (ttx * cos) - (tty * sin);
                        float nty = (ttx * sin) + (tty * cos);
                        float l = ((float) ((inPixels[(ImageMath.clamp((int) nty, 0, height - 1) * width) + ImageMath.clamp((int) ntx, 0, width - 1)] >> shift) & 255)) / 255.0f;
                        float dx = ((float) x) - ntx;
                        float dy = ((float) y) - nty;
                        float R = (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
                        f = Math.min(f, 1.0f - ImageMath.smoothStep(R, 1.0f + R, (float) (((double) (1.0f - (l * l))) * (((double) halfGridSize) * 1.414d))));
                    }
                    outPixels[x] = outPixels[x] & (((((int) (255.0f * f)) << shift) ^ (mask ^ -1)) | -16777216);
                }
            }
            int index = 0;
            int i;
            for (i = y * width; i < (y * width) + width; i++) {
                dst[i] = outPixels[index];
                index++;
            }
        }
        return dst;
    }

    public String toString() {
        return "Pixellate/Color Halftone...";
    }
}
