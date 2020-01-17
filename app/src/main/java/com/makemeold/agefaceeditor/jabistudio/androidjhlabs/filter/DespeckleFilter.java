package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import java.lang.reflect.Array;

public class DespeckleFilter extends WholeImageFilter {
    private short pepperAndSalt(short c, short v1, short v2) {
        if (c < v1) {
            c = (short) (c + 1);
        }
        if (c < v2) {
            c = (short) (c + 1);
        }
        if (c > v1) {
            c = (short) (c - 1);
        }
        if (c > v2) {
            return (short) (c - 1);
        }
        return c;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int x;
        int index = 0;
        short[][] r = (short[][]) Array.newInstance(Short.TYPE, new int[]{3, width});
        short[][] g = (short[][]) Array.newInstance(Short.TYPE, new int[]{3, width});
        short[][] b = (short[][]) Array.newInstance(Short.TYPE, new int[]{3, width});
        int[] outPixels = new int[(width * height)];
        for (x = 0; x < width; x++) {
            int rgb = inPixels[x];
            r[1][x] = (short) ((rgb >> 16) & 255);
            g[1][x] = (short) ((rgb >> 8) & 255);
            b[1][x] = (short) (rgb & 255);
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
                    int rgb = inPixels[nextRowIndex2];
                    r[2][x] = (short) ((rgb >> 16) & 255);
                    g[2][x] = (short) ((rgb >> 8) & 255);
                    b[2][x] = (short) (rgb & 255);
                    x++;
                    nextRowIndex2 = nextRowIndex;
                }
                nextRowIndex = nextRowIndex2;
            }
            x = 0;
            while (x < width) {
                boolean xIn = x > 0 && x < width - 1;
                short or = r[1][x];
                short og = g[1][x];
                short ob = b[1][x];
                int w = x - 1;
                int e = x + 1;
                if (yIn) {
                    or = pepperAndSalt(or, r[0][x], r[2][x]);
                    og = pepperAndSalt(og, g[0][x], g[2][x]);
                    ob = pepperAndSalt(ob, b[0][x], b[2][x]);
                }
                if (xIn) {
                    or = pepperAndSalt(or, r[1][w], r[1][e]);
                    og = pepperAndSalt(og, g[1][w], g[1][e]);
                    ob = pepperAndSalt(ob, b[1][w], b[1][e]);
                }
                if (yIn && xIn) {
                    or = pepperAndSalt(or, r[0][w], r[2][e]);
                    og = pepperAndSalt(og, g[0][w], g[2][e]);
                    ob = pepperAndSalt(ob, b[0][w], b[2][e]);
                    or = pepperAndSalt(or, r[2][w], r[0][e]);
                    og = pepperAndSalt(og, g[2][w], g[0][e]);
                    ob = pepperAndSalt(ob, b[2][w], b[0][e]);
                }
                outPixels[index] = (((inPixels[index] & -16777216) | (or << 16)) | (og << 8)) | ob;
                index++;
                x++;
            }
            short[] t = r[0];
            r[0] = r[1];
            r[1] = r[2];
            r[2] = t;
            t = g[0];
            g[0] = g[1];
            g[1] = g[2];
            g[2] = t;
            t = b[0];
            b[0] = b[1];
            b[1] = b[2];
            b[2] = t;
            y++;
        }
        return outPixels;
    }

    public String toString() {
        return "Blur/Despeckle...";
    }
}
