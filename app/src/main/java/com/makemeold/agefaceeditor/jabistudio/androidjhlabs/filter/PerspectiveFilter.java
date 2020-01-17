package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.PointF;
import android.graphics.Rect;

public class PerspectiveFilter extends TransformFilter {
    /* renamed from: A */
    private float f1006A;
    /* renamed from: B */
    private float f1007B;
    /* renamed from: C */
    private float f1008C;
    /* renamed from: D */
    private float f1009D;
    /* renamed from: E */
    private float f1010E;
    /* renamed from: F */
    private float f1011F;
    /* renamed from: G */
    private float f1012G;
    /* renamed from: H */
    private float f1013H;
    /* renamed from: I */
    private float f1014I;
    private float a11;
    private float a12;
    private float a13;
    private float a21;
    private float a22;
    private float a23;
    private float a31;
    private float a32;
    private float a33;
    private boolean clip;
    private float dx1;
    private float dx2;
    private float dx3;
    private float dy1;
    private float dy2;
    private float dy3;
    private boolean scaled;
    private float x0;
    private float x1;
    private float x2;
    private float x3;
    private float y0;
    private float y1;
    private float y2;
    private float y3;

    public PerspectiveFilter() {
        this(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    }

    public PerspectiveFilter(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        this.clip = false;
        unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
    }

    public void setClip(boolean clip) {
        this.clip = clip;
    }

    public boolean getClip() {
        return this.clip;
    }

    public void setCorners(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
        this.scaled = true;
    }

    public void unitSquareToQuad(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.dx1 = x1 - x2;
        this.dy1 = y1 - y2;
        this.dx2 = x3 - x2;
        this.dy2 = y3 - y2;
        this.dx3 = ((x0 - x1) + x2) - x3;
        this.dy3 = ((y0 - y1) + y2) - y3;
        if (this.dx3 == 0.0f && this.dy3 == 0.0f) {
            this.a11 = x1 - x0;
            this.a21 = x2 - x1;
            this.a31 = x0;
            this.a12 = y1 - y0;
            this.a22 = y2 - y1;
            this.a32 = y0;
            this.a23 = 0.0f;
            this.a13 = 0.0f;
        } else {
            this.a13 = ((this.dx3 * this.dy2) - (this.dx2 * this.dy3)) / ((this.dx1 * this.dy2) - (this.dy1 * this.dx2));
            this.a23 = ((this.dx1 * this.dy3) - (this.dy1 * this.dx3)) / ((this.dx1 * this.dy2) - (this.dy1 * this.dx2));
            this.a11 = (x1 - x0) + (this.a13 * x1);
            this.a21 = (x3 - x0) + (this.a23 * x3);
            this.a31 = x0;
            this.a12 = (y1 - y0) + (this.a13 * y1);
            this.a22 = (y3 - y0) + (this.a23 * y3);
            this.a32 = y0;
        }
        this.a33 = 1.0f;
        this.scaled = false;
    }

    public void quadToUnitSquare(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
        float ta21 = (this.a32 * this.a13) - (this.a12 * this.a33);
        float ta31 = (this.a12 * this.a23) - (this.a22 * this.a13);
        float ta12 = (this.a31 * this.a23) - (this.a21 * this.a33);
        float ta22 = (this.a11 * this.a33) - (this.a31 * this.a13);
        float ta32 = (this.a21 * this.a13) - (this.a11 * this.a23);
        float ta13 = (this.a21 * this.a32) - (this.a31 * this.a22);
        float ta23 = (this.a31 * this.a12) - (this.a11 * this.a32);
        float f = 1.0f / ((this.a11 * this.a22) - (this.a21 * this.a12));
        this.a11 = ((this.a22 * this.a33) - (this.a32 * this.a23)) * f;
        this.a21 = ta12 * f;
        this.a31 = ta13 * f;
        this.a12 = ta21 * f;
        this.a22 = ta22 * f;
        this.a32 = ta23 * f;
        this.a13 = ta31 * f;
        this.a23 = ta32 * f;
        this.a33 = 1.0f;
    }

    public int[] filter(int[] src, int w, int h) {
        this.f1006A = (this.a22 * this.a33) - (this.a32 * this.a23);
        this.f1007B = (this.a31 * this.a23) - (this.a21 * this.a33);
        this.f1008C = (this.a21 * this.a32) - (this.a31 * this.a22);
        this.f1009D = (this.a32 * this.a13) - (this.a12 * this.a33);
        this.f1010E = (this.a11 * this.a33) - (this.a31 * this.a13);
        this.f1011F = (this.a31 * this.a12) - (this.a11 * this.a32);
        this.f1012G = (this.a12 * this.a23) - (this.a22 * this.a13);
        this.f1013H = (this.a21 * this.a13) - (this.a11 * this.a23);
        this.f1014I = (this.a11 * this.a22) - (this.a21 * this.a12);
        if (!this.scaled) {
            float invWidth = 1.0f / ((float) w);
            float invHeight = 1.0f / ((float) h);
            this.f1006A *= invWidth;
            this.f1009D *= invWidth;
            this.f1012G *= invWidth;
            this.f1007B *= invHeight;
            this.f1010E *= invHeight;
            this.f1013H *= invHeight;
        }
        return super.filter(src, w, h);
    }

    protected void transformSpace(Rect rect) {
        if (this.scaled) {
            rect.left = (int) Math.min(Math.min(this.x0, this.x1), Math.min(this.x2, this.x3));
            rect.top = (int) Math.min(Math.min(this.y0, this.y1), Math.min(this.y2, this.y3));
            rect.right = ((int) Math.max(Math.max(this.x0, this.x1), Math.max(this.x2, this.x3))) - rect.left;
            rect.bottom = ((int) Math.max(Math.max(this.y0, this.y1), Math.max(this.y2, this.y3))) - rect.top;
        } else if (!this.clip) {
            float w = (float) rect.width();
            float h = (float) rect.height();
            Rect r = new Rect();
            PointF p1 = getPoint2D(new PointF(0.0f, 0.0f), null);
            PointF p2 = getPoint2D(new PointF(w, h), null);
            r.left = (int) p1.x;
            r.top = (int) p1.y;
            r.right = (int) p2.x;
            r.bottom = (int) p2.y;
            rect.set(r);
        }
    }

    public PointF getPoint2D(PointF srcPt, PointF dstPt) {
        if (dstPt == null) {
            dstPt = new PointF();
        }
        float x = srcPt.x;
        float y = srcPt.y;
        float f = 1.0f / (((this.a13 * x) + (this.a23 * y)) + this.a33);
        dstPt.set((((this.a11 * x) + (this.a21 * y)) + this.a31) * f, (((this.a12 * x) + (this.a22 * y)) + this.a32) * f);
        return dstPt;
    }

    public float getOriginX() {
        return this.x0 - ((float) ((int) Math.min(Math.min(this.x0, this.x1), Math.min(this.x2, this.x3))));
    }

    public float getOriginY() {
        return this.y0 - ((float) ((int) Math.min(Math.min(this.y0, this.y1), Math.min(this.y2, this.y3))));
    }

    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (((float) this.originalSpace.right) * (((this.f1006A * ((float) x)) + (this.f1007B * ((float) y))) + this.f1008C)) / (((this.f1012G * ((float) x)) + (this.f1013H * ((float) y))) + this.f1014I);
        out[1] = (((float) this.originalSpace.bottom) * (((this.f1009D * ((float) x)) + (this.f1010E * ((float) y))) + this.f1011F)) / (((this.f1012G * ((float) x)) + (this.f1013H * ((float) y))) + this.f1014I);
    }

    public String toString() {
        return "Distort/Perspective...";
    }
}
