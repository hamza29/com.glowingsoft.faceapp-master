package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.EmbossFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class EmbossFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int BUMP_HEIGHT_SEEKBAR_RESID = 21865;
    private static final String BUMP_HEIGHT_STRING = "BUMP HEIGHT:";
    private static final int DIRECTION_MAX_VALUE = 624;
    private static final int DIRECTION_SEEKBAR_RESID = 21863;
    private static final String DIRECTION_STRING = "DIRECTION:";
    private static final String ELEVATION_STRING = "ELEVATION:";
    private static final int ELEVEATION_SEEKBAR_RESID = 21864;
    private static final int MAX_VALUE = 100;
    private static final String TITLE = "Emboss";
    private SeekBar mBumpHeightSeekBar;
    private TextView mBumpHeightTextView;
    private int mBumpHeightValue;
    private int[] mColors;
    private SeekBar mDirectionSeekBar;
    private TextView mDirectionTextView;
    private int mDirectionValue;
    private SeekBar mElevationSeekBar;
    private TextView mElevationTextView;
    private int mElevationValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mDirectionTextView = new TextView(this);
        this.mDirectionTextView.setText(new StringBuilder(DIRECTION_STRING).append(this.mDirectionValue).toString());
        this.mDirectionTextView.setTextSize(22.0f);
        this.mDirectionTextView.setTextColor(-16777216);
        this.mDirectionTextView.setGravity(17);
        this.mDirectionSeekBar = new SeekBar(this);
        this.mDirectionSeekBar.setOnSeekBarChangeListener(this);
        this.mDirectionSeekBar.setId(DIRECTION_SEEKBAR_RESID);
        this.mDirectionSeekBar.setMax(DIRECTION_MAX_VALUE);
        this.mElevationTextView = new TextView(this);
        this.mElevationTextView.setText(new StringBuilder(ELEVATION_STRING).append(this.mElevationValue).toString());
        this.mElevationTextView.setTextSize(22.0f);
        this.mElevationTextView.setTextColor(-16777216);
        this.mElevationTextView.setGravity(17);
        this.mElevationSeekBar = new SeekBar(this);
        this.mElevationSeekBar.setOnSeekBarChangeListener(this);
        this.mElevationSeekBar.setId(ELEVEATION_SEEKBAR_RESID);
        this.mElevationSeekBar.setMax(100);
        this.mBumpHeightTextView = new TextView(this);
        this.mBumpHeightTextView.setText(new StringBuilder(BUMP_HEIGHT_STRING).append(this.mBumpHeightValue).toString());
        this.mBumpHeightTextView.setTextSize(22.0f);
        this.mBumpHeightTextView.setTextColor(-16777216);
        this.mBumpHeightTextView.setGravity(17);
        this.mBumpHeightSeekBar = new SeekBar(this);
        this.mBumpHeightSeekBar.setOnSeekBarChangeListener(this);
        this.mBumpHeightSeekBar.setId(BUMP_HEIGHT_SEEKBAR_RESID);
        this.mBumpHeightSeekBar.setMax(100);
        mainLayout.addView(this.mDirectionTextView);
        mainLayout.addView(this.mDirectionSeekBar);
        mainLayout.addView(this.mElevationTextView);
        mainLayout.addView(this.mElevationSeekBar);
        mainLayout.addView(this.mBumpHeightTextView);
        mainLayout.addView(this.mBumpHeightSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case DIRECTION_SEEKBAR_RESID /*21863*/:
                this.mDirectionValue = progress;
                this.mDirectionTextView.setText(new StringBuilder(DIRECTION_STRING).append(getValue(this.mDirectionValue)).toString());
                return;
            case ELEVEATION_SEEKBAR_RESID /*21864*/:
                this.mElevationValue = progress;
                this.mElevationTextView.setText(new StringBuilder(ELEVATION_STRING).append(getValue(this.mElevationValue)).toString());
                return;
            case BUMP_HEIGHT_SEEKBAR_RESID /*21865*/:
                this.mBumpHeightValue = progress;
                this.mBumpHeightTextView.setText(new StringBuilder(BUMP_HEIGHT_STRING).append(getValue(this.mBumpHeightValue)).toString());
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
                EmbossFilter filter = new EmbossFilter();
                filter.setAzimuth(EmbossFilterActivity.this.getValue(EmbossFilterActivity.this.mDirectionValue));
                filter.setElevation(EmbossFilterActivity.this.getValue(EmbossFilterActivity.this.mElevationValue));
                filter.setBumpHeight(EmbossFilterActivity.this.getValue(EmbossFilterActivity.this.mBumpHeightValue));
                EmbossFilterActivity.this.mColors = filter.filter(EmbossFilterActivity.this.mColors, width, height);
                EmbossFilterActivity embossFilterActivity = EmbossFilterActivity.this;
                final int i = width;
                final int i2 = height;
                embossFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        EmbossFilterActivity.this.setModifyView(EmbossFilterActivity.this.mColors, i, i2);
                    }
                });
                EmbossFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
