package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.makemeold.agefaceeditor.R;

public class LookupFilterActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText(R.string.none_filter);
        setContentView(view);
    }
}
