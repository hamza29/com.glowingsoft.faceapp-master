package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.DissolveFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class DissolveFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int DENSITY_SEEKBAR_RESID = 21864;
    private static final String DENSITY_STRING = "DENSITY:";
    private static final int MAX_VALUE = 102;
    private static final int SOFTNESS_SEEKBAR_RESID = 21865;
    private static final String SOFTNESS_STRING = "SOFTNESS:";
    private static final String TITLE = "Dissolve";
    private int[] mColors;
    private SeekBar mDensitySeekBar;
    private TextView mDensityTextView;
    private int mDensityValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mSoftnessSeekBar;
    private TextView mSoftnessTextView;
    private int mSoftnessValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mDensityTextView = new TextView(this);
        this.mDensityTextView.setText(new StringBuilder(DENSITY_STRING).append(this.mDensityValue).toString());
        this.mDensityTextView.setTextSize(22.0f);
        this.mDensityTextView.setTextColor(-16777216);
        this.mDensityTextView.setGravity(17);
        this.mDensitySeekBar = new SeekBar(this);
        this.mDensitySeekBar.setOnSeekBarChangeListener(this);
        this.mDensitySeekBar.setId(DENSITY_SEEKBAR_RESID);
        this.mDensitySeekBar.setMax(102);
        this.mSoftnessTextView = new TextView(this);
        this.mSoftnessTextView.setText(new StringBuilder(SOFTNESS_STRING).append(this.mSoftnessValue).toString());
        this.mSoftnessTextView.setTextSize(22.0f);
        this.mSoftnessTextView.setTextColor(-16777216);
        this.mSoftnessTextView.setGravity(17);
        this.mSoftnessSeekBar = new SeekBar(this);
        this.mSoftnessSeekBar.setOnSeekBarChangeListener(this);
        this.mSoftnessSeekBar.setId(SOFTNESS_SEEKBAR_RESID);
        this.mSoftnessSeekBar.setMax(102);
        mainLayout.addView(this.mDensityTextView);
        mainLayout.addView(this.mDensitySeekBar);
        mainLayout.addView(this.mSoftnessTextView);
        mainLayout.addView(this.mSoftnessSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case DENSITY_SEEKBAR_RESID /*21864*/:
                this.mDensityValue = progress;
                this.mDensityTextView.setText(new StringBuilder(DENSITY_STRING).append(getValue(this.mDensityValue)).toString());
                return;
            case SOFTNESS_SEEKBAR_RESID /*21865*/:
                this.mSoftnessValue = progress;
                this.mSoftnessTextView.setText(new StringBuilder(SOFTNESS_STRING).append(getValue(this.mSoftnessValue)).toString());
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
                DissolveFilter filter = new DissolveFilter();
                filter.setDensity(DissolveFilterActivity.this.getValue(DissolveFilterActivity.this.mDensityValue));
                filter.setSoftness(DissolveFilterActivity.this.getValue(DissolveFilterActivity.this.mSoftnessValue));
                DissolveFilterActivity.this.mColors = filter.filter(DissolveFilterActivity.this.mColors, width, height);
                DissolveFilterActivity dissolveFilterActivity = DissolveFilterActivity.this;
                final int i = width;
                final int i2 = height;
                dissolveFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        DissolveFilterActivity.this.setModifyView(DissolveFilterActivity.this.mColors, i, i2);
                    }
                });
                DissolveFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
