package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.NoiseFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class NoiseFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int AMOUNT_SEEKBAR_RESID = 21863;
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final int DENSITY_SEEKBAR_RESID = 21864;
    private static final String DENSITY_STRING = "DENSITY:";
    private static final int MAX_VALUE = 100;
    private static final String TITLE = "Noise";
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private int mAmountValue;
    private int[] mColors;
    private SeekBar mDensitySeekBar;
    private TextView mDensityTextView;
    private int mDensityValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mAmountTextView = new TextView(this);
        this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(this.mAmountValue).toString());
        this.mAmountTextView.setTextSize(22.0f);
        this.mAmountTextView.setTextColor(-16777216);
        this.mAmountTextView.setGravity(17);
        this.mAmountSeekBar = new SeekBar(this);
        this.mAmountSeekBar.setOnSeekBarChangeListener(this);
        this.mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        this.mAmountSeekBar.setMax(100);
        this.mDensityTextView = new TextView(this);
        this.mDensityTextView.setText(new StringBuilder(DENSITY_STRING).append(this.mDensityValue).toString());
        this.mDensityTextView.setTextSize(22.0f);
        this.mDensityTextView.setTextColor(-16777216);
        this.mDensityTextView.setGravity(17);
        this.mDensitySeekBar = new SeekBar(this);
        this.mDensitySeekBar.setOnSeekBarChangeListener(this);
        this.mDensitySeekBar.setId(DENSITY_SEEKBAR_RESID);
        this.mDensitySeekBar.setMax(100);
        mainLayout.addView(this.mAmountTextView);
        mainLayout.addView(this.mAmountSeekBar);
        mainLayout.addView(this.mDensityTextView);
        mainLayout.addView(this.mDensitySeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case AMOUNT_SEEKBAR_RESID /*21863*/:
                this.mAmountValue = progress;
                this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(this.mAmountValue).toString());
                return;
            case DENSITY_SEEKBAR_RESID /*21864*/:
                this.mDensityValue = progress;
                this.mDensityTextView.setText(new StringBuilder(DENSITY_STRING).append(this.mDensityValue).toString());
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
                NoiseFilter filter = new NoiseFilter();
                filter.setAmount(NoiseFilterActivity.this.mAmountValue);
                filter.setDensity((float) NoiseFilterActivity.this.mDensityValue);
                NoiseFilterActivity.this.mColors = filter.filter(NoiseFilterActivity.this.mColors, width, height);
                NoiseFilterActivity noiseFilterActivity = NoiseFilterActivity.this;
                final int i = width;
                final int i2 = height;
                noiseFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        NoiseFilterActivity.this.setModifyView(NoiseFilterActivity.this.mColors, i, i2);
                    }
                });
                NoiseFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
