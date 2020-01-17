package com.makemeold.agefaceeditor.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Marker {

    Bitmap markerBtmp;
    boolean movable;
    float xPos;
    float yPos;

    Marker(Bitmap marker, float x, float y) {
        this.markerBtmp = marker;
        this.xPos = x;
        this.yPos = y;
    }

    void doDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(0.0f, this.xPos, this.yPos);
        new Paint().setAntiAlias(true);
        canvas.drawBitmap(this.markerBtmp, this.xPos - (((float) this.markerBtmp.getWidth()) / 2.0f), this.yPos - (((float) this.markerBtmp.getHeight()) / 2.0f), null);
        canvas.restore();
    }

    boolean isTouched(float px, float py) {
        if (px <= this.xPos - ((float) (this.markerBtmp.getWidth() / 2)) || px >= this.xPos + ((float) (this.markerBtmp.getWidth() / 2)) || py <= this.yPos - ((float) (this.markerBtmp.getHeight() / 2)) || py >= this.yPos + ((float) (this.markerBtmp.getHeight() / 2))) {
            return false;
        }
        return true;
    }

    public Bitmap getMarker() {
        return this.markerBtmp;
    }

    public void setMarker(Bitmap marker) {
        this.markerBtmp = marker;
    }

    public float getX() {
        return this.xPos;
    }

    public void setX(float x) {
        this.xPos = x;
    }

    public float getY() {
        return this.yPos;
    }

    public void setY(float y) {
        this.yPos = y;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    void moveMarker(float dx, float dy) {
        Log.d("check", "marker pos x " + dx + " y " + dy);
        if (this.movable) {
            this.xPos += dx;
            this.yPos += dy;
            if (this.xPos < 0.0f) {
                this.xPos -= dx;
            }
            if (this.xPos > ((float) FatBooth.boundryW)) {
                this.xPos = (float) FatBooth.boundryW;
            }
            if (this.yPos < 0.0f) {
                this.yPos -= dy;
            }
            if (this.yPos > ((float) FatBooth.boundryH)) {
                this.yPos = (float) FatBooth.boundryH;
            }
        }
    }

    int getWidth() {
        return this.markerBtmp.getWidth();
    }

    int getHeight() {
        return this.markerBtmp.getHeight();
    }

    boolean isMovable() {
        return this.movable;
    }
}
