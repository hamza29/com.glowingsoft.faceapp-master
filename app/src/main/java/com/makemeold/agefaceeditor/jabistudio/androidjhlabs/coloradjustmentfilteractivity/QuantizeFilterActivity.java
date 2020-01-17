package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

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
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.DiffusionFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class QuantizeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener, OnCheckedChangeListener {
    private static final int COLORDITHER_CHECKBOX_RESID = 21864;
    private static final String COLORDITHER_STRING = "COLORDITHER";
    private static final int MAX_VALUE = 16;
    private static final int NUNBER_COLOR_SEEKBAR_RESID = 21863;
    private static final String NUNBER_COLOR_STRING = "NUNBER COLOR:";
    private static final int SERPENTINE_CHECKBOX_RESID = 21865;
    private static final String SERPENTINE_STRING = "SERPENTINE";
    private static final String TITLE = "Quantize";
    private CheckBox mColorDitherCheckBox;
    private int[] mColors;
    private boolean mIsColorDither = false;
    private boolean mIsSerpentine = false;
    private SeekBar mNumberColorSeekBar;
    private TextView mNumberColorTextView;
    private int mNumberColorValue;
    private ProgressDialog mProgressDialog;
    private CheckBox mSerpentineCheckBox;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mNumberColorTextView = new TextView(this);
        this.mNumberColorTextView.setText(new StringBuilder(NUNBER_COLOR_STRING).append(this.mNumberColorValue).toString());
        this.mNumberColorTextView.setTextSize(22.0f);
        this.mNumberColorTextView.setTextColor(-16777216);
        this.mNumberColorTextView.setGravity(17);
        this.mNumberColorSeekBar = new SeekBar(this);
        this.mNumberColorSeekBar.setOnSeekBarChangeListener(this);
        this.mNumberColorSeekBar.setId(NUNBER_COLOR_SEEKBAR_RESID);
        this.mNumberColorSeekBar.setMax(16);
        this.mColorDitherCheckBox = new CheckBox(this);
        this.mColorDitherCheckBox.setText(COLORDITHER_STRING);
        this.mColorDitherCheckBox.setTextSize(22.0f);
        this.mColorDitherCheckBox.setTextColor(-16777216);
        this.mColorDitherCheckBox.setGravity(17);
        this.mColorDitherCheckBox.setOnCheckedChangeListener(this);
        this.mColorDitherCheckBox.setId(COLORDITHER_CHECKBOX_RESID);
        this.mSerpentineCheckBox = new CheckBox(this);
        this.mSerpentineCheckBox.setText(SERPENTINE_STRING);
        this.mSerpentineCheckBox.setTextSize(22.0f);
        this.mSerpentineCheckBox.setTextColor(-16777216);
        this.mSerpentineCheckBox.setGravity(17);
        this.mSerpentineCheckBox.setOnCheckedChangeListener(this);
        this.mSerpentineCheckBox.setId(SERPENTINE_CHECKBOX_RESID);
        mainLayout.addView(this.mNumberColorTextView);
        mainLayout.addView(this.mNumberColorSeekBar);
        mainLayout.addView(this.mColorDitherCheckBox);
        mainLayout.addView(this.mSerpentineCheckBox);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case NUNBER_COLOR_SEEKBAR_RESID /*21863*/:
                this.mNumberColorValue = progress;
                this.mNumberColorTextView.setText(new StringBuilder(NUNBER_COLOR_STRING).append(this.mNumberColorValue).toString());
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

    public void onCheckedChanged(CompoundButton compoundbutton, boolean isChecked) {
        switch (compoundbutton.getId()) {
            case COLORDITHER_CHECKBOX_RESID /*21864*/:
                this.mIsColorDither = isChecked;
                applyFilter();
                return;
            case SERPENTINE_CHECKBOX_RESID /*21865*/:
                this.mIsSerpentine = isChecked;
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
                DiffusionFilter filter = new DiffusionFilter();
                filter.setColorDither(QuantizeFilterActivity.this.mIsColorDither);
                filter.setSerpentine(QuantizeFilterActivity.this.mIsSerpentine);
                filter.setLevels(QuantizeFilterActivity.this.mNumberColorValue);
                QuantizeFilterActivity.this.mColors = filter.filter(QuantizeFilterActivity.this.mColors, width, height);
                QuantizeFilterActivity quantizeFilterActivity = QuantizeFilterActivity.this;
                final int i = width;
                final int i2 = height;
                quantizeFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        QuantizeFilterActivity.this.setModifyView(QuantizeFilterActivity.this.mColors, i, i2);
                    }
                });
                QuantizeFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
