package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math;


public class ImageMath {
    public static final float HALF_PI = 1.5707964f;
    public static final float PI = 3.1415927f;
    public static final float QUARTER_PI = 0.7853982f;
    public static final float TWO_PI = 6.2831855f;
    private static final float m00 = -0.5f;
    private static final float m01 = 1.5f;
    private static final float m02 = -1.5f;
    private static final float m03 = 0.5f;
    private static final float m10 = 1.0f;
    private static final float m11 = -2.5f;
    private static final float m12 = 2.0f;
    private static final float m13 = -0.5f;
    private static final float m20 = -0.5f;
    private static final float m21 = 0.0f;
    private static final float m22 = 0.5f;
    private static final float m23 = 0.0f;
    private static final float m30 = 0.0f;
    private static final float m31 = 1.0f;
    private static final float m32 = 0.0f;
    private static final float m33 = 0.0f;

    public static float bias(float a, float b) {
        return a / ((((1.0f / b) - m12) * (1.0f - a)) + 1.0f);
    }

    public static float gain(float a, float b) {
        float c = ((1.0f / b) - m12) * (1.0f - (m12 * a));
        if (((double) a) < 0.5d) {
            return a / (c + 1.0f);
        }
        return (c - a) / (c - 1.0f);
    }

    public static float step(float a, float x) {
        return x < a ? 0.0f : 1.0f;
    }

    public static float pulse(float a, float b, float x) {
        return (x < a || x >= b) ? 0.0f : 1.0f;
    }

    public static float smoothPulse(float a1, float a2, float b1, float b2, float x) {
        if (x < a1 || x >= b2) {
            return 0.0f;
        }
        if (x < a2) {
            x = (x - a1) / (a2 - a1);
            return (x * x) * (3.0f - (m12 * x));
        } else if (x < b1) {
            return 1.0f;
        } else {
            x = (x - b1) / (b2 - b1);
            return 1.0f - ((x * x) * (3.0f - (m12 * x)));
        }
    }

    public static float smoothStep(float a, float b, float x) {
        if (x < a) {
            return 0.0f;
        }
        if (x >= b) {
            return 1.0f;
        }
        x = (x - a) / (b - a);
        return (x * x) * (3.0f - (m12 * x));
    }

    public static float circleUp(float x) {
        x = 1.0f - x;
        return (float) Math.sqrt((double) (1.0f - (x * x)));
    }

    public static float circleDown(float x) {
        return 1.0f - ((float) Math.sqrt((double) (1.0f - (x * x))));
    }

    public static float clamp(float x, float a, float b) {
        if (x < a) {
            return a;
        }
        return x > b ? b : x;
    }

    public static int clamp(int x, int a, int b) {
        if (x < a) {
            return a;
        }
        return x > b ? b : x;
    }

    public static double mod(double a, double b) {
        a -= ((double) ((int) (a / b))) * b;
        /*if (a < C0849c.f717r) {
            return a + b;
        }*/
        return a;
    }

    public static float mod(float a, float b) {
        a -= ((float) ((int) (a / b))) * b;
        if (a < 0.0f) {
            return a + b;
        }
        return a;
    }

    public static int mod(int a, int b) {
        a -= (a / b) * b;
        if (a < 0) {
            return a + b;
        }
        return a;
    }

    public static float triangle(float x) {
        float r = mod(x, 1.0f);
        if (((double) r) >= 0.5d) {
            r = 1.0f - r;
        }
        return m12 * r;
    }

    public static float lerp(float t, float a, float b) {
        return ((b - a) * t) + a;
    }

    public static int lerp(float t, int a, int b) {
        return (int) (((float) a) + (((float) (b - a)) * t));
    }

    public static int mixColors(float t, int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 255;
        int g1 = (rgb1 >> 8) & 255;
        int r2 = (rgb2 >> 16) & 255;
        int g2 = (rgb2 >> 8) & 255;
        return (((lerp(t, (rgb1 >> 24) & 255, (rgb2 >> 24) & 255) << 24) | (lerp(t, r1, r2) << 16)) | (lerp(t, g1, g2) << 8)) | lerp(t, rgb1 & 255, rgb2 & 255);
    }

    public static int bilinearInterpolate(float x, float y, int nw, int ne, int sw, int se) {
        float cx = 1.0f - x;
        float cy = 1.0f - y;
        int a = (int) ((cy * ((((float) ((nw >> 24) & 255)) * cx) + (((float) ((ne >> 24) & 255)) * x))) + (y * ((((float) ((sw >> 24) & 255)) * cx) + (((float) ((se >> 24) & 255)) * x))));
        int r = (int) ((cy * ((((float) ((nw >> 16) & 255)) * cx) + (((float) ((ne >> 16) & 255)) * x))) + (y * ((((float) ((sw >> 16) & 255)) * cx) + (((float) ((se >> 16) & 255)) * x))));
        int g = (int) ((cy * ((((float) ((nw >> 8) & 255)) * cx) + (((float) ((ne >> 8) & 255)) * x))) + (y * ((((float) ((sw >> 8) & 255)) * cx) + (((float) ((se >> 8) & 255)) * x))));
        return (((a << 24) | (r << 16)) | (g << 8)) | ((int) ((cy * ((((float) (nw & 255)) * cx) + (((float) (ne & 255)) * x))) + (y * ((((float) (sw & 255)) * cx) + (((float) (se & 255)) * x)))));
    }

    public static int brightnessNTSC(int rgb) {
        return (int) (((((float) ((rgb >> 16) & 255)) * 0.299f) + (((float) ((rgb >> 8) & 255)) * 0.587f)) + (((float) (rgb & 255)) * 0.114f));
    }

    public static float spline(float x, int numKnots, float[] knots) {
        int numSpans = numKnots - 3;
        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }
        x = clamp(x, 0.0f, 1.0f) * ((float) numSpans);
        int span = (int) x;
        if (span > numKnots - 4) {
            span = numKnots - 4;
        }
        x -= (float) span;
        float k0 = knots[span];
        float k1 = knots[span + 1];
        float k2 = knots[span + 2];
        float k3 = knots[span + 3];
        return (((((((((-0.5f * k0) + (m01 * k1)) + (m02 * k2)) + (0.5f * k3)) * x) + ((((1.0f * k0) + (m11 * k1)) + (m12 * k2)) + (-0.5f * k3))) * x) + ((((-0.5f * k0) + (0.0f * k1)) + (0.5f * k2)) + (0.0f * k3))) * x) + ((((0.0f * k0) + (1.0f * k1)) + (0.0f * k2)) + (0.0f * k3));
    }

    public static float spline(float x, int numKnots, int[] xknots, int[] yknots) {
        int numSpans = numKnots - 3;
        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }
        int span = 0;
        while (span < numSpans && ((float) xknots[span + 1]) <= x) {
            span++;
        }
        if (span > numKnots - 3) {
            span = numKnots - 3;
        }
        float t = (x - ((float) xknots[span])) / ((float) (xknots[span + 1] - xknots[span]));
        span--;
        if (span < 0) {
            span = 0;
            t = 0.0f;
        }
        float k0 = (float) yknots[span];
        float k1 = (float) yknots[span + 1];
        float k2 = (float) yknots[span + 2];
        float k3 = (float) yknots[span + 3];
        return (((((((((-0.5f * k0) + (m01 * k1)) + (m02 * k2)) + (0.5f * k3)) * t) + ((((1.0f * k0) + (m11 * k1)) + (m12 * k2)) + (-0.5f * k3))) * t) + ((((-0.5f * k0) + (0.0f * k1)) + (0.5f * k2)) + (0.0f * k3))) * t) + ((((0.0f * k0) + (1.0f * k1)) + (0.0f * k2)) + (0.0f * k3));
    }

    public static int colorSpline(float x, int numKnots, int[] knots) {
        int numSpans = numKnots - 3;
        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }
        x = clamp(x, 0.0f, 1.0f) * ((float) numSpans);
        int span = (int) x;
        if (span > numKnots - 4) {
            span = numKnots - 4;
        }
        x -= (float) span;
        int v = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            float k0 = (float) ((knots[span] >> shift) & 255);
            float k1 = (float) ((knots[span + 1] >> shift) & 255);
            float k2 = (float) ((knots[span + 2] >> shift) & 255);
            float k3 = (float) ((knots[span + 3] >> shift) & 255);
            float c2 = (((1.0f * k0) + (m11 * k1)) + (m12 * k2)) + (-0.5f * k3);
            float c1 = (((-0.5f * k0) + (0.0f * k1)) + (0.5f * k2)) + (0.0f * k3);
            float c0 = (((0.0f * k0) + (1.0f * k1)) + (0.0f * k2)) + (0.0f * k3);
            int n = (int) ((((((((((-0.5f * k0) + (m01 * k1)) + (m02 * k2)) + (0.5f * k3)) * x) + c2) * x) + c1) * x) + c0);
            if (n < 0) {
                n = 0;
            } else if (n > 255) {
                n = 255;
            }
            v |= n << shift;
        }
        return v;
    }

    public static int colorSpline(int x, int numKnots, int[] xknots, int[] yknots) {
        int numSpans = numKnots - 3;
        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }
        int span = 0;
        while (span < numSpans && xknots[span + 1] <= x) {
            span++;
        }
        if (span > numKnots - 3) {
            span = numKnots - 3;
        }
        float t = ((float) (x - xknots[span])) / ((float) (xknots[span + 1] - xknots[span]));
        span--;
        if (span < 0) {
            span = 0;
            t = 0.0f;
        }
        int v = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            float k0 = (float) ((yknots[span] >> shift) & 255);
            float k1 = (float) ((yknots[span + 1] >> shift) & 255);
            float k2 = (float) ((yknots[span + 2] >> shift) & 255);
            float k3 = (float) ((yknots[span + 3] >> shift) & 255);
            float c2 = (((1.0f * k0) + (m11 * k1)) + (m12 * k2)) + (-0.5f * k3);
            float c1 = (((-0.5f * k0) + (0.0f * k1)) + (0.5f * k2)) + (0.0f * k3);
            float c0 = (((0.0f * k0) + (1.0f * k1)) + (0.0f * k2)) + (0.0f * k3);
            int n = (int) ((((((((((-0.5f * k0) + (m01 * k1)) + (m02 * k2)) + (0.5f * k3)) * t) + c2) * t) + c1) * t) + c0);
            if (n < 0) {
                n = 0;
            } else if (n > 255) {
                n = 255;
            }
            v |= n << shift;
        }
        return v;
    }

    public static void resample(int[] source, int[] dest, int length, int offset, int stride, float[] out) {
        int srcIndex = offset;
        int destIndex = offset;
        int lastIndex = source.length;
        float[] in = new float[(length + 2)];
        int i = 0;
        for (int j = 0; j < length; j++) {
            while (out[i + 1] < ((float) j)) {
                i++;
            }
            in[j] = ((float) i) + ((((float) j) - out[i]) / (out[i + 1] - out[i]));
        }
        in[length] = (float) length;
        in[length + 1] = (float) length;
        float inSegment = 1.0f;
        float outSegment = in[1];
        float sizfac = outSegment;
        float bSum = 0.0f;
        float gSum = 0.0f;
        float rSum = 0.0f;
        float aSum = 0.0f;
        int rgb = source[srcIndex];
        int a = (rgb >> 24) & 255;
        int r = (rgb >> 16) & 255;
        int g = (rgb >> 8) & 255;
        int b = rgb & 255;
        srcIndex += stride;
        rgb = source[srcIndex];
        int nextA = (rgb >> 24) & 255;
        int nextR = (rgb >> 16) & 255;
        int nextG = (rgb >> 8) & 255;
        int nextB = rgb & 255;
        srcIndex += stride;
        i = 1;
        while (i <= length) {
            float aIntensity = (((float) a) * inSegment) + ((1.0f - inSegment) * ((float) nextA));
            float rIntensity = (((float) r) * inSegment) + ((1.0f - inSegment) * ((float) nextR));
            float gIntensity = (((float) g) * inSegment) + ((1.0f - inSegment) * ((float) nextG));
            float bIntensity = (((float) b) * inSegment) + ((1.0f - inSegment) * ((float) nextB));
            if (inSegment < outSegment) {
                aSum += aIntensity * inSegment;
                rSum += rIntensity * inSegment;
                gSum += gIntensity * inSegment;
                bSum += bIntensity * inSegment;
                outSegment -= inSegment;
                inSegment = 1.0f;
                a = nextA;
                r = nextR;
                g = nextG;
                b = nextB;
                if (srcIndex < lastIndex) {
                    rgb = source[srcIndex];
                }
                nextA = (rgb >> 24) & 255;
                nextR = (rgb >> 16) & 255;
                nextG = (rgb >> 8) & 255;
                nextB = rgb & 255;
                srcIndex += stride;
            } else {
                dest[destIndex] = (((((int) Math.min((aSum + (aIntensity * outSegment)) / sizfac, 255.0f)) << 24) | (((int) Math.min((rSum + (rIntensity * outSegment)) / sizfac, 255.0f)) << 16)) | (((int) Math.min((gSum + (gIntensity * outSegment)) / sizfac, 255.0f)) << 8)) | ((int) Math.min((bSum + (bIntensity * outSegment)) / sizfac, 255.0f));
                destIndex += stride;
                bSum = 0.0f;
                gSum = 0.0f;
                rSum = 0.0f;
                aSum = 0.0f;
                inSegment -= outSegment;
                outSegment = in[i + 1] - in[i];
                sizfac = outSegment;
                i++;
            }
        }
    }

    public static void premultiply(int[] p, int offset, int length) {
        length += offset;
        for (int i = offset; i < length; i++) {
            int rgb = p[i];
            int a = (rgb >> 24) & 255;
            float f = ((float) a) * 0.003921569f;
            int b = (int) (((float) (rgb & 255)) * f);
            p[i] = (((a << 24) | (((int) (((float) ((rgb >> 16) & 255)) * f)) << 16)) | (((int) (((float) ((rgb >> 8) & 255)) * f)) << 8)) | b;
        }
    }

    public static void unpremultiply(int[] p, int offset, int length) {
        length += offset;
        for (int i = offset; i < length; i++) {
            int rgb = p[i];
            int a = (rgb >> 24) & 255;
            int r = (rgb >> 16) & 255;
            int g = (rgb >> 8) & 255;
            int b = rgb & 255;
            if (!(a == 0 || a == 255)) {
                float f = 255.0f / ((float) a);
                r = (int) (((float) r) * f);
                g = (int) (((float) g) * f);
                b = (int) (((float) b) * f);
                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                if (b > 255) {
                    b = 255;
                }
                p[i] = (((a << 24) | (r << 16)) | (g << 8)) | b;
            }
        }
    }
}
