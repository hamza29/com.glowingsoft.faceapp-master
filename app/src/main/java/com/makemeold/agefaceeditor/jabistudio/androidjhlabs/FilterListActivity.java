package com.makemeold.agefaceeditor.jabistudio.androidjhlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class FilterListActivity extends Activity implements OnClickListener {
    private static final String ACTIVITY = "Activity";
    private static final String DOT = ".";
    private static final float FILTER_TEXT_SIZE = 14.6f;
    private int mDispalyHeight;
    private ScrollView mMainScrollView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.mDispalyHeight = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay().getHeight();
        int filterPackageNameId = getIntent().getIntExtra(AndroidjhlabsActivity.INTENT_PACKAGENAME_ID, -1);
        int filterNameArrayId = getIntent().getIntExtra(AndroidjhlabsActivity.INTENT_FILTERNAME_ARRAY_ID, -1);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.addView(getListLayout(filterPackageNameId, filterNameArrayId));
        this.mMainScrollView = new ScrollView(this);
        this.mMainScrollView.addView(linearLayout);
        setContentView(this.mMainScrollView);
    }

    private LinearLayout getListLayout(int activityNameId, int stringArrayId) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        String[] filterNameArray = getResources().getStringArray(stringArrayId);
        for (int i = 0; i < filterNameArray.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(filterNameArray[i]);
            textView.setTextColor(-1);
            textView.setTextSize((float) AndroidUtils.dipTopx(FILTER_TEXT_SIZE, this));
            textView.setGravity(16);
            textView.setBackgroundResource(17301602);
            textView.setClickable(true);
            textView.setOnClickListener(this);
            textView.setTag(new StringBuilder(DOT).append(getResources().getString(activityNameId)).append(DOT).append(filterNameArray[i]).append(ACTIVITY).toString());
            View saparator = new View(this);
            saparator.setBackgroundColor(-7829368);
            linearLayout.addView(textView);
            linearLayout.addView(saparator);
            LayoutParams textViewParams = (LayoutParams) textView.getLayoutParams();
            textViewParams.width = -1;
            textViewParams.height = (int) (((float) this.mDispalyHeight) / 7.5f);
            textView.setLayoutParams(textViewParams);
            LayoutParams saparatorParams = (LayoutParams) saparator.getLayoutParams();
            saparatorParams.width = -1;
            saparatorParams.height = 1;
            saparator.setLayoutParams(saparatorParams);
        }
        return linearLayout;
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), getPackageName() + ((String) v.getTag()));
        startActivity(intent);
    }
}
