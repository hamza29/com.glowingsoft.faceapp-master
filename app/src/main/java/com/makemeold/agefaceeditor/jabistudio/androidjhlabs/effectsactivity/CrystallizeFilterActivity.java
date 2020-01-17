package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.CrystallizeFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class CrystallizeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int EDGE_SEEKBAR_RESID = 21867;
    private static final String EDGE_STRING = "EDGE:";
    private static final int MAX_VALUE = 100;
    private static final int RANDOMNESS_SEEKBAR_RESID = 21866;
    private static final String RANDOMNESS_STRING = "RANDOMNESS:";
    private static final int SIZE_SEEKBAR_RESID = 21865;
    private static final String SIZE_STRING = "SIZE:";
    private static final String TITLE = "Crystallize";
    private int[] mColors;
    private SeekBar mEdgeSeekBar;
    private TextView mEdgeTextView;
    private int mEdgeValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRandomnessSeekBar;
    private TextView mRandomnessTextView;
    private int mRandomnessValue;
    private SeekBar mSizeSeekBar;
    private TextView mSizeTextView;
    private int mSizeValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mSizeTextView = new TextView(this);
        this.mSizeTextView.setText(new StringBuilder(SIZE_STRING).append(this.mSizeValue).toString());
        this.mSizeTextView.setTextSize(22.0f);
        this.mSizeTextView.setTextColor(-16777216);
        this.mSizeTextView.setGravity(17);
        this.mSizeSeekBar = new SeekBar(this);
        this.mSizeSeekBar.setOnSeekBarChangeListener(this);
        this.mSizeSeekBar.setId(SIZE_SEEKBAR_RESID);
        this.mSizeSeekBar.setMax(100);
        this.mRandomnessTextView = new TextView(this);
        this.mRandomnessTextView.setText(new StringBuilder(RANDOMNESS_STRING).append(this.mRandomnessValue).toString());
        this.mRandomnessTextView.setTextSize(22.0f);
        this.mRandomnessTextView.setTextColor(-16777216);
        this.mRandomnessTextView.setGravity(17);
        this.mRandomnessSeekBar = new SeekBar(this);
        this.mRandomnessSeekBar.setOnSeekBarChangeListener(this);
        this.mRandomnessSeekBar.setId(RANDOMNESS_SEEKBAR_RESID);
        this.mRandomnessSeekBar.setMax(100);
        this.mEdgeTextView = new TextView(this);
        this.mEdgeTextView.setText(new StringBuilder(EDGE_STRING).append(this.mEdgeValue).toString());
        this.mEdgeTextView.setTextSize(22.0f);
        this.mEdgeTextView.setTextColor(-16777216);
        this.mEdgeTextView.setGravity(17);
        this.mEdgeSeekBar = new SeekBar(this);
        this.mEdgeSeekBar.setOnSeekBarChangeListener(this);
        this.mEdgeSeekBar.setId(EDGE_SEEKBAR_RESID);
        this.mEdgeSeekBar.setMax(100);
        mainLayout.addView(this.mSizeTextView);
        mainLayout.addView(this.mSizeSeekBar);
        mainLayout.addView(this.mRandomnessTextView);
        mainLayout.addView(this.mRandomnessSeekBar);
        mainLayout.addView(this.mEdgeTextView);
        mainLayout.addView(this.mEdgeSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case SIZE_SEEKBAR_RESID /*21865*/:
                this.mSizeValue = progress;
                this.mSizeTextView.setText(new StringBuilder(SIZE_STRING).append(getAmout(this.mSizeValue)).toString());
                return;
            case RANDOMNESS_SEEKBAR_RESID /*21866*/:
                this.mRandomnessValue = progress;
                this.mRandomnessTextView.setText(new StringBuilder(RANDOMNESS_STRING).append(this.mRandomnessValue).toString());
                return;
            case EDGE_SEEKBAR_RESID /*21867*/:
                this.mEdgeValue = progress;
                this.mEdgeTextView.setText(new StringBuilder(EDGE_STRING).append(this.mEdgeValue).toString());
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
                CrystallizeFilter filter = new CrystallizeFilter();
                filter.setEdgeColor(-16777216);
                filter.setAmount(CrystallizeFilterActivity.this.getAmout(CrystallizeFilterActivity.this.mSizeValue));
                filter.setEdgeThickness(CrystallizeFilterActivity.this.getAmout(CrystallizeFilterActivity.this.mEdgeValue));
                filter.setRandomness(CrystallizeFilterActivity.this.getAmout(CrystallizeFilterActivity.this.mRandomnessValue));
                CrystallizeFilterActivity.this.mColors = filter.filter(CrystallizeFilterActivity.this.mColors, width, height);
                CrystallizeFilterActivity crystallizeFilterActivity = CrystallizeFilterActivity.this;
                final int i = width;
                final int i2 = height;
                crystallizeFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        CrystallizeFilterActivity.this.setModifyView(CrystallizeFilterActivity.this.mColors, i, i2);
                    }
                });
                CrystallizeFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getAmout(int value) {
        return ((float) value) / 100.0f;
    }
}
