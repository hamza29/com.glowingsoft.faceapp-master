package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public abstract class TransformFilter {
    public static final int BILINEAR = 1;
    public static final int CLAMP = 1;
    public static final int NEAREST_NEIGHBOUR = 0;
    public static final int RGB_CLAMP = 3;
    public static final int WRAP = 2;
    public static final int ZERO = 0;
    protected int edgeAction = 3;
    protected int interpolation = 1;
    protected Rect originalSpace;
    protected Rect transformedSpace;

    protected abstract void transformInverse(int i, int i2, float[] fArr);

    public void setEdgeAction(int edgeAction) {
        this.edgeAction = edgeAction;
    }

    public int getEdgeAction() {
        return this.edgeAction;
    }

    public void setInterpolation(int interpolation) {
        this.interpolation = interpolation;
    }

    public int getInterpolation() {
        return this.interpolation;
    }

    protected void transformSpace(Rect rect) {
    }

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        Log.d("DEBUG", "width = " + width + "  height = " + height);
        this.originalSpace = new Rect(0, 0, width, height);
        this.transformedSpace = new Rect(0, 0, width, height);
        transformSpace(this.transformedSpace);
        int[] inPixels = src;
        int[] dst = new int[(width * height)];
        if (this.interpolation == 0) {
            return filterPixelsNN(dst, width, height, inPixels, this.transformedSpace);
        }
        int srcWidth = width;
        int srcHeight = height;
        int srcWidth1 = width - 1;
        int srcHeight1 = height - 1;
        int outWidth = this.transformedSpace.right;
        int outHeight = this.transformedSpace.bottom;
        int[] outPixels = new int[outWidth];
        int outX = this.transformedSpace.left;
        int outY = this.transformedSpace.top;
        float[] out = new float[2];
        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                int se;
                int sw;
                int ne;
                int nw;
                transformInverse(outX + x, outY + y, out);
                int srcX = (int) Math.floor((double) out[0]);
                int srcY = (int) Math.floor((double) out[1]);
                float xWeight = out[0] - ((float) srcX);
                float yWeight = out[1] - ((float) srcY);
                if (srcX < 0 || srcX >= srcWidth1 || srcY < 0 || srcY >= srcHeight1) {
                    int nw2 = getPixel(inPixels, srcX, srcY, srcWidth, srcHeight);
                    int ne2 = getPixel(inPixels, srcX + 1, srcY, srcWidth, srcHeight);
                    int sw2 = getPixel(inPixels, srcX, srcY + 1, srcWidth, srcHeight);
                    se = getPixel(inPixels, srcX + 1, srcY + 1, srcWidth, srcHeight);
                    sw = sw2;
                    ne = ne2;
                    nw = nw2;
                } else {
                    int i = (srcWidth * srcY) + srcX;
                    nw = inPixels[i];
                    ne = inPixels[i + 1];
                    sw = inPixels[i + srcWidth];
                    se = inPixels[(i + srcWidth) + 1];
                }
                outPixels[x] = ImageMath.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
            }
            if (y < height) {
                PixelUtils.setLineRGB(dst, y, width, outPixels);
            }
        }
        return dst;
    }

    private final int getPixel(int[] pixels, int x, int y, int width, int height) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return pixels[(y * width) + x];
        }
        switch (this.edgeAction) {
            case 1:
                return pixels[ImageMath.clamp(x, 0, width - 1) + (ImageMath.clamp(y, 0, height - 1) * width)];
            case 2:
                return pixels[(ImageMath.mod(y, height) * width) + ImageMath.mod(x, width)];
            case 3:
                return pixels[ImageMath.clamp(x, 0, width - 1) + (ImageMath.clamp(y, 0, height - 1) * width)] & ViewCompat.MEASURED_SIZE_MASK;
            default:
                return 0;
        }
    }

    protected int[] filterPixelsNN(int[] dst, int width, int height, int[] inPixels, Rect transformedSpace) {
        int srcWidth = width;
        int srcHeight = height;
        int outWidth = transformedSpace.right;
        int outHeight = transformedSpace.bottom;
        int[] outPixels = new int[outWidth];
        int outX = transformedSpace.left;
        int outY = transformedSpace.top;
        int[] rgb = new int[4];
        float[] out = new float[2];
        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                transformInverse(outX + x, outY + y, out);
                int srcX = (int) out[0];
                int srcY = (int) out[1];
                if (out[0] < 0.0f || srcX >= srcWidth || out[1] < 0.0f || srcY >= srcHeight) {
                    int p;
                    switch (this.edgeAction) {
                        case 1:
                            p = inPixels[(ImageMath.clamp(srcY, 0, srcHeight - 1) * srcWidth) + ImageMath.clamp(srcX, 0, srcWidth - 1)];
                            break;
                        case 2:
                            p = inPixels[(ImageMath.mod(srcY, srcHeight) * srcWidth) + ImageMath.mod(srcX, srcWidth)];
                            break;
                        case 3:
                            p = inPixels[(ImageMath.clamp(srcY, 0, srcHeight - 1) * srcWidth) + ImageMath.clamp(srcX, 0, srcWidth - 1)] & ViewCompat.MEASURED_SIZE_MASK;
                            break;
                        default:
                            p = 0;
                            break;
                    }
                    outPixels[x] = p;
                } else {
                    int i = (srcWidth * srcY) + srcX;
                    rgb[0] = inPixels[i];
                    outPixels[x] = inPixels[i];
                }
            }
            if (y < height) {
                PixelUtils.setLineRGB(dst, y, width, outPixels);
            }
        }
        return dst;
    }
}
