package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class LaplaceFilter {
    private void brightness(int[] row) {
        for (int i = 0; i < row.length; i++) {
            int rgb = row[i];
            row[i] = ((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + (rgb & 255)) / 3;
        }
    }

    public int[] filter(int[] src, int w, int h) {
        int y;
        int r;
        int width = w;
        int height = h;
        int[] dst = new int[(w * h)];
        int[] row3 = null;
        int[] pixels = new int[width];
        int[] row1 = getRGB(src, 0, width);
        int[] row2 = getRGB(src, 0, width);
        brightness(row1);
        brightness(row2);
        for (y = 0; y < height; y++) {
            int x;
            if (y < height - 1) {
                row3 = getRGB(src, y + 1, width);
                brightness(row3);
            }
            pixels[width - 1] = -16777216;
            pixels[0] = -16777216;
            for (x = 1; x < width - 1; x++) {
                int l1 = row2[x - 1];
                int l2 = row1[x];
                int l3 = row3[x];
                int l4 = row2[x + 1];
                int l = row2[x];
                int gradient = (int) (0.5f * ((float) Math.max(Math.max(Math.max(l1, l2), Math.max(l3, l4)) - l, l - Math.min(Math.min(l1, l2), Math.min(l3, l4)))));
                if ((((((((row1[x - 1] + row1[x]) + row1[x + 1]) + row2[x - 1]) - (row2[x] * 8)) + row2[x + 1]) + row3[x - 1]) + row3[x]) + row3[x + 1] > 0) {
                    r = gradient;
                } else {
                    r = gradient + 128;
                }
                pixels[x] = r;
            }
            setRGB(dst, y, width, pixels);
            int[] t = row1;
            row1 = row2;
            row2 = row3;
            row3 = t;
        }
        row1 = getRGB(dst, 0, width);
        row2 = getRGB(dst, 0, width);
        for (y = 0; y < height; y++) {
            if (y < height - 1) {
                row3 = getRGB(dst, y + 1, width);
            }
            pixels[width - 1] = -16777216;
            pixels[0] = -16777216;
            int x = 1;
            while (x < width - 1) {
                r = row2[x];
                if (r > 128 || (row1[x - 1] <= 128 && row1[x] <= 128 && row1[x + 1] <= 128 && row2[x - 1] <= 128 && row2[x + 1] <= 128 && row3[x - 1] <= 128 && row3[x] <= 128 && row3[x + 1] <= 128)) {
                    r = 0;
                } else if (r >= 128) {
                    r -= 128;
                }
                pixels[x] = ((-16777216 | (r << 16)) | (r << 8)) | r;
                x++;
            }
            setRGB(dst, y, width, pixels);
            int[] t = row1;
            row1 = row2;
            row2 = row3;
            row3 = t;
        }
        return dst;
    }

    private int[] getRGB(int[] src, int y, int width) {
        int[] ret = new int[width];
        int index = 0;
        for (int i = y * width; i < (y * width) + width; i++) {
            ret[index] = src[i];
            index++;
        }
        return ret;
    }

    private void setRGB(int[] dst, int y, int width, int[] src) {
        int index = 0;
        for (int i = y * width; i < (y * width) + width; i++) {
            dst[i] = src[index];
            index++;
        }
    }

    public String toString() {
        return "Edges/Laplace...";
    }
}
