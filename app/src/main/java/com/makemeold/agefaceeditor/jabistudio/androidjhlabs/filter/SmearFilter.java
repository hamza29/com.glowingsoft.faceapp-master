package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import java.util.Date;
import java.util.Random;

public class SmearFilter extends WholeImageFilter {
    public static final int CIRCLES = 2;
    public static final int CROSSES = 0;
    public static final int LINES = 1;
    public static final int SQUARES = 3;
    private float angle = 0.0f;
    private boolean background = false;
    private float density = 0.5f;
    private int distance = 8;
    private int fadeout = 0;
    private float mix = 0.5f;
    private Random randomGenerator = new Random();
    private float scatter = 0.0f;
    private long seed = 567;
    private int shape = 1;

    public void setShape(int shape) {
        this.shape = shape;
    }

    public int getShape() {
        return this.shape;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return this.density;
    }

    public void setScatter(float scatter) {
        this.scatter = scatter;
    }

    public float getScatter() {
        return this.scatter;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setMix(float mix) {
        this.mix = mix;
    }

    public float getMix() {
        return this.mix;
    }

    public void setFadeout(int fadeout) {
        this.fadeout = fadeout;
    }

    public int getFadeout() {
        return this.fadeout;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public boolean getBackground() {
        return this.background;
    }

    public void randomize() {
        this.seed = new Date().getTime();
    }

    private float random(float low, float high) {
        return ((high - low) * this.randomGenerator.nextFloat()) + low;
    }

    protected int[] filterPixels(int width, int height, int[] inPixels, Rect transformedSpace) {
        int y;
        int x;
        int[] outPixels = new int[(width * height)];
        this.randomGenerator.setSeed(this.seed);
        float sinAngle = (float) Math.sin((double) this.angle);
        float cosAngle = (float) Math.cos((double) this.angle);
        int i = 0;
        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                outPixels[i] = this.background ? -1 : inPixels[i];
                i++;
            }
        }
        int numShapes;
        int length;
        int rgb;
        int x1;
        int y1;
        int sx;
        int sy;
        switch (this.shape) {
            case 0:
                numShapes = (int) ((((2.0f * this.density) * ((float) width)) * ((float) height)) / ((float) (this.distance + 1)));
                for (i = 0; i < numShapes; i++) {
                    x = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % width;
                    y = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % height;
                    length = (this.randomGenerator.nextInt() % this.distance) + 1;
                    rgb = inPixels[(y * width) + x];
                    x1 = x - length;
                    while (x1 < (x + length) + 1) {
                        if (x1 >= 0 && x1 < width) {
                            outPixels[(y * width) + x1] = ImageMath.mixColors(this.mix, this.background ? -1 : outPixels[(y * width) + x1], rgb);
                        }
                        x1++;
                    }
                    y1 = y - length;
                    while (y1 < (y + length) + 1) {
                        if (y1 >= 0 && y1 < height) {
                            outPixels[(y1 * width) + x] = ImageMath.mixColors(this.mix, this.background ? -1 : outPixels[(y1 * width) + x], rgb);
                        }
                        y1++;
                    }
                }
                break;
            case 1:
                numShapes = (int) ((((2.0f * this.density) * ((float) width)) * ((float) height)) / 2.0f);
                for (i = 0; i < numShapes; i++) {
                    int ddx;
                    int ddy;
                    sx = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % width;
                    sy = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % height;
                    rgb = inPixels[(sy * width) + sx];
                    length = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % this.distance;
                    int dx = (int) (((float) length) * cosAngle);
                    int dy = (int) (((float) length) * sinAngle);
                    int x0 = sx - dx;
                    int y0 = sy - dy;
                    x1 = sx + dx;
                    y1 = sy + dy;
                    if (x1 < x0) {
                        ddx = -1;
                    } else {
                        ddx = 1;
                    }
                    if (y1 < y0) {
                        ddy = -1;
                    } else {
                        ddy = 1;
                    }
                    dy = y1 - y0;
                    dx = Math.abs(x1 - x0);
                    dy = Math.abs(dy);
                    x = x0;
                    y = y0;
                    if (x < width && x >= 0 && y < height && y >= 0) {
                        outPixels[(y * width) + x] = ImageMath.mixColors(this.mix, this.background ? -1 : outPixels[(y * width) + x], rgb);
                    }
                    int d;
                    int incrE;
                    int incrNE;
                    if (Math.abs(dx) > Math.abs(dy)) {
                        d = (dy * 2) - dx;
                        incrE = dy * 2;
                        incrNE = (dy - dx) * 2;
                        while (x != x1) {
                            if (d <= 0) {
                                d += incrE;
                            } else {
                                d += incrNE;
                                y += ddy;
                            }
                            x += ddx;
                            if (x < width && x >= 0 && y < height && y >= 0) {
                                outPixels[(y * width) + x] = ImageMath.mixColors(this.mix, this.background ? -1 : outPixels[(y * width) + x], rgb);
                            }
                        }
                    } else {
                        d = (dx * 2) - dy;
                        incrE = dx * 2;
                        incrNE = (dx - dy) * 2;
                        while (y != y1) {
                            if (d <= 0) {
                                d += incrE;
                            } else {
                                d += incrNE;
                                x += ddx;
                            }
                            y += ddy;
                            if (x < width && x >= 0 && y < height && y >= 0) {
                                outPixels[(y * width) + x] = ImageMath.mixColors(this.mix, this.background ? -1 : outPixels[(y * width) + x], rgb);
                            }
                        }
                    }
                }
                break;
            case 2:
            case 3:
                int radius = this.distance + 1;
                int radius2 = radius * radius;
                numShapes = (int) ((((2.0f * this.density) * ((float) width)) * ((float) height)) / ((float) radius));
                for (i = 0; i < numShapes; i++) {
                    sx = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % width;
                    sy = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % height;
                    rgb = inPixels[(sy * width) + sx];
                    x = sx - radius;
                    while (x < (sx + radius) + 1) {
                        y = sy - radius;
                        while (y < (sy + radius) + 1) {
                            int f;
                            if (this.shape == 2) {
                                f = ((x - sx) * (x - sx)) + ((y - sy) * (y - sy));
                            } else {
                                f = 0;
                            }
                            if (x >= 0 && x < width && y >= 0 && y < height && f <= radius2) {
                                int rgb2;
                                if (this.background) {
                                    rgb2 = -1;
                                } else {
                                    rgb2 = outPixels[(y * width) + x];
                                }
                                outPixels[(y * width) + x] = ImageMath.mixColors(this.mix, rgb2, rgb);
                            }
                            y++;
                        }
                        x++;
                    }
                }
                break;
        }
        return outPixels;
    }

    public String toString() {
        return "Effects/Smear...";
    }
}
