package com.makemeold.agefaceeditor.jabistudio.androidjhlabs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SuperFilterActivity extends Activity {
    protected static final int TITLE_TEXT_SIZE = 22;
    protected Bitmap mFilterBitmap;
    protected LinearLayout mMainLayout;
    protected ImageView mModifyImageView;
    protected ImageView mOriginalImageView;
    private ScrollView mScrollView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mScrollView = new ScrollView(this);
        this.mMainLayout = new LinearLayout(this);
        this.mMainLayout.setOrientation(1);
        orignalLayoutSetup(this.mMainLayout);
        modifyLayoutSetup(this.mMainLayout);
        this.mScrollView.addView(this.mMainLayout);
        setContentView(this.mScrollView);
    }

    private void orignalLayoutSetup(LinearLayout mainLayout) {
        LinearLayout originalLayout = new LinearLayout(this);
        originalLayout.setOrientation(1);
        TextView originalTitleTextVeiw = new TextView(this);
        originalTitleTextVeiw.setText(C0666R.string.original_image);
        originalTitleTextVeiw.setTextSize(22.0f);
        originalTitleTextVeiw.setTextColor(-16777216);
        originalTitleTextVeiw.setGravity(17);
        this.mOriginalImageView = new ImageView(this);
        this.mOriginalImageView.setImageResource(C0666R.drawable.image);
        originalLayout.addView(originalTitleTextVeiw);
        originalLayout.addView(this.mOriginalImageView);
        mainLayout.addView(originalLayout);
    }

    private void modifyLayoutSetup(LinearLayout mainLayout) {
        LinearLayout modifyLayout = new LinearLayout(this);
        modifyLayout.setOrientation(1);
        TextView modifyTitleTextVeiw = new TextView(this);
        modifyTitleTextVeiw.setText(C0666R.string.modify_image);
        modifyTitleTextVeiw.setTextSize(22.0f);
        modifyTitleTextVeiw.setTextColor(-16777216);
        modifyTitleTextVeiw.setGravity(17);
        this.mModifyImageView = new ImageView(this);
        this.mModifyImageView.setImageResource(C0666R.drawable.image);
        modifyLayout.addView(modifyTitleTextVeiw);
        modifyLayout.addView(this.mModifyImageView);
        mainLayout.addView(modifyLayout);
    }

    protected void setModifyView(int[] colors, int width, int height) {
        this.mModifyImageView.setWillNotDraw(true);
        if (this.mFilterBitmap != null) {
            this.mFilterBitmap.recycle();
            this.mFilterBitmap = null;
        }
        this.mFilterBitmap = Bitmap.createBitmap(colors, 0, width, width, height, Config.ARGB_8888);
        this.mModifyImageView.setImageBitmap(this.mFilterBitmap);
        this.mModifyImageView.setWillNotDraw(false);
        this.mModifyImageView.postInvalidate();
    }

    protected void onDestroy() {
        if (this.mFilterBitmap != null) {
            this.mFilterBitmap.recycle();
            this.mFilterBitmap = null;
        }
        super.onDestroy();
    }
}
