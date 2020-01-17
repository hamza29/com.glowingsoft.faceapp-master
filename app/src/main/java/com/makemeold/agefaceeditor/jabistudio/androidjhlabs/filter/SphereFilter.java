package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class SphereFilter extends TransformFilter {
    /* renamed from: a */
    private float f1015a = 0.0f;
    private float a2 = 0.0f;
    /* renamed from: b */
    private float f1016b = 0.0f;
    private float b2 = 0.0f;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float icentreX;
    private float icentreY;
    private float refractionIndex = 1.5f;

    public SphereFilter() {
        setEdgeAction(1);
        setRadius(100.0f);
    }

    public void setRefractionIndex(float refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    public float getRefractionIndex() {
        return this.refractionIndex;
    }

    public void setRadius(float r) {
        this.f1015a = r;
        this.f1016b = r;
    }

    public float getRadius() {
        return this.f1015a;
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
        int width = w;
        int height = h;
        this.icentreX = ((float) width) * this.centreX;
        this.icentreY = ((float) height) * this.centreY;
        if (this.f1015a == 0.0f) {
            this.f1015a = (float) (width / 2);
        }
        if (this.f1016b == 0.0f) {
            this.f1016b = (float) (height / 2);
        }
        this.a2 = this.f1015a * this.f1015a;
        this.b2 = this.f1016b * this.f1016b;
        return super.filter(src, w, h);
    }

    protected void transformInverse(int x, int y, float[] out) {
        float dx = ((float) x) - this.icentreX;
        float dy = ((float) y) - this.icentreY;
        float x2 = dx * dx;
        float y2 = dy * dy;
        if (y2 >= this.b2 - ((this.b2 * x2) / this.a2)) {
            out[0] = (float) x;
            out[1] = (float) y;
            return;
        }
        float rRefraction = 1.0f / this.refractionIndex;
        float z = (float) Math.sqrt((double) (((1.0f - (x2 / this.a2)) - (y2 / this.b2)) * (this.f1015a * this.f1016b)));
        float z2 = z * z;
        float xAngle = (float) Math.acos(((double) dx) / Math.sqrt((double) (x2 + z2)));
        out[0] = ((float) x) - (((float) Math.tan((double) ((1.5707964f - xAngle) - ((float) Math.asin(Math.sin((double) (1.5707964f - xAngle)) * ((double) rRefraction)))))) * z);
        float yAngle = (float) Math.acos(((double) dy) / Math.sqrt((double) (y2 + z2)));
        out[1] = ((float) y) - (((float) Math.tan((double) ((1.5707964f - yAngle) - ((float) Math.asin(Math.sin((double) (1.5707964f - yAngle)) * ((double) rRefraction)))))) * z);
    }

    public String toString() {
        return "Distort/Sphere...";
    }
}
