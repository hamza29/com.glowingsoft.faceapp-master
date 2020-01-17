package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.HSBAdjustFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class HSBAdjustFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int BFACTOR_SEEKBAR_RESID = 21865;
    private static final String BFACTOR_STRING = "BFACTOR:";
    private static final int HFACTOR_SEEKBAR_RESID = 21863;
    private static final String HFACTOR_STRING = "HFACTOR:";
    private static final int MAX_VALUE = 200;
    private static final int SFACTOR_SEEKBAR_RESID = 21864;
    private static final String SFACTOR_STRING = "SFACTOR:";
    private static final String TITLE = "HSBAdjust";
    private SeekBar mBFactorSeekBar;
    private TextView mBFactorTextView;
    private int mBFactorValue;
    private int[] mColors;
    private SeekBar mHFactorSeekBar;
    private TextView mHFactorTextView;
    private int mHFactorValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mSFactorSeekBar;
    private TextView mSFactorTextView;
    private int mSFactorValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mHFactorTextView = new TextView(this);
        this.mHFactorTextView.setText(new StringBuilder(HFACTOR_STRING).append(this.mHFactorValue).toString());
        this.mHFactorTextView.setTextSize(22.0f);
        this.mHFactorTextView.setTextColor(-16777216);
        this.mHFactorTextView.setGravity(17);
        this.mHFactorSeekBar = new SeekBar(this);
        this.mHFactorSeekBar.setOnSeekBarChangeListener(this);
        this.mHFactorSeekBar.setId(HFACTOR_SEEKBAR_RESID);
        this.mHFactorSeekBar.setMax(200);
        this.mHFactorSeekBar.setProgress(100);
        this.mSFactorTextView = new TextView(this);
        this.mSFactorTextView.setText(new StringBuilder(SFACTOR_STRING).append(this.mSFactorValue).toString());
        this.mSFactorTextView.setTextSize(22.0f);
        this.mSFactorTextView.setTextColor(-16777216);
        this.mSFactorTextView.setGravity(17);
        this.mSFactorSeekBar = new SeekBar(this);
        this.mSFactorSeekBar.setOnSeekBarChangeListener(this);
        this.mSFactorSeekBar.setId(SFACTOR_SEEKBAR_RESID);
        this.mSFactorSeekBar.setMax(200);
        this.mSFactorSeekBar.setProgress(100);
        this.mBFactorTextView = new TextView(this);
        this.mBFactorTextView.setText(new StringBuilder(BFACTOR_STRING).append(this.mBFactorValue).toString());
        this.mBFactorTextView.setTextSize(22.0f);
        this.mBFactorTextView.setTextColor(-16777216);
        this.mBFactorTextView.setGravity(17);
        this.mBFactorSeekBar = new SeekBar(this);
        this.mBFactorSeekBar.setOnSeekBarChangeListener(this);
        this.mBFactorSeekBar.setId(BFACTOR_SEEKBAR_RESID);
        this.mBFactorSeekBar.setMax(200);
        this.mBFactorSeekBar.setProgress(100);
        mainLayout.addView(this.mHFactorTextView);
        mainLayout.addView(this.mHFactorSeekBar);
        mainLayout.addView(this.mSFactorTextView);
        mainLayout.addView(this.mSFactorSeekBar);
        mainLayout.addView(this.mBFactorTextView);
        mainLayout.addView(this.mBFactorSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case HFACTOR_SEEKBAR_RESID /*21863*/:
                this.mHFactorValue = progress;
                this.mHFactorTextView.setText(new StringBuilder(HFACTOR_STRING).append(getHFactor(this.mHFactorValue)).toString());
                return;
            case SFACTOR_SEEKBAR_RESID /*21864*/:
                this.mSFactorValue = progress;
                this.mSFactorTextView.setText(new StringBuilder(SFACTOR_STRING).append(getSFactor(this.mSFactorValue)).toString());
                return;
            case BFACTOR_SEEKBAR_RESID /*21865*/:
                this.mBFactorValue = progress;
                this.mBFactorTextView.setText(new StringBuilder(BFACTOR_STRING).append(getBFactor(this.mBFactorValue)).toString());
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
                HSBAdjustFilter filter = new HSBAdjustFilter();
                filter.setHFactor(HSBAdjustFilterActivity.this.getHFactor(HSBAdjustFilterActivity.this.mHFactorValue));
                filter.setSFactor(HSBAdjustFilterActivity.this.getSFactor(HSBAdjustFilterActivity.this.mSFactorValue));
                filter.setBFactor(HSBAdjustFilterActivity.this.getBFactor(HSBAdjustFilterActivity.this.mBFactorValue));
                HSBAdjustFilterActivity.this.mColors = filter.filter(HSBAdjustFilterActivity.this.mColors, width, height);
                HSBAdjustFilterActivity hSBAdjustFilterActivity = HSBAdjustFilterActivity.this;
                final int i = width;
                final int i2 = height;
                hSBAdjustFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        HSBAdjustFilterActivity.this.setModifyView(HSBAdjustFilterActivity.this.mColors, i, i2);
                    }
                });
                HSBAdjustFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getHFactor(int value) {
        return (float) (value - 100);
    }

    private float getSFactor(int value) {
        return ((float) (value - 100)) / 100.0f;
    }

    private float getBFactor(int value) {
        return ((float) (value - 100)) / 100.0f;
    }
}
