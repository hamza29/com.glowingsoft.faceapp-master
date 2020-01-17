package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.KaleidoscopeFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class KaleidoscopeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int ANGLE2_SEEKBAR_RESID = 21866;
    private static final String ANGLE2_STRING = "ANGLE2:";
    private static final int ANGLE_SEEKBAR_RESID = 21865;
    private static final String ANGLE_STRING = "ANGLE:";
    private static final int CENTERX_SEEKBAR_RESID = 21863;
    private static final String CENTERX_STRING = "CENTERX:";
    private static final int CENTERY_SEEKBAR_RESID = 21864;
    private static final String CENTERY_STRING = "CENTERY:";
    private static final int MAX_ANGLE_VALUE = 314;
    private static final int MAX_VALUE = 100;
    private static final int RADIUS_SEEKBAR_RESID = 21867;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final int SIDES_MAX_VALUE = 32;
    private static final int SIDES_SEEKBAR_RESID = 21868;
    private static final String SIDES_STRING = "SIDES:";
    private static final String TITLE = "Kaleidoscope";
    private SeekBar mAngle2SeekBar;
    private TextView mAngle2TextView;
    private int mAngle2Value;
    private SeekBar mAngleSeekBar;
    private TextView mAngleTextView;
    private int mAngleValue;
    private SeekBar mCenterXSeekBar;
    private TextView mCenterXTextView;
    private int mCenterXValue;
    private SeekBar mCenterYSeekBar;
    private TextView mCenterYTextView;
    private int mCenterYValue;
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private int mRadiusValue;
    private SeekBar mSidesSeekBar;
    private TextView mSidesTextView;
    private int mSidesValue;

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
        this.mAngleTextView = new TextView(this);
        this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(this.mAngleValue).toString());
        this.mAngleTextView.setTextSize(22.0f);
        this.mAngleTextView.setTextColor(-16777216);
        this.mAngleTextView.setGravity(17);
        this.mAngleSeekBar = new SeekBar(this);
        this.mAngleSeekBar.setOnSeekBarChangeListener(this);
        this.mAngleSeekBar.setId(ANGLE_SEEKBAR_RESID);
        this.mAngleSeekBar.setMax(MAX_ANGLE_VALUE);
        this.mAngleSeekBar.setProgress(157);
        this.mAngle2TextView = new TextView(this);
        this.mAngle2TextView.setText(new StringBuilder(ANGLE2_STRING).append(this.mAngle2Value).toString());
        this.mAngle2TextView.setTextSize(22.0f);
        this.mAngle2TextView.setTextColor(-16777216);
        this.mAngle2TextView.setGravity(17);
        this.mAngle2SeekBar = new SeekBar(this);
        this.mAngle2SeekBar.setOnSeekBarChangeListener(this);
        this.mAngle2SeekBar.setId(ANGLE2_SEEKBAR_RESID);
        this.mAngle2SeekBar.setMax(MAX_ANGLE_VALUE);
        this.mAngle2SeekBar.setProgress(157);
        this.mRadiusTextView = new TextView(this);
        this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
        this.mRadiusTextView.setTextSize(22.0f);
        this.mRadiusTextView.setTextColor(-16777216);
        this.mRadiusTextView.setGravity(17);
        this.mRadiusSeekBar = new SeekBar(this);
        this.mRadiusSeekBar.setOnSeekBarChangeListener(this);
        this.mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        this.mRadiusSeekBar.setMax(100);
        this.mSidesTextView = new TextView(this);
        this.mSidesTextView.setText(new StringBuilder(SIDES_STRING).append(this.mSidesValue).toString());
        this.mSidesTextView.setTextSize(22.0f);
        this.mSidesTextView.setTextColor(-16777216);
        this.mSidesTextView.setGravity(17);
        this.mSidesSeekBar = new SeekBar(this);
        this.mSidesSeekBar.setOnSeekBarChangeListener(this);
        this.mSidesSeekBar.setId(SIDES_SEEKBAR_RESID);
        this.mSidesSeekBar.setMax(32);
        mainLayout.addView(this.mCenterXTextView);
        mainLayout.addView(this.mCenterXSeekBar);
        mainLayout.addView(this.mCenterYTextView);
        mainLayout.addView(this.mCenterYSeekBar);
        mainLayout.addView(this.mAngleTextView);
        mainLayout.addView(this.mAngleSeekBar);
        mainLayout.addView(this.mAngle2TextView);
        mainLayout.addView(this.mAngle2SeekBar);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
        mainLayout.addView(this.mSidesTextView);
        mainLayout.addView(this.mSidesSeekBar);
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
            case ANGLE_SEEKBAR_RESID /*21865*/:
                this.mAngleValue = progress;
                this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(getAngle(this.mAngleValue)).toString());
                return;
            case ANGLE2_SEEKBAR_RESID /*21866*/:
                this.mAngle2Value = progress;
                this.mAngle2TextView.setText(new StringBuilder(ANGLE2_STRING).append(getAngle(this.mAngle2Value)).toString());
                return;
            case RADIUS_SEEKBAR_RESID /*21867*/:
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
                return;
            case SIDES_SEEKBAR_RESID /*21868*/:
                this.mSidesValue = progress;
                this.mSidesTextView.setText(new StringBuilder(SIDES_STRING).append(this.mSidesValue).toString());
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
                KaleidoscopeFilter filter = new KaleidoscopeFilter();
                filter.setCentreX(KaleidoscopeFilterActivity.this.getValue(KaleidoscopeFilterActivity.this.mCenterXValue));
                filter.setCentreY(KaleidoscopeFilterActivity.this.getValue(KaleidoscopeFilterActivity.this.mCenterYValue));
                filter.setAngle(KaleidoscopeFilterActivity.this.getAngle(KaleidoscopeFilterActivity.this.mAngleValue));
                filter.setAngle2(KaleidoscopeFilterActivity.this.getAngle(KaleidoscopeFilterActivity.this.mAngle2Value));
                filter.setRadius((float) KaleidoscopeFilterActivity.this.mRadiusValue);
                filter.setSides(KaleidoscopeFilterActivity.this.mSidesValue);
                KaleidoscopeFilterActivity.this.mColors = filter.filter(KaleidoscopeFilterActivity.this.mColors, width, height);
                KaleidoscopeFilterActivity kaleidoscopeFilterActivity = KaleidoscopeFilterActivity.this;
                final int i = width;
                final int i2 = height;
                kaleidoscopeFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        KaleidoscopeFilterActivity.this.setModifyView(KaleidoscopeFilterActivity.this.mColors, i, i2);
                    }
                });
                KaleidoscopeFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getAngle(int value) {
        return ((float) (value - 157)) / 100.0f;
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
