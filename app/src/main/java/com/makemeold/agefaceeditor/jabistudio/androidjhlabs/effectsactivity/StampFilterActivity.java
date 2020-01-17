package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.StampFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class StampFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int COLOR_MAX_VALUE = 16777215;
    private static final int LOW_COLOR_SEEKBAR_RESID = 21866;
    private static final String LOW_COLOR_STRING = "LOW_COLOR:";
    private static final int MAX_VALUE = 100;
    private static final int RADIUS_SEEKBAR_RESID = 21863;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final int SOFTNESS_SEEKBAR_RESID = 21865;
    private static final String SOFTNESS_STRING = "SOFTNESS:";
    private static final int THRESHOLD_SEEKBAR_RESID = 21864;
    private static final String THRESHOLD_STRING = "THRESHOLD:";
    private static final String TITLE = "Stamp";
    private static final int UPPER_COLOR_SEEKBAR_RESID = 21867;
    private static final String UPPER_COLOR_STRING = "UPPER_COLOR:";
    private int[] mColors;
    private SeekBar mLowColorSeekBar;
    private TextView mLowColorTextView;
    private int mLowColorValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private int mRadiusValue;
    private SeekBar mSoftnessSeekBar;
    private TextView mSoftnessTextView;
    private int mSoftnessValue;
    private SeekBar mThresholdSeekBar;
    private TextView mThresholdTextView;
    private int mThresholdValue;
    private SeekBar mUpperColorSeekBar;
    private TextView mUpperColorTextView;
    private int mUpperColorValue;

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
        this.mThresholdTextView = new TextView(this);
        this.mThresholdTextView.setText(new StringBuilder(THRESHOLD_STRING).append(this.mThresholdValue).toString());
        this.mThresholdTextView.setTextSize(22.0f);
        this.mThresholdTextView.setTextColor(-16777216);
        this.mThresholdTextView.setGravity(17);
        this.mThresholdSeekBar = new SeekBar(this);
        this.mThresholdSeekBar.setOnSeekBarChangeListener(this);
        this.mThresholdSeekBar.setId(THRESHOLD_SEEKBAR_RESID);
        this.mThresholdSeekBar.setMax(100);
        this.mSoftnessTextView = new TextView(this);
        this.mSoftnessTextView.setText(new StringBuilder(SOFTNESS_STRING).append(this.mSoftnessValue).toString());
        this.mSoftnessTextView.setTextSize(22.0f);
        this.mSoftnessTextView.setTextColor(-16777216);
        this.mSoftnessTextView.setGravity(17);
        this.mSoftnessSeekBar = new SeekBar(this);
        this.mSoftnessSeekBar.setOnSeekBarChangeListener(this);
        this.mSoftnessSeekBar.setId(SOFTNESS_SEEKBAR_RESID);
        this.mSoftnessSeekBar.setMax(100);
        this.mLowColorTextView = new TextView(this);
        this.mLowColorTextView.setText(new StringBuilder(LOW_COLOR_STRING).append(this.mLowColorValue).toString());
        this.mLowColorTextView.setTextSize(22.0f);
        this.mLowColorTextView.setTextColor(-16777216);
        this.mLowColorTextView.setGravity(17);
        this.mLowColorSeekBar = new SeekBar(this);
        this.mLowColorSeekBar.setOnSeekBarChangeListener(this);
        this.mLowColorSeekBar.setId(LOW_COLOR_SEEKBAR_RESID);
        this.mLowColorSeekBar.setMax(16777215);
        this.mUpperColorTextView = new TextView(this);
        this.mUpperColorTextView.setText(new StringBuilder(UPPER_COLOR_STRING).append(this.mUpperColorValue).toString());
        this.mUpperColorTextView.setTextSize(22.0f);
        this.mUpperColorTextView.setTextColor(-16777216);
        this.mUpperColorTextView.setGravity(17);
        this.mUpperColorSeekBar = new SeekBar(this);
        this.mUpperColorSeekBar.setOnSeekBarChangeListener(this);
        this.mUpperColorSeekBar.setId(UPPER_COLOR_SEEKBAR_RESID);
        this.mUpperColorSeekBar.setMax(16777215);
        this.mUpperColorSeekBar.setProgress(16777215);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
        mainLayout.addView(this.mThresholdTextView);
        mainLayout.addView(this.mThresholdSeekBar);
        mainLayout.addView(this.mSoftnessTextView);
        mainLayout.addView(this.mSoftnessSeekBar);
        mainLayout.addView(this.mLowColorTextView);
        mainLayout.addView(this.mLowColorSeekBar);
        mainLayout.addView(this.mUpperColorTextView);
        mainLayout.addView(this.mUpperColorSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case RADIUS_SEEKBAR_RESID /*21863*/:
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
                return;
            case THRESHOLD_SEEKBAR_RESID /*21864*/:
                this.mThresholdValue = progress;
                this.mThresholdTextView.setText(new StringBuilder(THRESHOLD_STRING).append(getValue(this.mThresholdValue)).toString());
                return;
            case SOFTNESS_SEEKBAR_RESID /*21865*/:
                this.mSoftnessValue = progress;
                this.mSoftnessTextView.setText(new StringBuilder(SOFTNESS_STRING).append(getValue(this.mSoftnessValue)).toString());
                return;
            case LOW_COLOR_SEEKBAR_RESID /*21866*/:
                this.mLowColorValue = progress;
                this.mLowColorTextView.setText(new StringBuilder(LOW_COLOR_STRING).append(getColorValue(this.mLowColorValue)).toString());
                return;
            case UPPER_COLOR_SEEKBAR_RESID /*21867*/:
                this.mUpperColorValue = progress;
                this.mUpperColorTextView.setText(new StringBuilder(UPPER_COLOR_STRING).append(getColorValue(this.mUpperColorValue)).toString());
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
                StampFilter filter = new StampFilter();
                filter.setRadius((float) StampFilterActivity.this.mRadiusValue);
                filter.setThreshold(StampFilterActivity.this.getValue(StampFilterActivity.this.mThresholdValue));
                filter.setSoftness(StampFilterActivity.this.getValue(StampFilterActivity.this.mSoftnessValue));
                filter.setBlack(StampFilterActivity.this.getColorValue(StampFilterActivity.this.mLowColorValue));
                filter.setWhite(StampFilterActivity.this.getColorValue(StampFilterActivity.this.mUpperColorValue));
                StampFilterActivity.this.mColors = filter.filter(StampFilterActivity.this.mColors, width, height);
                StampFilterActivity stampFilterActivity = StampFilterActivity.this;
                final int i = width;
                final int i2 = height;
                stampFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        StampFilterActivity.this.setModifyView(StampFilterActivity.this.mColors, i, i2);
                    }
                });
                StampFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }

    private int getColorValue(int value) {
        return value - 16777216;
    }
}
