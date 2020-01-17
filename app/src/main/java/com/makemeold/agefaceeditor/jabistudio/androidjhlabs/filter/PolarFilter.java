package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class PolarFilter extends TransformFilter {
    public static final int INVERT_IN_CIRCLE = 2;
    public static final int POLAR_TO_RECT = 1;
    public static final int RECT_TO_POLAR = 0;
    private float centreX;
    private float centreY;
    private float height;
    private float radius;
    private int type;
    private float width;

    public PolarFilter() {
        this(0);
    }

    public PolarFilter(int type) {
        this.type = type;
        setEdgeAction(1);
    }

    public int[] filter(int[] src, int w, int h) {
        this.width = (float) w;
        this.height = (float) h;
        this.centreX = this.width / 2.0f;
        this.centreY = this.height / 2.0f;
        this.radius = Math.max(this.centreY, this.centreX);
        return super.filter(src, w, h);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    private float sqr(float x) {
        return x * x;
    }

    protected void transformInverse(int x, int y, float[] out) {
        float r = 0.0f;
        float theta;
        float m;
        float f;
        float ymax;
        switch (this.type) {
            case 0:
                theta = 0.0f;
                if (((float) x) >= this.centreX) {
                    if (((float) y) > this.centreY) {
                        theta = 3.1415927f - ((float) Math.atan((double) ((((float) x) - this.centreX) / (((float) y) - this.centreY))));
                        r = (float) Math.sqrt((double) (sqr(((float) x) - this.centreX) + sqr(((float) y) - this.centreY)));
                    } else if (((float) y) < this.centreY) {
                        theta = (float) Math.atan((double) ((((float) x) - this.centreX) / (this.centreY - ((float) y))));
                        r = (float) Math.sqrt((double) (sqr(((float) x) - this.centreX) + sqr(this.centreY - ((float) y))));
                    } else {
                        theta = 1.5707964f;
                        r = ((float) x) - this.centreX;
                    }
                } else if (((float) x) < this.centreX) {
                    if (((float) y) < this.centreY) {
                        theta = 6.2831855f - ((float) Math.atan((double) ((this.centreX - ((float) x)) / (this.centreY - ((float) y)))));
                        r = (float) Math.sqrt((double) (sqr(this.centreX - ((float) x)) + sqr(this.centreY - ((float) y))));
                    } else if (((float) y) > this.centreY) {
                        theta = 3.1415927f + ((float) Math.atan((double) ((this.centreX - ((float) x)) / (((float) y) - this.centreY))));
                        r = (float) Math.sqrt((double) (sqr(this.centreX - ((float) x)) + sqr(((float) y) - this.centreY)));
                    } else {
                        theta = 4.712389f;
                        r = this.centreX - ((float) x);
                    }
                }
                if (((float) x) != this.centreX) {
                    m = Math.abs((((float) y) - this.centreY) / (((float) x) - this.centreX));
                } else {
                    m = 0.0f;
                }
                if (m > this.height / this.width) {
                    f = this.centreY / m;
                } else if (((float) x) == this.centreX) {
                    ymax = this.centreY;
                } else {
                    ymax = m * this.centreX;
                }
                out[0] = (this.width - 1.0f) - (((this.width - 1.0f) / 6.2831855f) * theta);
                out[1] = (this.height * r) / this.radius;
                return;
            case 1:
                float theta2;
                theta = (((float) x) / this.width) * 6.2831855f;
                if (theta >= 4.712389f) {
                    theta2 = 6.2831855f - theta;
                } else if (theta >= 3.1415927f) {
                    theta2 = theta - 3.1415927f;
                } else if (theta >= 1.5707964f) {
                    theta2 = 3.1415927f - theta;
                } else {
                    theta2 = theta;
                }
                float t = (float) Math.tan((double) theta2);
                if (t != 0.0f) {
                    m = 1.0f / t;
                } else {
                    m = 0.0f;
                }
                if (m > this.height / this.width) {
                    f = this.centreY / m;
                } else if (theta2 == 0.0f) {
                    ymax = this.centreY;
                } else {
                    ymax = m * this.centreX;
                }
                r = this.radius * (((float) y) / this.height);
                float nx = (-r) * ((float) Math.sin((double) theta2));
                float ny = r * ((float) Math.cos((double) theta2));
                if (theta >= 4.712389f) {
                    out[0] = this.centreX - nx;
                    out[1] = this.centreY - ny;
                    return;
                } else if (((double) theta) >= 3.141592653589793d) {
                    out[0] = this.centreX - nx;
                    out[1] = this.centreY + ny;
                    return;
                } else if (((double) theta) >= 1.5707963267948966d) {
                    out[0] = this.centreX + nx;
                    out[1] = this.centreY + ny;
                    return;
                } else {
                    out[0] = this.centreX + nx;
                    out[1] = this.centreY - ny;
                    return;
                }
            case 2:
                float dx = ((float) x) - this.centreX;
                float dy = ((float) y) - this.centreY;
                float distance2 = (dx * dx) + (dy * dy);
                out[0] = this.centreX + (((this.centreX * this.centreX) * dx) / distance2);
                out[1] = this.centreY + (((this.centreY * this.centreY) * dy) / distance2);
                return;
            default:
                return;
        }
    }

    public String toString() {
        return "Distort/Polar Coordinates...";
    }
}
