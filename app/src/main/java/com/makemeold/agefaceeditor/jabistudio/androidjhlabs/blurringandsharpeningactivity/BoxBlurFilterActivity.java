package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.BoxBlurFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class BoxBlurFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int HRADIUS_SEEKBAR_RESID = 21863;
    private static final String HRADIUS_STRING = "HRADIUS:";
    private static final int MAX_VALUE = 50;
    private static final int RADIUS_SEEKBAR_RESID = 21865;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String TITLE = "BoxBlur";
    private static final int VRADIUS_SEEKBAR_RESID = 21864;
    private static final String VRADIUS_STRING = "VRADIUS:";
    private int[] mColors;
    private SeekBar mHRadiusSeekBar;
    private TextView mHRadiusTextView;
    private int mHRadiusValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private int mRadiusValue;
    private SeekBar mVRadiusSeekBar;
    private TextView mVRadiusTextView;
    private int mVRadiusValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mHRadiusTextView = new TextView(this);
        this.mHRadiusTextView.setText(new StringBuilder(HRADIUS_STRING).append(this.mHRadiusValue).toString());
        this.mHRadiusTextView.setTextSize(22.0f);
        this.mHRadiusTextView.setTextColor(-16777216);
        this.mHRadiusTextView.setGravity(17);
        this.mHRadiusSeekBar = new SeekBar(this);
        this.mHRadiusSeekBar.setOnSeekBarChangeListener(this);
        this.mHRadiusSeekBar.setId(HRADIUS_SEEKBAR_RESID);
        this.mHRadiusSeekBar.setMax(50);
        this.mVRadiusTextView = new TextView(this);
        this.mVRadiusTextView.setText(new StringBuilder(VRADIUS_STRING).append(this.mVRadiusValue).toString());
        this.mVRadiusTextView.setTextSize(22.0f);
        this.mVRadiusTextView.setTextColor(-16777216);
        this.mVRadiusTextView.setGravity(17);
        this.mVRadiusSeekBar = new SeekBar(this);
        this.mVRadiusSeekBar.setOnSeekBarChangeListener(this);
        this.mVRadiusSeekBar.setId(VRADIUS_SEEKBAR_RESID);
        this.mVRadiusSeekBar.setMax(50);
        this.mRadiusTextView = new TextView(this);
        this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
        this.mRadiusTextView.setTextSize(22.0f);
        this.mRadiusTextView.setTextColor(-16777216);
        this.mRadiusTextView.setGravity(17);
        this.mRadiusSeekBar = new SeekBar(this);
        this.mRadiusSeekBar.setOnSeekBarChangeListener(this);
        this.mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        this.mRadiusSeekBar.setMax(50);
        mainLayout.addView(this.mHRadiusTextView);
        mainLayout.addView(this.mHRadiusSeekBar);
        mainLayout.addView(this.mVRadiusTextView);
        mainLayout.addView(this.mVRadiusSeekBar);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case HRADIUS_SEEKBAR_RESID /*21863*/:
                this.mHRadiusValue = progress;
                this.mHRadiusTextView.setText(new StringBuilder(HRADIUS_STRING).append(this.mHRadiusValue).toString());
                return;
            case VRADIUS_SEEKBAR_RESID /*21864*/:
                this.mVRadiusValue = progress;
                this.mVRadiusTextView.setText(new StringBuilder(VRADIUS_STRING).append(this.mVRadiusValue).toString());
                return;
            case RADIUS_SEEKBAR_RESID /*21865*/:
                this.mHRadiusValue = 0;
                this.mVRadiusValue = 0;
                this.mHRadiusSeekBar.setProgress(this.mHRadiusValue);
                this.mVRadiusSeekBar.setProgress(this.mVRadiusValue);
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
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
                BoxBlurFilter filter = new BoxBlurFilter();
                filter.setHRadius((float) BoxBlurFilterActivity.this.mHRadiusValue);
                filter.setVRadius((float) BoxBlurFilterActivity.this.mVRadiusValue);
                if (BoxBlurFilterActivity.this.mHRadiusValue == 0 && BoxBlurFilterActivity.this.mVRadiusValue == 0) {
                    filter.setRadius((float) BoxBlurFilterActivity.this.mRadiusValue);
                }
                BoxBlurFilterActivity.this.mColors = filter.filter(BoxBlurFilterActivity.this.mColors, width, height);
                BoxBlurFilterActivity boxBlurFilterActivity = BoxBlurFilterActivity.this;
                final int i = width;
                final int i2 = height;
                boxBlurFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        BoxBlurFilterActivity.this.setModifyView(BoxBlurFilterActivity.this.mColors, i, i2);
                    }
                });
                BoxBlurFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
