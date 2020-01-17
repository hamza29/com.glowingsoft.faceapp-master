package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.C0666R;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.ScaleFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ScaleFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int HEIGHT_SEEKBAR_RESID = 21867;
    private static final String HEIGHT_STRING = "HEIGHT:";
    private static final String TITLE = "Scale";
    private static final int WIDTH_SEEKBAR_RESID = 21865;
    private static final String WIDTH_STRING = "WIDTH:";
    private int[] mColors;
    private SeekBar mHeightSeekBar;
    private TextView mHeightTextView;
    private int mHeightValue;
    private int mMaxHeight;
    private int mMaxWidth;
    private ProgressDialog mProgressDialog;
    private SeekBar mWidthSeekBar;
    private TextView mWidthTextView;
    private int mWidthValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        this.mMaxWidth = AndroidUtils.getBitmapOfWidth(getResources(), C0666R.drawable.image);
        this.mMaxHeight = AndroidUtils.getBitmapOfHeight(getResources(), C0666R.drawable.image);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mWidthTextView = new TextView(this);
        this.mWidthTextView.setText(new StringBuilder(WIDTH_STRING).append(this.mWidthValue).toString());
        this.mWidthTextView.setTextSize(22.0f);
        this.mWidthTextView.setTextColor(-16777216);
        this.mWidthTextView.setGravity(17);
        this.mWidthSeekBar = new SeekBar(this);
        this.mWidthSeekBar.setOnSeekBarChangeListener(this);
        this.mWidthSeekBar.setId(WIDTH_SEEKBAR_RESID);
        this.mWidthSeekBar.setMax(this.mMaxWidth * 2);
        this.mWidthSeekBar.setProgress(this.mMaxWidth);
        this.mHeightTextView = new TextView(this);
        this.mHeightTextView.setText(new StringBuilder(HEIGHT_STRING).append(this.mHeightValue).toString());
        this.mHeightTextView.setTextSize(22.0f);
        this.mHeightTextView.setTextColor(-16777216);
        this.mHeightTextView.setGravity(17);
        this.mHeightSeekBar = new SeekBar(this);
        this.mHeightSeekBar.setOnSeekBarChangeListener(this);
        this.mHeightSeekBar.setId(HEIGHT_SEEKBAR_RESID);
        this.mHeightSeekBar.setMax(this.mMaxHeight * 2);
        this.mHeightSeekBar.setProgress(this.mMaxHeight);
        mainLayout.addView(this.mWidthTextView);
        mainLayout.addView(this.mWidthSeekBar);
        mainLayout.addView(this.mHeightTextView);
        mainLayout.addView(this.mHeightSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case WIDTH_SEEKBAR_RESID /*21865*/:
                this.mWidthValue = progress;
                this.mWidthTextView.setText(new StringBuilder(WIDTH_STRING).append(getValue(this.mWidthValue)).toString());
                return;
            case HEIGHT_SEEKBAR_RESID /*21867*/:
                this.mHeightValue = progress;
                this.mHeightTextView.setText(new StringBuilder(HEIGHT_STRING).append(getValue(this.mHeightValue)).toString());
                return;
            default:
                return;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        final int width = this.mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = this.mOriginalImageView.getDrawable().getIntrinsicHeight();
        this.mColors = AndroidUtils.drawableToIntArray(this.mOriginalImageView.getDrawable());
        this.mProgressDialog = ProgressDialog.show(this, BuildConfig.FLAVOR, "Wait......");
        Thread thread = new Thread() {


            class C07391 implements Runnable {
                C07391() {
                }

                public void run() {
                    ScaleFilterActivity.this.setModifyView(ScaleFilterActivity.this.mColors, ScaleFilterActivity.this.getValue(ScaleFilterActivity.this.mWidthValue), ScaleFilterActivity.this.getValue(ScaleFilterActivity.this.mHeightValue));
                }
            }

            public void run() {
                ScaleFilterActivity.this.mColors = new ScaleFilter(ScaleFilterActivity.this.getValue(ScaleFilterActivity.this.mWidthValue), ScaleFilterActivity.this.getValue(ScaleFilterActivity.this.mHeightValue)).filter(ScaleFilterActivity.this.mColors, width, height);
                ScaleFilterActivity.this.runOnUiThread(new C07391());
                ScaleFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private int getValue(int value) {
        if (value <= 0) {
            return 1;
        }
        return value;
    }
}
