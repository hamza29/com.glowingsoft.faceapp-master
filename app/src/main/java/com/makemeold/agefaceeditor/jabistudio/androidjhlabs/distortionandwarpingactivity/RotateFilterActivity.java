package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.RotateFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class RotateFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int ANGLE_SEEKBAR_RESID = 21865;
    private static final String ANGLE_STRING = "ANGLE:";
    private static final int MAX_VALUE = 624;
    private static final String TITLE = "Rotate";
    private SeekBar mAngleSeekBar;
    private TextView mAngleTextView;
    private int mAngleValue;
    private int[] mColors;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mAngleTextView = new TextView(this);
        this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(this.mAngleValue).toString());
        this.mAngleTextView.setTextSize(22.0f);
        this.mAngleTextView.setTextColor(-16777216);
        this.mAngleTextView.setGravity(17);
        this.mAngleSeekBar = new SeekBar(this);
        this.mAngleSeekBar.setOnSeekBarChangeListener(this);
        this.mAngleSeekBar.setId(ANGLE_SEEKBAR_RESID);
        this.mAngleSeekBar.setMax(MAX_VALUE);
        mainLayout.addView(this.mAngleTextView);
        mainLayout.addView(this.mAngleSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case ANGLE_SEEKBAR_RESID /*21865*/:
                this.mAngleValue = progress;
                this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(getAngle(this.mAngleValue)).toString());
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
                RotateFilter filter = new RotateFilter();
                filter.setAngle(RotateFilterActivity.this.getAngle(RotateFilterActivity.this.mAngleValue));
                RotateFilterActivity.this.mColors = filter.filter(RotateFilterActivity.this.mColors, width, height);
                RotateFilterActivity rotateFilterActivity = RotateFilterActivity.this;
                final int i = width;
                final int i2 = height;
                rotateFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        RotateFilterActivity.this.setModifyView(RotateFilterActivity.this.mColors, i, i2);
                    }
                });
                RotateFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getAngle(int value) {
        return ((float) value) / 100.0f;
    }
}
