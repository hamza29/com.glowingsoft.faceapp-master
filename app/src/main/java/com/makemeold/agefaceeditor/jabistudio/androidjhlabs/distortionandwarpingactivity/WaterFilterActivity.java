package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.WaterFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class WaterFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int AMPLITUTE_SEEKBAR_RESID = 21866;
    private static final String AMPLITUTE_STRING = "AMPLITUTE:";
    private static final int CENTERX_SEEKBAR_RESID = 21863;
    private static final String CENTERX_STRING = "CENTERX:";
    private static final int CENTERY_SEEKBAR_RESID = 21864;
    private static final String CENTERY_STRING = "CENTERY:";
    private static final int MAX_PHASE_VALUE = 624;
    private static final int MAX_RADIUS_VALUE = 400;
    private static final int MAX_VALUE = 100;
    private static final int MAX_WAVELENGTH_VALUE = 200;
    private static final int PHASE_SEEKBAR_RESID = 21868;
    private static final String PHASE_STRING = "PHASE:";
    private static final int RADIUS_SEEKBAR_RESID = 21867;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String TITLE = "Water";
    private static final int WAVELENGTH_SEEKBAR_RESID = 21865;
    private static final String WAVELENGTH_STRING = "WAVELENGTH:";
    private SeekBar mAmplituteSeekBar;
    private TextView mAmplituteTextView;
    private int mAmplituteValue;
    private SeekBar mCenterXSeekBar;
    private TextView mCenterXTextView;
    private int mCenterXValue;
    private SeekBar mCenterYSeekBar;
    private TextView mCenterYTextView;
    private int mCenterYValue;
    private int[] mColors;
    private SeekBar mPhaseSeekBar;
    private TextView mPhaseTextView;
    private int mPhaseValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private int mRadiusValue;
    private SeekBar mWavelengthSeekBar;
    private TextView mWavelengthTextView;
    private int mWavelengthValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mCenterXTextView = new TextView(this);
        this.mCenterXTextView.setText(new StringBuilder(CENTERX_STRING).append(this.mCenterXValue).toString());
        this.mCenterXTextView.setTextSize(22.0f);
        this.mCenterXTextView.setTextColor(-16777216);
        this.mCenterXTextView.setGravity(17);
        this.mCenterXSeekBar = new SeekBar(this);
        this.mCenterXSeekBar.setOnSeekBarChangeListener(this);
        this.mCenterXSeekBar.setId(CENTERX_SEEKBAR_RESID);
        this.mCenterXSeekBar.setMax(100);
        this.mCenterXSeekBar.setProgress(50);
        this.mCenterYTextView = new TextView(this);
        this.mCenterYTextView.setText(new StringBuilder(CENTERY_STRING).append(this.mCenterYValue).toString());
        this.mCenterYTextView.setTextSize(22.0f);
        this.mCenterYTextView.setTextColor(-16777216);
        this.mCenterYTextView.setGravity(17);
        this.mCenterYSeekBar = new SeekBar(this);
        this.mCenterYSeekBar.setOnSeekBarChangeListener(this);
        this.mCenterYSeekBar.setId(CENTERY_SEEKBAR_RESID);
        this.mCenterYSeekBar.setMax(100);
        this.mCenterYSeekBar.setProgress(50);
        this.mRadiusTextView = new TextView(this);
        this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
        this.mRadiusTextView.setTextSize(22.0f);
        this.mRadiusTextView.setTextColor(-16777216);
        this.mRadiusTextView.setGravity(17);
        this.mRadiusSeekBar = new SeekBar(this);
        this.mRadiusSeekBar.setOnSeekBarChangeListener(this);
        this.mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        this.mRadiusSeekBar.setMax(400);
        this.mWavelengthTextView = new TextView(this);
        this.mWavelengthTextView.setText(new StringBuilder(WAVELENGTH_STRING).append(this.mWavelengthValue).toString());
        this.mWavelengthTextView.setTextSize(22.0f);
        this.mWavelengthTextView.setTextColor(-16777216);
        this.mWavelengthTextView.setGravity(17);
        this.mWavelengthSeekBar = new SeekBar(this);
        this.mWavelengthSeekBar.setOnSeekBarChangeListener(this);
        this.mWavelengthSeekBar.setId(WAVELENGTH_SEEKBAR_RESID);
        this.mWavelengthSeekBar.setMax(200);
        this.mAmplituteTextView = new TextView(this);
        this.mAmplituteTextView.setText(new StringBuilder(AMPLITUTE_STRING).append(this.mAmplituteValue).toString());
        this.mAmplituteTextView.setTextSize(22.0f);
        this.mAmplituteTextView.setTextColor(-16777216);
        this.mAmplituteTextView.setGravity(17);
        this.mAmplituteSeekBar = new SeekBar(this);
        this.mAmplituteSeekBar.setOnSeekBarChangeListener(this);
        this.mAmplituteSeekBar.setId(AMPLITUTE_SEEKBAR_RESID);
        this.mAmplituteSeekBar.setMax(100);
        this.mPhaseTextView = new TextView(this);
        this.mPhaseTextView.setText(new StringBuilder(PHASE_STRING).append(this.mPhaseValue).toString());
        this.mPhaseTextView.setTextSize(22.0f);
        this.mPhaseTextView.setTextColor(-16777216);
        this.mPhaseTextView.setGravity(17);
        this.mPhaseSeekBar = new SeekBar(this);
        this.mPhaseSeekBar.setOnSeekBarChangeListener(this);
        this.mPhaseSeekBar.setId(PHASE_SEEKBAR_RESID);
        this.mPhaseSeekBar.setMax(MAX_PHASE_VALUE);
        mainLayout.addView(this.mCenterXTextView);
        mainLayout.addView(this.mCenterXSeekBar);
        mainLayout.addView(this.mCenterYTextView);
        mainLayout.addView(this.mCenterYSeekBar);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
        mainLayout.addView(this.mWavelengthTextView);
        mainLayout.addView(this.mWavelengthSeekBar);
        mainLayout.addView(this.mAmplituteTextView);
        mainLayout.addView(this.mAmplituteSeekBar);
        mainLayout.addView(this.mPhaseTextView);
        mainLayout.addView(this.mPhaseSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case CENTERX_SEEKBAR_RESID /*21863*/:
                this.mCenterXValue = progress;
                this.mCenterXTextView.setText(new StringBuilder(CENTERX_STRING).append(getValue(this.mCenterXValue)).toString());
                return;
            case CENTERY_SEEKBAR_RESID /*21864*/:
                this.mCenterYValue = progress;
                this.mCenterYTextView.setText(new StringBuilder(CENTERY_STRING).append(getValue(this.mCenterYValue)).toString());
                return;
            case WAVELENGTH_SEEKBAR_RESID /*21865*/:
                this.mWavelengthValue = progress;
                this.mWavelengthTextView.setText(new StringBuilder(WAVELENGTH_STRING).append(this.mWavelengthValue).toString());
                return;
            case AMPLITUTE_SEEKBAR_RESID /*21866*/:
                this.mAmplituteValue = progress;
                this.mAmplituteTextView.setText(new StringBuilder(AMPLITUTE_STRING).append(getValue(this.mAmplituteValue)).toString());
                return;
            case RADIUS_SEEKBAR_RESID /*21867*/:
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
                return;
            case PHASE_SEEKBAR_RESID /*21868*/:
                this.mPhaseValue = progress;
                this.mPhaseTextView.setText(new StringBuilder(PHASE_STRING).append(getValue(this.mPhaseValue)).toString());
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
                WaterFilter filter = new WaterFilter();
                filter.setCentreX(WaterFilterActivity.this.getValue(WaterFilterActivity.this.mCenterXValue));
                filter.setCentreY(WaterFilterActivity.this.getValue(WaterFilterActivity.this.mCenterYValue));
                filter.setRadius((float) WaterFilterActivity.this.mRadiusValue);
                filter.setWavelength((float) WaterFilterActivity.this.mWavelengthValue);
                filter.setAmplitude(WaterFilterActivity.this.getValue(WaterFilterActivity.this.mAmplituteValue));
                filter.setPhase(WaterFilterActivity.this.getValue(WaterFilterActivity.this.mPhaseValue));
                WaterFilterActivity.this.mColors = filter.filter(WaterFilterActivity.this.mColors, width, height);
                WaterFilterActivity waterFilterActivity = WaterFilterActivity.this;
                final int i = width;
                final int i2 = height;
                waterFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        WaterFilterActivity.this.setModifyView(WaterFilterActivity.this.mColors, i, i2);
                    }
                });
                WaterFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
