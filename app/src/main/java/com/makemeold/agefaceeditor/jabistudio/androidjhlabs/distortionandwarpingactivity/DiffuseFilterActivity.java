package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.DiffuseFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class DiffuseFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int MAX_VALUE = 100;
    private static final int SCALE_SEEKBAR_RESID = 21863;
    private static final String SCALE_STRING = "SCALE:";
    private static final String TITLE = "Diffuse";
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
        this.mScaleSeekBar.setMax(100);
        mainLayout.addView(this.mScaleTextView);
        mainLayout.addView(this.mScaleSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case SCALE_SEEKBAR_RESID /*21863*/:
                this.mScaleValue = progress;
                this.mScaleTextView.setText(new StringBuilder(SCALE_STRING).append(this.mScaleValue).toString());
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
                DiffuseFilter filter = new DiffuseFilter();
                filter.setScale((float) DiffuseFilterActivity.this.mScaleValue);
                DiffuseFilterActivity.this.mColors = filter.filter(DiffuseFilterActivity.this.mColors, width, height);
                DiffuseFilterActivity diffuseFilterActivity = DiffuseFilterActivity.this;
                final int i = width;
                final int i2 = height;
                diffuseFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        DiffuseFilterActivity.this.setModifyView(DiffuseFilterActivity.this.mColors, i, i2);
                    }
                });
                DiffuseFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
