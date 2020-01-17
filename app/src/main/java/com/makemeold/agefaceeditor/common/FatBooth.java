package com.makemeold.agefaceeditor.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.activity.MainScreen;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.GaussianFilter;

public class FatBooth extends View {

    float eyebrowx1 = 0.0f;
    float eyebrowx2 = 0.0f;
    float eyebrowy1 = 0.0f;
    float eyebrowy2 = 0.0f;
    MarkerController markerController;
    float reyeX = 0.0f;
    float reyeY = 0.0f;
    int rightColor = -7829368;
    public static int boundryH;
    public static int boundryW;
    public static Bitmap finalImage;
    public static Bitmap originalImage;
    float dis;
    
    float eyed = 0.0f;
    boolean faceDetected;
    FaceDetectionListener faceDetectionListener;
    boolean fatified;
    int image = R.drawable.ic_launcher;
    float initX;
    float initY;
    int initright;
    int initleft;
    int leftColor = -7829368;
    int leftForhead = -7829368;
    float leyeX = 0.0f;
    float leyeY = 0.0f;

    int rightForhead = -7829368;

    public FatBooth(Context context) {
        super(context);
        initialisation(context);
    }

    public boolean isFaceDetected() {
        return faceDetected;
    }

    public void setFaceDetected(boolean faceDetected) {
        faceDetected = faceDetected;
    }

    public void setFaceDetectionListener(FaceDetectionListener listener) {
        faceDetectionListener = listener;
    }

    void initialisation(Context context) {
        context = context;
        boundryW = MainScreen.width;
        boundryH = MainScreen.height;
        originalImage = BitmapFactory.decodeResource(context.getResources(), image);
        finalImage = BitmapFactory.decodeResource(context.getResources(), image);
        markerController = new MarkerController(context);
        markerController.addMarker(new Marker(BitmapFactory.decodeResource(getResources(), R.drawable.markers_eye), (float) (originalImage.getWidth() / 4), (float) (originalImage.getHeight() / 3)));
        markerController.addMarker(new Marker(BitmapFactory.decodeResource(getResources(), R.drawable.markers_eye), (float) (originalImage.getWidth() / 2), (float) (originalImage.getWidth() / 2)));
        markerController.addMarker(new Marker(BitmapFactory.decodeResource(getResources(), R.drawable.markers_mouth), (float) (originalImage.getWidth() / 3), ((float) originalImage.getWidth()) / 1.6f));
        markerController.addMarker(new Marker(BitmapFactory.decodeResource(getResources(), R.drawable.markers_chin), (float) (originalImage.getWidth() / 3), ((float) originalImage.getWidth()) / 1.2f));
    }
    class SetMarker extends AsyncTask<Void, Void, Void> {
        long starttime;

        SetMarker() {
        }

        protected Void doInBackground(Void... params) {
            starttime = System.currentTimeMillis();
           faceDetected = markerController.setMarkers(originalImage);
            Log.d("check", "facedetection " + faceDetected);
            return null;
        }

        protected void onPostExecute(Void result) {
            float vheight;
            super.onPostExecute(result);
            if (MainScreen.width <= 240) {
                if (originalImage.getHeight() > 310) {
                    vheight = (float) MainScreen.height;
                } else {
                    vheight = (float) originalImage.getHeight();
                }
            } else if (originalImage.getHeight() > MainScreen.height) {
                vheight = (float) MainScreen.height;
            } else {
                vheight = (float) originalImage.getHeight();
            }
            boundryW = originalImage.getWidth();
            boundryH = originalImage.getHeight();
            setLayoutParams(new LayoutParams(originalImage.getWidth(), (int) vheight));
            invalidate();
            if (faceDetectionListener != null) {
                faceDetectionListener.faceDetected(Boolean.valueOf(faceDetected));
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (finalImage != null) {
            canvas.drawBitmap(finalImage, 0.0f, 0.0f, null);
        }
        if (markerController != null) {
            markerController.doDraw(canvas);
        }
    }

    protected void onMeasure(int wSpec, int hSpec) {
        setMeasuredDimension(boundryW, boundryH);
    }

    public void resetImage(Context context) {
        finalImage = Bitmap.createBitmap(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                if (markerController != null) {
                    markerController.onTouchDown(event.getX(), event.getY());
                }
                initX = event.getX();
                initY = event.getY();
                return true;
            case 1:
                if (markerController != null) {
                    markerController.onTouchUp(event.getX(), event.getY());
                }
                initX = event.getX();
                initY = event.getY();
                invalidate();
                break;
            case 2:
                if (markerController != null) {
                    markerController.onTouchMove(event.getX() - initX, event.getY() - initY);
                }
                initX = event.getX();
                initY = event.getY();
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void showFatEffectbackup() {

        if (!fatified) {
            Log.d("fatify", "Fatified " + fatified);
            originalImage = null;
            originalImage = Bitmap.createBitmap(finalImage);
            Log.d("check", "final image dim W " + finalImage.getWidth() + " H " + finalImage.getHeight());
            Log.d("check", "effect cordinate px " + markerController.getMarker(3).getX() + " py " + markerController.getMarker(3).getY());
            leyeX = 0.0f;
            leyeY = 0.0f;
            reyeX = 0.0f;
            reyeY = 0.0f;
            if (markerController.getMarker(0).getX() < markerController.getMarker(1).getX()) {
                leyeX = markerController.getMarker(0).getX();
                leyeY = markerController.getMarker(0).getY();
                reyeX = markerController.getMarker(1).getX();
                reyeY = markerController.getMarker(1).getY();
            } else {
                leyeX = markerController.getMarker(1).getX();
                leyeY = markerController.getMarker(1).getY();
                reyeX = markerController.getMarker(0).getX();
                reyeY = markerController.getMarker(0).getY();
            }
            pickColor();
            System.gc();
            Bitmap mask = Bitmap.createBitmap(finalImage.getWidth(), finalImage.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(mask);
            canvas.drawColor(-1);
            Bitmap foreHead = getMaskBitColor(BitmapFactory.decodeResource(getResources(), R.drawable.forehead_wrinkle));
            foreHead = scaleBitmap(foreHead, ((reyeX - leyeX) * 1.5f) / ((float) foreHead.getWidth()));
            canvas.drawBitmap(foreHead, ((reyeX + leyeX) * 0.5f) - (((float) foreHead.getWidth()) * 0.5f), ((leyeY + reyeY) * 0.5f) - (((float) foreHead.getHeight()) * 1.5f), new Paint());
            finalImage = Effects.mergingEffect(finalImage, mask);
            canvas.drawColor(-1);
            Bitmap browHead = getMaskBitColor(BitmapFactory.decodeResource(getResources(), R.drawable.brow_furrow_wrinkle));
            browHead = scaleBitmap(browHead, ((reyeX - leyeX) * 0.3f) / ((float) browHead.getWidth()));
            canvas.drawBitmap(browHead, ((reyeX + leyeX) * 0.5f) - (((float) browHead.getWidth()) * 0.5f), ((leyeY + reyeY) * 0.5f) - (((float) browHead.getHeight()) * 1.3f), new Paint());
            finalImage = Effects.mergingEffect(finalImage, mask);
            canvas.drawColor(-1);
            Bitmap leyeWrinkle = getMaskBitColor(BitmapFactory.decodeResource(getResources(), R.drawable.left_eye_wrinkle));
            leyeWrinkle = scaleBitmap(leyeWrinkle, ((reyeX - leyeX) * 1.0f) / ((float) leyeWrinkle.getWidth()));
            canvas.drawBitmap(leyeWrinkle, (((reyeX + leyeX) * 0.5f) - ((float) leyeWrinkle.getWidth())) - ((reyeX - leyeX) * 0.1f), ((leyeY + reyeY) * 0.5f) - (((float) leyeWrinkle.getHeight()) * 0.2f), new Paint());
            finalImage = Effects.mergingEffect(finalImage, mask);
            canvas.drawColor(-1);
            Bitmap reyeWrinkle = getMaskBitColor(BitmapFactory.decodeResource(getResources(), R.drawable.right_eye_wrinkle));
            reyeWrinkle = scaleBitmap(reyeWrinkle, ((reyeX - leyeX) * 1.0f) / ((float) reyeWrinkle.getWidth()));
            canvas.drawBitmap(reyeWrinkle, ((reyeX + leyeX) * 0.5f) + ((reyeX - leyeX) * 0.1f), ((leyeY + reyeY) * 0.5f) - (((float) reyeWrinkle.getHeight()) * 0.2f), new Paint());
            finalImage = Effects.mergingEffect(finalImage, mask);
            canvas.drawColor(-1);
            Bitmap lchickWrinkle = getMaskBitColor(BitmapFactory.decodeResource(getResources(), R.drawable.left_cheek_wrinkle));
            lchickWrinkle = scaleBitmap(lchickWrinkle, (markerController.getMarker(2).getY() - (((reyeY + leyeY) / 2.0f) * 1.0f)) / ((float) lchickWrinkle.getHeight()));
            canvas.drawBitmap(lchickWrinkle, (((reyeX + leyeX) * 0.5f) - ((float) lchickWrinkle.getWidth())) - ((reyeX - leyeX) * 0.15f), ((leyeY + reyeY) * 0.5f) + (((float) lchickWrinkle.getHeight()) * 0.3f), new Paint());
            finalImage = Effects.mergingEffect(finalImage, mask);
            canvas.drawColor(-1);
            Bitmap rchickWrinkle = getMaskBitColor(BitmapFactory.decodeResource(getResources(), R.drawable.right_cheek_wrinkle));
            rchickWrinkle = scaleBitmap(rchickWrinkle, (markerController.getMarker(2).getY() - (((reyeY + leyeY) / 2.0f) * 1.0f)) / ((float) rchickWrinkle.getHeight()));
            canvas.drawBitmap(rchickWrinkle, ((reyeX + leyeX) * 0.5f) + ((reyeX - leyeX) * 0.1f), ((leyeY + reyeY) * 0.5f) + (((float) rchickWrinkle.getHeight()) * 0.3f), new Paint());
            finalImage = Effects.mergingEffect(finalImage, mask);
            canvas.drawColor(-1);
            Bitmap mouthWrinkle = getMaskBitColor(BitmapFactory.decodeResource(getResources(), R.drawable.mouth_wrinkle));
            canvas.drawBitmap(mouthWrinkle, markerController.getMarker(2).getX() - (((float) mouthWrinkle.getWidth()) * 0.5f), markerController.getMarker(2).getY() - (((float) mouthWrinkle.getHeight()) * 0.5f), new Paint());
            finalImage = Effects.mergingEffect(finalImage, mask);
            canvas.drawColor(-1);
            Bitmap maskOver = getMaskBitColor(BitmapFactory.decodeResource(getResources(), R.drawable.mask_over_change));
            float scaleX = (reyeX - leyeX) / (((0.7214286f - 0.26666668f) * ((float) maskOver.getWidth())) * 1.0f);
            float scaleY = ((markerController.getMarker(3).getY() - ((leyeY + reyeY) * 0.5f)) / (((float) maskOver.getHeight()) - (((float) maskOver.getHeight()) * 0.42553192f))) * 1.0f;
            Log.d("agify", "scale " + scaleX + ", " + scaleY);
            Matrix localMatrix = new Matrix();
            localMatrix.setScale(scaleX, scaleY);
            maskOver = Bitmap.createBitmap(maskOver, 0, 0, maskOver.getWidth(), maskOver.getHeight(), localMatrix, true);
            float fx = ((leyeX + reyeX) * 0.5f) - (((0.7214286f + 0.26666668f) * ((float) maskOver.getWidth())) * 0.5f);
            float fy = ((leyeY + reyeY) * 0.5f) - (((float) maskOver.getHeight()) * 0.42553192f);
            Log.d("agify", "coordinate  " + fx + ", " + fy);
            canvas.drawBitmap(maskOver, fx, fy, new Paint());
            Bitmap bit = Effects.mergingEffect(finalImage, mask);
            finalImage = setSepiaColorFilter(liquifyImage(bit, bit, markerController.getMarker(2).getX() - (eyed * 1.5f), markerController.getMarker(2).getY() + (eyed * 0.3f)));
            System.gc();
            fatified = true;
        }
    }

    private Bitmap scaleBitmap(Bitmap bm, float mScale) {

        Matrix localMatrix = new Matrix();
        localMatrix.setScale(mScale, mScale);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), localMatrix, true);
    }

    public void setImage(Bitmap image) {

        originalImage = image;
        finalImage = Bitmap.createBitmap(originalImage);
        new SetMarker().execute(new Void[0]);
        invalidate();
    }

    public Bitmap getMaskBitColor(Bitmap mask) {

        Bitmap temp = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        canvas.save();
        Paint paint = new Paint();
        Log.i("clr", "val are " + leftColor + " new clr " + ((leftColor + 120000) - 100086));
        paint.setColor((leftColor + 10970000) - 10000086);
        canvas.drawRect(0.0f, 0.0f, (float) mask.getWidth(), (float) mask.getHeight(), paint);
        canvas.restore();
        Bitmap src = temp;
        Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
        Canvas canvas1 = new Canvas(output);
        canvas1.drawBitmap(src, 0.0f, 0.0f, null);
        Paint paint1 = new Paint();
        paint1.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
        canvas1.drawBitmap(Bitmap.createScaledBitmap(mask, src.getWidth(), src.getHeight(), true), 0.0f, 0.0f, paint1);
        return output;
    }

    void pickColor() {

        eyed = reyeX - leyeX;
        eyebrowx1 = leyeX;
        eyebrowy1 = leyeY - (eyed * 0.3f);
        eyebrowx2 = reyeX;
        eyebrowy2 = reyeY - (eyed * 0.3f);
        Log.i("clr", "leyex " + leyeX + " reyex " + reyeX + " eyed " + eyed);
        setBlurFilter(Bitmap.createBitmap(originalImage, (int) (leyeX - (eyed * 0.25f)), (int) (leyeY + (eyed * 0.2f)), (int) (eyed * 0.4f), (int) (eyed * 0.6f)), 0, 100);
        setBlurFilter(Bitmap.createBitmap(originalImage, (int) (reyeX - (eyed * 0.1f)), (int) (reyeY + (eyed * 0.2f)), (int) (eyed * 0.4f), (int) (eyed * 0.6f)), 1, 100);
        setBlurFilter(Bitmap.createBitmap(originalImage, (int) (leyeX - (eyed * 0.25f)), (int) (eyebrowy1 - (eyed * 0.4f)), (int) (eyed * 0.4f), (int) (eyed * 0.3f)), 2, 100);
        setBlurFilter(Bitmap.createBitmap(originalImage, (int) (reyeX - (eyed * 0.1f)), (int) (reyeY - (eyed * 0.4f)), (int) (eyed * 0.4f), (int) (eyed * 0.3f)), 3, 100);
        initleft = leftColor;
        initright = rightColor;
        int tempcolorLeft = Color.argb(0, Color.red(initleft), Color.green(initleft), Color.blue(initleft));
        int tempcolorRight = Color.argb(0, Color.red(initright), Color.green(initright), Color.blue(initright));
        int tempcolorforeheadLeft = Color.argb(0, Color.red(leftForhead), Color.green(leftForhead), Color.blue(leftForhead));
        int tempcolorforeheadRight = Color.argb(0, Color.red(rightForhead), Color.green(rightForhead), Color.blue(rightForhead));
        if (tempcolorLeft < tempcolorforeheadLeft) {
            leftColor = leftForhead;
            tempcolorLeft = tempcolorforeheadLeft;
        }
        if (tempcolorRight < tempcolorforeheadRight) {
            rightColor = rightForhead;
            tempcolorRight = tempcolorforeheadRight;
        }
        int diff;
        if (tempcolorLeft > tempcolorRight) {
            diff = tempcolorLeft - tempcolorRight;
        } else {
            diff = (tempcolorLeft - tempcolorRight) * -1;
        }
    }

    public void setBlurFilter(Bitmap image, int color, int radius) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] mColors = new int[(width * height)];
        image.getPixels(mColors, 0, width, 0, 0, width, height);
        GaussianFilter filter = new GaussianFilter();
        filter.setRadius((float) radius);
        modifyview(filter.filter(mColors, width, height), width, height, color);
    }

    public void modifyview(int[] colors, int width, int height, int color) {
        if (color == 0) {
            leftColor = colors[(int) (((float) colors.length) * 0.5f)];
        } else if (color == 1) {
            rightColor = colors[(int) (((float) colors.length) * 0.5f)];
        } else if (color == 2) {
            leftForhead = colors[(int) (((float) colors.length) * 0.5f)];
        } else {
            rightForhead = colors[(int) (((float) colors.length) * 0.5f)];
        }
    }

    Bitmap setSepiaColorFilter(Bitmap bit) {
        Bitmap bitmap = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ColorMatrix matrixA = new ColorMatrix();
        matrixA.setSaturation(0.5f);
        ColorMatrix matrixB = new ColorMatrix();
        matrixB.setScale(1.0f, 0.95f, 0.82f, 1.0f);
        matrixA.setConcat(matrixB, matrixA);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrixA));
        canvas.drawBitmap(bit, 0.0f, 0.0f, paint);
        return bitmap;
    }


    public Bitmap liquifyImage(Bitmap image1, Bitmap image2, float px, float py) {
        float delta = py;
        int imgW = (int) (eyed * 0.25f);
        int imgH = (int) ((markerController.getMarker(3).getY() - markerController.getMarker(2).getY()) * 0.8f);
        int imgX = (int) (px - ((float) (imgW / 2)));
        int imgY = (int) (py - ((float) (imgH / 2)));
        if (imgX < 0) {
            imgX = 0;
        }
        if (imgY < 0) {
            imgY = 0;
        }
        int width = image1.getWidth();
        int height = image1.getHeight();
        int[] pixel3 = new int[(width * height)];
        image2.getPixels(pixel3, 0, width, 0, 0, width, height);
        int[] output = new int[2];
        dis = delta / 2.0f;
        float maxM = ((float) imgH) * 0.2f;
        float minM = maxM * 0.2f;
        float imc = ((float) imgW) / 2.0f;
        int looping = 0;
        for (int y = (imgY + imgH) - 1; y > imgY; y -= 2) {
            if (y <= ((imgH / 2) + imgY) - 1) {
                dis = maxM;
                for (int x = imgX; x < imgX + imgW; x++) {
                    float dx = ((float) Math.sin((double) (1.5707964f * (((float) Math.abs(imgX - x)) / imc)))) / 2.0f;
                    output[0] = (int) Math.floor((double) x);
                    int val = (int) Math.max(minM, dis * dx);
                    output[1] = (int) Math.floor((double) (y - val));
                    if (output[1] < 0) {
                        output[1] = 0;
                    }
                    int i = (output[1] * width) + output[0];
                    for (int z = 0; z < val; z++) {
                        pixel3[((y - z) * width) + x] = pixel3[i];
                    }
                }
            }
            looping++;
        }
        return Bitmap.createBitmap(pixel3, width, height, Config.ARGB_8888);
    }
}


