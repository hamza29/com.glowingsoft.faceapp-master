package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.LevelsFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class LevelsFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int HIGHLEVEL_SEEKBAR_RESID = 21864;
    private static final String HIGHLEVEL_STRING = "HIGHLEVEL:";
    private static final int HIGHOUTPUT_SEEKBAR_RESID = 21866;
    private static final String HIGHOUTPUT_STRING = "HIGHOUTPUT:";
    private static final int LOWLEVEL_SEEKBAR_RESID = 21863;
    private static final String LOWLEVEL_STRING = "LOWLEVEL:";
    private static final int LOWOUTPUT_SEEKBAR_RESID = 21865;
    private static final String LOWOUTPUT_STRING = "LOWOUTPUT:";
    private static final int MAX_VALUE = 100;
    private static final String TITLE = "Levels";
    private int[] mColors;
    private SeekBar mHighOutputSeekBar;
    private TextView mHighOutputTextView;
    private int mHighOutputValue;
    private SeekBar mHighSeekBar;
    private TextView mHighTextView;
    private int mHighValue;
    private SeekBar mLowOutputSeekBar;
    private TextView mLowOutputTextView;
    private int mLowOutputValue;
    private SeekBar mLowSeekBar;
    private TextView mLowTextView;
    private int mLowValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mLowTextView = new TextView(this);
        this.mLowTextView.setText(new StringBuilder(LOWLEVEL_STRING).append(this.mLowValue).toString());
        this.mLowTextView.setTextSize(22.0f);
        this.mLowTextView.setTextColor(-16777216);
        this.mLowTextView.setGravity(17);
        this.mLowSeekBar = new SeekBar(this);
        this.mLowSeekBar.setOnSeekBarChangeListener(this);
        this.mLowSeekBar.setId(LOWLEVEL_SEEKBAR_RESID);
        this.mLowSeekBar.setMax(100);
        this.mHighTextView = new TextView(this);
        this.mHighTextView.setText(new StringBuilder(HIGHLEVEL_STRING).append(this.mHighValue).toString());
        this.mHighTextView.setTextSize(22.0f);
        this.mHighTextView.setTextColor(-16777216);
        this.mHighTextView.setGravity(17);
        this.mHighSeekBar = new SeekBar(this);
        this.mHighSeekBar.setOnSeekBarChangeListener(this);
        this.mHighSeekBar.setId(HIGHLEVEL_SEEKBAR_RESID);
        this.mHighSeekBar.setMax(100);
        this.mHighSeekBar.setProgress(100);
        this.mLowOutputTextView = new TextView(this);
        this.mLowOutputTextView.setText(new StringBuilder(LOWOUTPUT_STRING).append(this.mLowOutputValue).toString());
        this.mLowOutputTextView.setTextSize(22.0f);
        this.mLowOutputTextView.setTextColor(-16777216);
        this.mLowOutputTextView.setGravity(17);
        this.mLowOutputSeekBar = new SeekBar(this);
        this.mLowOutputSeekBar.setOnSeekBarChangeListener(this);
        this.mLowOutputSeekBar.setId(LOWOUTPUT_SEEKBAR_RESID);
        this.mLowOutputSeekBar.setMax(100);
        this.mHighOutputTextView = new TextView(this);
        this.mHighOutputTextView.setText(new StringBuilder(HIGHOUTPUT_STRING).append(this.mHighOutputValue).toString());
        this.mHighOutputTextView.setTextSize(22.0f);
        this.mHighOutputTextView.setTextColor(-16777216);
        this.mHighOutputTextView.setGravity(17);
        this.mHighOutputSeekBar = new SeekBar(this);
        this.mHighOutputSeekBar.setOnSeekBarChangeListener(this);
        this.mHighOutputSeekBar.setId(HIGHOUTPUT_SEEKBAR_RESID);
        this.mHighOutputSeekBar.setMax(100);
        this.mHighOutputSeekBar.setProgress(100);
        mainLayout.addView(this.mLowTextView);
        mainLayout.addView(this.mLowSeekBar);
        mainLayout.addView(this.mHighTextView);
        mainLayout.addView(this.mHighSeekBar);
        mainLayout.addView(this.mLowOutputTextView);
        mainLayout.addView(this.mLowOutputSeekBar);
        mainLayout.addView(this.mHighOutputTextView);
        mainLayout.addView(this.mHighOutputSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case LOWLEVEL_SEEKBAR_RESID /*21863*/:
                this.mLowValue = progress;
                this.mLowTextView.setText(new StringBuilder(LOWLEVEL_STRING).append(getValue(this.mLowValue)).toString());
                return;
            case HIGHLEVEL_SEEKBAR_RESID /*21864*/:
                this.mHighValue = progress;
                this.mHighTextView.setText(new StringBuilder(HIGHLEVEL_STRING).append(getValue(this.mHighValue)).toString());
                return;
            case LOWOUTPUT_SEEKBAR_RESID /*21865*/:
                this.mLowOutputValue = progress;
                this.mLowOutputTextView.setText(new StringBuilder(LOWOUTPUT_STRING).append(getValue(this.mLowOutputValue)).toString());
                return;
            case HIGHOUTPUT_SEEKBAR_RESID /*21866*/:
                this.mHighOutputValue = progress;
                this.mHighOutputTextView.setText(new StringBuilder(HIGHOUTPUT_STRING).append(getValue(this.mHighOutputValue)).toString());
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
                LevelsFilter filter = new LevelsFilter();
                filter.setLowLevel(LevelsFilterActivity.this.getValue(LevelsFilterActivity.this.mLowValue));
                filter.setHighLevel(LevelsFilterActivity.this.getValue(LevelsFilterActivity.this.mHighValue));
                filter.setLowOutputLevel(LevelsFilterActivity.this.getValue(LevelsFilterActivity.this.mLowOutputValue));
                filter.setHighOutputLevel(LevelsFilterActivity.this.getValue(LevelsFilterActivity.this.mHighOutputValue));
                LevelsFilterActivity.this.mColors = filter.filter(LevelsFilterActivity.this.mColors, width, height);
                LevelsFilterActivity levelsFilterActivity = LevelsFilterActivity.this;
                final int i = width;
                final int i2 = height;
                levelsFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        LevelsFilterActivity.this.setModifyView(LevelsFilterActivity.this.mColors, i, i2);
                    }
                });
                LevelsFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
