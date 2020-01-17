package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.RescaleFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class RescaleFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int MAX_VALUE = 500;
    private static final int SCALE_SEEKBAR_RESID = 21865;
    private static final String SCALE_STRING = "SCALE:";
    private static final String TITLE = "Rescale";
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    private int mScaleValue;

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
        this.mScaleSeekBar.setMax(500);
        mainLayout.addView(this.mScaleTextView);
        mainLayout.addView(this.mScaleSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case SCALE_SEEKBAR_RESID /*21865*/:
                this.mScaleValue = progress;
                this.mScaleTextView.setText(new StringBuilder(SCALE_STRING).append(getScale(this.mScaleValue)).toString());
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
                RescaleFilter filter = new RescaleFilter();
                filter.setScale(RescaleFilterActivity.this.getScale(RescaleFilterActivity.this.mScaleValue));
                RescaleFilterActivity.this.mColors = filter.filter(RescaleFilterActivity.this.mColors, width, height);
                RescaleFilterActivity rescaleFilterActivity = RescaleFilterActivity.this;
                final int i = width;
                final int i2 = height;
                rescaleFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        RescaleFilterActivity.this.setModifyView(RescaleFilterActivity.this.mColors, i, i2);
                    }
                });
                RescaleFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getScale(int value) {
        return ((float) value) / 100.0f;
    }
}
