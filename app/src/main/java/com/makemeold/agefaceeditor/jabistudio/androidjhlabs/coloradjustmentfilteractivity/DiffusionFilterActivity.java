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

public class DiffusionFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener, OnCheckedChangeListener {
    private static final int COLORDITHER_CHECKBOX_RESID = 21864;
    private static final String COLORDITHER_STRING = "COLORDITHER";
    private static final int LEVEL_SEEKBAR_RESID = 21863;
    private static final String LEVEL_STRING = "LEVEL:";
    private static final int MAX_VALUE = 16;
    private static final int SERPENTINE_CHECKBOX_RESID = 21865;
    private static final String SERPENTINE_STRING = "SERPENTINE";
    private static final String TITLE = "Diffusion";
    private CheckBox mColorDitherCheckBox;
    private int[] mColors;
    private boolean mIsColorDither = false;
    private boolean mIsSerpentine = false;
    private SeekBar mLevelSeekBar;
    private TextView mLevelTextView;
    private int mLevelValue;
    private ProgressDialog mProgressDialog;
    private CheckBox mSerpentineCheckBox;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mLevelTextView = new TextView(this);
        this.mLevelTextView.setText(new StringBuilder(LEVEL_STRING).append(this.mLevelValue).toString());
        this.mLevelTextView.setTextSize(22.0f);
        this.mLevelTextView.setTextColor(-16777216);
        this.mLevelTextView.setGravity(17);
        this.mLevelSeekBar = new SeekBar(this);
        this.mLevelSeekBar.setOnSeekBarChangeListener(this);
        this.mLevelSeekBar.setId(LEVEL_SEEKBAR_RESID);
        this.mLevelSeekBar.setMax(16);
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
        mainLayout.addView(this.mLevelTextView);
        mainLayout.addView(this.mLevelSeekBar);
        mainLayout.addView(this.mColorDitherCheckBox);
        mainLayout.addView(this.mSerpentineCheckBox);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case LEVEL_SEEKBAR_RESID /*21863*/:
                this.mLevelValue = progress;
                this.mLevelTextView.setText(new StringBuilder(LEVEL_STRING).append(this.mLevelValue).toString());
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
                filter.setColorDither(DiffusionFilterActivity.this.mIsColorDither);
                filter.setSerpentine(DiffusionFilterActivity.this.mIsSerpentine);
                filter.setLevels(DiffusionFilterActivity.this.mLevelValue);
                DiffusionFilterActivity.this.mColors = filter.filter(DiffusionFilterActivity.this.mColors, width, height);
                DiffusionFilterActivity diffusionFilterActivity = DiffusionFilterActivity.this;
                final int i = width;
                final int i2 = height;
                diffusionFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        DiffusionFilterActivity.this.setModifyView(DiffusionFilterActivity.this.mColors, i, i2);
                    }
                });
                DiffusionFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
