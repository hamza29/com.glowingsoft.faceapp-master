package com.makemeold.agefaceeditor.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;

public class Effects {

    static int clamp(int a) {
        if (a > 255) {
            return 255;
        }
        return a < 0 ? 0 : a;
    }

    public static Bitmap mergingEffect(Bitmap bit1, Bitmap bit2) {

        int[] pixels1 = new int[(bit1.getHeight() * bit1.getWidth())];
        bit1.getPixels(pixels1, 0, bit1.getWidth(), 0, 0, bit1.getWidth(), bit1.getHeight());
        int[] pixels2 = new int[(bit2.getHeight() * bit2.getWidth())];
        bit2.getPixels(pixels2, 0, bit2.getWidth(), 0, 0, bit2.getWidth(), bit2.getHeight());
        int[] pixel3 = new int[(bit1.getWidth() * bit1.getHeight())];
        for (int i = 0; i < bit1.getHeight() * bit1.getWidth(); i++) {
            int rgb1 = pixels1[i];
            int a1 = (rgb1 >> 24) & 255;
            int r1 = (rgb1 >> 16) & 255;
            int g1 = (rgb1 >> 8) & 255;
            int b1 = rgb1 & 255;
            int rgb2 = pixels2[i];
            int a2 = (rgb2 >> 24) & 255;
            int g2 = (rgb2 >> 8) & 255;
            int b2 = rgb2 & 255;
            int temp = (r1 * ((rgb2 >> 16) & 255)) + 128;
            int dor = clamp(((temp >> 8) + temp) >> 8);
            temp = (g1 * g2) + 128;
            int dog = clamp(((temp >> 8) + temp) >> 8);
            temp = (b1 * b2) + 128;
            pixel3[i] = ((((a1 << 24) & -16777216) | ((dor << 16) & 16711680)) | ((dog << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)) | (clamp(((temp >> 8) + temp) >> 8) & 255);
        }
        return Bitmap.createBitmap(pixel3, bit1.getWidth(), bit1.getHeight(), Config.ARGB_8888);
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {

        Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(bmpOriginal, 0.0f, 0.0f, paint);
        return bmpGrayscale;
    }
}
