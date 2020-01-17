package com.makemeold.agefaceeditor.jabistudio.androidjhlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class AndroidjhlabsActivity extends Activity implements OnClickListener {
    public static final int[] FILTER_NAME_ARRAY = new int[]{C0666R.array.color_adjustment_filtername, C0666R.array.distortion_and_warping_filtername, C0666R.array.effects_filtername, C0666R.array.blurring_and_sharpening_filtername, C0666R.array.edge_detection_filtername, C0666R.array.alpha_channel_filtername};
    public static final int[] FILTER_PACKAGE_NAME_ARRAY = new int[]{C0666R.string.coloradjustmentfilteractivity, C0666R.string.distortionandwarpingactivity, C0666R.string.effectsactivity, C0666R.string.blurringandsharpeningactivity, C0666R.string.edgedetectionactivity, C0666R.string.alphachannelactivity};
    public static final String INTENT_FILTERNAME_ARRAY_ID = "filternamearrayid";
    public static final String INTENT_PACKAGENAME_ID = "packagenameid";
    private static final float TEXT_SIZE = 14.6f;

    private int mDispalyHeight;
    private RelativeLayout mMainLayout;
    private ScrollView mMainScrollView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.mDispalyHeight = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay().getHeight();
        this.mMainLayout = new RelativeLayout(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        String[] titleNameArray = getResources().getStringArray(C0666R.array.filter_title_name);
        for (int i = 0; i < titleNameArray.length; i++) {
            setListLayout(linearLayout, titleNameArray[i], i);
        }
        this.mMainScrollView = new ScrollView(this);
        this.mMainScrollView.addView(linearLayout);
        this.mMainLayout.addView(this.mMainScrollView);
        setContentView(this.mMainLayout);

    }



    private void setListLayout(LinearLayout linearLayout, String titleString, int index) {
        TextView filterTitle = new TextView(this);
        filterTitle.setTextSize((float) AndroidUtils.dipTopx(TEXT_SIZE, this));
        filterTitle.setTextColor(-1);
        filterTitle.setGravity(16);
        filterTitle.setText(titleString);
        filterTitle.setOnClickListener(this);
        filterTitle.setBackgroundResource(17301602);
        filterTitle.setClickable(true);
        filterTitle.setTag(Integer.valueOf(index));
        View titleSaparator = new View(this);
        titleSaparator.setBackgroundColor(-7829368);
        linearLayout.addView(filterTitle);
        linearLayout.addView(titleSaparator);
        LinearLayout.LayoutParams textViewParams = (LinearLayout.LayoutParams) filterTitle.getLayoutParams();
        textViewParams.width = -1;
        textViewParams.height = (int) (((float) this.mDispalyHeight) / 7.5f);
        filterTitle.setLayoutParams(textViewParams);
        LinearLayout.LayoutParams titleSaparatorParams = (LinearLayout.LayoutParams) titleSaparator.getLayoutParams();
        titleSaparatorParams.width = -1;
        titleSaparatorParams.height = 1;
        titleSaparator.setLayoutParams(titleSaparatorParams);
    }

    public void onClick(View v) {
        int index = ((Integer) v.getTag()).intValue();
        Intent intent = new Intent(this, FilterListActivity.class);
        intent.putExtra(INTENT_PACKAGENAME_ID, FILTER_PACKAGE_NAME_ARRAY[index]);
        intent.putExtra(INTENT_FILTERNAME_ARRAY_ID, FILTER_NAME_ARRAY[index]);
        startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();

    }
}
