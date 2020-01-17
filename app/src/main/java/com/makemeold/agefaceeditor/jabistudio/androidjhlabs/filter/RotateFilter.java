package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Point;
import android.graphics.Rect;

public class RotateFilter extends TransformFilter {
    private float angle;
    private float cos;
    private boolean resize;
    private float sin;

    public RotateFilter() {
        this(3.1415927f);
    }

    public RotateFilter(float angle) {
        this(angle, true);
    }

    public RotateFilter(float angle, boolean resize) {
        this.resize = true;
        setAngle(angle);
        this.resize = resize;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        this.cos = (float) Math.cos((double) this.angle);
        this.sin = (float) Math.sin((double) this.angle);
    }

    public float getAngle() {
        return this.angle;
    }

    protected void transformSpace(Rect rect) {
        if (this.resize) {
            Point out = new Point(0, 0);
            int minx = Integer.MAX_VALUE;
            int miny = Integer.MAX_VALUE;
            int maxx = Integer.MIN_VALUE;
            int maxy = Integer.MIN_VALUE;
            int w = rect.right;
            int h = rect.bottom;
            int x = rect.left;
            int y = rect.top;
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case 0:
                        transform(x, y, out);
                        break;
                    case 1:
                        transform(x + w, y, out);
                        break;
                    case 2:
                        transform(x, y + h, out);
                        break;
                    case 3:
                        transform(x + w, y + h, out);
                        break;
                    default:
                        break;
                }
                minx = Math.min(minx, out.x);
                miny = Math.min(miny, out.y);
                maxx = Math.max(maxx, out.x);
                maxy = Math.max(maxy, out.y);
            }
            rect.left = minx;
            rect.top = miny;
            rect.right = maxx - rect.left;
            rect.bottom = maxy - rect.top;
        }
    }

    private void transform(int x, int y, Point out) {
        out.x = (int) ((((float) x) * this.cos) + (((float) y) * this.sin));
        out.y = (int) ((((float) y) * this.cos) - (((float) x) * this.sin));
    }

    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (((float) x) * this.cos) - (((float) y) * this.sin);
        out[1] = (((float) y) * this.cos) + (((float) x) * this.sin);
    }

    public String toString() {
        return "Rotate " + ((int) (((double) (this.angle * 180.0f)) / 3.141592653589793d));
    }
}
