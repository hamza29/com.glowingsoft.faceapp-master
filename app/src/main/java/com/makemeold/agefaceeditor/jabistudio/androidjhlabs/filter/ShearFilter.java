package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
//import net.daum.adam.publisher.impl.p003d.C0849c;

public class ShearFilter extends TransformFilter {
    private boolean resize = true;
    private float shx = 0.0f;
    private float shy = 0.0f;
    private float xangle = 0.0f;
    private float xoffset = 0.0f;
    private float yangle = 0.0f;
    private float yoffset = 0.0f;

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isResize() {
        return this.resize;
    }

    public void setXAngle(float xangle) {
        this.xangle = xangle;
        initialize();
    }

    public float getXAngle() {
        return this.xangle;
    }

    public void setYAngle(float yangle) {
        this.yangle = yangle;
        initialize();
    }

    public float getYAngle() {
        return this.yangle;
    }

    private void initialize() {
        this.shx = (float) Math.sin((double) this.xangle);
        this.shy = (float) Math.sin((double) this.yangle);
    }

    protected void transformSpace(Rect r) {
        float tangent = (float) Math.tan((double) this.xangle);
        this.xoffset = ((float) (-r.bottom)) * tangent;
        /*if (((double) tangent) < C0849c.f717r) {
            tangent = -tangent;
        }*/
        r.right = (int) (((((float) r.bottom) * tangent) + ((float) r.right)) + 0.999999f);
        tangent = (float) Math.tan((double) this.yangle);
        this.yoffset = ((float) (-r.right)) * tangent;
        /*if (((double) tangent) < C0849c.f717r) {
            tangent = -tangent;
        }*/
        r.bottom = (int) (((((float) r.right) * tangent) + ((float) r.bottom)) + 0.999999f);
    }

    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (((float) x) + this.xoffset) + (((float) y) * this.shx);
        out[1] = (((float) y) + this.yoffset) + (((float) x) * this.shy);
    }

    public String toString() {
        return "Distort/Shear...";
    }
}
