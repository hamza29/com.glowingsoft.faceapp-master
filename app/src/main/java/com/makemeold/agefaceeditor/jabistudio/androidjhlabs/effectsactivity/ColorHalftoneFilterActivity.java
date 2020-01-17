package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.ColorHalftoneFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ColorHalftoneFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int ANGLE_VALUE = 360;
    private static final int CYAN_ANGLE_SEEKBAR_RESID = 21863;
    private static final String CYAN_ANGLE_STRING = "CYANANGLE:";
    private static final int MAGENTA_ANGLE_SEEKBAR_RESID = 21864;
    private static final String MAGENTA_ANGLE_STRING = "MAGENTAANGLE:";
    private static final int MAX_VALUE = 30;
    private static final int RADIUS_SEEKBAR_RESID = 21862;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String TITLE = "ColorHalftone";
    private static final int YELLOW_ANGLE_SEEKBAR_RESID = 21865;
    private static final String YELLOW_ANGLE_STRING = "YELLOWANGLE:";
    private int[] mColors;
    private SeekBar mCyanAngleSeekBar;
    private TextView mCyanAngleTextView;
    private int mCyanAngleValue;
    private SeekBar mMagentaAngleSeekBar;
    private TextView mMagentaAngleTextView;
    private int mMagentaAngleValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private int mRadiusValue;
    private SeekBar mYellowAngleSeekBar;
    private TextView mYellowAngleTextView;
    private int mYellowAngleValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mRadiusTextView = new TextView(this);
        this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
        this.mRadiusTextView.setTextSize(22.0f);
        this.mRadiusTextView.setTextColor(-16777216);
        this.mRadiusTextView.setGravity(17);
        this.mRadiusSeekBar = new SeekBar(this);
        this.mRadiusSeekBar.setOnSeekBarChangeListener(this);
        this.mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        this.mRadiusSeekBar.setMax(30);
        this.mCyanAngleTextView = new TextView(this);
        this.mCyanAngleTextView.setText(new StringBuilder(CYAN_ANGLE_STRING).append(this.mCyanAngleValue).toString());
        this.mCyanAngleTextView.setTextSize(22.0f);
        this.mCyanAngleTextView.setTextColor(-16777216);
        this.mCyanAngleTextView.setGravity(17);
        this.mCyanAngleSeekBar = new SeekBar(this);
        this.mCyanAngleSeekBar.setOnSeekBarChangeListener(this);
        this.mCyanAngleSeekBar.setId(CYAN_ANGLE_SEEKBAR_RESID);
        this.mCyanAngleSeekBar.setMax(ANGLE_VALUE);
        this.mMagentaAngleTextView = new TextView(this);
        this.mMagentaAngleTextView.setText(new StringBuilder(MAGENTA_ANGLE_STRING).append(this.mMagentaAngleValue).toString());
        this.mMagentaAngleTextView.setTextSize(22.0f);
        this.mMagentaAngleTextView.setTextColor(-16777216);
        this.mMagentaAngleTextView.setGravity(17);
        this.mMagentaAngleSeekBar = new SeekBar(this);
        this.mMagentaAngleSeekBar.setOnSeekBarChangeListener(this);
        this.mMagentaAngleSeekBar.setId(MAGENTA_ANGLE_SEEKBAR_RESID);
        this.mMagentaAngleSeekBar.setMax(ANGLE_VALUE);
        this.mYellowAngleTextView = new TextView(this);
        this.mYellowAngleTextView.setText(new StringBuilder(YELLOW_ANGLE_STRING).append(this.mYellowAngleValue).toString());
        this.mYellowAngleTextView.setTextSize(22.0f);
        this.mYellowAngleTextView.setTextColor(-16777216);
        this.mYellowAngleTextView.setGravity(17);
        this.mYellowAngleSeekBar = new SeekBar(this);
        this.mYellowAngleSeekBar.setOnSeekBarChangeListener(this);
        this.mYellowAngleSeekBar.setId(YELLOW_ANGLE_SEEKBAR_RESID);
        this.mYellowAngleSeekBar.setMax(ANGLE_VALUE);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
        mainLayout.addView(this.mCyanAngleTextView);
        mainLayout.addView(this.mCyanAngleSeekBar);
        mainLayout.addView(this.mMagentaAngleTextView);
        mainLayout.addView(this.mMagentaAngleSeekBar);
        mainLayout.addView(this.mYellowAngleTextView);
        mainLayout.addView(this.mYellowAngleSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case RADIUS_SEEKBAR_RESID /*21862*/:
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
                return;
            case CYAN_ANGLE_SEEKBAR_RESID /*21863*/:
                this.mCyanAngleValue = progress;
                this.mCyanAngleTextView.setText(new StringBuilder(CYAN_ANGLE_STRING).append(this.mCyanAngleValue).toString());
                return;
            case MAGENTA_ANGLE_SEEKBAR_RESID /*21864*/:
                this.mMagentaAngleValue = progress;
                this.mMagentaAngleTextView.setText(new StringBuilder(MAGENTA_ANGLE_STRING).append(this.mMagentaAngleValue).toString());
                return;
            case YELLOW_ANGLE_SEEKBAR_RESID /*21865*/:
                this.mYellowAngleValue = progress;
                this.mYellowAngleTextView.setText(new StringBuilder(YELLOW_ANGLE_STRING).append(this.mYellowAngleValue).toString());
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
                ColorHalftoneFilter filter = new ColorHalftoneFilter();
                filter.setdotRadius((float) ColorHalftoneFilterActivity.this.mRadiusValue);
                filter.setCyanScreenAngle((float) ColorHalftoneFilterActivity.this.mCyanAngleValue);
                filter.setMagentaScreenAngle((float) ColorHalftoneFilterActivity.this.mMagentaAngleValue);
                filter.setYellowScreenAngle((float) ColorHalftoneFilterActivity.this.mYellowAngleValue);
                ColorHalftoneFilterActivity.this.mColors = filter.filter(ColorHalftoneFilterActivity.this.mColors, width, height);
                ColorHalftoneFilterActivity colorHalftoneFilterActivity = ColorHalftoneFilterActivity.this;
                final int i = width;
                final int i2 = height;
                colorHalftoneFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ColorHalftoneFilterActivity.this.setModifyView(ColorHalftoneFilterActivity.this.mColors, i, i2);
                    }
                });
                ColorHalftoneFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
