package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.ContrastFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ContrastFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int BRIGHTNESS_SEEKBAR_RESID = 21863;
    private static final String BRIGHTNESS_STRING = "BRIGHTNESS:";
    private static final int CONTRAST_SEEKBAR_RESID = 21864;
    private static final String CONTRAST_STRING = "CONTRAST:";
    private static final int MAX_VALUE = 200;
    private static final String TITLE = "Contrast";
    private SeekBar mBrightnessSeekBar;
    private TextView mBrightnessTextView;
    private int mBrightnessValue;
    private int[] mColors;
    private SeekBar mContrastSeekBar;
    private TextView mContrastTextView;
    private int mContrastValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mBrightnessTextView = new TextView(this);
        this.mBrightnessTextView.setText(new StringBuilder(BRIGHTNESS_STRING).append(getValue(this.mBrightnessValue)).toString());
        this.mBrightnessTextView.setTextSize(22.0f);
        this.mBrightnessTextView.setTextColor(-16777216);
        this.mBrightnessTextView.setGravity(17);
        this.mBrightnessSeekBar = new SeekBar(this);
        this.mBrightnessSeekBar.setOnSeekBarChangeListener(this);
        this.mBrightnessSeekBar.setId(BRIGHTNESS_SEEKBAR_RESID);
        this.mBrightnessSeekBar.setMax(200);
        this.mBrightnessSeekBar.setProgress(100);
        this.mContrastTextView = new TextView(this);
        this.mContrastTextView.setText(new StringBuilder(CONTRAST_STRING).append(getValue(this.mContrastValue)).toString());
        this.mContrastTextView.setTextSize(22.0f);
        this.mContrastTextView.setTextColor(-16777216);
        this.mContrastTextView.setGravity(17);
        this.mContrastSeekBar = new SeekBar(this);
        this.mContrastSeekBar.setOnSeekBarChangeListener(this);
        this.mContrastSeekBar.setId(CONTRAST_SEEKBAR_RESID);
        this.mContrastSeekBar.setMax(200);
        this.mContrastSeekBar.setProgress(100);
        mainLayout.addView(this.mBrightnessTextView);
        mainLayout.addView(this.mBrightnessSeekBar);
        mainLayout.addView(this.mContrastTextView);
        mainLayout.addView(this.mContrastSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case BRIGHTNESS_SEEKBAR_RESID /*21863*/:
                this.mBrightnessValue = progress;
                this.mBrightnessTextView.setText(new StringBuilder(BRIGHTNESS_STRING).append(this.mBrightnessValue).toString());
                return;
            case CONTRAST_SEEKBAR_RESID /*21864*/:
                this.mContrastValue = progress;
                this.mContrastTextView.setText(new StringBuilder(CONTRAST_STRING).append(this.mContrastValue).toString());
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
                ContrastFilter filter = new ContrastFilter();
                filter.setBrightness(ContrastFilterActivity.this.getValue(ContrastFilterActivity.this.mBrightnessValue));
                filter.setContrast(ContrastFilterActivity.this.getValue(ContrastFilterActivity.this.mContrastValue));
                ContrastFilterActivity.this.mColors = filter.filter(ContrastFilterActivity.this.mColors, width, height);
                ContrastFilterActivity contrastFilterActivity = ContrastFilterActivity.this;
                final int i = width;
                final int i2 = height;
                contrastFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ContrastFilterActivity.this.setModifyView(ContrastFilterActivity.this.mColors, i, i2);
                    }
                });
                ContrastFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
