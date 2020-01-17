package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.RippleFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class RippleFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener, OnItemSelectedListener {
    private static final int MAX_VALUE = 100;
    private static final String SHAPE_TYPE1_STRING = "Sine";
    private static final String SHAPE_TYPE2_STRING = "Sawtooth";
    private static final String SHAPE_TYPE3_STRING = "Triangle";
    private static final String SHAPE_TYPE4_STRING = "Noise";
    private static final String TITLE = "Ripple";
    private static final int XAMPLITUTE_SEEKBAR_RESID = 21863;
    private static final String XAMPLITUTE_STRING = "XAMPLITUTE:";
    private static final int XWAVELENGTH_SEEKBAR_RESID = 21864;
    private static final String XWAVELENGTH_STRING = "XWAVELENGTH:";
    private static final int YAMPLITUTE_SEEKBAR_RESID = 21865;
    private static final String YAMPLITUTE_STRING = "YAMPLITUTE:";
    private static final int YWAVELENGTH_SEEKBAR_RESID = 21866;
    private static final String YWAVELENGTH_STRING = "YWAVELENGTH:";
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private Spinner mShapeSpinner;
    private SeekBar mXAmplituteSeekBar;
    private TextView mXAmplituteTextView;
    private int mXAmplituteValue;
    private SeekBar mXWavelengthSeekBar;
    private TextView mXWavelengthTextView;
    private int mXWavelengthValue;
    private SeekBar mYAmplituteSeekBar;
    private TextView mYAmplituteTextView;
    private int mYAmplituteValue;
    private SeekBar mYWavelengthSeekBar;
    private TextView mYWavelengthTextView;
    private int mYWavelengthValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mXAmplituteTextView = new TextView(this);
        this.mXAmplituteTextView.setText(new StringBuilder(XAMPLITUTE_STRING).append(this.mXAmplituteValue).toString());
        this.mXAmplituteTextView.setTextSize(22.0f);
        this.mXAmplituteTextView.setTextColor(-16777216);
        this.mXAmplituteTextView.setGravity(17);
        this.mXAmplituteSeekBar = new SeekBar(this);
        this.mXAmplituteSeekBar.setOnSeekBarChangeListener(this);
        this.mXAmplituteSeekBar.setId(XAMPLITUTE_SEEKBAR_RESID);
        this.mXAmplituteSeekBar.setMax(100);
        this.mXWavelengthTextView = new TextView(this);
        this.mXWavelengthTextView.setText(new StringBuilder(XWAVELENGTH_STRING).append(this.mXWavelengthValue).toString());
        this.mXWavelengthTextView.setTextSize(22.0f);
        this.mXWavelengthTextView.setTextColor(-16777216);
        this.mXWavelengthTextView.setGravity(17);
        this.mXWavelengthSeekBar = new SeekBar(this);
        this.mXWavelengthSeekBar.setOnSeekBarChangeListener(this);
        this.mXWavelengthSeekBar.setId(XWAVELENGTH_SEEKBAR_RESID);
        this.mXWavelengthSeekBar.setMax(100);
        this.mYAmplituteTextView = new TextView(this);
        this.mYAmplituteTextView.setText(new StringBuilder(YAMPLITUTE_STRING).append(this.mYAmplituteValue).toString());
        this.mYAmplituteTextView.setTextSize(22.0f);
        this.mYAmplituteTextView.setTextColor(-16777216);
        this.mYAmplituteTextView.setGravity(17);
        this.mYAmplituteSeekBar = new SeekBar(this);
        this.mYAmplituteSeekBar.setOnSeekBarChangeListener(this);
        this.mYAmplituteSeekBar.setId(YAMPLITUTE_SEEKBAR_RESID);
        this.mYAmplituteSeekBar.setMax(100);
        this.mYWavelengthTextView = new TextView(this);
        this.mYWavelengthTextView.setText(new StringBuilder(YWAVELENGTH_STRING).append(this.mYWavelengthValue).toString());
        this.mYWavelengthTextView.setTextSize(22.0f);
        this.mYWavelengthTextView.setTextColor(-16777216);
        this.mYWavelengthTextView.setGravity(17);
        this.mYWavelengthSeekBar = new SeekBar(this);
        this.mYWavelengthSeekBar.setOnSeekBarChangeListener(this);
        this.mYWavelengthSeekBar.setId(YWAVELENGTH_SEEKBAR_RESID);
        this.mYWavelengthSeekBar.setMax(100);
        this.mShapeSpinner = new Spinner(this);
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter(this, 17367048);
        shapeAdapter.add(SHAPE_TYPE1_STRING);
        shapeAdapter.add(SHAPE_TYPE2_STRING);
        shapeAdapter.add(SHAPE_TYPE3_STRING);
        shapeAdapter.add(SHAPE_TYPE4_STRING);
        this.mShapeSpinner.setAdapter(shapeAdapter);
        this.mShapeSpinner.setOnItemSelectedListener(this);
        mainLayout.addView(this.mXAmplituteTextView);
        mainLayout.addView(this.mXAmplituteSeekBar);
        mainLayout.addView(this.mXWavelengthTextView);
        mainLayout.addView(this.mXWavelengthSeekBar);
        mainLayout.addView(this.mYAmplituteTextView);
        mainLayout.addView(this.mYAmplituteSeekBar);
        mainLayout.addView(this.mYWavelengthTextView);
        mainLayout.addView(this.mYWavelengthSeekBar);
        mainLayout.addView(this.mShapeSpinner);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case XAMPLITUTE_SEEKBAR_RESID /*21863*/:
                this.mXAmplituteValue = progress;
                this.mXAmplituteTextView.setText(new StringBuilder(XAMPLITUTE_STRING).append(this.mXAmplituteValue).toString());
                return;
            case XWAVELENGTH_SEEKBAR_RESID /*21864*/:
                this.mXWavelengthValue = progress;
                this.mXWavelengthTextView.setText(new StringBuilder(XWAVELENGTH_STRING).append(this.mXWavelengthValue).toString());
                return;
            case YAMPLITUTE_SEEKBAR_RESID /*21865*/:
                this.mYAmplituteValue = progress;
                this.mYAmplituteTextView.setText(new StringBuilder(YAMPLITUTE_STRING).append(this.mYAmplituteValue).toString());
                return;
            case YWAVELENGTH_SEEKBAR_RESID /*21866*/:
                this.mYWavelengthValue = progress;
                this.mYWavelengthTextView.setText(new StringBuilder(YWAVELENGTH_STRING).append(this.mYWavelengthValue).toString());
                return;
            default:
                return;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        applyFilter();
    }

    private void applyFilter() {
        final int width = this.mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = this.mOriginalImageView.getDrawable().getIntrinsicHeight();
        this.mColors = AndroidUtils.drawableToIntArray(this.mOriginalImageView.getDrawable());
        this.mProgressDialog = ProgressDialog.show(this, BuildConfig.FLAVOR, "Wait......");
        Thread thread = new Thread() {
            public void run() {
                RippleFilter filter = new RippleFilter();
                filter.setXAmplitude((float) RippleFilterActivity.this.mXAmplituteValue);
                filter.setXWavelength((float) RippleFilterActivity.this.mXWavelengthValue);
                filter.setYAmplitude((float) RippleFilterActivity.this.mYAmplituteValue);
                filter.setYWavelength((float) RippleFilterActivity.this.mYWavelengthValue);
                filter.setWaveType(RippleFilterActivity.this.getSelectType());
                RippleFilterActivity.this.mColors = filter.filter(RippleFilterActivity.this.mColors, width, height);
                RippleFilterActivity rippleFilterActivity = RippleFilterActivity.this;
                final int i = width;
                final int i2 = height;
                rippleFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        RippleFilterActivity.this.setModifyView(RippleFilterActivity.this.mColors, i, i2);
                    }
                });
                RippleFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private int getSelectType() {
        if (this.mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE1_STRING)) {
            return 0;
        }
        if (this.mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE2_STRING)) {
            return 1;
        }
        if (this.mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE3_STRING)) {
            return 2;
        }
        return 3;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        applyFilter();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
