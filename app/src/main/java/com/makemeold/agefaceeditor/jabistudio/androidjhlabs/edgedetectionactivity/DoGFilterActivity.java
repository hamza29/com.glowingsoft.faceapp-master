package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.edgedetectionactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.DoGFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class DoGFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener, OnCheckedChangeListener {
    private static final int INVERT_CHECKBOX_RESID = 21865;
    private static final String INVERT_STRING = "INVERT:";
    private static final int MAX_VALUE = 1000;
    private static final int NORMALIZE_CHECKBOX_RESID = 21866;
    private static final String NORMALIZE_STRING = "NORMALIZE:";
    private static final int RADIUS1_SEEKBAR_RESID = 21863;
    private static final String RADIUS1_STRING = "RADIUS1:";
    private static final int RADIUS2_SEEKBAR_RESID = 21864;
    private static final String RADIUS2_STRING = "RADIUS2:";
    private static final String TITLE = "DoG";
    private int[] mColors;
    private CheckBox mInvertCheckBox;
    private boolean mIsInvert;
    private boolean mIsNormalize;
    private CheckBox mNormalizeCheckBox;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadius1SeekBar;
    private TextView mRadius1TextView;
    private int mRadius1Value;
    private SeekBar mRadius2SeekBar;
    private TextView mRadius2TextView;
    private int mRadius2Value;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mRadius1TextView = new TextView(this);
        this.mRadius1TextView.setText(new StringBuilder(RADIUS1_STRING).append(this.mRadius1Value).toString());
        this.mRadius1TextView.setTextSize(22.0f);
        this.mRadius1TextView.setTextColor(-16777216);
        this.mRadius1TextView.setGravity(17);
        this.mRadius1SeekBar = new SeekBar(this);
        this.mRadius1SeekBar.setOnSeekBarChangeListener(this);
        this.mRadius1SeekBar.setId(RADIUS1_SEEKBAR_RESID);
        this.mRadius1SeekBar.setMax(1000);
        this.mRadius2TextView = new TextView(this);
        this.mRadius2TextView.setText(new StringBuilder(RADIUS2_STRING).append(this.mRadius2Value).toString());
        this.mRadius2TextView.setTextSize(22.0f);
        this.mRadius2TextView.setTextColor(-16777216);
        this.mRadius2TextView.setGravity(17);
        this.mRadius2SeekBar = new SeekBar(this);
        this.mRadius2SeekBar.setOnSeekBarChangeListener(this);
        this.mRadius2SeekBar.setId(RADIUS2_SEEKBAR_RESID);
        this.mRadius2SeekBar.setMax(1000);
        this.mInvertCheckBox = new CheckBox(this);
        this.mInvertCheckBox.setText(INVERT_STRING);
        this.mInvertCheckBox.setTextSize(22.0f);
        this.mInvertCheckBox.setTextColor(-16777216);
        this.mInvertCheckBox.setGravity(17);
        this.mInvertCheckBox.setOnCheckedChangeListener(this);
        this.mInvertCheckBox.setId(INVERT_CHECKBOX_RESID);
        this.mNormalizeCheckBox = new CheckBox(this);
        this.mNormalizeCheckBox.setText(NORMALIZE_STRING);
        this.mNormalizeCheckBox.setTextSize(22.0f);
        this.mNormalizeCheckBox.setTextColor(-16777216);
        this.mNormalizeCheckBox.setGravity(17);
        this.mNormalizeCheckBox.setOnCheckedChangeListener(this);
        this.mNormalizeCheckBox.setId(NORMALIZE_CHECKBOX_RESID);
        mainLayout.addView(this.mRadius1TextView);
        mainLayout.addView(this.mRadius1SeekBar);
        mainLayout.addView(this.mRadius2TextView);
        mainLayout.addView(this.mRadius2SeekBar);
        mainLayout.addView(this.mInvertCheckBox);
        mainLayout.addView(this.mNormalizeCheckBox);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case RADIUS1_SEEKBAR_RESID /*21863*/:
                this.mRadius1Value = progress;
                this.mRadius1TextView.setText(new StringBuilder(RADIUS1_STRING).append(getAmout(this.mRadius1Value)).toString());
                return;
            case RADIUS2_SEEKBAR_RESID /*21864*/:
                this.mRadius2Value = progress;
                this.mRadius2TextView.setText(new StringBuilder(RADIUS2_STRING).append(getAmout(this.mRadius2Value)).toString());
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

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case INVERT_CHECKBOX_RESID /*21865*/:
                this.mIsInvert = isChecked;
                applyFilter();
                return;
            case NORMALIZE_CHECKBOX_RESID /*21866*/:
                this.mIsNormalize = isChecked;
                applyFilter();
                return;
            default:
                return;
        }
    }

    private void applyFilter() {
        final int width = this.mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = this.mOriginalImageView.getDrawable().getIntrinsicHeight();
        this.mColors = AndroidUtils.drawableToIntArray(this.mOriginalImageView.getDrawable());
        this.mProgressDialog = ProgressDialog.show(this, BuildConfig.FLAVOR, "Wait......");
        Thread thread = new Thread() {
            public void run() {
                DoGFilter filter = new DoGFilter();
                filter.setInvert(DoGFilterActivity.this.mIsInvert);
                filter.setNormalize(DoGFilterActivity.this.mIsNormalize);
                filter.setRadius1(DoGFilterActivity.this.getAmout(DoGFilterActivity.this.mRadius1Value));
                filter.setRadius2(DoGFilterActivity.this.getAmout(DoGFilterActivity.this.mRadius2Value));
                DoGFilterActivity.this.mColors = filter.filter(DoGFilterActivity.this.mColors, width, height);
                DoGFilterActivity doGFilterActivity = DoGFilterActivity.this;
                final int i = width;
                final int i2 = height;
                doGFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        DoGFilterActivity.this.setModifyView(DoGFilterActivity.this.mColors, i, i2);
                    }
                });
                DoGFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getAmout(int value) {
        return ((float) value) / 100.0f;
    }
}
