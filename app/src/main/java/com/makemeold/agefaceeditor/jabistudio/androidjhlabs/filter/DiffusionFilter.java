package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class DiffusionFilter extends WholeImageFilter {
    private static final int[] diffusionMatrix;
    private boolean colorDither = true;
    private int levels = 6;
    private int[] matrix;
    private boolean serpentine = true;
    private int sum = 16;

    static {
        int[] iArr = new int[9];
        iArr[5] = 7;
        iArr[6] = 3;
        iArr[7] = 5;
        iArr[8] = 1;
        diffusionMatrix = iArr;
    }

    public DiffusionFilter() {
        setMatrix(diffusionMatrix);
    }

    public void setSerpentine(boolean serpentine) {
        this.serpentine = serpentine;
    }

    public boolean getSerpentine() {
        return this.serpentine;
    }

    public void setColorDither(boolean colorDither) {
        this.colorDither = colorDither;
    }

    public boolean getColorDither() {
        return this.colorDither;
    }

    public void setMatrix(int[] matrix) {
        this.matrix = matrix;
        this.sum = 0;
        for (int i : matrix) {
            this.sum += i;
        }
    }

    public int[] getMatrix() {
        return this.matrix;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getLevels() {
        return this.levels;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int i;
        int[] outPixels = new int[(width * height)];
        int[] map = new int[this.levels];
        for (i = 0; i < this.levels; i++) {
            map[i] = (i * 255) / (this.levels - 1);
        }
        int[] div = new int[256];
        for (i = 0; i < 256; i++) {
            div[i] = (this.levels * i) / 256;
        }
        for (int y = 0; y < height; y++) {
            int index;
            int direction;
            boolean reverse = this.serpentine && (y & 1) == 1;
            if (reverse) {
                index = ((y * width) + width) - 1;
                direction = -1;
            } else {
                index = y * width;
                direction = 1;
            }
            for (int x = 0; x < width; x++) {
                int rgb1 = inPixels[index];
                int r1 = (rgb1 >> 16) & 255;
                int g1 = (rgb1 >> 8) & 255;
                int b1 = rgb1 & 255;
                if (!this.colorDither) {
                    b1 = ((r1 + g1) + b1) / 3;
                    g1 = b1;
                    r1 = b1;
                }
                int r2 = map[div[r1]];
                int g2 = map[div[g1]];
                int b2 = map[div[b1]];
                outPixels[index] = (((-16777216 & rgb1) | (r2 << 16)) | (g2 << 8)) | b2;
                int er = r1 - r2;
                int eg = g1 - g2;
                int eb = b1 - b2;
                for (i = -1; i <= 1; i++) {
                    int iy = i + y;
                    if (iy >= 0 && iy < height) {
                        int j = -1;
                        while (j <= 1) {
                            int jx = j + x;
                            if (jx >= 0 && jx < width) {
                                int w;
                                if (reverse) {
                                    w = this.matrix[(((i + 1) * 3) - j) + 1];
                                } else {
                                    w = this.matrix[(((i + 1) * 3) + j) + 1];
                                }
                                if (w != 0) {
                                    int k = reverse ? index - j : index + j;
                                    rgb1 = inPixels[k];
                                    inPixels[k] = (((inPixels[k] & -16777216) | (PixelUtils.clamp(((rgb1 >> 16) & 255) + ((er * w) / this.sum)) << 16)) | (PixelUtils.clamp(((rgb1 >> 8) & 255) + ((eg * w) / this.sum)) << 8)) | PixelUtils.clamp((rgb1 & 255) + ((eb * w) / this.sum));
                                }
                            }
                            j++;
                        }
                    }
                }
                index += direction;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Colors/Diffusion Dither...";
    }
}
