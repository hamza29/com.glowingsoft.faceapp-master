package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makemeold.agefaceeditor.BuildConfig;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.BlockFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class BlockFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener {
    private static final int BLOCKSIZE_SEEKBAR_RESID = 21865;
    private static final String BLOCKSIZE_STRING = "BLOCKSIZE:";
    private static final int MAX_VALUE = 100;
    private static final String TITLE = "Block";
    private SeekBar mBlockSeekBar;
    private TextView mBlockTextView;
    private int mBlockValue;
    private int[] mColors;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(this.mMainLayout);
    }

    private void filterSeekBarSetup(LinearLayout mainLayout) {
        this.mBlockTextView = new TextView(this);
        this.mBlockTextView.setText(new StringBuilder(BLOCKSIZE_STRING).append(this.mBlockValue).toString());
        this.mBlockTextView.setTextSize(22.0f);
        this.mBlockTextView.setTextColor(-16777216);
        this.mBlockTextView.setGravity(17);
        this.mBlockSeekBar = new SeekBar(this);
        this.mBlockSeekBar.setOnSeekBarChangeListener(this);
        this.mBlockSeekBar.setId(BLOCKSIZE_SEEKBAR_RESID);
        this.mBlockSeekBar.setMax(100);
        mainLayout.addView(this.mBlockTextView);
        mainLayout.addView(this.mBlockSeekBar);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case BLOCKSIZE_SEEKBAR_RESID /*21865*/:
                this.mBlockValue = progress;
                this.mBlockTextView.setText(new StringBuilder(BLOCKSIZE_STRING).append(this.mBlockValue).toString());
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
                BlockFilter filter = new BlockFilter();
                if (BlockFilterActivity.this.mBlockValue == 0) {
                    BlockFilterActivity.this.mBlockValue = 1;
                }
                filter.setBlockSize(BlockFilterActivity.this.mBlockValue);
                BlockFilterActivity.this.mColors = filter.filter(BlockFilterActivity.this.mColors, width, height);
                BlockFilterActivity blockFilterActivity = BlockFilterActivity.this;
                final int i = width;
                final int i2 = height;
                blockFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        BlockFilterActivity.this.setModifyView(BlockFilterActivity.this.mColors, i, i2);
                    }
                });
                BlockFilterActivity.this.mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
