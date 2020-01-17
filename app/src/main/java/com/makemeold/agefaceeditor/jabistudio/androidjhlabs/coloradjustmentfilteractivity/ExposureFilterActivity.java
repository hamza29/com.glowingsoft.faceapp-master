package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.ExposureFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ExposureFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int EXPOSURE_SEEKBAR_RESID = 21865;
    private static final String EXPOSURE_STRING = "EXPOSURE:";
    private static final int MAX_VALUE = 500;
    private static final String TITLE = "Exposure";
    private int[] mColors;
    private SeekBar mExposureSeekBar;
    private TextView mExposureTextView;
    private int mExposureValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mExposureTextView = new TextView(this);
        this.mExposureTextView.setText(new StringBuilder(EXPOSURE_STRING).append(this.mExposureValue).toString());
        this.mExposureTextView.setTextSize(22.0f);
        this.mExposureTextView.setTextColor(-16777216);
        this.mExposureTextView.setGravity(17);
        this.mExposureSeekBar = new SeekBar(this);
        this.mExposureSeekBar.setOnSeekBarChangeListener(this);
        this.mExposureSeekBar.setId(EXPOSURE_SEEKBAR_RESID);
        this.mExposureSeekBar.setMax(500);
        mainLayout.addView(this.mExposureTextView);
        mainLayout.addView(this.mExposureSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case EXPOSURE_SEEKBAR_RESID /*21865*/:
                this.mExposureValue = progress;
                this.mExposureTextView.setText(new StringBuilder(EXPOSURE_STRING).append(getExposure(this.mExposureValue)).toString());
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
                ExposureFilter filter = new ExposureFilter();
                filter.setExposure(ExposureFilterActivity.this.getExposure(ExposureFilterActivity.this.mExposureValue));
                ExposureFilterActivity.this.mColors = filter.filter(ExposureFilterActivity.this.mColors, width, height);
                ExposureFilterActivity exposureFilterActivity = ExposureFilterActivity.this;
                final int i = width;
                final int i2 = height;
                exposureFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ExposureFilterActivity.this.setModifyView(ExposureFilterActivity.this.mColors, i, i2);
                    }
                });
                ExposureFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getExposure(int value) {
        return ((float) value) / 100.0f;
    }
}
