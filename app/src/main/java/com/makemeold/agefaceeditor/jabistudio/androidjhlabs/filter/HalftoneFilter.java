package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.support.v4.view.ViewCompat;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class HalftoneFilter {
    private boolean invert;
    private int[] mask;
    private int maskHeight;
    private int maskWidth;
    private boolean monochrome;
    private float softness = 0.1f;

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public float getSoftness() {
        return this.softness;
    }

    public void setMask(int[] mask) {
        this.mask = mask;
    }

    public int[] getMask() {
        return this.mask;
    }

    public void setMaskWidth(int maskwidth) {
        this.maskWidth = maskwidth;
    }

    public void setMaskHeight(int maskheight) {
        this.maskHeight = maskheight;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public boolean getInvert() {
        return this.invert;
    }

    public void setMonochrome(boolean monochrome) {
        this.monochrome = monochrome;
    }

    public boolean getMonochrome() {
        return this.monochrome;
    }

    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;
        int[] dst = new int[(w * h)];
        if (this.mask != null) {
            int maskWidth = this.maskWidth;
            int maskHeight = this.maskHeight;
            float s = 255.0f * this.softness;
            int[] inPixels = new int[width];
            int[] maskPixels = new int[maskWidth];
            for (int y = 0; y < height; y++) {
                PixelUtils.getLineRGB(src, y, width, inPixels);
                PixelUtils.getLineRGB(this.mask, y % maskHeight, maskWidth, maskPixels);
                for (int x = 0; x < width; x++) {
                    int maskRGB = maskPixels[x % maskWidth];
                    int inRGB = inPixels[x];
                    if (this.invert) {
                        maskRGB ^= ViewCompat.MEASURED_SIZE_MASK;
                    }
                    if (this.monochrome) {
                        int v = PixelUtils.brightness(maskRGB);
                        int iv = PixelUtils.brightness(inRGB);
                        int a = (int) (255.0f * (1.0f - ImageMath.smoothStep(((float) iv) - s, ((float) iv) + s, (float) v)));
                        inPixels[x] = (((-16777216 & inRGB) | (a << 16)) | (a << 8)) | a;
                    } else {
                        int ir = (inRGB >> 16) & 255;
                        int ig = (inRGB >> 8) & 255;
                        int ib = inRGB & 255;
                        int smoothStep = ((-16777216 & inRGB) | (((int) (255.0f * (1.0f - ImageMath.smoothStep(((float) ir) - s, ((float) ir) + s, (float) ((maskRGB >> 16) & 255))))) << 16)) | (((int) (255.0f * (1.0f - ImageMath.smoothStep(((float) ig) - s, ((float) ig) + s, (float) ((maskRGB >> 8) & 255))))) << 8);
                        inPixels[x] = smoothStep | ((int) (255.0f * (1.0f - ImageMath.smoothStep(((float) ib) - s, ((float) ib) + s, (float) (maskRGB & 255)))));
                    }
                }
                PixelUtils.setLineRGB(dst, y, width, inPixels);
            }
        }
        return dst;
    }

    public String toString() {
        return "Stylize/Halftone...";
    }
}
