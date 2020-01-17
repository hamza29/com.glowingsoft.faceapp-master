package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class QuantizeFilter extends WholeImageFilter {
    protected static final int[] matrix;
    private boolean dither;
    private int numColors = 256;
    private boolean serpentine = true;
    private int sum = 16;

    static {
        int[] iArr = new int[9];
        iArr[5] = 7;
        iArr[6] = 3;
        iArr[7] = 5;
        iArr[8] = 1;
        matrix = iArr;
    }

    public void setNumColors(int numColors) {
        this.numColors = Math.min(Math.max(numColors, 8), 256);
    }

    public int getNumColors() {
        return this.numColors;
    }

    public void setDither(boolean dither) {
        this.dither = dither;
    }

    public boolean getDither() {
        return this.dither;
    }

    public void setSerpentine(boolean serpentine) {
        this.serpentine = serpentine;
    }

    public boolean getSerpentine() {
        return this.serpentine;
    }

    public void quantize(int[] inPixels, int[] outPixels, int width, int height, int numColors, boolean dither, boolean serpentine) {
        int count = width * height;
        Quantizer quantizer = new OctTreeQuantizer();
        quantizer.setup(numColors);
        quantizer.addPixels(inPixels, 0, count);
        int[] table = quantizer.buildColorTable();
        int i;
        if (dither) {
            for (int y = 0; y < height; y++) {
                int index;
                int direction;
                boolean reverse = serpentine && (y & 1) == 1;
                if (reverse) {
                    index = ((y * width) + width) - 1;
                    direction = -1;
                } else {
                    index = y * width;
                    direction = 1;
                }
                for (int x = 0; x < width; x++) {
                    int rgb1 = inPixels[index];
                    int rgb2 = table[quantizer.getIndexForColor(rgb1)];
                    outPixels[index] = rgb2;
                    int er = ((rgb1 >> 16) & 255) - ((rgb2 >> 16) & 255);
                    int eg = ((rgb1 >> 8) & 255) - ((rgb2 >> 8) & 255);
                    int eb = (rgb1 & 255) - (rgb2 & 255);
                    for (i = -1; i <= 1; i++) {
                        int iy = i + y;
                        if (iy >= 0 && iy < height) {
                            int j = -1;
                            while (j <= 1) {
                                int jx = j + x;
                                if (jx >= 0 && jx < width) {
                                    int w;
                                    if (reverse) {
                                        w = matrix[(((i + 1) * 3) - j) + 1];
                                    } else {
                                        w = matrix[(((i + 1) * 3) + j) + 1];
                                    }
                                    if (w != 0) {
                                        int k = reverse ? index - j : index + j;
                                        rgb1 = inPixels[k];
                                        inPixels[k] = ((PixelUtils.clamp(((rgb1 >> 16) & 255) + ((er * w) / this.sum)) << 16) | (PixelUtils.clamp(((rgb1 >> 8) & 255) + ((eg * w) / this.sum)) << 8)) | PixelUtils.clamp((rgb1 & 255) + ((eb * w) / this.sum));
                                    }
                                }
                                j++;
                            }
                        }
                    }
                    index += direction;
                }
            }
            return;
        }
        for (i = 0; i < count; i++) {
            outPixels[i] = table[quantizer.getIndexForColor(inPixels[i])];
        }
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int[] outPixels = new int[(width * height)];
        quantize(inPixels, outPixels, width, height, this.numColors, this.dither, this.serpentine);
        return outPixels;
    }

    public String toString() {
        return "Colors/Quantize...";
    }
}
