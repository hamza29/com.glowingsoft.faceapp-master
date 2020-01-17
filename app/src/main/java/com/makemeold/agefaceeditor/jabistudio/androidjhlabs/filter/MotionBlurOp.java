package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

public class MotionBlurOp {
    private float angle;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float distance;
    private float rotation;
    private float zoom;

    public MotionBlurOp(float distance, float angle, float rotation, float zoom) {
        this.distance = distance;
        this.angle = angle;
        this.rotation = rotation;
        this.zoom = zoom;
    }

    public MotionBlurOp() {

    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getZoom() {
        return this.zoom;
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

    public void setCentre(float centerX, float centerY) {
        this.centreX = centerX;
        this.centreY = centerY;
    }

    private int log2(int n) {
        int m = 1;
        int log2n = 0;
        while (m < n) {
            m *= 2;
            log2n++;
        }
        return log2n;
    }

    public int[] filter(int[] src, int w, int h) {
        int[] dst = new int[(w * h)];
        Bitmap srcBitmap = Bitmap.createBitmap(src, w, h, Config.ARGB_8888);
        Bitmap tSrcBitmap = Bitmap.createBitmap(src, w, h, Config.ARGB_8888);
        Bitmap dstBitmap = srcBitmap.copy(Config.ARGB_8888, true);
        float cx = ((float) w) * this.centreX;
        float cy = ((float) h) * this.centreY;
        float imageRadius = (float) Math.sqrt((double) ((cx * cx) + (cy * cy)));
        float translateX = (float) (((double) this.distance) * Math.cos((double) this.angle));
        float translateY = (float) (((double) this.distance) * (-Math.sin((double) this.angle)));
        float scale = this.zoom;
        float rotate = this.rotation;
        float maxDistance = (this.distance + Math.abs(this.rotation * imageRadius)) + (this.zoom * imageRadius);
        int steps = log2((int) maxDistance);
        translateX /= maxDistance;
        translateY /= maxDistance;
        scale /= maxDistance;
        rotate /= maxDistance;
        if (steps == 0) {
            dstBitmap.getPixels(dst, 0, w, 0, 0, w, h);
            srcBitmap.recycle();
            tSrcBitmap.recycle();
            dstBitmap.recycle();
        } else {
            Bitmap tmpBitmap = srcBitmap.copy(Config.ARGB_8888, true);
            Bitmap ti = null;
            Paint p = new Paint();
            p.setAlpha(128);
            p.setAntiAlias(true);
            p.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
            for (int i = 0; i < steps; i++) {
                Canvas c = new Canvas(dstBitmap);
                c.translate(cx + translateX, cy + translateY);
                c.scale((float) (1.0001d + ((double) scale)), (float) (1.0001d + ((double) scale)), 0.5f, 0.5f);
                c.scale((float) (1.0001d + ((double) scale)), (float) (1.0001d + ((double) scale)));
                if (this.rotation != 0.0f) {
                    c.rotate(rotate);
                }
                c.translate(-cx, -cy);
                c.drawBitmap(tSrcBitmap, 0.0f, 0.0f, p);
                ti = dstBitmap;
                dstBitmap = tmpBitmap;
                tmpBitmap = ti;
                tSrcBitmap = dstBitmap;
                translateX *= 2.0f;
                translateY *= 2.0f;
                scale *= 2.0f;
                rotate *= 2.0f;
            }
            dstBitmap.getPixels(dst, 0, w, 0, 0, w, h);
            if (ti != null) {
                ti.recycle();
            }
            srcBitmap.recycle();
            tSrcBitmap.recycle();
            tmpBitmap.recycle();
            dstBitmap.recycle();
        }
        return dst;
    }

    public String toString() {
        return "Blur/Faster Motion Blur...";
    }
}
