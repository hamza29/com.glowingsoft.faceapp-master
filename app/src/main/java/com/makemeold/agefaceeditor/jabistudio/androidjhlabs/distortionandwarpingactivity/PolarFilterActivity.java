package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.PolarFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class PolarFilterActivity extends SuperFilterActivity implements OnCheckedChangeListener, OnClickListener {
    private static final String APPLY_STRING = "Apply";
    private static final int INVERT_IN_CIRCLE_CHECKBOX_RESID = 21866;
    private static final String INVERT_IN_CIRCLE_STRING = "INVERT IN CIRCLE";
    private static final int POLAR_TO_RECT_CHECKBOX_RESID = 21865;
    private static final String POLAR_TO_RECT_STRING = "POLAR TO RECT";
    private static final int RECT_TO_POLAR_CHECKBOX_RESID = 21864;
    private static final String RECT_TO_POLAR_STRING = "RECT TO POLAR";
    private static final String TITLE = "Polar";
    private int[] mColors;
    private RadioButton mInvertInCircleRadioButton;
    private boolean mIsInvertInCircle = false;
    private boolean mIsPolarToRect = false;
    private boolean mIsRectToPolar = false;
    private RadioButton mPolarToRectRadioButton;
    private ProgressDialog mProgressDialog;
    private RadioGroup mRadioGroup;
    private RadioButton mRectToPolarRadioButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mRadioGroup = new RadioGroup(this);
        this.mRectToPolarRadioButton = new RadioButton(this);
        this.mRectToPolarRadioButton.setText(RECT_TO_POLAR_STRING);
        this.mRectToPolarRadioButton.setTextSize(22.0f);
        this.mRectToPolarRadioButton.setTextColor(-16777216);
        this.mRectToPolarRadioButton.setGravity(17);
        this.mRectToPolarRadioButton.setOnCheckedChangeListener(this);
        this.mRectToPolarRadioButton.setId(RECT_TO_POLAR_CHECKBOX_RESID);
        this.mPolarToRectRadioButton = new RadioButton(this);
        this.mPolarToRectRadioButton.setText(POLAR_TO_RECT_STRING);
        this.mPolarToRectRadioButton.setTextSize(22.0f);
        this.mPolarToRectRadioButton.setTextColor(-16777216);
        this.mPolarToRectRadioButton.setGravity(17);
        this.mPolarToRectRadioButton.setOnCheckedChangeListener(this);
        this.mPolarToRectRadioButton.setId(POLAR_TO_RECT_CHECKBOX_RESID);
        this.mInvertInCircleRadioButton = new RadioButton(this);
        this.mInvertInCircleRadioButton.setText(INVERT_IN_CIRCLE_STRING);
        this.mInvertInCircleRadioButton.setTextSize(22.0f);
        this.mInvertInCircleRadioButton.setTextColor(-16777216);
        this.mInvertInCircleRadioButton.setGravity(17);
        this.mInvertInCircleRadioButton.setOnCheckedChangeListener(this);
        this.mInvertInCircleRadioButton.setId(INVERT_IN_CIRCLE_CHECKBOX_RESID);
        this.mRadioGroup.addView(this.mRectToPolarRadioButton);
        this.mRadioGroup.addView(this.mPolarToRectRadioButton);
        this.mRadioGroup.addView(this.mInvertInCircleRadioButton);
        Button button = new Button(this);
        button.setText(APPLY_STRING);
        button.setOnClickListener(this);
        mainLayout.addView(this.mRadioGroup);
        mainLayout.addView(button);
    }

    public void onCheckedChanged(CompoundButton compoundbutton, boolean isChecked) {
        switch (compoundbutton.getId()) {
            case RECT_TO_POLAR_CHECKBOX_RESID /*21864*/:
                this.mIsRectToPolar = isChecked;
                return;
            case POLAR_TO_RECT_CHECKBOX_RESID /*21865*/:
                this.mIsPolarToRect = isChecked;
                return;
            case INVERT_IN_CIRCLE_CHECKBOX_RESID /*21866*/:
                this.mIsInvertInCircle = isChecked;
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
                PolarFilter filter = new PolarFilter();
                if (PolarFilterActivity.this.mIsRectToPolar) {
                    filter.setType(0);
                } else if (PolarFilterActivity.this.mIsPolarToRect) {
                    filter.setType(1);
                } else if (PolarFilterActivity.this.mIsInvertInCircle) {
                    filter.setType(2);
                }
                PolarFilterActivity.this.mColors = filter.filter(PolarFilterActivity.this.mColors, width, height);
                PolarFilterActivity polarFilterActivity = PolarFilterActivity.this;
                final int i = width;
                final int i2 = height;
                polarFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        PolarFilterActivity.this.setModifyView(PolarFilterActivity.this.mColors, i, i2);
                    }
                });
                PolarFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void onClick(View v) {
        applyFilter();
    }
}
