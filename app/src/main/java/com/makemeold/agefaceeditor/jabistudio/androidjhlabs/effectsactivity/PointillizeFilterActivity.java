package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.annotation.SuppressLint;
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
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.PointillizeFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class PointillizeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener, OnItemSelectedListener {
    private static final int FUZZINESS_SEEKBAR_RESID = 21868;
    private static final String FUZZINESS_STRING = "FUZZINESS:";
    private static final int MAX_VALUE = 100;
    private static final int RANDOMNESS_SEEKBAR_RESID = 21866;
    private static final String RANDOMNESS_STRING = "RANDOMNESS:";
    private static final String SHAPE_TYPE1_STRING = "Squares";
    private static final String SHAPE_TYPE2_STRING = "Hexagons";
    private static final String SHAPE_TYPE3_STRING = "Octagon";
    private static final String SHAPE_TYPE4_STRING = "Triangle";
    private static final int SIZE_SEEKBAR_RESID = 21865;
    private static final String SIZE_STRING = "SIZE:";
    private static final String TITLE = "Pointillize";
    private int[] mColors;
    private SeekBar mFuzzinessSeekBar;
    private TextView mFuzzinessTextView;
    private int mFuzzinessValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRandomnessSeekBar;
    private TextView mRandomnessTextView;
    private int mRandomnessValue;
    private Spinner mShapeSpinner;
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
        this.mFuzzinessTextView = new TextView(this);
        this.mFuzzinessTextView.setText(new StringBuilder(FUZZINESS_STRING).append(this.mFuzzinessValue).toString());
        this.mFuzzinessTextView.setTextSize(22.0f);
        this.mFuzzinessTextView.setTextColor(-16777216);
        this.mFuzzinessTextView.setGravity(17);
        this.mFuzzinessSeekBar = new SeekBar(this);
        this.mFuzzinessSeekBar.setOnSeekBarChangeListener(this);
        this.mFuzzinessSeekBar.setId(FUZZINESS_SEEKBAR_RESID);
        this.mFuzzinessSeekBar.setMax(100);
        this.mShapeSpinner = new Spinner(this);
        @SuppressLint("ResourceType") ArrayAdapter<String> shapeAdapter = new ArrayAdapter(this, 17367048);
        shapeAdapter.add(SHAPE_TYPE1_STRING);
        shapeAdapter.add(SHAPE_TYPE2_STRING);
        shapeAdapter.add(SHAPE_TYPE3_STRING);
        shapeAdapter.add(SHAPE_TYPE4_STRING);
        this.mShapeSpinner.setAdapter(shapeAdapter);
        this.mShapeSpinner.setOnItemSelectedListener(this);
        mainLayout.addView(this.mSizeTextView);
        mainLayout.addView(this.mSizeSeekBar);
        mainLayout.addView(this.mRandomnessTextView);
        mainLayout.addView(this.mRandomnessSeekBar);
        mainLayout.addView(this.mFuzzinessTextView);
        mainLayout.addView(this.mFuzzinessSeekBar);
        mainLayout.addView(this.mShapeSpinner);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case SIZE_SEEKBAR_RESID /*21865*/:
                this.mSizeValue = progress;
                this.mSizeTextView.setText(new StringBuilder(SIZE_STRING).append(this.mSizeValue).toString());
                return;
            case RANDOMNESS_SEEKBAR_RESID /*21866*/:
                this.mRandomnessValue = progress;
                this.mRandomnessTextView.setText(new StringBuilder(RANDOMNESS_STRING).append(getAmout(this.mRandomnessValue)).toString());
                return;
            case FUZZINESS_SEEKBAR_RESID /*21868*/:
                this.mFuzzinessValue = progress;
                this.mFuzzinessTextView.setText(new StringBuilder(FUZZINESS_STRING).append(getAmout(this.mFuzzinessValue)).toString());
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
                PointillizeFilter filter = new PointillizeFilter();
                filter.setEdgeColor(-16777216);
                filter.setScale((float) PointillizeFilterActivity.this.mSizeValue);
                filter.setRandomness(PointillizeFilterActivity.this.getAmout(PointillizeFilterActivity.this.mRandomnessValue));
                filter.setAmount(0.0f);
                filter.setFuzziness(PointillizeFilterActivity.this.getAmout(PointillizeFilterActivity.this.mFuzzinessValue));
                filter.setGridType(PointillizeFilterActivity.this.getSelectType());
                PointillizeFilterActivity.this.mColors = filter.filter(PointillizeFilterActivity.this.mColors, width, height);
                PointillizeFilterActivity pointillizeFilterActivity = PointillizeFilterActivity.this;
                final int i = width;
                final int i2 = height;
                pointillizeFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        PointillizeFilterActivity.this.setModifyView(PointillizeFilterActivity.this.mColors, i, i2);
                    }
                });
                PointillizeFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getAmout(int value) {
        return ((float) value) / 100.0f;
    }

    private int getSelectType() {
        if (this.mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE1_STRING)) {
            return 1;
        }
        if (this.mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE2_STRING)) {
            return 2;
        }
        if (this.mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE3_STRING)) {
            return 3;
        }
        return 4;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        applyFilter();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
