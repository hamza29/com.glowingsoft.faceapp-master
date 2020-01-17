package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.SuperFilterActivity;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.MinimumFilter;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class MinimumFilterActivity extends SuperFilterActivity implements OnClickListener {
    private static final String APPLY_STRING = "Apply:";
    private static final String TITLE = "Minimum";
    private int mApplyCount = 0;
    private TextView mApplyText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterButtonSetup(this.mMainLayout);
    }

    private void filterButtonSetup(LinearLayout mainLayout) {
        this.mApplyText = new TextView(this);
        this.mApplyText.setText(new StringBuilder(APPLY_STRING).append(this.mApplyCount).toString());
        this.mApplyText.setTextSize(22.0f);
        this.mApplyText.setTextColor(-16777216);
        this.mApplyText.setGravity(17);
        Button button = new Button(this);
        button.setText(APPLY_STRING);
        button.setOnClickListener(this);
        mainLayout.addView(this.mApplyText);
        mainLayout.addView(button);
    }

    public void onClick(View view) {
        int width = this.mModifyImageView.getDrawable().getIntrinsicWidth();
        int height = this.mModifyImageView.getDrawable().getIntrinsicHeight();
        setModifyView(new MinimumFilter().filter(AndroidUtils.drawableToIntArray(this.mModifyImageView.getDrawable()), width, height), width, height);
        this.mApplyCount++;
        this.mApplyText.setText(new StringBuilder(APPLY_STRING).append(this.mApplyCount).toString());
    }
}
