package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class DisplaceFilter extends TransformFilter {
    private float amount = 1.0f;
    private int dh;
    private int dw;
    private int[] xmap;
    private int[] ymap;

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public int[] filter(int[] src, int w, int h) {
        int i;
        int y;
        this.dw = w;
        this.dh = h;
        int[] dst = new int[(w * h)];
        int[] mapPixels = new int[(this.dw * this.dh)];
        for (i = 0; i < mapPixels.length; i++) {
            mapPixels[i] = src[i];
        }
        this.xmap = new int[(this.dw * this.dh)];
        this.ymap = new int[(this.dw * this.dh)];
        i = 0;
        for (y = 0; y < this.dh; y++) {
            int x;
            for (x = 0; x < this.dw; x++) {
                int rgb = mapPixels[i];
                mapPixels[i] = ((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + (rgb & 255)) / 8;
                i++;
            }
        }
        i = 0;
        for (y = 0; y < this.dh; y++) {
            int j1 = (((this.dh + y) - 1) % this.dh) * this.dw;
            int j2 = y * this.dw;
            int j3 = ((y + 1) % this.dh) * this.dw;
            int x;
            for (x = 0; x < this.dw; x++) {
                int k1 = ((this.dw + x) - 1) % this.dw;
                int k2 = x;
                int k3 = (x + 1) % this.dw;
                this.xmap[i] = ((((mapPixels[k1 + j1] + mapPixels[k1 + j2]) + mapPixels[k1 + j3]) - mapPixels[k3 + j1]) - mapPixels[k3 + j2]) - mapPixels[k3 + j3];
                this.ymap[i] = ((((mapPixels[k1 + j3] + mapPixels[k2 + j3]) + mapPixels[k3 + j3]) - mapPixels[k1 + j1]) - mapPixels[k2 + j1]) - mapPixels[k3 + j1];
                i++;
            }
        }
        dst = super.filter(src, w, h);
        this.ymap = null;
        this.xmap = null;
        return dst;
    }

    protected void transformInverse(int x, int y, float[] out) {
        float nx = (float) x;
        float ny = (float) y;
        int i = ((y % this.dh) * this.dw) + (x % this.dw);
        out[0] = ((float) x) + (this.amount * ((float) this.xmap[i]));
        out[1] = ((float) y) + (this.amount * ((float) this.ymap[i]));
    }

    public String toString() {
        return "Distort/Displace...";
    }
}
