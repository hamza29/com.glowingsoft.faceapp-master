package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
//import android.support.v4.media.TransportMediator;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.ThresholdFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ThresholdFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int LOWER_SEEKBAR_RESID = 21863;
    private static final String LOWER_STRING = "LOWER:";
    private static final int MAX_VALUE = 255;
    private static final String TITLE = "Threshold";
    private static final int UPPER_SEEKBAR_RESID = 21864;
    private static final String UPPER_STRING = "UPPER:";
    private int[] mColors;
    private SeekBar mLowerSeekBar;
    private TextView mLowerTextView;
    private int mLowerValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mUpperSeekBar;
    private TextView mUpperTextView;
    private int mUpperValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mLowerTextView = new TextView(this);
        this.mLowerTextView.setText(new StringBuilder(LOWER_STRING).append(this.mLowerValue).toString());
        this.mLowerTextView.setTextSize(22.0f);
        this.mLowerTextView.setTextColor(-16777216);
        this.mLowerTextView.setGravity(17);
        this.mLowerSeekBar = new SeekBar(this);
        this.mLowerSeekBar.setOnSeekBarChangeListener(this);
        this.mLowerSeekBar.setId(LOWER_SEEKBAR_RESID);
        this.mLowerSeekBar.setMax(255);
        //this.mLowerSeekBar.setProgress(TransportMediator.KEYCODE_MEDIA_PAUSE);
        this.mUpperTextView = new TextView(this);
        this.mUpperTextView.setText(new StringBuilder(UPPER_STRING).append(this.mUpperValue).toString());
        this.mUpperTextView.setTextSize(22.0f);
        this.mUpperTextView.setTextColor(-16777216);
        this.mUpperTextView.setGravity(17);
        this.mUpperSeekBar = new SeekBar(this);
        this.mUpperSeekBar.setOnSeekBarChangeListener(this);
        this.mUpperSeekBar.setId(UPPER_SEEKBAR_RESID);
        this.mUpperSeekBar.setMax(255);
       // this.mUpperSeekBar.setProgress(TransportMediator.KEYCODE_MEDIA_PAUSE);
        mainLayout.addView(this.mLowerTextView);
        mainLayout.addView(this.mLowerSeekBar);
        mainLayout.addView(this.mUpperTextView);
        mainLayout.addView(this.mUpperSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case LOWER_SEEKBAR_RESID /*21863*/:
                this.mLowerValue = progress;
                this.mLowerTextView.setText(new StringBuilder(LOWER_STRING).append(this.mLowerValue).toString());
                return;
            case UPPER_SEEKBAR_RESID /*21864*/:
                this.mUpperValue = progress;
                this.mUpperTextView.setText(new StringBuilder(UPPER_STRING).append(this.mUpperValue).toString());
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
                ThresholdFilter filter = new ThresholdFilter();
                filter.setLowerThreshold(ThresholdFilterActivity.this.mLowerValue);
                filter.setUpperThreshold(ThresholdFilterActivity.this.mUpperValue);
                ThresholdFilterActivity.this.mColors = filter.filter(ThresholdFilterActivity.this.mColors, width, height);
                ThresholdFilterActivity thresholdFilterActivity = ThresholdFilterActivity.this;
                final int i = width;
                final int i2 = height;
                thresholdFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ThresholdFilterActivity.this.setModifyView(ThresholdFilterActivity.this.mColors, i, i2);
                    }
                });
                ThresholdFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
