package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class OffsetFilter extends TransformFilter {
    private int height;
    private int width;
    private boolean wrap;
    private int xOffset;
    private int yOffset;

    public OffsetFilter() {
        this(0, 0, true);
    }

    public OffsetFilter(int xOffset, int yOffset, boolean wrap) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.wrap = wrap;
        setEdgeAction(0);
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getXOffset() {
        return this.xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getYOffset() {
        return this.yOffset;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    public boolean getWrap() {
        return this.wrap;
    }

    protected void transformInverse(int x, int y, float[] out) {
        if (this.wrap) {
            out[0] = (float) (((this.width + x) - this.xOffset) % this.width);
            out[1] = (float) (((this.height + y) - this.yOffset) % this.height);
            return;
        }
        out[0] = (float) (x - this.xOffset);
        out[1] = (float) (y - this.yOffset);
    }

    public int[] filter(int[] src, int w, int h) {
        this.width = w;
        this.height = h;
        if (this.wrap) {
            while (this.xOffset < 0) {
                this.xOffset += this.width;
            }
            while (this.yOffset < 0) {
                this.yOffset += this.height;
            }
            this.xOffset %= this.width;
            this.yOffset %= this.height;
        }
        return super.filter(src, w, h);
    }

    public String toString() {
        return "Distort/Offset...";
    }
}
