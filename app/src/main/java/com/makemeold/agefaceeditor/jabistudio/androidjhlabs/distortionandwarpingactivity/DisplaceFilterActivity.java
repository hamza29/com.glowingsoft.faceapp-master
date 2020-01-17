package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.DisplaceFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class DisplaceFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener, OnItemSelectedListener {
    private static final int AMOUNT_SEEKBAR_RESID = 21865;
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final String EDGES_TYPE1_STRING = "Transparent";
    private static final String EDGES_TYPE2_STRING = "Clamp";
    private static final String EDGES_TYPE3_STRING = "Wrap";
    private static final int MAX_VALUE = 100;
    private static final String TITLE = "Displace";
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private int mAmountValue;
    private int[] mColors;
    private Spinner mEdgesSpinner;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mAmountTextView = new TextView(this);
        this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(this.mAmountValue).toString());
        this.mAmountTextView.setTextSize(22.0f);
        this.mAmountTextView.setTextColor(-16777216);
        this.mAmountTextView.setGravity(17);
        this.mAmountSeekBar = new SeekBar(this);
        this.mAmountSeekBar.setOnSeekBarChangeListener(this);
        this.mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        this.mAmountSeekBar.setMax(100);
        this.mEdgesSpinner = new Spinner(this);
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter(this, 17367048);
        shapeAdapter.add(EDGES_TYPE1_STRING);
        shapeAdapter.add(EDGES_TYPE2_STRING);
        shapeAdapter.add(EDGES_TYPE3_STRING);
        this.mEdgesSpinner.setAdapter(shapeAdapter);
        this.mEdgesSpinner.setOnItemSelectedListener(this);
        mainLayout.addView(this.mAmountTextView);
        mainLayout.addView(this.mAmountSeekBar);
        mainLayout.addView(this.mEdgesSpinner);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case AMOUNT_SEEKBAR_RESID /*21865*/:
                this.mAmountValue = progress;
                this.mAmountTextView.setText(new StringBuilder(AMOUNT_STRING).append(getValue(this.mAmountValue)).toString());
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
            public void run() {
                DisplaceFilter filter = new DisplaceFilter();
                filter.setAmount(DisplaceFilterActivity.this.getValue(DisplaceFilterActivity.this.mAmountValue));
                filter.setEdgeAction(DisplaceFilterActivity.this.getSelectType());
                DisplaceFilterActivity.this.mColors = filter.filter(DisplaceFilterActivity.this.mColors, width, height);
                DisplaceFilterActivity displaceFilterActivity = DisplaceFilterActivity.this;
                final int i = width;
                final int i2 = height;
                displaceFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        DisplaceFilterActivity.this.setModifyView(DisplaceFilterActivity.this.mColors, i, i2);
                    }
                });
                DisplaceFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private int getSelectType() {
        if (this.mEdgesSpinner.getSelectedItem().equals(EDGES_TYPE1_STRING)) {
            return 0;
        }
        if (this.mEdgesSpinner.getSelectedItem().equals(EDGES_TYPE2_STRING)) {
            return 1;
        }
        return 2;
    }

    private float getValue(int value) {
        return ((float) value) / 100.0f;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        applyFilter();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
