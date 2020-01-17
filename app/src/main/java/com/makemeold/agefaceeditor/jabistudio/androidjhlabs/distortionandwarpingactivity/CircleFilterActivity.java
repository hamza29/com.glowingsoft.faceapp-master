package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.CircleFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class CircleFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int ANGLE_SEEKBAR_RESID = 21867;
    private static final String ANGLE_STRING = "ANGLE:";
    private static final int CENTERX_SEEKBAR_RESID = 21863;
    private static final String CENTERX_STRING = "CENTERX:";
    private static final int CENTERY_SEEKBAR_RESID = 21864;
    private static final String CENTERY_STRING = "CENTERY:";
    private static final int HEIGHT_SEEKBAR_RESID = 21866;
    private static final String HEIGHT_STRING = "HEIGHT:";
    private static final int MAX_ANGLE_VALUE = 314;
    private static final int MAX_SPREAD_ANGLE_VALUE = 628;
    private static final int MAX_VALUE = 100;
    private static final int RADIUS_SEEKBAR_RESID = 21865;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final int SPREAD_SEEKBAR_RESID = 21868;
    private static final String SPREAD_STRING = "SPREAD:";
    private static final String TITLE = "Circle";
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
    private SeekBar mHeightSeekBar;
    private TextView mHeightTextView;
    private int mHeightValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private int mRadiusValue;
    private SeekBar mSpreadSeekBar;
    private TextView mSpreadTextView;
    private int mSpreadValue;

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
        this.mRadiusSeekBar.setMax(100);
        this.mHeightTextView = new TextView(this);
        this.mHeightTextView.setText(new StringBuilder(HEIGHT_STRING).append(this.mHeightValue).toString());
        this.mHeightTextView.setTextSize(22.0f);
        this.mHeightTextView.setTextColor(-16777216);
        this.mHeightTextView.setGravity(17);
        this.mHeightSeekBar = new SeekBar(this);
        this.mHeightSeekBar.setOnSeekBarChangeListener(this);
        this.mHeightSeekBar.setId(HEIGHT_SEEKBAR_RESID);
        this.mHeightSeekBar.setMax(100);
        this.mHeightSeekBar.setProgress(50);
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
        this.mSpreadTextView = new TextView(this);
        this.mSpreadTextView.setText(new StringBuilder(SPREAD_STRING).append(this.mSpreadValue).toString());
        this.mSpreadTextView.setTextSize(22.0f);
        this.mSpreadTextView.setTextColor(-16777216);
        this.mSpreadTextView.setGravity(17);
        this.mSpreadSeekBar = new SeekBar(this);
        this.mSpreadSeekBar.setOnSeekBarChangeListener(this);
        this.mSpreadSeekBar.setId(SPREAD_SEEKBAR_RESID);
        this.mSpreadSeekBar.setMax(MAX_SPREAD_ANGLE_VALUE);
        mainLayout.addView(this.mCenterXTextView);
        mainLayout.addView(this.mCenterXSeekBar);
        mainLayout.addView(this.mCenterYTextView);
        mainLayout.addView(this.mCenterYSeekBar);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
        mainLayout.addView(this.mHeightTextView);
        mainLayout.addView(this.mHeightSeekBar);
        mainLayout.addView(this.mAngleTextView);
        mainLayout.addView(this.mAngleSeekBar);
        mainLayout.addView(this.mSpreadTextView);
        mainLayout.addView(this.mSpreadSeekBar);
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
            case RADIUS_SEEKBAR_RESID /*21865*/:
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
                return;
            case HEIGHT_SEEKBAR_RESID /*21866*/:
                this.mHeightValue = progress;
                this.mHeightTextView.setText(new StringBuilder(HEIGHT_STRING).append(this.mHeightValue).toString());
                return;
            case ANGLE_SEEKBAR_RESID /*21867*/:
                this.mAngleValue = progress;
                this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(getAngle(this.mAngleValue)).toString());
                return;
            case SPREAD_SEEKBAR_RESID /*21868*/:
                this.mSpreadValue = progress;
                this.mSpreadTextView.setText(new StringBuilder(SPREAD_STRING).append(getValue(this.mSpreadValue)).toString());
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
                CircleFilter filter = new CircleFilter();
                filter.setCentreX(CircleFilterActivity.this.getValue(CircleFilterActivity.this.mCenterXValue));
                filter.setCentreY(CircleFilterActivity.this.getValue(CircleFilterActivity.this.mCenterYValue));
                filter.setAngle(CircleFilterActivity.this.getAngle(CircleFilterActivity.this.mAngleValue));
                filter.setHeight((float) CircleFilterActivity.this.mHeightValue);
                filter.setRadius((float) CircleFilterActivity.this.mRadiusValue);
                filter.setSpreadAngle(CircleFilterActivity.this.getValue(CircleFilterActivity.this.mSpreadValue));
                CircleFilterActivity.this.mColors = filter.filter(CircleFilterActivity.this.mColors, width, height);
                CircleFilterActivity circleFilterActivity = CircleFilterActivity.this;
                final int i = width;
                final int i2 = height;
                circleFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        CircleFilterActivity.this.setModifyView(CircleFilterActivity.this.mColors, i, i2);
                    }
                });
                CircleFilterActivity.this.mProgressDialog.dismiss();
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
