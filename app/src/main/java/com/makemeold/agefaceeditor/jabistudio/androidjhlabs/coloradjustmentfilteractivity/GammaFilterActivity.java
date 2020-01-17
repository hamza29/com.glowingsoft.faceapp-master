package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.GammaFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class GammaFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int GAMMA_SEEKBAR_RESID = 21865;
    private static final String GAMMA_STRING = "GAMMA:";
    private static final int MAX_VALUE = 500;
    private static final String TITLE = "Gamma";
    private int[] mColors;
    private SeekBar mGammaSeekBar;
    private TextView mGammaTextView;
    private int mGammaValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mGammaTextView = new TextView(this);
        this.mGammaTextView.setText(new StringBuilder(GAMMA_STRING).append(this.mGammaValue).toString());
        this.mGammaTextView.setTextSize(22.0f);
        this.mGammaTextView.setTextColor(-16777216);
        this.mGammaTextView.setGravity(17);
        this.mGammaSeekBar = new SeekBar(this);
        this.mGammaSeekBar.setOnSeekBarChangeListener(this);
        this.mGammaSeekBar.setId(GAMMA_SEEKBAR_RESID);
        this.mGammaSeekBar.setMax(500);
        this.mGammaSeekBar.setProgress(100);
        mainLayout.addView(this.mGammaTextView);
        mainLayout.addView(this.mGammaSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case GAMMA_SEEKBAR_RESID /*21865*/:
                this.mGammaValue = progress;
                this.mGammaTextView.setText(new StringBuilder(GAMMA_STRING).append(getGamma(this.mGammaValue)).toString());
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
                GammaFilter filter = new GammaFilter();
                filter.setGamma(GammaFilterActivity.this.getGamma(GammaFilterActivity.this.mGammaValue));
                GammaFilterActivity.this.mColors = filter.filter(GammaFilterActivity.this.mColors, width, height);
                GammaFilterActivity gammaFilterActivity = GammaFilterActivity.this;
                final int i = width;
                final int i2 = height;
                gammaFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        GammaFilterActivity.this.setModifyView(GammaFilterActivity.this.mColors, i, i2);
                    }
                });
                GammaFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getGamma(int value) {
        return ((float) value) / 100.0f;
    }
}
