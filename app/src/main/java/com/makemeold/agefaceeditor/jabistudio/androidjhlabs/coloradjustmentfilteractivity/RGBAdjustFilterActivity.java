package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.RGBAdjustFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class RGBAdjustFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int BFACTOR_SEEKBAR_RESID = 21865;
    private static final String BFACTOR_STRING = "BFACTOR:";
    private static final int GFACTOR_SEEKBAR_RESID = 21864;
    private static final String GFACTOR_STRING = "GFACTOR:";
    private static final int MAX_VALUE = 200;
    private static final int RFACTOR_SEEKBAR_RESID = 21863;
    private static final String RFACTOR_STRING = "RFACTOR:";
    private static final String TITLE = "RGBAdjust";
    private SeekBar mBFactorSeekBar;
    private TextView mBFactorTextView;
    private int mBFactorValue;
    private int[] mColors;
    private SeekBar mGFactorSeekBar;
    private TextView mGFactorTextView;
    private int mGFactorValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRFactorSeekBar;
    private TextView mRFactorTextView;
    private int mRFactorValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mRFactorTextView = new TextView(this);
        this.mRFactorTextView.setText(new StringBuilder(RFACTOR_STRING).append(this.mRFactorValue).toString());
        this.mRFactorTextView.setTextSize(22.0f);
        this.mRFactorTextView.setTextColor(-16777216);
        this.mRFactorTextView.setGravity(17);
        this.mRFactorSeekBar = new SeekBar(this);
        this.mRFactorSeekBar.setOnSeekBarChangeListener(this);
        this.mRFactorSeekBar.setId(RFACTOR_SEEKBAR_RESID);
        this.mRFactorSeekBar.setMax(200);
        this.mRFactorSeekBar.setProgress(100);
        this.mGFactorTextView = new TextView(this);
        this.mGFactorTextView.setText(new StringBuilder(GFACTOR_STRING).append(this.mGFactorValue).toString());
        this.mGFactorTextView.setTextSize(22.0f);
        this.mGFactorTextView.setTextColor(-16777216);
        this.mGFactorTextView.setGravity(17);
        this.mGFactorSeekBar = new SeekBar(this);
        this.mGFactorSeekBar.setOnSeekBarChangeListener(this);
        this.mGFactorSeekBar.setId(GFACTOR_SEEKBAR_RESID);
        this.mGFactorSeekBar.setMax(200);
        this.mGFactorSeekBar.setProgress(100);
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
        mainLayout.addView(this.mRFactorTextView);
        mainLayout.addView(this.mRFactorSeekBar);
        mainLayout.addView(this.mGFactorTextView);
        mainLayout.addView(this.mGFactorSeekBar);
        mainLayout.addView(this.mBFactorTextView);
        mainLayout.addView(this.mBFactorSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case RFACTOR_SEEKBAR_RESID /*21863*/:
                this.mRFactorValue = progress;
                this.mRFactorTextView.setText(new StringBuilder(RFACTOR_STRING).append(getRFactor(this.mRFactorValue)).toString());
                return;
            case GFACTOR_SEEKBAR_RESID /*21864*/:
                this.mGFactorValue = progress;
                this.mGFactorTextView.setText(new StringBuilder(GFACTOR_STRING).append(getGFactor(this.mGFactorValue)).toString());
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
                RGBAdjustFilter filter = new RGBAdjustFilter();
                filter.setRFactor(RGBAdjustFilterActivity.this.getRFactor(RGBAdjustFilterActivity.this.mRFactorValue));
                filter.setGFactor(RGBAdjustFilterActivity.this.getGFactor(RGBAdjustFilterActivity.this.mGFactorValue));
                filter.setBFactor(RGBAdjustFilterActivity.this.getBFactor(RGBAdjustFilterActivity.this.mBFactorValue));
                RGBAdjustFilterActivity.this.mColors = filter.filter(RGBAdjustFilterActivity.this.mColors, width, height);
                RGBAdjustFilterActivity rGBAdjustFilterActivity = RGBAdjustFilterActivity.this;
                final int i = width;
                final int i2 = height;
                rGBAdjustFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        RGBAdjustFilterActivity.this.setModifyView(RGBAdjustFilterActivity.this.mColors, i, i2);
                    }
                });
                RGBAdjustFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getRFactor(int value) {
        return ((float) (value - 100)) / 100.0f;
    }

    private float getGFactor(int value) {
        return ((float) (value - 100)) / 100.0f;
    }

    private float getBFactor(int value) {
        return ((float) (value - 100)) / 100.0f;
    }
}
