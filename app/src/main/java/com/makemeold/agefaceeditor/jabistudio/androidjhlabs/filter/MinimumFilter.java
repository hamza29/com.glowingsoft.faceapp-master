package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class MinimumFilter extends WholeImageFilter {
    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int index = 0;
        int[] outPixels = new int[(width * height)];
        int y = 0;
        while (y < height) {
            int x = 0;
            int index2 = index;
            while (x < width) {
                int pixel = -1;
                for (int dy = -1; dy <= 1; dy++) {
                    int iy = y + dy;
                    if (iy >= 0 && iy < height) {
                        int ioffset = iy * width;
                        for (int dx = -1; dx <= 1; dx++) {
                            int ix = x + dx;
                            if (ix >= 0 && ix < width) {
                                pixel = PixelUtils.combinePixels(pixel, inPixels[ioffset + ix], 2);
                            }
                        }
                    }
                }
                index = index2 + 1;
                outPixels[index2] = pixel;
                x++;
                index2 = index;
            }
            y++;
            index = index2;
        }
        return outPixels;
    }

    public String toString() {
        return "Blur/Minimum";
    }
}
