package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class GaussianFilter extends ConvolveFilter {
    protected Kernel kernel;
    protected float radius;

    public GaussianFilter() {
        this(2.0f);
    }

    public GaussianFilter(float radius) {
        setRadius(radius);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.kernel = makeKernel(radius);
    }

    public float getRadius() {
        return this.radius;
    }

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        int[] inPixels = new int[(width * height)];
        int[] outPixels = new int[(width * height)];
        inPixels = src;
        if (this.radius > 0.0f) {
            Kernel kernel = this.kernel;
            boolean z = this.alpha;
            boolean z2 = this.alpha && this.premultiplyAlpha;
            convolveAndTranspose(kernel, inPixels, outPixels, width, height, z, z2, false, CLAMP_EDGES);
            Kernel kernel2 = this.kernel;
            boolean z3 = this.alpha;
            boolean z4 = this.alpha && this.premultiplyAlpha;
            convolveAndTranspose(kernel2, outPixels, inPixels, height, width, z3, false, z4, CLAMP_EDGES);
        }
        return inPixels;
    }

    public static void convolveAndTranspose(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, boolean premultiply, boolean unpremultiply, int edgeAction) {
        float[] matrix = kernel.getKernelData(null);
        int cols2 = kernel.getWidth() / 2;
        for (int y = 0; y < height; y++) {
            int index = y;
            int ioffset = y * width;
            for (int x = 0; x < width; x++) {
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                float a = 0.0f;
                int moffset = cols2;
                for (int col = -cols2; col <= cols2; col++) {
                    float f = matrix[moffset + col];
                    if (f != 0.0f) {
                        int ix = x + col;
                        if (ix < 0) {
                            if (edgeAction == CLAMP_EDGES) {
                                ix = 0;
                            } else if (edgeAction == WRAP_EDGES) {
                                ix = (x + width) % width;
                            }
                        } else if (ix >= width) {
                            if (edgeAction == CLAMP_EDGES) {
                                ix = width - 1;
                            } else if (edgeAction == WRAP_EDGES) {
                                ix = (x + width) % width;
                            }
                        }
                        int rgb = inPixels[ioffset + ix];
                        int pa = (rgb >> 24) & 255;
                        int pr = (rgb >> 16) & 255;
                        int pg = (rgb >> 8) & 255;
                        int pb = rgb & 255;
                        if (premultiply) {
                            float a255 = ((float) pa) * 0.003921569f;
                            pr = (int) (((float) pr) * a255);
                            pg = (int) (((float) pg) * a255);
                            pb = (int) (((float) pb) * a255);
                        }
                        a += ((float) pa) * f;
                        r += ((float) pr) * f;
                        g += ((float) pg) * f;
                        b += ((float) pb) * f;
                    }
                }
                if (!(!unpremultiply || a == 0.0f || a == 255.0f)) {
                    float f = 255.0f / a;
                    r *= f;
                    g *= f;
                    b *= f;
                }
                int ia = alpha ? PixelUtils.clamp((int) (((double) a) + 0.5d)) : 255;
                outPixels[index] = (((ia << 24) | (PixelUtils.clamp((int) (((double) r) + 0.5d)) << 16)) | (PixelUtils.clamp((int) (((double) g) + 0.5d)) << 8)) | PixelUtils.clamp((int) (((double) b) + 0.5d));
                index += height;
            }
        }
    }

    public static Kernel makeKernel(float radius) {
        int r = (int) Math.ceil((double) radius);
        int rows = (r * 2) + 1;
        float[] matrix = new float[rows];
        float sigma = radius / 3.0f;
        float sigma22 = (2.0f * sigma) * sigma;
        float sqrtSigmaPi2 = (float) Math.sqrt((double) (6.2831855f * sigma));
        float radius2 = radius * radius;
        float total = 0.0f;
        int index = 0;
        for (int row = -r; row <= r; row++) {
            float distance = (float) (row * row);
            if (distance > radius2) {
                matrix[index] = 0.0f;
            } else {
                matrix[index] = ((float) Math.exp((double) ((-distance) / sigma22))) / sqrtSigmaPi2;
            }
            total += matrix[index];
            index++;
        }
        for (int i = 0; i < rows; i++) {
            matrix[i] = matrix[i] / total;
        }
        return new Kernel(rows, 1, matrix);
    }

    public String toString() {
        return "Blur/Gaussian Blur...";
    }
}
