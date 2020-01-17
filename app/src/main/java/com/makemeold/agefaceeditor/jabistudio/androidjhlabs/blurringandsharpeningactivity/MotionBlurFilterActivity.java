package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.MotionBlurOp;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class MotionBlurFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int ANGLE_SEEKBAR_RESID = 21867;
    private static final String ANGLE_STRING = "ANGLE:";
    private static final int CENTERX_SEEKBAR_RESID = 21865;
    private static final String CENTERX_STRING = "CENTERX:";
    private static final int CENTERY_SEEKBAR_RESID = 21866;
    private static final String CENTERY_STRING = "CENTERY:";
    private static final int DISTANCE_SEEKBAR_RESID = 21868;
    private static final String DISTANCE_STRING = "DISTANCE:";
    private static final int MAX_ROTATION_VALUE = 360;
    private static final int MAX_VALUE = 100;
    private static final int ROTATION_SEEKBAR_RESID = 21869;
    private static final String ROTATION_STRING = "ROTATION:";
    private static final String TITLE = "MotionBlur";
    private static final int ZOOM_SEEKBAR_RESID = 21870;
    private static final String ZOOM_STRING = "ZOOM:";
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
    private SeekBar mDistanceSeekBar;
    private TextView mDistanceTextView;
    private int mDistanceValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRotationSeekBar;
    private TextView mRotationTextView;
    private int mRotationValue;
    private SeekBar mZoomSeekBar;
    private TextView mZoomTextView;
    private int mZoomValue;

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
        this.mAngleSeekBar.setMax(100);
        this.mDistanceTextView = new TextView(this);
        this.mDistanceTextView.setText(new StringBuilder(DISTANCE_STRING).append(this.mDistanceValue).toString());
        this.mDistanceTextView.setTextSize(22.0f);
        this.mDistanceTextView.setTextColor(-16777216);
        this.mDistanceTextView.setGravity(17);
        this.mDistanceSeekBar = new SeekBar(this);
        this.mDistanceSeekBar.setOnSeekBarChangeListener(this);
        this.mDistanceSeekBar.setId(DISTANCE_SEEKBAR_RESID);
        this.mDistanceSeekBar.setMax(100);
        this.mRotationTextView = new TextView(this);
        this.mRotationTextView.setText(new StringBuilder(ROTATION_STRING).append(this.mRotationValue).toString());
        this.mRotationTextView.setTextSize(22.0f);
        this.mRotationTextView.setTextColor(-16777216);
        this.mRotationTextView.setGravity(17);
        this.mRotationSeekBar = new SeekBar(this);
        this.mRotationSeekBar.setOnSeekBarChangeListener(this);
        this.mRotationSeekBar.setId(ROTATION_SEEKBAR_RESID);
        this.mRotationSeekBar.setMax(MAX_ROTATION_VALUE);
        this.mRotationSeekBar.setProgress(180);
        this.mZoomTextView = new TextView(this);
        this.mZoomTextView.setText(new StringBuilder(ZOOM_STRING).append(this.mZoomValue).toString());
        this.mZoomTextView.setTextSize(22.0f);
        this.mZoomTextView.setTextColor(-16777216);
        this.mZoomTextView.setGravity(17);
        this.mZoomSeekBar = new SeekBar(this);
        this.mZoomSeekBar.setOnSeekBarChangeListener(this);
        this.mZoomSeekBar.setId(ZOOM_SEEKBAR_RESID);
        this.mZoomSeekBar.setMax(100);
        mainLayout.addView(this.mCenterXTextView);
        mainLayout.addView(this.mCenterXSeekBar);
        mainLayout.addView(this.mCenterYTextView);
        mainLayout.addView(this.mCenterYSeekBar);
        mainLayout.addView(this.mAngleTextView);
        mainLayout.addView(this.mAngleSeekBar);
        mainLayout.addView(this.mDistanceTextView);
        mainLayout.addView(this.mDistanceSeekBar);
        mainLayout.addView(this.mRotationTextView);
        mainLayout.addView(this.mRotationSeekBar);
        mainLayout.addView(this.mZoomTextView);
        mainLayout.addView(this.mZoomSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case CENTERX_SEEKBAR_RESID /*21865*/:
                this.mCenterXValue = progress;
                this.mCenterXTextView.setText(new StringBuilder(CENTERX_STRING).append(getCenterAndZoom(this.mCenterXValue)).toString());
                return;
            case CENTERY_SEEKBAR_RESID /*21866*/:
                this.mCenterYValue = progress;
                this.mCenterYTextView.setText(new StringBuilder(CENTERY_STRING).append(getCenterAndZoom(this.mCenterYValue)).toString());
                return;
            case ANGLE_SEEKBAR_RESID /*21867*/:
                this.mAngleValue = progress;
                this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(getAngle(this.mAngleValue)).toString());
                return;
            case DISTANCE_SEEKBAR_RESID /*21868*/:
                this.mDistanceValue = progress;
                this.mDistanceTextView.setText(new StringBuilder(DISTANCE_STRING).append(this.mDistanceValue).toString());
                return;
            case ROTATION_SEEKBAR_RESID /*21869*/:
                this.mRotationValue = progress;
                this.mRotationTextView.setText(new StringBuilder(ROTATION_STRING).append(getRotation(this.mRotationValue)).toString());
                return;
            case ZOOM_SEEKBAR_RESID /*21870*/:
                this.mZoomValue = progress;
                this.mZoomTextView.setText(new StringBuilder(ZOOM_STRING).append(getCenterAndZoom(this.mZoomValue)).toString());
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
                MotionBlurOp filter = new MotionBlurOp();
                filter.setCentreX(MotionBlurFilterActivity.this.getCenterAndZoom(MotionBlurFilterActivity.this.mCenterXValue));
                filter.setCentreY(MotionBlurFilterActivity.this.getCenterAndZoom(MotionBlurFilterActivity.this.mCenterYValue));
                filter.setAngle(MotionBlurFilterActivity.this.getAngle(MotionBlurFilterActivity.this.mAngleValue));
                filter.setDistance((float) MotionBlurFilterActivity.this.mDistanceValue);
                filter.setRotation((float) MotionBlurFilterActivity.this.getRotation(MotionBlurFilterActivity.this.mRotationValue));
                filter.setZoom(MotionBlurFilterActivity.this.getCenterAndZoom(MotionBlurFilterActivity.this.mZoomValue));
                MotionBlurFilterActivity.this.mColors = filter.filter(MotionBlurFilterActivity.this.mColors, width, height);
                MotionBlurFilterActivity motionBlurFilterActivity = MotionBlurFilterActivity.this;
                final int i = width;
                final int i2 = height;
                motionBlurFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        MotionBlurFilterActivity.this.setModifyView(MotionBlurFilterActivity.this.mColors, i, i2);
                    }
                });
                MotionBlurFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getCenterAndZoom(int value) {
        return ((float) value) / 100.0f;
    }

    private float getAngle(int value) {
        return ((float) value) / 100.0f;
    }

    private int getRotation(int value) {
        return value - 180;
    }

    private float getDistance(int value) {
        return ((float) value) / 100.0f;
    }
}
