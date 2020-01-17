package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.OilFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class OilFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int LEVEL_SEEKBAR_RESID = 21865;
    private static final String LEVEL_STRING = "LEVEL:";
    private static final int MAX_VALUE = 10;
    private static final int RANGE_SEEKBAR_RESID = 21866;
    private static final String RANGE_STRING = "RANGE:";
    private static final String TITLE = "Oil";
    private int[] mColors;
    private SeekBar mLevelSeekBar;
    private TextView mLevelTextView;
    private int mLevelValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRangeSeekBar;
    private TextView mRangeTextView;
    private int mRangeValue;

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
        this.mLevelSeekBar.setMax(10);
        this.mRangeTextView = new TextView(this);
        this.mRangeTextView.setText(new StringBuilder(RANGE_STRING).append(this.mRangeValue).toString());
        this.mRangeTextView.setTextSize(22.0f);
        this.mRangeTextView.setTextColor(-16777216);
        this.mRangeTextView.setGravity(17);
        this.mRangeSeekBar = new SeekBar(this);
        this.mRangeSeekBar.setOnSeekBarChangeListener(this);
        this.mRangeSeekBar.setId(RANGE_SEEKBAR_RESID);
        this.mRangeSeekBar.setMax(10);
        mainLayout.addView(this.mLevelTextView);
        mainLayout.addView(this.mLevelSeekBar);
        mainLayout.addView(this.mRangeTextView);
        mainLayout.addView(this.mRangeSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case LEVEL_SEEKBAR_RESID /*21865*/:
                this.mLevelValue = progress;
                this.mLevelTextView.setText(new StringBuilder(LEVEL_STRING).append(this.mLevelValue).toString());
                return;
            case RANGE_SEEKBAR_RESID /*21866*/:
                this.mRangeValue = progress;
                this.mRangeTextView.setText(new StringBuilder(RANGE_STRING).append(this.mRangeValue).toString());
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
                OilFilter filter = new OilFilter();
                filter.setLevels(OilFilterActivity.this.mLevelValue);
                filter.setRange(OilFilterActivity.this.mRangeValue);
                OilFilterActivity.this.mColors = filter.filter(OilFilterActivity.this.mColors, width, height);
                OilFilterActivity oilFilterActivity = OilFilterActivity.this;
                final int i = width;
                final int i2 = height;
                oilFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        OilFilterActivity.this.setModifyView(OilFilterActivity.this.mColors, i, i2);
                    }
                });
                OilFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
