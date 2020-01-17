package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Point;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class FieldWarpFilter extends TransformFilter {
    private float amount = 1.0f;
    private Line[] inLines;
    private Line[] intermediateLines;
    private Line[] outLines;
    private float power = 1.0f;
    private float strength = 2.0f;

    public static class Line {
        public int dx;
        public int dy;
        public float length;
        public float lengthSquared;
        public int x1;
        public int x2;
        public int y1;
        public int y2;

        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public void setup() {
            this.dx = this.x2 - this.x1;
            this.dy = this.y2 - this.y1;
            this.lengthSquared = (float) ((this.dx * this.dx) + (this.dy * this.dy));
            this.length = (float) Math.sqrt((double) this.lengthSquared);
        }
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getPower() {
        return this.power;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public float getStrength() {
        return this.strength;
    }

    public void setInLines(Line[] inLines) {
        this.inLines = inLines;
    }

    public Line[] getInLines() {
        return this.inLines;
    }

    public void setOutLines(Line[] outLines) {
        this.outLines = outLines;
    }

    public Line[] getOutLines() {
        return this.outLines;
    }

    protected void transform(int x, int y, Point out) {
    }

    protected void transformInverse(int x, int y, float[] out) {
        float b = (1.5f * this.strength) + 0.5f;
        float p = this.power;
        float totalWeight = 0.0f;
        float sumX = 0.0f;
        float sumY = 0.0f;
        for (int line = 0; line < this.inLines.length; line++) {
            float distance;
            Line l1 = this.inLines[line];
            Line l = this.intermediateLines[line];
            float dx = (float) (x - l.x1);
            float dy = (float) (y - l.y1);
            float fraction = ((((float) l.dx) * dx) + (((float) l.dy) * dy)) / l.lengthSquared;
            float fdist = ((((float) l.dx) * dy) - (((float) l.dy) * dx)) / l.length;
            if (fraction <= 0.0f) {
                distance = (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
            } else if (fraction >= 1.0f) {
                dx = (float) (x - l.x2);
                dy = (float) (y - l.y2);
                distance = (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
            } else if (fdist >= 0.0f) {
                distance = fdist;
            } else {
                distance = -fdist;
            }
            float weight = (float) Math.pow(Math.pow((double) l.length, (double) p) / ((double) (0.001f + distance)), (double) b);
            sumX += (((((float) l1.x1) + (((float) l1.dx) * fraction)) - ((((float) l1.dy) * fdist) / l1.length)) - ((float) x)) * weight;
            sumY += (((((float) l1.y1) + (((float) l1.dy) * fraction)) + ((((float) l1.dx) * fdist) / l1.length)) - ((float) y)) * weight;
            totalWeight += weight;
        }
        out[0] = (((float) x) + (sumX / totalWeight)) + 0.5f;
        out[1] = (((float) y) + (sumY / totalWeight)) + 0.5f;
    }

    public int[] filter(int[] src, int w, int h) {
        if (this.inLines == null || this.outLines == null) {
            return src;
        }
        this.intermediateLines = new Line[this.inLines.length];
        for (int line = 0; line < this.inLines.length; line++) {
            Line[] lineArr = this.intermediateLines;
            Line l = new Line(ImageMath.lerp(this.amount, this.inLines[line].x1, this.outLines[line].x1), ImageMath.lerp(this.amount, this.inLines[line].y1, this.outLines[line].y1), ImageMath.lerp(this.amount, this.inLines[line].x2, this.outLines[line].x2), ImageMath.lerp(this.amount, this.inLines[line].y2, this.outLines[line].y2));
            lineArr[line] = l;
            l.setup();
            this.inLines[line].setup();
        }
        int[] dst = super.filter(src, w, h);
        this.intermediateLines = null;
        return dst;
    }

    public String toString() {
        return "Distort/Field Warp...";
    }
}
