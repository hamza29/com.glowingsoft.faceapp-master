package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.effectsactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.makemeold.agefaceeditor.R;

public class BorderFilterActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText(R.string.none_filter);
        setContentView(view);
    }
}
