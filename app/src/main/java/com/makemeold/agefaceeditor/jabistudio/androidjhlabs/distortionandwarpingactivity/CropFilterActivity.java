package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.C0666R;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.CropFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class CropFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int HEIGHT_SEEKBAR_RESID = 21866;
    private static final String HEIGHT_STRING = "HEIGHT:";
    private static final String TITLE = "Ripple";
    private static final int WIDTH_SEEKBAR_RESID = 21865;
    private static final String WIDTH_STRING = "WIDTH:";
    private static final int X_SEEKBAR_RESID = 21863;
    private static final String X_STRING = "X:";
    private static final int Y_SEEKBAR_RESID = 21864;
    private static final String Y_STRING = "Y:";
    private int[] mColors;
    private SeekBar mHeightSeekBar;
    private int mHeightValue;
    private TextView mHeightView;
    private ProgressDialog mProgressDialog;
    private SeekBar mWidthSeekBar;
    private TextView mWidthTextView;
    private int mWidthValue;
    private SeekBar mXSeekBar;
    private TextView mXTextView;
    private int mXValue;
    private SeekBar mYSeekBar;
    private TextView mYTextView;
    private int mYValue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mXTextView = new TextView(this);
        this.mXTextView.setText(new StringBuilder(X_STRING).append(this.mXValue).toString());
        this.mXTextView.setTextSize(22.0f);
        this.mXTextView.setTextColor(-16777216);
        this.mXTextView.setGravity(17);
        this.mXSeekBar = new SeekBar(this);
        this.mXSeekBar.setOnSeekBarChangeListener(this);
        this.mXSeekBar.setId(X_SEEKBAR_RESID);
        this.mXSeekBar.setMax(AndroidUtils.getBitmapOfWidth(getResources(), C0666R.drawable.image));
        this.mYTextView = new TextView(this);
        this.mYTextView.setText(new StringBuilder(Y_STRING).append(this.mYValue).toString());
        this.mYTextView.setTextSize(22.0f);
        this.mYTextView.setTextColor(-16777216);
        this.mYTextView.setGravity(17);
        this.mYSeekBar = new SeekBar(this);
        this.mYSeekBar.setOnSeekBarChangeListener(this);
        this.mYSeekBar.setId(Y_SEEKBAR_RESID);
        this.mYSeekBar.setMax(AndroidUtils.getBitmapOfHeight(getResources(), C0666R.drawable.image));
        this.mWidthTextView = new TextView(this);
        this.mWidthTextView.setText(new StringBuilder(WIDTH_STRING).append(this.mWidthValue).toString());
        this.mWidthTextView.setTextSize(22.0f);
        this.mWidthTextView.setTextColor(-16777216);
        this.mWidthTextView.setGravity(17);
        this.mWidthSeekBar = new SeekBar(this);
        this.mWidthSeekBar.setOnSeekBarChangeListener(this);
        this.mWidthSeekBar.setId(WIDTH_SEEKBAR_RESID);
        this.mWidthSeekBar.setMax(AndroidUtils.getBitmapOfWidth(getResources(), C0666R.drawable.image));
        this.mWidthSeekBar.setProgress(AndroidUtils.getBitmapOfWidth(getResources(), C0666R.drawable.image));
        this.mHeightView = new TextView(this);
        this.mHeightView.setText(new StringBuilder(HEIGHT_STRING).append(this.mHeightValue).toString());
        this.mHeightView.setTextSize(22.0f);
        this.mHeightView.setTextColor(-16777216);
        this.mHeightView.setGravity(17);
        this.mHeightSeekBar = new SeekBar(this);
        this.mHeightSeekBar.setOnSeekBarChangeListener(this);
        this.mHeightSeekBar.setId(HEIGHT_SEEKBAR_RESID);
        this.mHeightSeekBar.setMax(AndroidUtils.getBitmapOfHeight(getResources(), C0666R.drawable.image));
        this.mHeightSeekBar.setProgress(AndroidUtils.getBitmapOfHeight(getResources(), C0666R.drawable.image));
        mainLayout.addView(this.mXTextView);
        mainLayout.addView(this.mXSeekBar);
        mainLayout.addView(this.mYTextView);
        mainLayout.addView(this.mYSeekBar);
        mainLayout.addView(this.mWidthTextView);
        mainLayout.addView(this.mWidthSeekBar);
        mainLayout.addView(this.mHeightView);
        mainLayout.addView(this.mHeightSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case X_SEEKBAR_RESID /*21863*/:
                this.mXValue = progress;
                this.mXTextView.setText(new StringBuilder(X_STRING).append(this.mXValue).toString());
                return;
            case Y_SEEKBAR_RESID /*21864*/:
                this.mYValue = progress;
                this.mYTextView.setText(new StringBuilder(Y_STRING).append(this.mYValue).toString());
                return;
            case WIDTH_SEEKBAR_RESID /*21865*/:
                this.mWidthValue = progress;
                this.mWidthTextView.setText(new StringBuilder(WIDTH_STRING).append(this.mWidthValue).toString());
                return;
            case HEIGHT_SEEKBAR_RESID /*21866*/:
                this.mHeightValue = progress;
                this.mHeightView.setText(new StringBuilder(HEIGHT_STRING).append(this.mHeightValue).toString());
                return;
            default:
                return;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        applyFilter();
    }

    private void applyFilter() {
        final int width = this.mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = this.mOriginalImageView.getDrawable().getIntrinsicHeight();
        this.mColors = AndroidUtils.drawableToIntArray(this.mOriginalImageView.getDrawable());
        this.mProgressDialog = ProgressDialog.show(this, BuildConfig.FLAVOR, "Wait......");
        Thread thread = new Thread() {


            class C07191 implements Runnable {
                C07191() {
                }

                public void run() {
                    CropFilterActivity.this.setModifyView(CropFilterActivity.this.mColors, CropFilterActivity.this.mWidthValue, CropFilterActivity.this.mHeightValue);
                }
            }

            public void run() {
                CropFilter filter = new CropFilter();
                filter.setX(CropFilterActivity.this.mXValue);
                filter.setY(CropFilterActivity.this.mYValue);
                filter.setWidth(CropFilterActivity.this.mWidthValue);
                filter.setHeight(CropFilterActivity.this.mHeightValue);
                CropFilterActivity.this.mColors = filter.filter(CropFilterActivity.this.mColors, width, height);
                CropFilterActivity.this.runOnUiThread(new C07191());
                CropFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
