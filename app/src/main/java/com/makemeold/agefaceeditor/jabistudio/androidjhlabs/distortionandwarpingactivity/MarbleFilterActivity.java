package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.MarbleFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class MarbleFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int AMOUNT_SEEKBAR_RESID = 21864;
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final int MAX_VALUE = 100;
    private static final int SCALE_SEEKBAR_RESID = 21863;
    private static final String SCALE_STRING = "SCALE:";
    private static final String TITLE = "Marble";
    private static final int TURBULENCE_SEEKBAR_RESID = 21865;
    private static final String TURBULENCE_STRING = "TURBULENCE:";
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private int mAmountValue;
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    private int mScaleValue;
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
        this.mScaleSeekBar.setMax(100);
        this.mAmountTextView = new TextView(this);
        this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(this.mAmountValue).toString());
        this.mAmountTextView.setTextSize(22.0f);
        this.mAmountTextView.setTextColor(-16777216);
        this.mAmountTextView.setGravity(17);
        this.mAmountSeekBar = new SeekBar(this);
        this.mAmountSeekBar.setOnSeekBarChangeListener(this);
        this.mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        this.mAmountSeekBar.setMax(100);
        this.mTurbulenceTextView = new TextView(this);
        this.mTurbulenceTextView.setText(new StringBuilder(TURBULENCE_STRING).append(this.mTurbulenceValue).toString());
        this.mTurbulenceTextView.setTextSize(22.0f);
        this.mTurbulenceTextView.setTextColor(-16777216);
        this.mTurbulenceTextView.setGravity(17);
        this.mTurbulenceSeekBar = new SeekBar(this);
        this.mTurbulenceSeekBar.setOnSeekBarChangeListener(this);
        this.mTurbulenceSeekBar.setId(TURBULENCE_SEEKBAR_RESID);
        this.mTurbulenceSeekBar.setMax(100);
        mainLayout.addView(this.mScaleTextView);
        mainLayout.addView(this.mScaleSeekBar);
        mainLayout.addView(this.mAmountTextView);
        mainLayout.addView(this.mAmountSeekBar);
        mainLayout.addView(this.mTurbulenceTextView);
        mainLayout.addView(this.mTurbulenceSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case SCALE_SEEKBAR_RESID /*21863*/:
                this.mScaleValue = progress;
                this.mScaleTextView.setText(new StringBuilder(SCALE_STRING).append(this.mScaleValue).toString());
                return;
            case AMOUNT_SEEKBAR_RESID /*21864*/:
                this.mAmountValue = progress;
                this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(this.mAmountValue).toString());
                return;
            case TURBULENCE_SEEKBAR_RESID /*21865*/:
                this.mTurbulenceValue = progress;
                this.mTurbulenceTextView.setText(new StringBuilder(TURBULENCE_STRING).append(this.mTurbulenceValue).toString());
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
                MarbleFilter filter = new MarbleFilter();
                filter.setXScale((float) MarbleFilterActivity.this.mScaleValue);
                filter.setYScale((float) MarbleFilterActivity.this.mScaleValue);
                filter.setAmount((float) MarbleFilterActivity.this.mAmountValue);
                filter.setTurbulence((float) MarbleFilterActivity.this.mTurbulenceValue);
                MarbleFilterActivity.this.mColors = filter.filter(MarbleFilterActivity.this.mColors, width, height);
                MarbleFilterActivity marbleFilterActivity = MarbleFilterActivity.this;
                final int i = width;
                final int i2 = height;
                marbleFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        MarbleFilterActivity.this.setModifyView(MarbleFilterActivity.this.mColors, i, i2);
                    }
                });
                MarbleFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
