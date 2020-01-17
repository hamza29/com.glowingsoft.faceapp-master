package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.ShearFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ShearFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int MAX_VALUE = 104;
    private static final String TITLE = "Shear";
    private static final int XANGLE_SEEKBAR_RESID = 21863;
    private static final String XANGLE_STRING = "XANGLE:";
    private static final int YANGLE_SEEKBAR_RESID = 21864;
    private static final String YANGLE_STRING = "YANGLE:";
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private SeekBar mXAngleSeekBar;
    private TextView mXAngleTextView;
    private int mXAngleValue;
    private SeekBar mYAngleSeekBar;
    private TextView mYAngleTextView;
    private int mYAngleValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mXAngleTextView = new TextView(this);
        this.mXAngleTextView.setText(new StringBuilder(XANGLE_STRING).append(this.mXAngleValue).toString());
        this.mXAngleTextView.setTextSize(22.0f);
        this.mXAngleTextView.setTextColor(-16777216);
        this.mXAngleTextView.setGravity(17);
        this.mXAngleSeekBar = new SeekBar(this);
        this.mXAngleSeekBar.setOnSeekBarChangeListener(this);
        this.mXAngleSeekBar.setId(XANGLE_SEEKBAR_RESID);
        this.mXAngleSeekBar.setMax(104);
        this.mXAngleSeekBar.setProgress(52);
        this.mYAngleTextView = new TextView(this);
        this.mYAngleTextView.setText(new StringBuilder(YANGLE_STRING).append(this.mYAngleValue).toString());
        this.mYAngleTextView.setTextSize(22.0f);
        this.mYAngleTextView.setTextColor(-16777216);
        this.mYAngleTextView.setGravity(17);
        this.mYAngleSeekBar = new SeekBar(this);
        this.mYAngleSeekBar.setOnSeekBarChangeListener(this);
        this.mYAngleSeekBar.setId(YANGLE_SEEKBAR_RESID);
        this.mYAngleSeekBar.setMax(104);
        this.mYAngleSeekBar.setProgress(52);
        mainLayout.addView(this.mXAngleTextView);
        mainLayout.addView(this.mXAngleSeekBar);
        mainLayout.addView(this.mYAngleTextView);
        mainLayout.addView(this.mYAngleSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case XANGLE_SEEKBAR_RESID /*21863*/:
                this.mXAngleValue = progress;
                this.mXAngleTextView.setText(new StringBuilder(XANGLE_STRING).append(getValue(this.mXAngleValue)).toString());
                return;
            case YANGLE_SEEKBAR_RESID /*21864*/:
                this.mYAngleValue = progress;
                this.mYAngleTextView.setText(new StringBuilder(YANGLE_STRING).append(getValue(this.mYAngleValue)).toString());
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
                ShearFilter filter = new ShearFilter();
                filter.setXAngle(ShearFilterActivity.this.getValue(ShearFilterActivity.this.mXAngleValue));
                filter.setYAngle(ShearFilterActivity.this.getValue(ShearFilterActivity.this.mYAngleValue));
                ShearFilterActivity.this.mColors = filter.filter(ShearFilterActivity.this.mColors, width, height);
                ShearFilterActivity shearFilterActivity = ShearFilterActivity.this;
                final int i = width;
                final int i2 = height;
                shearFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        ShearFilterActivity.this.setModifyView(ShearFilterActivity.this.mColors, i, i2);
                    }
                });
                ShearFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) (value - 52)) / 100.0f;
    }
}
