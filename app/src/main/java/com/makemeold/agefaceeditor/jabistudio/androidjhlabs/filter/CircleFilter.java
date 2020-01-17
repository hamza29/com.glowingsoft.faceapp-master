package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class CircleFilter extends TransformFilter {
    private float angle = 0.0f;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float height = 20.0f;
    private float iHeight;
    private float iWidth;
    private float icentreX;
    private float icentreY;
    private float radius = 10.0f;
    private float spreadAngle = 3.1415927f;

    public CircleFilter() {
        setEdgeAction(0);
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return this.height;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setSpreadAngle(float spreadAngle) {
        this.spreadAngle = spreadAngle;
    }

    public float getSpreadAngle() {
        return this.spreadAngle;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
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

    public void setCentre(float x, float y) {
        this.centreX = x;
        this.centreY = y;
    }

    public float[] getCentre() {
        return new float[]{this.centreX, this.centreY};
    }

    public int[] filter(int[] src, int w, int h) {
        this.iWidth = (float) w;
        this.iHeight = (float) h;
        this.icentreX = this.iWidth * this.centreX;
        this.icentreY = this.iHeight * this.centreY;
        this.iWidth -= 1.0f;
        return super.filter(src, w, h);
    }

    protected void transformInverse(int x, int y, float[] out) {
        float dx = ((float) x) - this.icentreX;
        float dy = ((float) y) - this.icentreY;
        float r = (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
        out[0] = (this.iWidth * ImageMath.mod(((float) Math.atan2((double) (-dy), (double) (-dx))) + this.angle, 6.2831855f)) / (this.spreadAngle + 1.0E-5f);
        out[1] = this.iHeight * (1.0f - ((r - this.radius) / (this.height + 1.0E-5f)));
    }

    public String toString() {
        return "Distort/Circle...";
    }
}
