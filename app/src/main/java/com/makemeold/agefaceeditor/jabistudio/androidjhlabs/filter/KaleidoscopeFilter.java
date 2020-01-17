package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class KaleidoscopeFilter extends TransformFilter {
    private float angle = 0.0f;
    private float angle2 = 0.0f;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float icentreX;
    private float icentreY;
    private float radius = 0.0f;
    private int sides = 3;

    public KaleidoscopeFilter() {
        setEdgeAction(1);
    }

    public void setSides(int sides) {
        this.sides = sides;
    }

    public int getSides() {
        return this.sides;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle2(float angle2) {
        this.angle2 = angle2;
    }

    public float getAngle2() {
        return this.angle2;
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

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public int[] filter(int[] src, int w, int h) {
        this.icentreX = ((float) w) * this.centreX;
        this.icentreY = ((float) h) * this.centreY;
        return super.filter(src, w, h);
    }

    protected void transformInverse(int x, int y, float[] out) {
        double dx = (double) (((float) x) - this.icentreX);
        double dy = (double) (((float) y) - this.icentreY);
        double r = Math.sqrt((dx * dx) + (dy * dy));
        double theta = (double) ImageMath.triangle((float) (((((Math.atan2(dy, dx) - ((double) this.angle)) - ((double) this.angle2)) / 3.141592653589793d) * ((double) this.sides)) * 0.5d));
        if (this.radius != 0.0f) {
            double radiusc = ((double) this.radius) / Math.cos(theta);
            r = radiusc * ((double) ImageMath.triangle((float) (r / radiusc)));
        }
        theta += (double) this.angle;
        out[0] = (float) (((double) this.icentreX) + (Math.cos(theta) * r));
        out[1] = (float) (((double) this.icentreY) + (Math.sin(theta) * r));
    }

    public String toString() {
        return "Distort/Kaleidoscope...";
    }
}
