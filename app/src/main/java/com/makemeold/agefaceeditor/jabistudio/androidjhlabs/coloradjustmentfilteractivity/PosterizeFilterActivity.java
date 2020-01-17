package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.PosterizeFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class PosterizeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int LEVEL_SEEKBAR_RESID = 21865;
    private static final String LEVEL_STRING = "LEVEL:";
    private static final int MAX_VALUE = 16;
    private static final String TITLE = "Posterize";
    private int[] mColors;
    private SeekBar mLevelSeekBar;
    private TextView mLevelTextView;
    private int mLevelValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mLevelTextView = new TextView(this);
        this.mLevelTextView.setText(new StringBuilder(LEVEL_STRING).append(this.mLevelValue).toString());
        this.mLevelTextView.setTextSize(22.0f);
        this.mLevelTextView.setTextColor(-16777216);
        this.mLevelTextView.setGravity(17);
        this.mLevelSeekBar = new SeekBar(this);
        this.mLevelSeekBar.setOnSeekBarChangeListener(this);
        this.mLevelSeekBar.setId(LEVEL_SEEKBAR_RESID);
        this.mLevelSeekBar.setMax(16);
        mainLayout.addView(this.mLevelTextView);
        mainLayout.addView(this.mLevelSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case LEVEL_SEEKBAR_RESID /*21865*/:
                this.mLevelValue = progress;
                this.mLevelTextView.setText(new StringBuilder(LEVEL_STRING).append(this.mLevelValue).toString());
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
                PosterizeFilter filter = new PosterizeFilter();
                filter.setNumLevels(PosterizeFilterActivity.this.mLevelValue);
                PosterizeFilterActivity.this.mColors = filter.filter(PosterizeFilterActivity.this.mColors, width, height);
                PosterizeFilterActivity posterizeFilterActivity = PosterizeFilterActivity.this;
                final int i = width;
                final int i2 = height;
                posterizeFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        PosterizeFilterActivity.this.setModifyView(PosterizeFilterActivity.this.mColors, i, i2);
                    }
                });
                PosterizeFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
