package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class AndroidUtils {
    public static int dipTopx(float dip, Context context) {
        return (int) TypedValue.applyDimension(1, dip, context.getResources().getDisplayMetrics());
    }

    public static float pxTodip(int px, Context context) {
        return ((float) px) / context.getResources().getDisplayMetrics().density;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static int[] drawableToIntArray(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int[] colors = new int[(bitmapWidth * bitmapHeight)];
        bitmap.getPixels(colors, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        return colors;
    }

    public static int[] bitmapToIntArray(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int[] colors = new int[(bitmapWidth * bitmapHeight)];
        bitmap.getPixels(colors, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        return colors;
    }

    public static int getBitmapOfWidth(Resources res, int id) {
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, options);
            return options.outWidth;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getBitmapOfHeight(Resources res, int id) {
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, options);
            return options.outHeight;
        } catch (Exception e) {
            return 0;
        }
    }
}
