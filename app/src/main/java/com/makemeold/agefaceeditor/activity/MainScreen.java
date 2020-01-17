package com.makemeold.agefaceeditor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.makemeold.agefaceeditor.R;
import com.makemeold.agefaceeditor.common.FaceDetectionListener;
import com.makemeold.agefaceeditor.common.FatBooth;
import com.makemeold.agefaceeditor.common.LoadImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainScreen extends Wrapper implements FaceDetectionListener {

    public static int height;
    public static int imageH;
    public static int imageW;
    public static int width;
    OnClickListener clickListener = new C07771();
    ProgressDialog progressDialog;
    FatBooth fatBooth;


    public class MyTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... params) {
            fatBooth.showFatEffectbackup();
            return null;
        }

        protected void onPostExecute(Void result) {
            fatBooth.invalidate();
            startActivity(new Intent(MainScreen.this, ResultScreen.class));
            finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);

        initViews();

    }

    private void initViews() {

        Display d = getWindowManager().getDefaultDisplay();
        width = d.getWidth();
        height = d.getHeight();
        Bitmap face = extractImageFromIntent();
        if (face == null) {
            finish();
        }
        imageW = face.getWidth();
        imageH = face.getHeight();
        fatBooth = new FatBooth(this);
        fatBooth.setLayoutParams(new LayoutParams(width, width));
        fatBooth.setImage(Bitmap.createScaledBitmap(face, width, (int) (((float) (width * face.getHeight())) / (((float) face.getWidth()) * 1.0f)), true));
        fatBooth.setFaceDetectionListener(this);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.fatlayout);
        fatBooth.setVisibility(4);
        rl.addView(fatBooth);
        ((LinearLayout) findViewById(R.id.fateffect)).setOnClickListener(clickListener);
        ImageView view = (ImageView) findViewById(R.id.waiting_loading);
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(-1);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    Bitmap extractImageFromIntent() {
        return getPhoto(Uri.parse(getIntent().getStringExtra("URI")));
    }

    Bitmap getPhoto(Uri selectedImage) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Options bitmapOptions = new Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
        int imageWidth = bitmapOptions.outWidth;
        int imageHeight = bitmapOptions.outHeight;
        int angle = (int) rotationImage(this, selectedImage);
        try {
            InputStream is = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        float scale = 1.0f;
        if (angle == 0) {
            if (imageWidth < imageHeight) {
                if (imageWidth > width) {
                    scale = ((float) width) / (((float) imageWidth) * 1.0f);
                }
            } else if (imageHeight > height) {
                scale = ((float) height) / (((float) imageHeight) * 1.0f);
            }
        } else if (imageWidth > imageHeight) {
            if (imageHeight > width) {
                scale = ((float) width) / (((float) imageHeight) * 1.0f);
            }
        } else if (imageWidth > height) {
            scale = ((float) height) / (((float) imageWidth) * 1.0f);
        }
        Bitmap phoBitmap = LoadImage.decodeSampledBitmapFromResource(this, selectedImage, (int) (((float) imageWidth) * scale), (int) (((float) imageHeight) * scale));
        if (angle != 0) {
            phoBitmap = rotate(phoBitmap, angle);
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        return phoBitmap;
    }

    public float rotationImage(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            Cursor c = context.getContentResolver().query(uri, new String[]{"orientation"}, null, null, null);
            if (c.moveToFirst()) {
                return (float) c.getInt(0);
            }
        } else if (uri.getScheme().equals("file")) {
            try {
                return (float) ((int) exifOrientationToDegrees(new ExifInterface(uri.getPath()).getAttributeInt("Orientation", 1)));
            } catch (IOException e) {
            }
        }
        return 0.0f;
    }

    public Bitmap rotate(Bitmap b, int degrees) {
        if (degrees == 0 || b == null) {
            return b;
        }
        Matrix m = new Matrix();
        m.setRotate((float) degrees, ((float) b.getWidth()) / 2.0f, ((float) b.getHeight()) / 2.0f);
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
            if (b == b2) {
                return b;
            }
            b.recycle();
            return b2;
        } catch (OutOfMemoryError ex) {
            throw ex;
        }
    }

    private float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == 6) {
            return 90.0f;
        }
        if (exifOrientation == 3) {
            return 180.0f;
        }
        if (exifOrientation == 8) {
            return 270.0f;
        }
        return 0.0f;
    }

    public void faceDetected(Boolean detected) {
        if (detected.booleanValue()) {
            ((RelativeLayout) findViewById(R.id.loadingparent)).setVisibility(4);
            ((ImageView) findViewById(R.id.frontface)).setVisibility(4);
            ((LinearLayout) findViewById(R.id.fateffect)).setVisibility(0);
            fatBooth.setVisibility(0);
            return;
        }
        Toast.makeText(getApplicationContext(), "No face Detected ,Please select another image", 1).show();
        finish();
    }
    class C07771 implements OnClickListener {
        C07771() {
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fateffect:
                    findViewById(R.id.fateffect).setEnabled(false);
                    findViewById(R.id.waiting).setVisibility(0);
                    new MyTask().execute(new Void[0]);
                    return;
                default:
                    return;
            }
        }
    }

}
