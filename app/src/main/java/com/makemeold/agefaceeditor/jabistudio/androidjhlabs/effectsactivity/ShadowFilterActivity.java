package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.C0666R;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ShadowFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int ANGLE_SEEKBAR_RESID = 21863;
    private static final String ANGLE_STRING = "ANGLE:";
    private static final int DISTANCE_SEEKBAR_RESID = 21864;
    private static final String DISTANCE_STRING = "DISTANCE:";
    private static final int MAX_VALUE = 100;
    private static final int OPACITY_SEEKBAR_RESID = 21866;
    private static final String OPACITY_STRING = "OPACITY:";
    private static final int SOFTNESS_SEEKBAR_RESID = 21865;
    private static final String SOFTNESS_STRING = "SOFTNESS:";
    private static final String TITLE = "Shadow";
    private SeekBar mAngleSeekBar;
    private TextView mAngleTextView;
    private int mAngleValue;
    private int[] mColors;
    private SeekBar mDistanceSeekBar;
    private TextView mDistanceTextView;
    private int mDistanceValue;
    private SeekBar mOpacitySeekBar;
    private TextView mOpacityTextView;
    private int mOpacityValue;
    private ProgressDialog mProgressDialog;
    private SeekBar mSoftnessSeekBar;
    private TextView mSoftnessTextView;
    private int mSoftnessValue;


    class C07691 extends Thread {
        C07691() {
        }

        public void run() {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText(C0666R.string.none_filter);
        setContentView(view);
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
        this.mAngleSeekBar.setMax(100);
        this.mDistanceTextView = new TextView(this);
        this.mDistanceTextView.setText(new StringBuilder(DISTANCE_STRING).append(this.mDistanceValue).toString());
        this.mDistanceTextView.setTextSize(22.0f);
        this.mDistanceTextView.setTextColor(-16777216);
        this.mDistanceTextView.setGravity(17);
        this.mDistanceSeekBar = new SeekBar(this);
        this.mDistanceSeekBar.setOnSeekBarChangeListener(this);
        this.mDistanceSeekBar.setId(DISTANCE_SEEKBAR_RESID);
        this.mDistanceSeekBar.setMax(100);
        this.mSoftnessTextView = new TextView(this);
        this.mSoftnessTextView.setText(new StringBuilder(SOFTNESS_STRING).append(this.mSoftnessValue).toString());
        this.mSoftnessTextView.setTextSize(22.0f);
        this.mSoftnessTextView.setTextColor(-16777216);
        this.mSoftnessTextView.setGravity(17);
        this.mSoftnessSeekBar = new SeekBar(this);
        this.mSoftnessSeekBar.setOnSeekBarChangeListener(this);
        this.mSoftnessSeekBar.setId(SOFTNESS_SEEKBAR_RESID);
        this.mSoftnessSeekBar.setMax(100);
        this.mOpacityTextView = new TextView(this);
        this.mOpacityTextView.setText(new StringBuilder(OPACITY_STRING).append(this.mOpacityValue).toString());
        this.mOpacityTextView.setTextSize(22.0f);
        this.mOpacityTextView.setTextColor(-16777216);
        this.mOpacityTextView.setGravity(17);
        this.mOpacitySeekBar = new SeekBar(this);
        this.mOpacitySeekBar.setOnSeekBarChangeListener(this);
        this.mOpacitySeekBar.setId(OPACITY_SEEKBAR_RESID);
        this.mOpacitySeekBar.setMax(100);
        mainLayout.addView(this.mAngleTextView);
        mainLayout.addView(this.mAngleSeekBar);
        mainLayout.addView(this.mDistanceTextView);
        mainLayout.addView(this.mDistanceSeekBar);
        mainLayout.addView(this.mSoftnessTextView);
        mainLayout.addView(this.mSoftnessSeekBar);
        mainLayout.addView(this.mOpacityTextView);
        mainLayout.addView(this.mOpacitySeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case ANGLE_SEEKBAR_RESID /*21863*/:
                this.mAngleValue = progress;
                this.mAngleTextView.setText(new StringBuilder(ANGLE_STRING).append(this.mAngleValue).toString());
                return;
            case DISTANCE_SEEKBAR_RESID /*21864*/:
                this.mDistanceValue = progress;
                this.mDistanceTextView.setText(new StringBuilder(DISTANCE_STRING).append(this.mDistanceValue).toString());
                return;
            case SOFTNESS_SEEKBAR_RESID /*21865*/:
                this.mSoftnessValue = progress;
                this.mSoftnessTextView.setText(new StringBuilder(SOFTNESS_STRING).append(this.mSoftnessValue).toString());
                return;
            case OPACITY_SEEKBAR_RESID /*21866*/:
                this.mOpacityValue = progress;
                this.mOpacityTextView.setText(new StringBuilder(OPACITY_STRING).append(this.mOpacityValue).toString());
                return;
            default:
                return;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        int width = this.mOriginalImageView.getDrawable().getIntrinsicWidth();
        int height = this.mOriginalImageView.getDrawable().getIntrinsicHeight();
        this.mColors = AndroidUtils.drawableToIntArray(this.mOriginalImageView.getDrawable());
        this.mProgressDialog = ProgressDialog.show(this, BuildConfig.FLAVOR, "Wait......");
        Thread thread = new C07691();
        thread.setDaemon(true);
        thread.start();
    }
}
