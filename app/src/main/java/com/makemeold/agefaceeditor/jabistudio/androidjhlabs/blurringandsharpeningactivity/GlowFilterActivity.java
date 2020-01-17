package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.GlowFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class GlowFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int AMOUNT_SEEKBAR_RESID = 21866;
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final int MAX_VALUE = 100;
    private static final int RADIUS_SEEKBAR_RESID = 21865;
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String TITLE = "Glow";
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private int mAmountValue;
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private int mRadiusValue;

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
        this.mRadiusSeekBar.setMax(100);
        this.mAmountTextView = new TextView(this);
        this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(this.mAmountValue).toString());
        this.mAmountTextView.setTextSize(22.0f);
        this.mAmountTextView.setTextColor(-16777216);
        this.mAmountTextView.setGravity(17);
        this.mAmountSeekBar = new SeekBar(this);
        this.mAmountSeekBar.setOnSeekBarChangeListener(this);
        this.mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        this.mAmountSeekBar.setMax(100);
        mainLayout.addView(this.mAmountTextView);
        mainLayout.addView(this.mAmountSeekBar);
        mainLayout.addView(this.mRadiusTextView);
        mainLayout.addView(this.mRadiusSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case RADIUS_SEEKBAR_RESID /*21865*/:
                this.mRadiusValue = progress;
                this.mRadiusTextView.setText(new StringBuilder(RADIUS_STRING).append(this.mRadiusValue).toString());
                return;
            case AMOUNT_SEEKBAR_RESID /*21866*/:
                this.mAmountValue = progress;
                this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(getAmout(this.mAmountValue)).toString());
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
                GlowFilter filter = new GlowFilter();
                filter.setAmount(GlowFilterActivity.this.getAmout(GlowFilterActivity.this.mAmountValue));
                filter.setRadius((float) GlowFilterActivity.this.mRadiusValue);
                GlowFilterActivity.this.mColors = filter.filter(GlowFilterActivity.this.mColors, width, height);
                GlowFilterActivity glowFilterActivity = GlowFilterActivity.this;
                final int i = width;
                final int i2 = height;
                glowFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        GlowFilterActivity.this.setModifyView(GlowFilterActivity.this.mColors, i, i2);
                    }
                });
                GlowFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getAmout(int value) {
        return ((float) value) / 100.0f;
    }
}
