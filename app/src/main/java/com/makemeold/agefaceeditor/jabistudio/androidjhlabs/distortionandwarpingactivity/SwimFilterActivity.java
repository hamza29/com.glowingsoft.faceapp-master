package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.SwimFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class SwimFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int AMOUNT_SEEKBAR_RESID = 21867;
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final int ANGLE_SEEKBAR_RESID = 21864;
    private static final String ANGLE_STRING = "ANGLE:";
    private static final int MAX_ANGLE_VALUE = 624;
    private static final int MAX_SCALE_VALUE = 300;
    private static final int MAX_STRETCH_VALUE = 50;
    private static final int MAX_TURBULENCE_VALUE = 10;
    private static final int MAX_VALUE = 100;
    private static final int SCALE_SEEKBAR_RESID = 21863;
    private static final String SCALE_STRING = "SCALE:";
    private static final int STRETCH_SEEKBAR_RESID = 21865;
    private static final String STRETCH_STRING = "STRETCH:";
    private static final int TIME_SEEKBAR_RESID = 21868;
    private static final String TIME_STRING = "TIME:";
    private static final String TITLE = "Swim";
    private static final int TURBULENCE_SEEKBAR_RESID = 21866;
    private static final String TURBULENCE_STRING = "TURBULENCE:";
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private int mAmountValue;
    private SeekBar mAngleSeekBar;
    private TextView mAngleTextView;
    private int mAngleValue;
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    private int mScaleValue;
    private SeekBar mStretchSeekBar;
    private TextView mStretchTextView;
    private int mStretchValue;
    private SeekBar mTimeSeekBar;
    private TextView mTimeTextView;
    private int mTimeValue;
    private SeekBar mTurbulenceSeekBar;
    private TextView mTurbulenceTextView;
    private int mTurbulenceValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mScaleTextView = new TextView(this);
        this.mScaleTextView.setText(new StringBuilder(SCALE_STRING).append(this.mScaleValue).toString());
        this.mScaleTextView.setTextSize(22.0f);
        this.mScaleTextView.setTextColor(-16777216);
        this.mScaleTextView.setGravity(17);
        this.mScaleSeekBar = new SeekBar(this);
        this.mScaleSeekBar.setOnSeekBarChangeListener(this);
        this.mScaleSeekBar.setId(SCALE_SEEKBAR_RESID);
        this.mScaleSeekBar.setMax(300);
        this.mAngleTextView = new TextView(this);
        this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(this.mAngleValue).toString());
        this.mAngleTextView.setTextSize(22.0f);
        this.mAngleTextView.setTextColor(-16777216);
        this.mAngleTextView.setGravity(17);
        this.mAngleSeekBar = new SeekBar(this);
        this.mAngleSeekBar.setOnSeekBarChangeListener(this);
        this.mAngleSeekBar.setId(ANGLE_SEEKBAR_RESID);
        this.mAngleSeekBar.setMax(MAX_ANGLE_VALUE);
        this.mStretchTextView = new TextView(this);
        this.mStretchTextView.setText(new StringBuilder(STRETCH_STRING).append(this.mStretchValue).toString());
        this.mStretchTextView.setTextSize(22.0f);
        this.mStretchTextView.setTextColor(-16777216);
        this.mStretchTextView.setGravity(17);
        this.mStretchSeekBar = new SeekBar(this);
        this.mStretchSeekBar.setOnSeekBarChangeListener(this);
        this.mStretchSeekBar.setId(STRETCH_SEEKBAR_RESID);
        this.mStretchSeekBar.setMax(50);
        this.mTurbulenceTextView = new TextView(this);
        this.mTurbulenceTextView.setText(new StringBuilder(TURBULENCE_STRING).append(this.mTurbulenceValue).toString());
        this.mTurbulenceTextView.setTextSize(22.0f);
        this.mTurbulenceTextView.setTextColor(-16777216);
        this.mTurbulenceTextView.setGravity(17);
        this.mTurbulenceSeekBar = new SeekBar(this);
        this.mTurbulenceSeekBar.setOnSeekBarChangeListener(this);
        this.mTurbulenceSeekBar.setId(TURBULENCE_SEEKBAR_RESID);
        this.mTurbulenceSeekBar.setMax(10);
        this.mAmountTextView = new TextView(this);
        this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(this.mAmountValue).toString());
        this.mAmountTextView.setTextSize(22.0f);
        this.mAmountTextView.setTextColor(-16777216);
        this.mAmountTextView.setGravity(17);
        this.mAmountSeekBar = new SeekBar(this);
        this.mAmountSeekBar.setOnSeekBarChangeListener(this);
        this.mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        this.mAmountSeekBar.setMax(100);
        this.mTimeTextView = new TextView(this);
        this.mTimeTextView.setText(new StringBuilder(TIME_STRING).append(this.mTimeValue).toString());
        this.mTimeTextView.setTextSize(22.0f);
        this.mTimeTextView.setTextColor(-16777216);
        this.mTimeTextView.setGravity(17);
        this.mTimeSeekBar = new SeekBar(this);
        this.mTimeSeekBar.setOnSeekBarChangeListener(this);
        this.mTimeSeekBar.setId(TIME_SEEKBAR_RESID);
        this.mTimeSeekBar.setMax(100);
        mainLayout.addView(this.mScaleTextView);
        mainLayout.addView(this.mScaleSeekBar);
        mainLayout.addView(this.mAngleTextView);
        mainLayout.addView(this.mAngleSeekBar);
        mainLayout.addView(this.mStretchTextView);
        mainLayout.addView(this.mStretchSeekBar);
        mainLayout.addView(this.mTurbulenceTextView);
        mainLayout.addView(this.mTurbulenceSeekBar);
        mainLayout.addView(this.mAmountTextView);
        mainLayout.addView(this.mAmountSeekBar);
        mainLayout.addView(this.mTimeTextView);
        mainLayout.addView(this.mTimeSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case SCALE_SEEKBAR_RESID /*21863*/:
                this.mScaleValue = progress;
                this.mScaleTextView.setText(new StringBuilder(SCALE_STRING).append(this.mScaleValue).toString());
                return;
            case ANGLE_SEEKBAR_RESID /*21864*/:
                this.mAngleValue = progress;
                this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(getValue(this.mAngleValue)).toString());
                return;
            case STRETCH_SEEKBAR_RESID /*21865*/:
                this.mStretchValue = progress;
                this.mStretchTextView.setText(new StringBuilder(STRETCH_STRING).append(this.mStretchValue).toString());
                return;
            case TURBULENCE_SEEKBAR_RESID /*21866*/:
                this.mTurbulenceValue = progress;
                this.mTurbulenceTextView.setText(new StringBuilder(TURBULENCE_STRING).append(this.mTurbulenceValue).toString());
                return;
            case AMOUNT_SEEKBAR_RESID /*21867*/:
                this.mAmountValue = progress;
                this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(this.mAmountValue).toString());
                return;
            case TIME_SEEKBAR_RESID /*21868*/:
                this.mTimeValue = progress;
                this.mTimeTextView.setText(new StringBuilder(TIME_STRING).append(this.mTimeValue).toString());
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
                SwimFilter filter = new SwimFilter();
                filter.setScale((float) (SwimFilterActivity.this.mScaleValue + 1));
                filter.setAngle(SwimFilterActivity.this.getValue(SwimFilterActivity.this.mAngleValue));
                filter.setStretch((float) (SwimFilterActivity.this.mStretchValue + 1));
                filter.setTurbulence((float) (SwimFilterActivity.this.mTurbulenceValue + 1));
                filter.setAmount((float) SwimFilterActivity.this.mAmountValue);
                filter.setTime((float) SwimFilterActivity.this.mTimeValue);
                SwimFilterActivity.this.mColors = filter.filter(SwimFilterActivity.this.mColors, width, height);
                SwimFilterActivity swimFilterActivity = SwimFilterActivity.this;
                final int i = width;
                final int i2 = height;
                swimFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        SwimFilterActivity.this.setModifyView(SwimFilterActivity.this.mColors, i, i2);
                    }
                });
                SwimFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
