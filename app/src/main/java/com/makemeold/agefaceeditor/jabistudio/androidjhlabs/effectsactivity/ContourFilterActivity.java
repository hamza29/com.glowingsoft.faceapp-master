package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.ContourFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ContourFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int LEVEL_MAX_VALUE = 30;
    private static final int LEVEL_SEEKBAR_RESID = 21865;
    private static final String LEVEL_STRING = "LEVEL:";
    private static final int MAX_VALUE = 100;
    private static final int OFFSET_SEEKBAR_RESID = 21866;
    private static final String OFFSET_STRING = "OFFSET:";
    private static final int SCALE_SEEKBAR_RESID = 21867;
    private static final String SCALE_STRING = "SCALE:";
    private static final String TITLE = "Contour";
    private int[] mColors;
    private SeekBar mLevelSeekBar;
    private TextView mLevelTextView;
    private int mLevelValue;
    private SeekBar mOffsetSeekBar;
    private TextView mOffsetTextView;
    private int mOffsetValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    private int mScaleValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mLevelTextView = new TextView(this);
        this.mLevelTextView.setText(new StringBuilder(LEVEL_STRING).append(this.mLevelValue).toString());
        this.mLevelTextView.setTextSize(22.0f);
        this.mLevelTextView.setTextColor(-16777216);
        this.mLevelTextView.setGravity(17);
        this.mLevelSeekBar = new SeekBar(this);
        this.mLevelSeekBar.setOnSeekBarChangeListener(this);
        this.mLevelSeekBar.setId(LEVEL_SEEKBAR_RESID);
        this.mLevelSeekBar.setMax(30);
        this.mOffsetTextView = new TextView(this);
        this.mOffsetTextView.setText(new StringBuilder(OFFSET_STRING).append(this.mOffsetValue).toString());
        this.mOffsetTextView.setTextSize(22.0f);
        this.mOffsetTextView.setTextColor(-16777216);
        this.mOffsetTextView.setGravity(17);
        this.mOffsetSeekBar = new SeekBar(this);
        this.mOffsetSeekBar.setOnSeekBarChangeListener(this);
        this.mOffsetSeekBar.setId(OFFSET_SEEKBAR_RESID);
        this.mOffsetSeekBar.setMax(100);
        this.mScaleTextView = new TextView(this);
        this.mScaleTextView.setText(new StringBuilder(SCALE_STRING).append(this.mScaleValue).toString());
        this.mScaleTextView.setTextSize(22.0f);
        this.mScaleTextView.setTextColor(-16777216);
        this.mScaleTextView.setGravity(17);
        this.mScaleSeekBar = new SeekBar(this);
        this.mScaleSeekBar.setOnSeekBarChangeListener(this);
        this.mScaleSeekBar.setId(SCALE_SEEKBAR_RESID);
        this.mScaleSeekBar.setMax(100);
        mainLayout.addView(this.mLevelTextView);
        mainLayout.addView(this.mLevelSeekBar);
        mainLayout.addView(this.mOffsetTextView);
        mainLayout.addView(this.mOffsetSeekBar);
        mainLayout.addView(this.mScaleTextView);
        mainLayout.addView(this.mScaleSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case LEVEL_SEEKBAR_RESID /*21865*/:
                this.mLevelValue = progress;
                this.mLevelTextView.setText(new StringBuilder(LEVEL_STRING).append(this.mLevelValue).toString());
                return;
            case OFFSET_SEEKBAR_RESID /*21866*/:
                this.mOffsetValue = progress;
                this.mOffsetTextView.setText(new StringBuilder(OFFSET_STRING).append(getValue(this.mOffsetValue)).toString());
                return;
            case SCALE_SEEKBAR_RESID /*21867*/:
                this.mScaleValue = progress;
                this.mScaleTextView.setText(new StringBuilder(SCALE_STRING).append(getValue(this.mScaleValue)).toString());
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
                ContourFilter filter = new ContourFilter();
                filter.setLevels((float) ContourFilterActivity.this.mLevelValue);
                filter.setOffset(ContourFilterActivity.this.getValue(ContourFilterActivity.this.mOffsetValue));
                filter.setScale(ContourFilterActivity.this.getValue(ContourFilterActivity.this.mScaleValue));
                ContourFilterActivity.this.mColors = filter.filter(ContourFilterActivity.this.mColors, width, height);
                ContourFilterActivity contourFilterActivity = ContourFilterActivity.this;
                final int i = width;
                final int i2 = height;
                contourFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ContourFilterActivity.this.setModifyView(ContourFilterActivity.this.mColors, i, i2);
                    }
                });
                ContourFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
