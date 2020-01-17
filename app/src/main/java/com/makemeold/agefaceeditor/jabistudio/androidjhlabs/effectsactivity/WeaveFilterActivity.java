package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.WeaveFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class WeaveFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int MAX_VALUE = 150;
    private static final String TITLE = "Weave";
    private static final int XGAP_SEEKBAR_RESID = 21865;
    private static final String XGAP_STRING = "XGAP:";
    private static final int XWIDTH_SEEKBAR_RESID = 21863;
    private static final String XWIDTH_STRING = "XWIDTH:";
    private static final int YGAP_SEEKBAR_RESID = 21866;
    private static final String YGAP_STRING = "YGAP:";
    private static final int YWIDTH_SEEKBAR_RESID = 21864;
    private static final String YWIDTH_STRING = "YWIDTH:";
    private int[] mColors;
    private ProgressDialog mProgressDialog;
    private SeekBar mXGapSeekBar;
    private TextView mXGapTextView;
    private int mXGapValue;
    private SeekBar mXWidthSeekBar;
    private TextView mXWidthTextView;
    private int mXWidthValue;
    private SeekBar mYGapSeekBar;
    private TextView mYGapTextView;
    private int mYGapValue;
    private SeekBar mYWidthSeekBar;
    private TextView mYWidthTextView;
    private int mYWidthValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mXWidthTextView = new TextView(this);
        this.mXWidthTextView.setText(new StringBuilder(XWIDTH_STRING).append(this.mXWidthValue).toString());
        this.mXWidthTextView.setTextSize(22.0f);
        this.mXWidthTextView.setTextColor(-16777216);
        this.mXWidthTextView.setGravity(17);
        this.mXWidthSeekBar = new SeekBar(this);
        this.mXWidthSeekBar.setOnSeekBarChangeListener(this);
        this.mXWidthSeekBar.setId(XWIDTH_SEEKBAR_RESID);
        this.mXWidthSeekBar.setMax(MAX_VALUE);
        this.mYWidthTextView = new TextView(this);
        this.mYWidthTextView.setText(new StringBuilder(YWIDTH_STRING).append(this.mYWidthValue).toString());
        this.mYWidthTextView.setTextSize(22.0f);
        this.mYWidthTextView.setTextColor(-16777216);
        this.mYWidthTextView.setGravity(17);
        this.mYWidthSeekBar = new SeekBar(this);
        this.mYWidthSeekBar.setOnSeekBarChangeListener(this);
        this.mYWidthSeekBar.setId(YWIDTH_SEEKBAR_RESID);
        this.mYWidthSeekBar.setMax(MAX_VALUE);
        this.mXGapTextView = new TextView(this);
        this.mXGapTextView.setText(new StringBuilder(XGAP_STRING).append(this.mXGapValue).toString());
        this.mXGapTextView.setTextSize(22.0f);
        this.mXGapTextView.setTextColor(-16777216);
        this.mXGapTextView.setGravity(17);
        this.mXGapSeekBar = new SeekBar(this);
        this.mXGapSeekBar.setOnSeekBarChangeListener(this);
        this.mXGapSeekBar.setId(XGAP_SEEKBAR_RESID);
        this.mXGapSeekBar.setMax(MAX_VALUE);
        this.mYGapTextView = new TextView(this);
        this.mYGapTextView.setText(new StringBuilder(YGAP_STRING).append(this.mYGapValue).toString());
        this.mYGapTextView.setTextSize(22.0f);
        this.mYGapTextView.setTextColor(-16777216);
        this.mYGapTextView.setGravity(17);
        this.mYGapSeekBar = new SeekBar(this);
        this.mYGapSeekBar.setOnSeekBarChangeListener(this);
        this.mYGapSeekBar.setId(YGAP_SEEKBAR_RESID);
        this.mYGapSeekBar.setMax(MAX_VALUE);
        mainLayout.addView(this.mXWidthTextView);
        mainLayout.addView(this.mXWidthSeekBar);
        mainLayout.addView(this.mYWidthTextView);
        mainLayout.addView(this.mYWidthSeekBar);
        mainLayout.addView(this.mXGapTextView);
        mainLayout.addView(this.mXGapSeekBar);
        mainLayout.addView(this.mYGapTextView);
        mainLayout.addView(this.mYGapSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case XWIDTH_SEEKBAR_RESID /*21863*/:
                this.mXWidthValue = progress;
                this.mXWidthTextView.setText(new StringBuilder(XWIDTH_STRING).append(this.mXWidthValue).toString());
                return;
            case YWIDTH_SEEKBAR_RESID /*21864*/:
                this.mYWidthValue = progress;
                this.mYWidthTextView.setText(new StringBuilder(YWIDTH_STRING).append(this.mYWidthValue).toString());
                return;
            case XGAP_SEEKBAR_RESID /*21865*/:
                this.mXGapValue = progress;
                this.mXGapTextView.setText(new StringBuilder(XGAP_STRING).append(this.mXGapValue).toString());
                return;
            case YGAP_SEEKBAR_RESID /*21866*/:
                this.mYGapValue = progress;
                this.mYGapTextView.setText(new StringBuilder(YGAP_STRING).append(this.mYGapValue).toString());
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
                WeaveFilter filter = new WeaveFilter();
                filter.setXWidth((float) WeaveFilterActivity.this.mXWidthValue);
                filter.setYWidth((float) WeaveFilterActivity.this.mYWidthValue);
                filter.setXGap((float) WeaveFilterActivity.this.mXGapValue);
                filter.setYGap((float) WeaveFilterActivity.this.mYGapValue);
                WeaveFilterActivity.this.mColors = filter.filter(WeaveFilterActivity.this.mColors, width, height);
                WeaveFilterActivity weaveFilterActivity = WeaveFilterActivity.this;
                final int i = width;
                final int i2 = height;
                weaveFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        WeaveFilterActivity.this.setModifyView(WeaveFilterActivity.this.mColors, i, i2);
                    }
                });
                WeaveFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
