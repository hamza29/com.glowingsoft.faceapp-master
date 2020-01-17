package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.SphereFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class SphereFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int CENTERX_SEEKBAR_RESID = 21863;
    private static final String CENTERX_STRING = "CENTERX:";
    private static final int CENTERY_SEEKBAR_RESID = 21864;
    private static final String CENTERY_STRING = "CENTERY:";
    private static final int MAX_VALUE = 100;
    private static final int RADIUS_SEEKBAR_RESID = 21867;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final int REFRACTION_MAX_VALUE = 200;
    private static final int REFRACTION_SEEKBAR_RESID = 21868;
    private static final String REFRACTION_STRING = "REFRACTION:";
    private static final String TITLE = "Sphere";
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
    private SeekBar mRefractionSeekBar;
    private TextView mRefractionTextView;
    private int mRefractionValue;

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
        this.mRefractionTextView = new TextView(this);
        this.mRefractionTextView.setText(new StringBuilder(REFRACTION_STRING).append(this.mRefractionValue).toString());
        this.mRefractionTextView.setTextSize(22.0f);
        this.mRefractionTextView.setTextColor(-16777216);
        this.mRefractionTextView.setGravity(17);
        this.mRefractionSeekBar = new SeekBar(this);
        this.mRefractionSeekBar.setOnSeekBarChangeListener(this);
        this.mRefractionSeekBar.setId(REFRACTION_SEEKBAR_RESID);
        this.mRefractionSeekBar.setMax(200);
        mainLayout.addView(this.mCenterXTextView);
        mainLayout.addView(this.mCenterXSeekBar);
        mainLayout.addView(this.mCenterYTextView);
        mainLayout.addView(this.mCenterYSeekBar);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
        mainLayout.addView(this.mRefractionTextView);
        mainLayout.addView(this.mRefractionSeekBar);
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
            case RADIUS_SEEKBAR_RESID /*21867*/:
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
                return;
            case REFRACTION_SEEKBAR_RESID /*21868*/:
                this.mRefractionValue = progress;
                this.mRefractionTextView.setText(new StringBuilder(REFRACTION_STRING).append(getRefractionValue(this.mRefractionValue)).toString());
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
                SphereFilter filter = new SphereFilter();
                filter.setCentreX(SphereFilterActivity.this.getValue(SphereFilterActivity.this.mCenterXValue));
                filter.setCentreY(SphereFilterActivity.this.getValue(SphereFilterActivity.this.mCenterYValue));
                filter.setRadius((float) SphereFilterActivity.this.mRadiusValue);
                filter.setRefractionIndex(SphereFilterActivity.this.getRefractionValue(SphereFilterActivity.this.mRefractionValue));
                SphereFilterActivity.this.mColors = filter.filter(SphereFilterActivity.this.mColors, width, height);
                SphereFilterActivity sphereFilterActivity = SphereFilterActivity.this;
                final int i = width;
                final int i2 = height;
                sphereFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        SphereFilterActivity.this.setModifyView(SphereFilterActivity.this.mColors, i, i2);
                    }
                });
                SphereFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getRefractionValue(int value) {
        return ((float) (value + 100)) / 100.0f;
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
