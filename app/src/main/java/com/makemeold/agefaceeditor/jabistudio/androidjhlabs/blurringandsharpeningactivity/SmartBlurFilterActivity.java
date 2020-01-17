package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.SmartBlurFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class SmartBlurFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int MAX_VALUE = 100;
    private static final int RADIUS_SEEKBAR_RESID = 21865;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final int THRESHOLD_SEEKBAR_RESID = 21866;
    private static final String THRESHOLD_STRING = "THRESHOLD:";
    private static final String TITLE = "SmartBlur";
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private int mRadiusValue;
    private SeekBar mThresHoldSeekBar;
    private TextView mThresHoldTextView;
    private int mThresHoldValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mRadiusTextView = new TextView(this);
        this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
        this.mRadiusTextView.setTextSize(22.0f);
        this.mRadiusTextView.setTextColor(-16777216);
        this.mRadiusTextView.setGravity(17);
        this.mRadiusSeekBar = new SeekBar(this);
        this.mRadiusSeekBar.setOnSeekBarChangeListener(this);
        this.mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        this.mRadiusSeekBar.setMax(100);
        this.mThresHoldTextView = new TextView(this);
        this.mThresHoldTextView.setText(new StringBuilder(THRESHOLD_STRING).append(this.mThresHoldValue).toString());
        this.mThresHoldTextView.setTextSize(22.0f);
        this.mThresHoldTextView.setTextColor(-16777216);
        this.mThresHoldTextView.setGravity(17);
        this.mThresHoldSeekBar = new SeekBar(this);
        this.mThresHoldSeekBar.setOnSeekBarChangeListener(this);
        this.mThresHoldSeekBar.setId(THRESHOLD_SEEKBAR_RESID);
        this.mThresHoldSeekBar.setMax(100);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
        mainLayout.addView(this.mThresHoldTextView);
        mainLayout.addView(this.mThresHoldSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case RADIUS_SEEKBAR_RESID /*21865*/:
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
                return;
            case THRESHOLD_SEEKBAR_RESID /*21866*/:
                this.mThresHoldValue = progress;
                this.mThresHoldTextView.setText(new StringBuilder(THRESHOLD_STRING).append(this.mThresHoldValue).toString());
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
            public void run() {
                SmartBlurFilter filter = new SmartBlurFilter();
                filter.setRadius(SmartBlurFilterActivity.this.mRadiusValue);
                filter.setThreshold(SmartBlurFilterActivity.this.mThresHoldValue);
                SmartBlurFilterActivity.this.mColors = filter.filter(SmartBlurFilterActivity.this.mColors, width, height);
                SmartBlurFilterActivity smartBlurFilterActivity = SmartBlurFilterActivity.this;
                final int i = width;
                final int i2 = height;
                smartBlurFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        SmartBlurFilterActivity.this.setModifyView(SmartBlurFilterActivity.this.mColors, i, i2);
                    }
                });
                SmartBlurFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
