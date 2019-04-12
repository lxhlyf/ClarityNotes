package com.claritynotes.ui.activity;

import android.os.Bundle;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseThemedActivity;

public class WidgetActivity extends BaseThemedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        findViewById(R.id.seek_disabled).setEnabled(false);
    }
}
