package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.GainFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class GainFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int BAIS_SEEKBAR_RESID = 21866;
    private static final String BAIS_STRING = "BAIS:";
    private static final int GAIN_SEEKBAR_RESID = 21865;
    private static final String GAIN_STRING = "GAIN:";
    private static final int MAX_VALUE = 100;
    private static final String TITLE = "Gain";
    private SeekBar mBaisSeekBar;
    private TextView mBaisTextView;
    private int mBaisValue;
    private int[] mColors;
    private SeekBar mGainSeekBar;
    private TextView mGainTextView;
    private int mGainValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mGainTextView = new TextView(this);
        this.mGainTextView.setText(new StringBuilder(GAIN_STRING).append(getValue(this.mGainValue)).toString());
        this.mGainTextView.setTextSize(22.0f);
        this.mGainTextView.setTextColor(-16777216);
        this.mGainTextView.setGravity(17);
        this.mGainSeekBar = new SeekBar(this);
        this.mGainSeekBar.setOnSeekBarChangeListener(this);
        this.mGainSeekBar.setId(GAIN_SEEKBAR_RESID);
        this.mGainSeekBar.setMax(100);
        this.mBaisTextView = new TextView(this);
        this.mBaisTextView.setText(new StringBuilder(BAIS_STRING).append(getValue(this.mBaisValue)).toString());
        this.mBaisTextView.setTextSize(22.0f);
        this.mBaisTextView.setTextColor(-16777216);
        this.mBaisTextView.setGravity(17);
        this.mBaisSeekBar = new SeekBar(this);
        this.mBaisSeekBar.setOnSeekBarChangeListener(this);
        this.mBaisSeekBar.setId(BAIS_SEEKBAR_RESID);
        this.mBaisSeekBar.setMax(100);
        mainLayout.addView(this.mGainTextView);
        mainLayout.addView(this.mGainSeekBar);
        mainLayout.addView(this.mBaisTextView);
        mainLayout.addView(this.mBaisSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case GAIN_SEEKBAR_RESID /*21865*/:
                this.mGainValue = progress;
                this.mGainTextView.setText(new StringBuilder(GAIN_STRING).append(getValue(this.mGainValue)).toString());
                return;
            case BAIS_SEEKBAR_RESID /*21866*/:
                this.mBaisValue = progress;
                this.mBaisTextView.setText(new StringBuilder(BAIS_STRING).append(getValue(this.mBaisValue)).toString());
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
                GainFilter filter = new GainFilter();
                filter.setGain(GainFilterActivity.this.getValue(GainFilterActivity.this.mGainValue));
                filter.setBias(GainFilterActivity.this.getValue(GainFilterActivity.this.mBaisValue));
                GainFilterActivity.this.mColors = filter.filter(GainFilterActivity.this.mColors, width, height);
                GainFilterActivity gainFilterActivity = GainFilterActivity.this;
                final int i = width;
                final int i2 = height;
                gainFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        GainFilterActivity.this.setModifyView(GainFilterActivity.this.mColors, i, i2);
                    }
                });
                GainFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
