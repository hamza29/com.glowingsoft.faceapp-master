package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.ChannelMixFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ChannelMixFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int INTOB_SEEKBAR_RESID = 21865;
    private static final String INTOB_STRING = "INTOB:";
    private static final int INTOG_SEEKBAR_RESID = 21864;
    private static final String INTOG_STRING = "INTOG:";
    private static final int INTOR_SEEKBAR_RESID = 21863;
    private static final String INTOR_STRING = "INTOR:";
    private static final int MAX_VALUE = 255;
    private static final String TITLE = "ChannelMix";
    private int[] mColors;
    private SeekBar mIntoBSeekBar;
    private TextView mIntoBTextView;
    private int mIntoBValue;
    private SeekBar mIntoGSeekBar;
    private TextView mIntoGTextView;
    private int mIntoGValue;
    private SeekBar mIntoRSeekBar;
    private TextView mIntoRTextView;
    private int mIntoRValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mIntoRTextView = new TextView(this);
        this.mIntoRTextView.setText(new StringBuilder(INTOR_STRING).append(this.mIntoRValue).toString());
        this.mIntoRTextView.setTextSize(22.0f);
        this.mIntoRTextView.setTextColor(-16777216);
        this.mIntoRTextView.setGravity(17);
        this.mIntoRSeekBar = new SeekBar(this);
        this.mIntoRSeekBar.setOnSeekBarChangeListener(this);
        this.mIntoRSeekBar.setId(INTOR_SEEKBAR_RESID);
        this.mIntoRSeekBar.setMax(255);
        this.mIntoGTextView = new TextView(this);
        this.mIntoGTextView.setText(new StringBuilder(INTOG_STRING).append(this.mIntoGValue).toString());
        this.mIntoGTextView.setTextSize(22.0f);
        this.mIntoGTextView.setTextColor(-16777216);
        this.mIntoGTextView.setGravity(17);
        this.mIntoGSeekBar = new SeekBar(this);
        this.mIntoGSeekBar.setOnSeekBarChangeListener(this);
        this.mIntoGSeekBar.setId(INTOG_SEEKBAR_RESID);
        this.mIntoGSeekBar.setMax(255);
        this.mIntoBTextView = new TextView(this);
        this.mIntoBTextView.setText(new StringBuilder(INTOB_STRING).append(this.mIntoBValue).toString());
        this.mIntoBTextView.setTextSize(22.0f);
        this.mIntoBTextView.setTextColor(-16777216);
        this.mIntoBTextView.setGravity(17);
        this.mIntoBSeekBar = new SeekBar(this);
        this.mIntoBSeekBar.setOnSeekBarChangeListener(this);
        this.mIntoBSeekBar.setId(INTOB_SEEKBAR_RESID);
        this.mIntoBSeekBar.setMax(255);
        mainLayout.addView(this.mIntoRTextView);
        mainLayout.addView(this.mIntoRSeekBar);
        mainLayout.addView(this.mIntoGTextView);
        mainLayout.addView(this.mIntoGSeekBar);
        mainLayout.addView(this.mIntoBTextView);
        mainLayout.addView(this.mIntoBSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case INTOR_SEEKBAR_RESID /*21863*/:
                this.mIntoRValue = progress;
                this.mIntoRTextView.setText(new StringBuilder(INTOR_STRING).append(this.mIntoRValue).toString());
                return;
            case INTOG_SEEKBAR_RESID /*21864*/:
                this.mIntoGValue = progress;
                this.mIntoGTextView.setText(new StringBuilder(INTOG_STRING).append(this.mIntoGValue).toString());
                return;
            case INTOB_SEEKBAR_RESID /*21865*/:
                this.mIntoBValue = progress;
                this.mIntoBTextView.setText(new StringBuilder(INTOB_STRING).append(this.mIntoBValue).toString());
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
                ChannelMixFilter filter = new ChannelMixFilter();
                filter.setIntoR(ChannelMixFilterActivity.this.mIntoRValue);
                filter.setIntoG(ChannelMixFilterActivity.this.mIntoGValue);
                filter.setIntoB(ChannelMixFilterActivity.this.mIntoBValue);
                ChannelMixFilterActivity.this.mColors = filter.filter(ChannelMixFilterActivity.this.mColors, width, height);
                ChannelMixFilterActivity channelMixFilterActivity = ChannelMixFilterActivity.this;
                final int i = width;
                final int i2 = height;
                channelMixFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ChannelMixFilterActivity.this.setModifyView(ChannelMixFilterActivity.this.mColors, i, i2);
                    }
                });
                ChannelMixFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
