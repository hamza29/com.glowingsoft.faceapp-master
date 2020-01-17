package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class ConvolveFilter {
    public static int CLAMP_EDGES = 1;
    public static int WRAP_EDGES = 2;
    public static int ZERO_EDGES = 0;
    protected boolean alpha;
    private int edgeAction;
    protected Kernel kernel;
    protected boolean premultiplyAlpha;

    public ConvolveFilter() {
        this(new float[9]);
    }

    public ConvolveFilter(float[] matrix) {
        this(new Kernel(3, 3, matrix));
    }

    public ConvolveFilter(int rows, int cols, float[] matrix) {
        this(new Kernel(cols, rows, matrix));
    }

    public ConvolveFilter(Kernel kernel) {
        this.kernel = null;
        this.alpha = true;
        this.premultiplyAlpha = true;
        this.edgeAction = CLAMP_EDGES;
        this.kernel = kernel;
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    public Kernel getKernel() {
        return this.kernel;
    }

    public void setEdgeAction(int edgeAction) {
        this.edgeAction = edgeAction;
    }

    public int getEdgeAction() {
        return this.edgeAction;
    }

    public void setUseAlpha(boolean useAlpha) {
        this.alpha = useAlpha;
    }

    public boolean getUseAlpha() {
        return this.alpha;
    }

    public void setPremultiplyAlpha(boolean premultiplyAlpha) {
        this.premultiplyAlpha = premultiplyAlpha;
    }

    public boolean getPremultiplyAlpha() {
        return this.premultiplyAlpha;
    }

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        int[] inPixels = new int[(width * height)];
        int[] outPixels = new int[(width * height)];
        inPixels = src;
        if (this.premultiplyAlpha) {
            ImageMath.premultiply(inPixels, 0, inPixels.length);
        }
        convolve(this.kernel, inPixels, outPixels, width, height, this.alpha, this.edgeAction);
        if (this.premultiplyAlpha) {
            ImageMath.unpremultiply(outPixels, 0, outPixels.length);
        }
        return outPixels;
    }

    public static void convolve(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, int edgeAction) {
        convolve(kernel, inPixels, outPixels, width, height, true, edgeAction);
    }

    public static void convolve(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, int edgeAction) {
        if (kernel.getHeight() == 1) {
            convolveH(kernel, inPixels, outPixels, width, height, alpha, edgeAction);
        } else if (kernel.getWidth() == 1) {
            convolveV(kernel, inPixels, outPixels, width, height, alpha, edgeAction);
        } else {
            convolveHV(kernel, inPixels, outPixels, width, height, alpha, edgeAction);
        }
    }

    public static void convolveHV(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, int edgeAction) {
        int index = 0;
        float[] matrix = kernel.getKernelData(null);
        int rows = kernel.getHeight();
        int cols = kernel.getWidth();
        int rows2 = rows / 2;
        int cols2 = cols / 2;
        int y = 0;
        while (y < height) {
            int x = 0;
            int index2 = index;
            while (x < width) {
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                float a = 0.0f;
                for (int row = -rows2; row <= rows2; row++) {
                    int ioffset = 0;
                    int iy = y + row;
                    if (iy >= 0 && iy < height) {
                        ioffset = iy * width;
                    } else if (edgeAction == CLAMP_EDGES) {
                        ioffset = y * width;
                    } else if (edgeAction == WRAP_EDGES) {
                        ioffset = ((iy + height) % height) * width;
                    } else {
                    }
                    int moffset = ((row + rows2) * cols) + cols2;
                    for (int col = -cols2; col <= cols2; col++) {
                        float f = matrix[moffset + col];
                        if (f != 0.0f) {
                            int ix = x + col;
                            if (ix < 0 || ix >= width) {
                                if (edgeAction == CLAMP_EDGES) {
                                    ix = x;
                                } else if (edgeAction == WRAP_EDGES) {
                                    ix = (x + width) % width;
                                }
                            }
                            int rgb = inPixels[ioffset + ix];
                            a += ((float) ((rgb >> 24) & 255)) * f;
                            r += ((float) ((rgb >> 16) & 255)) * f;
                            g += ((float) ((rgb >> 8) & 255)) * f;
                            b += ((float) (rgb & 255)) * f;
                        }
                    }
                }
                int ia = alpha ? PixelUtils.clamp((int) (((double) a) + 0.5d)) : 255;
                index = index2 + 1;
                outPixels[index2] = (((ia << 24) | (PixelUtils.clamp((int) (((double) r) + 0.5d)) << 16)) | (PixelUtils.clamp((int) (((double) g) + 0.5d)) << 8)) | PixelUtils.clamp((int) (((double) b) + 0.5d));
                x++;
                index2 = index;
            }
            y++;
            index = index2;
        }
    }

    public static void convolveH(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, int edgeAction) {
        int index = 0;
        float[] matrix = kernel.getKernelData(null);
        int cols2 = kernel.getWidth() / 2;
        int y = 0;
        while (y < height) {
            int ioffset = y * width;
            int x = 0;
            int index2 = index;
            while (x < width) {
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
                        a += ((float) ((rgb >> 24) & 255)) * f;
                        r += ((float) ((rgb >> 16) & 255)) * f;
                        g += ((float) ((rgb >> 8) & 255)) * f;
                        b += ((float) (rgb & 255)) * f;
                    }
                }
                int ia = alpha ? PixelUtils.clamp((int) (((double) a) + 0.5d)) : 255;
                index = index2 + 1;
                outPixels[index2] = (((ia << 24) | (PixelUtils.clamp((int) (((double) r) + 0.5d)) << 16)) | (PixelUtils.clamp((int) (((double) g) + 0.5d)) << 8)) | PixelUtils.clamp((int) (((double) b) + 0.5d));
                x++;
                index2 = index;
            }
            y++;
            index = index2;
        }
    }

    public static void convolveV(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, int edgeAction) {
        int index = 0;
        float[] matrix = kernel.getKernelData(null);
        int rows2 = kernel.getHeight() / 2;
        int y = 0;
        while (y < height) {
            int x = 0;
            int index2 = index;
            while (x < width) {
                float r = 0.0f;
                float g = 0.0f;
                float b = 0.0f;
                float a = 0.0f;
                for (int row = -rows2; row <= rows2; row++) {
                    int ioffset;
                    int iy = y + row;
                    if (iy < 0) {
                        if (edgeAction == CLAMP_EDGES) {
                            ioffset = 0;
                        } else if (edgeAction == WRAP_EDGES) {
                            ioffset = ((y + height) % height) * width;
                        } else {
                            ioffset = iy * width;
                        }
                    } else if (iy < height) {
                        ioffset = iy * width;
                    } else if (edgeAction == CLAMP_EDGES) {
                        ioffset = (height - 1) * width;
                    } else if (edgeAction == WRAP_EDGES) {
                        ioffset = ((y + height) % height) * width;
                    } else {
                        ioffset = iy * width;
                    }
                    float f = matrix[row + rows2];
                    if (f != 0.0f) {
                        int rgb = inPixels[ioffset + x];
                        a += ((float) ((rgb >> 24) & 255)) * f;
                        r += ((float) ((rgb >> 16) & 255)) * f;
                        g += ((float) ((rgb >> 8) & 255)) * f;
                        b += ((float) (rgb & 255)) * f;
                    }
                }
                int ia = alpha ? PixelUtils.clamp((int) (((double) a) + 0.5d)) : 255;
                index = index2 + 1;
                outPixels[index2] = (((ia << 24) | (PixelUtils.clamp((int) (((double) r) + 0.5d)) << 16)) | (PixelUtils.clamp((int) (((double) g) + 0.5d)) << 8)) | PixelUtils.clamp((int) (((double) b) + 0.5d));
                x++;
                index2 = index;
            }
            y++;
            index = index2;
        }
    }

    public String toString() {
        return "Blur/Convolve...";
    }
}
