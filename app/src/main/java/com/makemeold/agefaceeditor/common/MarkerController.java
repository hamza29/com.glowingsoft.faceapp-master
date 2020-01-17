package com.makemeold.agefaceeditor.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;

import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.activity.MainScreen;

import java.util.ArrayList;

public class MarkerController {

    private ArrayList<Marker> markerArrayList = new ArrayList();

    Bitmap maskBitmap;
    Bitmap mask_boundry;


    private int no_of_face_detected;
    int selectedMarker = -1;
    Context context;
    Bitmap drawBitmap;



    MarkerController(Context context) {
        this.context = context;
    }

    void addMarker(Marker marker) {
        this.markerArrayList.add(marker);
    }

    void doDraw(Canvas canvas) {
        for (int i = 0; i < this.markerArrayList.size(); i++) {
            Log.d("check", "mark count " + this.markerArrayList.size());
            ((Marker) this.markerArrayList.get(i)).doDraw(canvas);
        }
        if (this.selectedMarker != -1) {
            previewDisplay(canvas);
        }
    }

    public ArrayList<Marker> getMarkers() {
        return this.markerArrayList;
    }

    public void setMarkers(ArrayList<Marker> markers) {
        this.markerArrayList = markers;
    }

    Marker getMarker(int i) {
        if (i < this.markerArrayList.size()) {
            return (Marker) this.markerArrayList.get(i);
        }
        return null;
    }

    void onTouchDown(float x, float y) {
        for (int i = 0; i < this.markerArrayList.size(); i++) {
            if (((Marker) this.markerArrayList.get(i)).isTouched(x, y)) {
                ((Marker) this.markerArrayList.get(i)).setMovable(true);
                this.selectedMarker = i;
                if (i < 2) {
                    this.maskBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.mask_eye);
                    this.mask_boundry = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.preview_eyeboundry);
                    this.drawBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.markers_eye);
                    return;
                }
                this.maskBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.mask_mouth);
                this.mask_boundry = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.preview_mouthboundry);
                if (this.selectedMarker == 2) {
                    this.drawBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.markers_mouth);
                    return;
                } else {
                    this.drawBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.markers_chin);
                    return;
                }
            }
        }
    }

    void clearMarker() {
        this.markerArrayList.clear();
    }

    void onTouchMove(float dx, float dy) {
        if (this.selectedMarker != -1) {
            ((Marker) this.markerArrayList.get(this.selectedMarker)).moveMarker(dx, dy);
        }
    }

    void onTouchUp(float x, float y) {
        if (this.selectedMarker != -1) {
            ((Marker) this.markerArrayList.get(this.selectedMarker)).setMovable(false);
        }
        this.selectedMarker = -1;
        this.maskBitmap = null;
        this.mask_boundry = null;
        this.drawBitmap = null;
    }

    boolean setMarkers(Bitmap faceBitmap) {
        float mScale = (float) (480.0d / ((double) FatBooth.originalImage.getWidth()));
        Bitmap bmpFD = Effects.toGrayscale(scaleBitmap(FatBooth.originalImage, mScale));
        Face[] detectedFaces = new Face[1];
        int detected_face_count = new FaceDetector(bmpFD.getWidth(), bmpFD.getHeight(), 1).findFaces(bmpFD, detectedFaces);
        System.gc();
        this.no_of_face_detected = detected_face_count;
        if (detected_face_count <= 0) {
            return false;
        }
        int x;
        int y;
        int desWidth;
        int desHeight;
        Face face = detectedFaces[0];
        PointF midPoint = new PointF();
        face.getMidPoint(midPoint);
        float eye_distance = face.eyesDistance() / mScale;

        float coordX = midPoint.x / mScale;
        float coordY = midPoint.y / mScale;
        Matrix mat = new Matrix();
        float scale = ((float) MainScreen.width) / (3.0f * eye_distance);
        mat.setScale(scale, scale);
        if (((int) (((double) coordX) - (1.5d * ((double) eye_distance)))) > 0) {
            x = (int) (((double) coordX) - (1.5d * ((double) eye_distance)));
        } else {
            x = 0;
        }
        if (((int) (coordY - (2.0f * eye_distance))) > 0) {
            y = (int) (coordY - (2.0f * eye_distance));
        } else {
            y = 0;
        }
        if (((int) (((float) x) + (3.0f * eye_distance))) > faceBitmap.getWidth()) {
            desWidth = faceBitmap.getWidth() - x;
        } else {
            desWidth = (int) (3.0f * eye_distance);
        }
        if (((int) (((double) y) + (4.5d * ((double) eye_distance)))) > faceBitmap.getHeight()) {
            desHeight = faceBitmap.getHeight() - y;
        } else {
            desHeight = (int) (4.5d * ((double) eye_distance));
        }
        clearMarker();
        FatBooth.originalImage = Bitmap.createBitmap(faceBitmap, x, y, desWidth, desHeight, mat, true);
        FatBooth.finalImage = Bitmap.createBitmap(FatBooth.originalImage);
        Bitmap eye = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.markers_eye);
        addMarker(new Marker(eye, ((coordX - ((float) x)) - (eye_distance / 2.0f)) * scale, (coordY - ((float) y)) * scale));
        addMarker(new Marker(eye, ((coordX - ((float) x)) + (eye_distance / 2.0f)) * scale, (coordY - ((float) y)) * scale));
        addMarker(new Marker(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.markers_mouth), (coordX - ((float) x)) * scale, ((coordY - ((float) y)) + eye_distance) * scale));
        addMarker(new Marker(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.markers_chin), (coordX - ((float) x)) * scale, ((coordY - ((float) y)) + (1.8f * eye_distance)) * scale));
        return true;
    }

    private Bitmap scaleBitmap(Bitmap bm, float mScale) {
        Matrix localMatrix = new Matrix();
        localMatrix.setScale(mScale, mScale);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), localMatrix, true);
    }

    void previewDisplay(Canvas canvas) {
        if (this.selectedMarker != -1 && this.maskBitmap != null && this.mask_boundry != null) {
            int width;
            int height;
            float mx = ((Marker) this.markerArrayList.get(this.selectedMarker)).getX();
            float my = ((Marker) this.markerArrayList.get(this.selectedMarker)).getY();
            Bitmap output = Bitmap.createBitmap(this.maskBitmap.getWidth(), this.maskBitmap.getHeight(), Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(output);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            bitmapCanvas.drawBitmap(this.maskBitmap, 0.0f, 0.0f, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            Rect src = new Rect((int) (((Marker) this.markerArrayList.get(this.selectedMarker)).getX() - (((float) this.maskBitmap.getWidth()) / 2.0f)), (int) (((Marker) this.markerArrayList.get(this.selectedMarker)).getY() + (((float) this.maskBitmap.getHeight()) / 2.0f)), (int) (((Marker) this.markerArrayList.get(this.selectedMarker)).getX() + (((float) this.maskBitmap.getWidth()) / 2.0f)), (int) (((Marker) this.markerArrayList.get(this.selectedMarker)).getY() - (((float) this.maskBitmap.getHeight()) / 2.0f)));
            RectF des = new RectF(0.0f, 0.0f, (float) this.maskBitmap.getWidth(), (float) this.maskBitmap.getHeight());
            int x = ((int) (mx - (((float) this.maskBitmap.getWidth()) / 2.0f))) > 0 ? (int) (mx - (((float) this.maskBitmap.getWidth()) / 2.0f)) : 0;
            int y = ((int) (my - (((float) this.maskBitmap.getHeight()) / 2.0f))) > 0 ? (int) (my - (((float) this.maskBitmap.getHeight()) / 2.0f)) : 0;
            if (this.maskBitmap.getWidth() + x > FatBooth.originalImage.getWidth()) {
                width = FatBooth.originalImage.getWidth() - x;
            } else {
                width = this.maskBitmap.getWidth();
            }
            if (this.maskBitmap.getHeight() + y > FatBooth.originalImage.getHeight()) {
                height = FatBooth.originalImage.getHeight() - y;
            } else {
                height = this.maskBitmap.getHeight();
            }
            if (height < 0) {
            }
            if (width > 0 && height > 0) {
                if (mx - (((float) this.maskBitmap.getWidth()) / 2.0f) > 0.0f) {
                    bitmapCanvas.drawBitmap(Bitmap.createBitmap(FatBooth.originalImage, x, y, width, height), 0.0f, 0.0f, paint);
                } else {
                    bitmapCanvas.drawBitmap(Bitmap.createBitmap(FatBooth.originalImage, x, y, (int) ((((float) this.maskBitmap.getWidth()) / 2.0f) + mx), height), (float) ((int) ((((float) this.maskBitmap.getWidth()) / 2.0f) - mx)), 0.0f, paint);
                }
            }
            bitmapCanvas.drawBitmap(this.mask_boundry, 0.0f, 0.0f, null);
            bitmapCanvas.drawBitmap(this.drawBitmap, ((float) (this.mask_boundry.getWidth() - this.drawBitmap.getWidth())) / 2.0f, ((float) (this.mask_boundry.getHeight() - this.drawBitmap.getHeight())) / 2.0f, null);
            canvas.drawBitmap(output, mx - (((float) this.maskBitmap.getWidth()) / 2.0f), (my - ((float) this.maskBitmap.getHeight())) - ((float) (((Marker) this.markerArrayList.get(this.selectedMarker)).getHeight() / 2)), null);
        }
    }
}
