package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.Function2D;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.Noise;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;
import java.util.Random;

public class CellularFilter extends WholeImageFilter implements Function2D, Cloneable {
    public static final int HEXAGONAL = 2;
    public static final int OCTAGONAL = 3;
    public static final int RANDOM = 0;
    public static final int SQUARE = 1;
    public static final int TRIANGULAR = 4;
    private static byte[] probabilities;
    public float amount;
    protected float angle;
    protected float angleCoefficient;
    public float bias;
    protected float[] coefficients;
    protected Colormap colormap;
    public float distancePower;
    public float gain;
    private float gradientCoefficient;
    protected int gridType;
    protected float m00;
    protected float m01;
    protected float m10;
    protected float m11;
    private float max;
    private float min;
    protected Random random;
    protected float randomness;
    protected Point[] results;
    protected float scale;
    protected float stretch;
    public float turbulence;
    public boolean useColor;

    public class Point {
        public float cubeX;
        public float cubeY;
        public float distance;
        public float dx;
        public float dy;
        public int index;
        /* renamed from: x */
        public float f446x;
        /* renamed from: y */
        public float f447y;
    }

    public CellularFilter() {
        int j;
        this.scale = 32.0f;
        this.stretch = 1.0f;
        this.angle = 0.0f;
        this.amount = 1.0f;
        this.turbulence = 1.0f;
        this.gain = 0.5f;
        this.bias = 0.5f;
        this.distancePower = 2.0f;
        this.useColor = false;
        this.colormap = new Gradient();
        this.coefficients = new float[]{1.0f, 0.0f, 0.0f, 0.0f};
        this.random = new Random();
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.results = null;
        this.randomness = 0.0f;
        this.gridType = 2;
        this.results = new Point[3];
        for (j = 0; j < this.results.length; j++) {
            this.results[j] = new Point();
        }
        if (probabilities == null) {
            probabilities = new byte[8192];
            float factorial = 1.0f;
            float total = 0.0f;
            for (int i = 0; i < 10; i++) {
                if (i > 1) {
                    factorial *= (float) i;
                }
                int start = (int) (total * 8192.0f);
                total += (((float) Math.pow((double) 1075838976, (double) i)) * ((float) Math.exp((double) (-1075838976)))) / factorial;
                int end = (int) (total * 8192.0f);
                for (j = start; j < end; j++) {
                    probabilities[j] = (byte) i;
                }
            }
        }
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    public void setStretch(float stretch) {
        this.stretch = stretch;
    }

    public float getStretch() {
        return this.stretch;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        float cos = (float) Math.cos((double) angle);
        float sin = (float) Math.sin((double) angle);
        this.m00 = cos;
        this.m01 = sin;
        this.m10 = -sin;
        this.m11 = cos;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setCoefficient(int i, float v) {
        this.coefficients[i] = v;
    }

    public float getCoefficient(int i) {
        return this.coefficients[i];
    }

    public void setAngleCoefficient(float angleCoefficient) {
        this.angleCoefficient = angleCoefficient;
    }

    public float getAngleCoefficient() {
        return this.angleCoefficient;
    }

    public void setGradientCoefficient(float gradientCoefficient) {
        this.gradientCoefficient = gradientCoefficient;
    }

    public float getGradientCoefficient() {
        return this.gradientCoefficient;
    }

    public void setF1(float v) {
        this.coefficients[0] = v;
    }

    public float getF1() {
        return this.coefficients[0];
    }

    public void setF2(float v) {
        this.coefficients[1] = v;
    }

    public float getF2() {
        return this.coefficients[1];
    }

    public void setF3(float v) {
        this.coefficients[2] = v;
    }

    public float getF3() {
        return this.coefficients[2];
    }

    public void setF4(float v) {
        this.coefficients[3] = v;
    }

    public float getF4() {
        return this.coefficients[3];
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    public void setRandomness(float randomness) {
        this.randomness = randomness;
    }

    public float getRandomness() {
        return this.randomness;
    }

    public void setGridType(int gridType) {
        this.gridType = gridType;
    }

    public int getGridType() {
        return this.gridType;
    }

    public void setDistancePower(float distancePower) {
        this.distancePower = distancePower;
    }

    public float getDistancePower() {
        return this.distancePower;
    }

    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    public float getTurbulence() {
        return this.turbulence;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    private float checkCube(float x, float y, int cubeX, int cubeY, Point[] results) {
        int numPoints;
        this.random.setSeed((long) ((cubeX * 571) + (cubeY * 23)));
        switch (this.gridType) {
            case 1:
                numPoints = 1;
                break;
            case 2:
                numPoints = 1;
                break;
            case 3:
                numPoints = 2;
                break;
            case 4:
                numPoints = 2;
                break;
            default:
                numPoints = probabilities[this.random.nextInt() & 8191];
                break;
        }
        for (int i = 0; i < numPoints; i++) {
            float d;
            float px = 0.0f;
            float py = 0.0f;
            float weight = 1.0f;
            switch (this.gridType) {
                case 0:
                    px = this.random.nextFloat();
                    py = this.random.nextFloat();
                    break;
                case 1:
                    py = 0.5f;
                    px = 0.5f;
                    if (this.randomness != 0.0f) {
                        px = (float) (((double) px) + (((double) this.randomness) * (((double) this.random.nextFloat()) - 0.5d)));
                        py = (float) (((double) 1056964608) + (((double) this.randomness) * (((double) this.random.nextFloat()) - 0.5d)));
                        break;
                    }
                    break;
                case 2:
                    if ((cubeX & 1) == 0) {
                        px = 0.75f;
                        py = 0.0f;
                    } else {
                        px = 0.75f;
                        py = 0.5f;
                    }
                    if (this.randomness != 0.0f) {
                        px += this.randomness * Noise.noise2(271.0f * (((float) cubeX) + px), 271.0f * (((float) cubeY) + py));
                        py += this.randomness * Noise.noise2((271.0f * (((float) cubeX) + px)) + 89.0f, (271.0f * (((float) cubeY) + py)) + 137.0f);
                        break;
                    }
                    break;
                case 3:
                    switch (i) {
                        case 0:
                            px = 0.207f;
                            py = 0.207f;
                            break;
                        case 1:
                            px = 0.707f;
                            py = 0.707f;
                            weight = 1.6f;
                            break;
                    }
                    if (this.randomness != 0.0f) {
                        px += this.randomness * Noise.noise2(271.0f * (((float) cubeX) + px), 271.0f * (((float) cubeY) + py));
                        py += this.randomness * Noise.noise2((271.0f * (((float) cubeX) + px)) + 89.0f, (271.0f * (((float) cubeY) + py)) + 137.0f);
                        break;
                    }
                    break;
                case 4:
                    if ((cubeY & 1) == 0) {
                        if (i == 0) {
                            px = 0.25f;
                            py = 0.35f;
                        } else {
                            px = 0.75f;
                            py = 0.65f;
                        }
                    } else if (i == 0) {
                        px = 0.75f;
                        py = 0.35f;
                    } else {
                        px = 0.25f;
                        py = 0.65f;
                    }
                    if (this.randomness != 0.0f) {
                        px += this.randomness * Noise.noise2(271.0f * (((float) cubeX) + px), 271.0f * (((float) cubeY) + py));
                        py += this.randomness * Noise.noise2((271.0f * (((float) cubeX) + px)) + 89.0f, (271.0f * (((float) cubeY) + py)) + 137.0f);
                        break;
                    }
                    break;
            }
            float dx = Math.abs(x - px) * weight;
            float dy = Math.abs(y - py) * weight;
            if (this.distancePower == 1.0f) {
                d = dx + dy;
            } else if (this.distancePower == 2.0f) {
                d = (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
            } else {
                d = (float) Math.pow((double) (((float) Math.pow((double) dx, (double) this.distancePower)) + ((float) Math.pow((double) dy, (double) this.distancePower))), (double) (1.0f / this.distancePower));
            }
            Point p;
            if (d < results[0].distance) {
                p = results[2];
                results[2] = results[1];
                results[1] = results[0];
                results[0] = p;
                p.distance = d;
                p.dx = dx;
                p.dy = dy;
                p.f446x = ((float) cubeX) + px;
                p.f447y = ((float) cubeY) + py;
            } else if (d < results[1].distance) {
                p = results[2];
                results[2] = results[1];
                results[1] = p;
                p.distance = d;
                p.dx = dx;
                p.dy = dy;
                p.f446x = ((float) cubeX) + px;
                p.f447y = ((float) cubeY) + py;
            } else if (d < results[2].distance) {
                p = results[2];
                p.distance = d;
                p.dx = dx;
                p.dy = dy;
                p.f446x = ((float) cubeX) + px;
                p.f447y = ((float) cubeY) + py;
            }
        }
        return results[2].distance;
    }

    public float evaluate(float x, float y) {
        for (Point point : this.results) {
            point.distance = Float.POSITIVE_INFINITY;
        }
        int ix = (int) x;
        int iy = (int) y;
        float fx = x - ((float) ix);
        float fy = y - ((float) iy);
        float d = checkCube(fx, fy, ix, iy, this.results);
        if (d > fy) {
            d = checkCube(fx, fy + 1.0f, ix, iy - 1, this.results);
        }
        if (d > 1.0f - fy) {
            d = checkCube(fx, fy - 1.0f, ix, iy + 1, this.results);
        }
        if (d > fx) {
            checkCube(fx + 1.0f, fy, ix - 1, iy, this.results);
            if (d > fy) {
                d = checkCube(fx + 1.0f, fy + 1.0f, ix - 1, iy - 1, this.results);
            }
            if (d > 1.0f - fy) {
                d = checkCube(fx + 1.0f, fy - 1.0f, ix - 1, iy + 1, this.results);
            }
        }
        if (d > 1.0f - fx) {
            d = checkCube(fx - 1.0f, fy, ix + 1, iy, this.results);
            if (d > fy) {
                d = checkCube(fx - 1.0f, fy + 1.0f, ix + 1, iy - 1, this.results);
            }
            if (d > 1.0f - fy) {
                d = checkCube(fx - 1.0f, fy - 1.0f, ix + 1, iy + 1, this.results);
            }
        }
        float t = 0.0f;
        for (int i = 0; i < 3; i++) {
            t += this.coefficients[i] * this.results[i].distance;
        }
        if (this.angleCoefficient != 0.0f) {
            float angle = (float) Math.atan2((double) (y - this.results[0].f447y), (double) (x - this.results[0].f446x));
            if (angle < 0.0f) {
                angle += 6.2831855f;
            }
            t += this.angleCoefficient * (angle / 12.566371f);
        }
        if (this.gradientCoefficient == 0.0f) {
            return t;
        }
        return t + (this.gradientCoefficient * (1.0f / (this.results[0].dy + this.results[0].dx)));
    }

    public float turbulence2(float x, float y, float freq) {
        float t = 0.0f;
        for (float f = 1.0f; f <= freq; f *= 2.0f) {
            t += evaluate(f * x, f * y) / f;
        }
        return t;
    }

    public int getPixel(int x, int y, int[] inPixels, int width, int height) {
        float f;
        float nx = (((this.m00 * ((float) x)) + (this.m01 * ((float) y))) / this.scale) + 1000.0f;
        float ny = (((this.m10 * ((float) x)) + (this.m11 * ((float) y))) / (this.scale * this.stretch)) + 1000.0f;
        if (this.turbulence == 1.0f) {
            f = evaluate(nx, ny);
        } else {
            f = turbulence2(nx, ny, this.turbulence);
        }
        f = (f * 2.0f) * this.amount;
        int v;
        if (this.colormap != null) {
            v = this.colormap.getColor(f);
            if (this.useColor) {
                v = ImageMath.mixColors(ImageMath.smoothStep(this.coefficients[1], this.coefficients[0], (this.results[1].distance - this.results[0].distance) / (this.results[1].distance + this.results[0].distance)), -16777216, inPixels[(ImageMath.clamp((int) ((this.results[0].f447y - 1000.0f) * this.scale), 0, height - 1) * width) + ImageMath.clamp((int) ((this.results[0].f446x - 1000.0f) * this.scale), 0, width - 1)]);
            }
            return v;
        }
        v = PixelUtils.clamp((int) (255.0f * f));
        return ((-16777216 | (v << 16)) | (v << 8)) | v;
    }

    public Object clone() {
        CellularFilter f = null;
        try {
            f = (CellularFilter) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        f.coefficients = (float[]) this.coefficients.clone();
        f.results = (Point[]) this.results.clone();
        f.random = new Random();
        return f;
    }

    public String toString() {
        return "Texture/Cellular...";
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int index = 0;
        int[] outPixels = new int[(width * height)];
        int y = 0;
        while (y < height) {
            int x = 0;
            int index2 = index;
            while (x < width) {
                index = index2 + 1;
                outPixels[index2] = getPixel(x, y, inPixels, width, height);
                x++;
                index2 = index;
            }
            y++;
            index = index2;
        }
        return outPixels;
    }
}
