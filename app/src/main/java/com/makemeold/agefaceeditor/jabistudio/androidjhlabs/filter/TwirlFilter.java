package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class TwirlFilter extends TransformFilter {
    private float angle = 0.0f;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float icentreX;
    private float icentreY;
    private float radius = 100.0f;
    private float radius2 = 0.0f;

    public TwirlFilter() {
        setEdgeAction(1);
    }

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
        if (distance > this.radius2) {
            out[0] = (float) x;
            out[1] = (float) y;
            return;
        }
        distance = (float) Math.sqrt((double) distance);
        float a = ((float) Math.atan2((double) dy, (double) dx)) + ((this.angle * (this.radius - distance)) / this.radius);
        out[0] = this.icentreX + (((float) Math.cos((double) a)) * distance);
        out[1] = this.icentreY + (((float) Math.sin((double) a)) * distance);
    }

    public String toString() {
        return "Distort/Twirl...";
    }
}
