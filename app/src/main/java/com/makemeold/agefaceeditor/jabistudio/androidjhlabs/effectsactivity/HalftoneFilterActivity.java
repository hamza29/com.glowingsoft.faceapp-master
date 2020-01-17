package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

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
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.C0666R;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.HalftoneFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class HalftoneFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener, OnCheckedChangeListener {
    private static final int INVERT_CHECKBOX_RESID = 21866;
    private static final String INVERT_STRING = "INVERT:";
    private static final int MAX_VALUE = 100;
    private static final int MONOCHROME_CHECKBOX_RESID = 21867;
    private static final String MONOCHROME_STRING = "MONOCHROME:";
    private static final int SOFTNESS_SEEKBAR_RESID = 21865;
    private static final String SOFTNESS_STRING = "SOFTNESS:";
    private static final String TITLE = "Halftone";
    private int[] mColors;
    private CheckBox mInvertCheckBox;
    private boolean mInvertValue;
    private CheckBox mMonochromeCheckBox;
    private boolean mMonochromeValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mSoftnessSeekBar;
    private TextView mSoftnessTextView;
    private int mSoftnessValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mSoftnessTextView = new TextView(this);
        this.mSoftnessTextView.setText(new StringBuilder(SOFTNESS_STRING).append(this.mSoftnessValue).toString());
        this.mSoftnessTextView.setTextSize(22.0f);
        this.mSoftnessTextView.setTextColor(-16777216);
        this.mSoftnessTextView.setGravity(17);
        this.mSoftnessSeekBar = new SeekBar(this);
        this.mSoftnessSeekBar.setOnSeekBarChangeListener(this);
        this.mSoftnessSeekBar.setId(SOFTNESS_SEEKBAR_RESID);
        this.mSoftnessSeekBar.setMax(100);
        this.mSoftnessSeekBar.setProgress(50);
        this.mInvertCheckBox = new CheckBox(this);
        this.mInvertCheckBox.setText(INVERT_STRING);
        this.mInvertCheckBox.setTextSize(22.0f);
        this.mInvertCheckBox.setTextColor(-16777216);
        this.mInvertCheckBox.setGravity(17);
        this.mInvertCheckBox.setOnCheckedChangeListener(this);
        this.mInvertCheckBox.setId(INVERT_CHECKBOX_RESID);
        this.mMonochromeCheckBox = new CheckBox(this);
        this.mMonochromeCheckBox.setText(MONOCHROME_STRING);
        this.mMonochromeCheckBox.setTextSize(22.0f);
        this.mMonochromeCheckBox.setTextColor(-16777216);
        this.mMonochromeCheckBox.setGravity(17);
        this.mMonochromeCheckBox.setOnCheckedChangeListener(this);
        this.mMonochromeCheckBox.setId(MONOCHROME_CHECKBOX_RESID);
        mainLayout.addView(this.mSoftnessTextView);
        mainLayout.addView(this.mSoftnessSeekBar);
        mainLayout.addView(this.mInvertCheckBox);
        mainLayout.addView(this.mMonochromeCheckBox);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case SOFTNESS_SEEKBAR_RESID /*21865*/:
                this.mSoftnessValue = progress;
                this.mSoftnessTextView.setText(new StringBuilder(SOFTNESS_STRING).append(this.mSoftnessValue).toString());
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
                HalftoneFilter filter = new HalftoneFilter();
                filter.setMask(AndroidUtils.drawableToIntArray(HalftoneFilterActivity.this.getResources().getDrawable(C0666R.drawable.halftone1)));
                filter.setMaskWidth(220);
                filter.setMaskHeight(220);
                filter.setSoftness(HalftoneFilterActivity.this.getValue(HalftoneFilterActivity.this.mSoftnessValue));
                filter.setInvert(HalftoneFilterActivity.this.mInvertValue);
                filter.setMonochrome(HalftoneFilterActivity.this.mMonochromeValue);
                HalftoneFilterActivity.this.mColors = filter.filter(HalftoneFilterActivity.this.mColors, width, height);
                HalftoneFilterActivity halftoneFilterActivity = HalftoneFilterActivity.this;
                final int i = width;
                final int i2 = height;
                halftoneFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        HalftoneFilterActivity.this.setModifyView(HalftoneFilterActivity.this.mColors, i, i2);
                    }
                });
                HalftoneFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case INVERT_CHECKBOX_RESID /*21866*/:
                this.mInvertValue = isChecked;
                applyFilter();
                return;
            case MONOCHROME_CHECKBOX_RESID /*21867*/:
                this.mMonochromeValue = isChecked;
                applyFilter();
                return;
            default:
                return;
        }
    }
}
