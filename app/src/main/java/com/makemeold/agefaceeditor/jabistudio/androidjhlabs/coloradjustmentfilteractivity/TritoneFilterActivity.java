package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.TritoneFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class TritoneFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int HIGH_SEEKBAR_RESID = 21863;
    private static final String HIGH_STRING = "HIGH:";
    private static final int MAX_VALUE = 16777215;
    private static final int MID_SEEKBAR_RESID = 21864;
    private static final String MID_STRING = "MID:";
    private static final int SHADOW_SEEKBAR_RESID = 21865;
    private static final String SHADOW_STRING = "SHADOW";
    private static final String TITLE = "Tritone";
    private int[] mColors;
    private SeekBar mHighSeekBar;
    private TextView mHighTextView;
    private int mHighValue;
    private SeekBar mMidSeekBar;
    private TextView mMidTextView;
    private int mMidValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mShadowSeekBar;
    private TextView mShadowTextView;
    private int mShadowValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mHighTextView = new TextView(this);
        this.mHighTextView.setText(new StringBuilder(HIGH_STRING).append(this.mHighValue).toString());
        this.mHighTextView.setTextSize(22.0f);
        this.mHighTextView.setTextColor(-16777216);
        this.mHighTextView.setGravity(17);
        this.mHighSeekBar = new SeekBar(this);
        this.mHighSeekBar.setOnSeekBarChangeListener(this);
        this.mHighSeekBar.setId(HIGH_SEEKBAR_RESID);
        this.mHighSeekBar.setMax(16777215);
        this.mMidTextView = new TextView(this);
        this.mMidTextView.setText(new StringBuilder(MID_STRING).append(this.mMidValue).toString());
        this.mMidTextView.setTextSize(22.0f);
        this.mMidTextView.setTextColor(-16777216);
        this.mMidTextView.setGravity(17);
        this.mMidSeekBar = new SeekBar(this);
        this.mMidSeekBar.setOnSeekBarChangeListener(this);
        this.mMidSeekBar.setId(MID_SEEKBAR_RESID);
        this.mMidSeekBar.setMax(16777215);
        this.mShadowTextView = new TextView(this);
        this.mShadowTextView.setText(new StringBuilder(SHADOW_STRING).append(this.mShadowValue).toString());
        this.mShadowTextView.setTextSize(22.0f);
        this.mShadowTextView.setTextColor(-16777216);
        this.mShadowTextView.setGravity(17);
        this.mShadowSeekBar = new SeekBar(this);
        this.mShadowSeekBar.setOnSeekBarChangeListener(this);
        this.mShadowSeekBar.setId(SHADOW_SEEKBAR_RESID);
        this.mShadowSeekBar.setMax(16777215);
        mainLayout.addView(this.mHighTextView);
        mainLayout.addView(this.mHighSeekBar);
        mainLayout.addView(this.mMidTextView);
        mainLayout.addView(this.mMidSeekBar);
        mainLayout.addView(this.mShadowTextView);
        mainLayout.addView(this.mShadowSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case HIGH_SEEKBAR_RESID /*21863*/:
                this.mHighValue = progress;
                this.mHighTextView.setText(new StringBuilder(HIGH_STRING).append(getValue(this.mHighValue)).toString());
                return;
            case MID_SEEKBAR_RESID /*21864*/:
                this.mMidValue = progress;
                this.mMidTextView.setText(new StringBuilder(MID_STRING).append(getValue(this.mMidValue)).toString());
                return;
            case SHADOW_SEEKBAR_RESID /*21865*/:
                this.mShadowValue = progress;
                this.mShadowTextView.setText(new StringBuilder(SHADOW_STRING).append(getValue(this.mShadowValue)).toString());
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
                TritoneFilter filter = new TritoneFilter();
                filter.setHighColor(TritoneFilterActivity.this.getValue(TritoneFilterActivity.this.mHighValue));
                filter.setMidColor(TritoneFilterActivity.this.getValue(TritoneFilterActivity.this.mMidValue));
                filter.setShadowColor(TritoneFilterActivity.this.getValue(TritoneFilterActivity.this.mShadowValue));
                TritoneFilterActivity.this.mColors = filter.filter(TritoneFilterActivity.this.mColors, width, height);
                TritoneFilterActivity tritoneFilterActivity = TritoneFilterActivity.this;
                final int i = width;
                final int i2 = height;
                tritoneFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        TritoneFilterActivity.this.setModifyView(TritoneFilterActivity.this.mColors, i, i2);
                    }
                });
                TritoneFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private int getValue(int value) {
        return value - 16777216;
    }
}
