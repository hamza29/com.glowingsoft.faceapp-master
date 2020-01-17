package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class Curve {
    /* renamed from: x */
    public float[] f450x;
    /* renamed from: y */
    public float[] f451y;

    public Curve() {
        this.f450x = new float[]{0.0f, 1.0f};
        this.f451y = new float[]{0.0f, 1.0f};
    }

    public Curve(Curve curve) {
        this.f450x = (float[]) curve.f450x.clone();
        this.f451y = (float[]) curve.f451y.clone();
    }

    public int addKnot(float kx, float ky) {
        int pos = -1;
        int numKnots = this.f450x.length;
        float[] nx = new float[(numKnots + 1)];
        float[] ny = new float[(numKnots + 1)];
        int j = 0;
        int i = 0;
        while (i < numKnots) {
            if (pos == -1 && this.f450x[i] > kx) {
                pos = j;
                nx[j] = kx;
                ny[j] = ky;
                j++;
            }
            nx[j] = this.f450x[i];
            ny[j] = this.f451y[i];
            j++;
            i++;
        }
        if (pos == -1) {
            pos = j;
            nx[j] = kx;
            ny[j] = ky;
        }
        this.f450x = nx;
        this.f451y = ny;
        return pos;
    }

    public void removeKnot(int n) {
        int numKnots = this.f450x.length;
        if (numKnots > 2) {
            float[] nx = new float[(numKnots - 1)];
            float[] ny = new float[(numKnots - 1)];
            int j = 0;
            for (int i = 0; i < numKnots - 1; i++) {
                if (i == n) {
                    j++;
                }
                nx[i] = this.f450x[j];
                ny[i] = this.f451y[j];
                j++;
            }
            this.f450x = nx;
            this.f451y = ny;
        }
    }

    private void sortKnots() {
        int numKnots = this.f450x.length;
        for (int i = 1; i < numKnots - 1; i++) {
            for (int j = 1; j < i; j++) {
                if (this.f450x[i] < this.f450x[j]) {
                    float t = this.f450x[i];
                    this.f450x[i] = this.f450x[j];
                    this.f450x[j] = t;
                    t = this.f451y[i];
                    this.f451y[i] = this.f451y[j];
                    this.f451y[j] = t;
                }
            }
        }
    }

    protected int[] makeTable() {
        int numKnots = this.f450x.length;
        float[] nx = new float[(numKnots + 2)];
        float[] ny = new float[(numKnots + 2)];
        System.arraycopy(this.f450x, 0, nx, 1, numKnots);
        System.arraycopy(this.f451y, 0, ny, 1, numKnots);
        nx[0] = nx[1];
        ny[0] = ny[1];
        nx[numKnots + 1] = nx[numKnots];
        ny[numKnots + 1] = ny[numKnots];
        int[] table = new int[256];
        for (int i = 0; i < 1024; i++) {
            float f = ((float) i) / 1024.0f;
            table[ImageMath.clamp((int) ((ImageMath.spline(f, nx.length, nx) * 255.0f) + 0.5f), 0, 255)] = ImageMath.clamp((int) ((ImageMath.spline(f, nx.length, ny) * 255.0f) + 0.5f), 0, 255);
        }
        return table;
    }
}
