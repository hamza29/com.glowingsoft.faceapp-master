package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.OffsetFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class OffsetFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int MAX_VALUE = 200;
    private static final int OFFSETX_SEEKBAR_RESID = 21863;
    private static final String OFFSETX_STRING = "OFFSETX:";
    private static final int OFFSETY_SEEKBAR_RESID = 21864;
    private static final String OFFSETY_STRING = "OFFSETY:";
    private static final String TITLE = "Offset";
    private int[] mColors;
    private SeekBar mOffsetXSeekBar;
    private TextView mOffsetXTextView;
    private int mOffsetXValue;
    private SeekBar mOffsetYSeekBar;
    private TextView mOffsetYTextView;
    private int mOffsetYValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mOffsetXTextView = new TextView(this);
        this.mOffsetXTextView.setText(new StringBuilder(OFFSETX_STRING).append(this.mOffsetXValue).toString());
        this.mOffsetXTextView.setTextSize(22.0f);
        this.mOffsetXTextView.setTextColor(-16777216);
        this.mOffsetXTextView.setGravity(17);
        this.mOffsetXSeekBar = new SeekBar(this);
        this.mOffsetXSeekBar.setOnSeekBarChangeListener(this);
        this.mOffsetXSeekBar.setId(OFFSETX_SEEKBAR_RESID);
        this.mOffsetXSeekBar.setMax(200);
        this.mOffsetYTextView = new TextView(this);
        this.mOffsetYTextView.setText(new StringBuilder(OFFSETY_STRING).append(this.mOffsetYValue).toString());
        this.mOffsetYTextView.setTextSize(22.0f);
        this.mOffsetYTextView.setTextColor(-16777216);
        this.mOffsetYTextView.setGravity(17);
        this.mOffsetYSeekBar = new SeekBar(this);
        this.mOffsetYSeekBar.setOnSeekBarChangeListener(this);
        this.mOffsetYSeekBar.setId(OFFSETY_SEEKBAR_RESID);
        this.mOffsetYSeekBar.setMax(200);
        mainLayout.addView(this.mOffsetXTextView);
        mainLayout.addView(this.mOffsetXSeekBar);
        mainLayout.addView(this.mOffsetYTextView);
        mainLayout.addView(this.mOffsetYSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case OFFSETX_SEEKBAR_RESID /*21863*/:
                this.mOffsetXValue = progress;
                this.mOffsetXTextView.setText(new StringBuilder(OFFSETX_STRING).append(this.mOffsetXValue).toString());
                return;
            case OFFSETY_SEEKBAR_RESID /*21864*/:
                this.mOffsetYValue = progress;
                this.mOffsetYTextView.setText(new StringBuilder(OFFSETY_STRING).append(this.mOffsetYValue).toString());
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
                OffsetFilter filter = new OffsetFilter();
                filter.setXOffset(OffsetFilterActivity.this.mOffsetXValue);
                filter.setYOffset(OffsetFilterActivity.this.mOffsetYValue);
                OffsetFilterActivity.this.mColors = filter.filter(OffsetFilterActivity.this.mColors, width, height);
                OffsetFilterActivity offsetFilterActivity = OffsetFilterActivity.this;
                final int i = width;
                final int i2 = height;
                offsetFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        OffsetFilterActivity.this.setModifyView(OffsetFilterActivity.this.mColors, i, i2);
                    }
                });
                OffsetFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
