package com.makemeold.agefaceeditor.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.StickerView.DrawableSticker;
import com.makemeold.agefaceeditor.StickerView.StickerView;
import com.makemeold.agefaceeditor.common.FatBooth;
import com.makemeold.agefaceeditor.common.ShakeEventListener;
import com.makemeold.agefaceeditor.share.MainApplication;
import com.makemeold.agefaceeditor.share.Share;
import com.google.android.gms.ads.AdListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultScreen extends Wrapper implements OnClickListener {

    AsyncTask save_task;
    RelativeLayout main_layout;
    public static List<DrawableSticker> drawables_sticker = new ArrayList<>();
    public static StickerView stickerView;

    ImageView imgMain;
    String filename;
    private ShakeEventListener mSensorListener;
    private SensorManager mSensorManager;
    boolean showFatify = true;
    boolean shownow;
    LinearLayout lnSave, lnRetry, lnSticker,lnFonttext;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultlayout);

        findViews();
        initViews();

    }

    private void findViews() {

        lnSave = findViewById(R.id.save);
        lnRetry = findViewById(R.id.retry);
        lnSticker = findViewById(R.id.sticker);
        lnFonttext = findViewById(R.id.fonttext);
        main_layout = findViewById(R.id.main_layout);
        stickerView = (StickerView) findViewById(R.id.stickerView);
    }


    private void initViews() {

        filename = System.currentTimeMillis() + ".png";
        imgMain = (ImageView) findViewById(R.id.mainimg);
        imgMain.setImageBitmap(getRoundBitmap(FatBooth.finalImage));
        shownow = false;

        lnSave.setOnClickListener(this);
        lnRetry.setOnClickListener(this);
        lnSticker.setOnClickListener(this);
        lnFonttext.setOnClickListener(this);

        mSensorManager = (SensorManager) getSystemService("sensor");
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new shakeListener());
    }

    class shakeListener implements ShakeEventListener.OnShakeListener {
        shakeListener() {
        }

        public void onShake() {
            showFatify = !showFatify;
            Log.d("fatify", "onShake showfatify " + showFatify);
            if (showFatify) {
                imgMain.setImageBitmap(ResultScreen.getRoundBitmap(FatBooth.finalImage));
            } else {
                imgMain.setImageBitmap(ResultScreen.getRoundBitmap(FatBooth.originalImage));
            }
        }
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, 12.0f, 12.0f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public void onBackPressed() {

        retry();
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
        super.onStop();
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.save:

                saveImage();
                return;

            case R.id.retry:

                retry();
                return;

            case R.id.sticker:

                Intent intent = new Intent(ResultScreen.this, StickerScreen.class);
                startActivity(intent);
                return;

            case R.id.fonttext:

                if (!MainApplication.getInstance().requestNewInterstitial()) {

                    Intent intentt = new Intent(ResultScreen.this, FontActivity.class);
                    startActivity(intentt);
                    return;

                } else {

                    MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();

                            MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                            MainApplication.getInstance().mInterstitialAd = null;
                            MainApplication.getInstance().ins_adRequest = null;
                            MainApplication.getInstance().LoadAds();

                            Intent intentt = new Intent(ResultScreen.this, FontActivity.class);
                            startActivity(intentt);
                            return;

                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                        }
                    });
                }


                return;
            default:
                return;
        }
    }

    private void saveImage() {

        stickerView.setControlItemsHidden();
        main_layout.setDrawingCacheEnabled(true);
        Share.EDITED_IMAGE = Bitmap.createBitmap(main_layout.getDrawingCache());
        main_layout.setDrawingCacheEnabled(false);

        save_task = new saveImage().execute();
    }

    public class saveImage extends AsyncTask<Void, Void, Void> {

        Dialog saveDialog;
        Bitmap finalBmp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            saveDialog = Share.showProgress(ResultScreen.this, "Saving...");
            saveDialog.show();
            finalBmp = Share.EDITED_IMAGE;
        }

        @Override
        protected Void doInBackground(Void... params) {

            File path = new File(Share.IMAGE_PATH);
            if (!path.exists())
                path.mkdirs();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            try {

                if (finalBmp != null) {
                    File imageFile = new File(path, timeStamp + ".png");
                    Log.e("TAG", "imageFile=>" + imageFile);

                    if (!imageFile.exists())
                        imageFile.createNewFile();
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(imageFile);
                        finalBmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.close();

                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {
                            }

                            @Override
                            public void onScanCompleted(String path, final Uri uri) {
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null)
                                fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.e("TAG", "Not Saved Image------------------------------------------------------->");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Share.Fragment = "MyPhotosFragment";

            if (saveDialog.isShowing()) {

                saveDialog.dismiss();
                Toast.makeText(ResultScreen.this, getString(R.string.save_image), Toast.LENGTH_LONG).show();

                Share.Fragment = "MyPhotosFragment";


                if (!MainApplication.getInstance().requestNewInterstitial()) {

                    nextActivity();

                } else {

                    MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();

                            MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                            MainApplication.getInstance().mInterstitialAd = null;
                            MainApplication.getInstance().ins_adRequest = null;
                            MainApplication.getInstance().LoadAds();

                            nextActivity();
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                        }
                    });
                }

            }
        }

    }

    private void nextActivity() {

        save_task = null;
        Intent intent = new Intent(ResultScreen.this, FullScreenImageScreen.class);
        Share.Fragment = "MyPhotosFragment";
        intent.putExtra("avairy", "");
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    void retry() {

        finish();
    }


    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(1), 2);

        if (Share.FONT_FLAG) {
            Share.FONT_FLAG = false;
            stickerView = (StickerView) findViewById(R.id.stickerView);

            DrawableSticker drawableSticker = Share.TEXT_DRAWABLE;
            drawableSticker.setTag("text");
            stickerView.addSticker(drawableSticker);

            drawables_sticker.add(drawableSticker);
            Share.isStickerAvail = true;
            Share.isStickerTouch = true;
        }
    }

}
