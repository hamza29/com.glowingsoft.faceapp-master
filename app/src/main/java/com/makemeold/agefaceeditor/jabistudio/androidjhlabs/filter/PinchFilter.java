package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.PointF;

public class PinchFilter extends TransformFilter {
    private float amount = 0.5f;
    private float angle = 0.0f;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float height;
    private float icentreX;
    private float icentreY;
    private float radius = 100.0f;
    private float radius2 = 0.0f;
    private float width;

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setCentreX(float centreX) {
        this.centreX = centreX;
    }

    public float getCentreX() {
        return this.centreX;
    }

    public void setCentreY(float centreY) {
        this.centreY = centreY;
    }

    public float getCentreY() {
        return this.centreY;
    }

    public void setCentre(PointF centre) {
        this.centreX = centre.x;
        this.centreY = centre.y;
    }

    public PointF getCentre() {
        return new PointF(this.centreX, this.centreY);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public int[] filter(int[] src, int w, int h) {
        this.width = (float) w;
        this.height = (float) h;
        this.icentreX = this.width * this.centreX;
        this.icentreY = this.height * this.centreY;
        if (this.radius == 0.0f) {
            this.radius = Math.min(this.icentreX, this.icentreY);
        }
        this.radius2 = this.radius * this.radius;
        return super.filter(src, w, h);
    }

    protected void transformInverse(int x, int y, float[] out) {
        float dx = ((float) x) - this.icentreX;
        float dy = ((float) y) - this.icentreY;
        float distance = (dx * dx) + (dy * dy);
        if (distance > this.radius2 || distance == 0.0f) {
            out[0] = (float) x;
            out[1] = (float) y;
            return;
        }
        float d = (float) Math.sqrt((double) (distance / this.radius2));
        float t = (float) Math.pow(Math.sin(1.5707963267948966d * ((double) d)), (double) (-this.amount));
        dx *= t;
        dy *= t;
        float e = 1.0f - d;
        float a = (this.angle * e) * e;
        float s = (float) Math.sin((double) a);
        float c = (float) Math.cos((double) a);
        out[0] = (this.icentreX + (c * dx)) - (s * dy);
        out[1] = (this.icentreY + (s * dx)) + (c * dy);
    }

    public String toString() {
        return "Distort/Pinch...";
    }
}
