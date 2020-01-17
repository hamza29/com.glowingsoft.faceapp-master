package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math;

import android.support.v4.view.InputDeviceCompat;
import java.lang.reflect.Array;
import java.util.Random;

public class Noise implements Function1D, Function2D, Function3D {
    /* renamed from: B */
    private static final int f1017B = 256;
    private static final int BM = 255;
    /* renamed from: N */
    private static final int f1018N = 4096;
    static float[] g1 = new float[514];
    static float[][] g2 = ((float[][]) Array.newInstance(Float.TYPE, new int[]{514, 2}));
    static float[][] g3 = ((float[][]) Array.newInstance(Float.TYPE, new int[]{514, 3}));
    /* renamed from: p */
    static int[] f1019p = new int[514];
    private static Random randomGenerator = new Random();
    static boolean start = true;

    public float evaluate(float x) {
        return noise1(x);
    }

    public float evaluate(float x, float y) {
        return noise2(x, y);
    }

    public float evaluate(float x, float y, float z) {
        return noise3(x, y, z);
    }

    public static float turbulence2(float x, float y, float octaves) {
        float t = 0.0f;
        for (float f = 1.0f; f <= octaves; f *= 2.0f) {
            t += Math.abs(noise2(f * x, f * y)) / f;
        }
        return t;
    }

    public static float turbulence3(float x, float y, float z, float octaves) {
        float t = 0.0f;
        for (float f = 1.0f; f <= octaves; f *= 2.0f) {
            t += Math.abs(noise3(f * x, f * y, f * z)) / f;
        }
        return t;
    }

    private static float sCurve(float t) {
        return (t * t) * (3.0f - (2.0f * t));
    }

    public static float noise1(float x) {
        if (start) {
            start = false;
            init();
        }
        float t = x + 4096.0f;
        int bx0 = ((int) t) & 255;
        float rx0 = t - ((float) ((int) t));
        return 2.3f * lerp(sCurve(rx0), rx0 * g1[f1019p[bx0]], (rx0 - 1.0f) * g1[f1019p[(bx0 + 1) & 255]]);
    }

    public static float noise2(float x, float y) {
        if (start) {
            start = false;
            init();
        }
        float t = x + 4096.0f;
        int bx0 = ((int) t) & 255;
        int bx1 = (bx0 + 1) & 255;
        float rx0 = t - ((float) ((int) t));
        float rx1 = rx0 - 1.0f;
        t = y + 4096.0f;
        int by0 = ((int) t) & 255;
        int by1 = (by0 + 1) & 255;
        float ry0 = t - ((float) ((int) t));
        float ry1 = ry0 - 1.0f;
        int i = f1019p[bx0];
        int j = f1019p[bx1];
        int b00 = f1019p[i + by0];
        int b10 = f1019p[j + by0];
        int b01 = f1019p[i + by1];
        int b11 = f1019p[j + by1];
        float sx = sCurve(rx0);
        float sy = sCurve(ry0);
        float[] q = g2[b00];
        float u = (q[0] * rx0) + (q[1] * ry0);
        q = g2[b10];
        float a = lerp(sx, u, (q[0] * rx1) + (q[1] * ry0));
        q = g2[b01];
        u = (q[0] * rx0) + (q[1] * ry1);
        q = g2[b11];
        return 1.5f * lerp(sy, a, lerp(sx, u, (q[0] * rx1) + (q[1] * ry1)));
    }

    public static float noise3(float x, float y, float z) {
        if (start) {
            start = false;
            init();
        }
        float t = x + 4096.0f;
        int bx0 = ((int) t) & 255;
        int bx1 = (bx0 + 1) & 255;
        float rx0 = t - ((float) ((int) t));
        float rx1 = rx0 - 1.0f;
        t = y + 4096.0f;
        int by0 = ((int) t) & 255;
        int by1 = (by0 + 1) & 255;
        float ry0 = t - ((float) ((int) t));
        float ry1 = ry0 - 1.0f;
        t = z + 4096.0f;
        int bz0 = ((int) t) & 255;
        int bz1 = (bz0 + 1) & 255;
        float rz0 = t - ((float) ((int) t));
        float rz1 = rz0 - 1.0f;
        int i = f1019p[bx0];
        int j = f1019p[bx1];
        int b00 = f1019p[i + by0];
        int b10 = f1019p[j + by0];
        int b01 = f1019p[i + by1];
        int b11 = f1019p[j + by1];
        t = sCurve(rx0);
        float sy = sCurve(ry0);
        float sz = sCurve(rz0);
        float[] q = g3[b00 + bz0];
        float u = ((q[0] * rx0) + (q[1] * ry0)) + (q[2] * rz0);
        q = g3[b10 + bz0];
        float a = lerp(t, u, ((q[0] * rx1) + (q[1] * ry0)) + (q[2] * rz0));
        q = g3[b01 + bz0];
        u = ((q[0] * rx0) + (q[1] * ry1)) + (q[2] * rz0);
        q = g3[b11 + bz0];
        float c = lerp(sy, a, lerp(t, u, ((q[0] * rx1) + (q[1] * ry1)) + (q[2] * rz0)));
        q = g3[b00 + bz1];
        u = ((q[0] * rx0) + (q[1] * ry0)) + (q[2] * rz1);
        q = g3[b10 + bz1];
        a = lerp(t, u, ((q[0] * rx1) + (q[1] * ry0)) + (q[2] * rz1));
        q = g3[b01 + bz1];
        u = ((q[0] * rx0) + (q[1] * ry1)) + (q[2] * rz1);
        q = g3[b11 + bz1];
        return 1.5f * lerp(sz, c, lerp(sy, a, lerp(t, u, ((q[0] * rx1) + (q[1] * ry1)) + (q[2] * rz1))));
    }

    public static float lerp(float t, float a, float b) {
        return ((b - a) * t) + a;
    }

    private static void normalize2(float[] v) {
        float s = (float) Math.sqrt((double) ((v[0] * v[0]) + (v[1] * v[1])));
        v[0] = v[0] / s;
        v[1] = v[1] / s;
    }

    static void normalize3(float[] v) {
        float s = (float) Math.sqrt((double) (((v[0] * v[0]) + (v[1] * v[1])) + (v[2] * v[2])));
        v[0] = v[0] / s;
        v[1] = v[1] / s;
        v[2] = v[2] / s;
    }

    private static int random() {
        return randomGenerator.nextInt() & Integer.MAX_VALUE;
    }

    private static void init() {
        int i;
        int j;
        for (i = 0; i < 256; i++) {
            f1019p[i] = i;
            g1[i] = ((float) ((random() % 512) + InputDeviceCompat.SOURCE_ANY)) / 256.0f;
            for (j = 0; j < 2; j++) {
                g2[i][j] = ((float) ((random() % 512) + InputDeviceCompat.SOURCE_ANY)) / 256.0f;
            }
            normalize2(g2[i]);
            for (j = 0; j < 3; j++) {
                g3[i][j] = ((float) ((random() % 512) + InputDeviceCompat.SOURCE_ANY)) / 256.0f;
            }
            normalize3(g3[i]);
        }
        for (i = 255; i >= 0; i--) {
            int k = f1019p[i];
            j = random() % 256;
            f1019p[i] = f1019p[j];
            f1019p[j] = k;
        }
        for (i = 0; i < 258; i++) {
            f1019p[i + 256] = f1019p[i];
            g1[i + 256] = g1[i];
            for (j = 0; j < 2; j++) {
                g2[i + 256][j] = g2[i][j];
            }
            for (j = 0; j < 3; j++) {
                g3[i + 256][j] = g3[i][j];
            }
        }
    }

    public static float[] findRange(Function1D f, float[] minmax) {
        if (minmax == null) {
            minmax = new float[2];
        }
        float min = 0.0f;
        float max = 0.0f;
        for (float x = -100.0f; x < 100.0f; x = (float) (((double) x) + 1.27139d)) {
            float n = f.evaluate(x);
            min = Math.min(min, n);
            max = Math.max(max, n);
        }
        minmax[0] = min;
        minmax[1] = max;
        return minmax;
    }

    public static float[] findRange(Function2D f, float[] minmax) {
        if (minmax == null) {
            minmax = new float[2];
        }
        float min = 0.0f;
        float max = 0.0f;
        for (float y = -100.0f; y < 100.0f; y = (float) (((double) y) + 10.35173d)) {
            for (float x = -100.0f; x < 100.0f; x = (float) (((double) x) + 10.77139d)) {
                float n = f.evaluate(x, y);
                min = Math.min(min, n);
                max = Math.max(max, n);
            }
        }
        minmax[0] = min;
        minmax[1] = max;
        return minmax;
    }
}
