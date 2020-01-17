package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.Curve;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.CurvesFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class CurvesFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int KX_SEEKBAR_RESID = 21863;
    private static final String KX_STRING = "KX:";
    private static final int KY_SEEKBAR_RESID = 21864;
    private static final String KY_STRING = "KY:";
    private static final int MAX_VALUE = 100;
    private static final String TITLE = "Curves";
    private int[] mColors;
    private SeekBar mKXSeekBar;
    private TextView mKXTextView;
    private int mKXValue;
    private SeekBar mKYSeekBar;
    private TextView mKYTextView;
    private int mKYValue;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mKXTextView = new TextView(this);
        this.mKXTextView.setText(new StringBuilder(KX_STRING).append(this.mKXValue).toString());
        this.mKXTextView.setTextSize(22.0f);
        this.mKXTextView.setTextColor(-16777216);
        this.mKXTextView.setGravity(17);
        this.mKXSeekBar = new SeekBar(this);
        this.mKXSeekBar.setOnSeekBarChangeListener(this);
        this.mKXSeekBar.setId(KX_SEEKBAR_RESID);
        this.mKXSeekBar.setMax(100);
        this.mKYTextView = new TextView(this);
        this.mKYTextView.setText(new StringBuilder(KY_STRING).append(this.mKYValue).toString());
        this.mKYTextView.setTextSize(22.0f);
        this.mKYTextView.setTextColor(-16777216);
        this.mKYTextView.setGravity(17);
        this.mKYSeekBar = new SeekBar(this);
        this.mKYSeekBar.setOnSeekBarChangeListener(this);
        this.mKYSeekBar.setId(KY_SEEKBAR_RESID);
        this.mKYSeekBar.setMax(100);
        mainLayout.addView(this.mKXTextView);
        mainLayout.addView(this.mKXSeekBar);
        mainLayout.addView(this.mKYTextView);
        mainLayout.addView(this.mKYSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case KX_SEEKBAR_RESID /*21863*/:
                this.mKXValue = progress;
                this.mKXTextView.setText(new StringBuilder(KX_STRING).append(getValue(this.mKXValue)).toString());
                return;
            case KY_SEEKBAR_RESID /*21864*/:
                this.mKYValue = progress;
                this.mKYTextView.setText(new StringBuilder(KY_STRING).append(getValue(this.mKYValue)).toString());
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
                CurvesFilter filter = new CurvesFilter();
                Curve curve = new Curve();
                curve.addKnot(CurvesFilterActivity.this.getValue(CurvesFilterActivity.this.mKXValue), CurvesFilterActivity.this.getValue(CurvesFilterActivity.this.mKYValue));
                filter.setCurve(curve);
                CurvesFilterActivity.this.mColors = filter.filter(CurvesFilterActivity.this.mColors, width, height);
                CurvesFilterActivity curvesFilterActivity = CurvesFilterActivity.this;
                final int i = width;
                final int i2 = height;
                curvesFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        CurvesFilterActivity.this.setModifyView(CurvesFilterActivity.this.mColors, i, i2);
                    }
                });
                CurvesFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }
}
