package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class BlockFilter {
    private int blockSize = 2;

    public BlockFilter(int blockSize) {
        this.blockSize = blockSize;
    }

    public BlockFilter() {

    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public int[] filter(int[] src, int W, int H) {
        int width = W;
        int height = H;
        int[] dst = new int[(width * height)];
        int[] pixels = new int[(this.blockSize * this.blockSize)];
        int y = 0;
        while (y < height) {
            int x = 0;
            while (x < width) {
                int by;
                int argb;
                int w = Math.min(this.blockSize, width - x);
                int h = Math.min(this.blockSize, height - y);
                int t = w * h;
                PixelUtils.getRGB(src, x, y, w, h, width, pixels);
                int r = 0;
                int g = 0;
                int b = 0;
                int i = 0;
                for (by = 0; by < h; by++) {
                    int bx;
                    for (bx = 0; bx < w; bx++) {
                        argb = pixels[i];
                        r += (argb >> 16) & 255;
                        g += (argb >> 8) & 255;
                        b += argb & 255;
                        i++;
                    }
                }
                argb = (((r / t) << 16) | ((g / t) << 8)) | (b / t);
                i = 0;
                for (by = 0; by < h; by++) {
                    int bx;
                    for (bx = 0; bx < w; bx++) {
                        pixels[i] = (pixels[i] & -16777216) | argb;
                        i++;
                    }
                }
                PixelUtils.setRGB(dst, x, y, w, h, width, pixels);
                x += this.blockSize;
            }
            y += this.blockSize;
        }
        return dst;
    }

    public String toString() {
        return "Pixellate/Mosaic...";
    }
}
