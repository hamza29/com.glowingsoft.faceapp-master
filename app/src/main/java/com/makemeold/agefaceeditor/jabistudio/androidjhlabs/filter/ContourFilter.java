package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;
import java.lang.reflect.Array;

public class ContourFilter extends WholeImageFilter {
    private int contourColor = -16777216;
    private float levels = 5.0f;
    private float offset = 0.0f;
    private float scale = 1.0f;

    public void setLevels(float levels) {
        this.levels = levels;
    }

    public float getLevels() {
        return this.levels;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public float getOffset() {
        return this.offset;
    }

    public void setContourColor(int contourColor) {
        this.contourColor = contourColor;
    }

    public int getContourColor() {
        return this.contourColor;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int x;
        int index = 0;
        short[][] r = (short[][]) Array.newInstance(Short.TYPE, new int[]{3, width});
        int[] outPixels = new int[(width * height)];
        short[] table = new short[256];
        int offsetl = (int) ((this.offset * 256.0f) / this.levels);
        for (int i = 0; i < 256; i++) {
            table[i] = (short) PixelUtils.clamp((int) (((255.0d * Math.floor((double) ((this.levels * ((float) (i + offsetl))) / 256.0f))) / ((double) (this.levels - 1.0f))) - ((double) offsetl)));
        }
        for (x = 0; x < width; x++) {
            r[1][x] = (short) PixelUtils.brightness(inPixels[x]);
        }
        int y = 0;
        while (y < height) {
            boolean yIn = y > 0 && y < height - 1;
            int nextRowIndex = index + width;
            if (y < height - 1) {
                x = 0;
                int nextRowIndex2 = nextRowIndex;
                while (x < width) {
                    nextRowIndex = nextRowIndex2 + 1;
                    r[2][x] = (short) PixelUtils.brightness(inPixels[nextRowIndex2]);
                    x++;
                    nextRowIndex2 = nextRowIndex;
                }
                nextRowIndex = nextRowIndex2;
            }
            x = 0;
            while (x < width) {
                boolean xIn = x > 0 && x < width - 1;
                int w = x - 1;
                int e = x + 1;
                int v = 0;
                if (yIn && xIn) {
                    short nwb = r[0][w];
                    short neb = r[0][x];
                    short swb = r[1][w];
                    short seb = r[1][x];
                    short nw = table[nwb];
                    short ne = table[neb];
                    short sw = table[swb];
                    short se = table[seb];
                    if (!(nw == ne && nw == sw && ne == se && sw == se)) {
                        v = (int) (this.scale * ((float) (((Math.abs(nwb - neb) + Math.abs(nwb - swb)) + Math.abs(neb - seb)) + Math.abs(swb - seb))));
                        if (v > 255) {
                            v = 255;
                        }
                    }
                }
                if (v != 0) {
                    outPixels[index] = PixelUtils.combinePixels(inPixels[index], this.contourColor, 1, v);
                } else {
                    outPixels[index] = inPixels[index];
                }
                index++;
                x++;
            }
            short[] t = r[0];
            r[0] = r[1];
            r[1] = r[2];
            r[2] = t;
            y++;
        }
        return outPixels;
    }
}
