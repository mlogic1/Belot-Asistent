package com.merodyadt.belotasistent;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar t = (Toolbar)findViewById(R.id.toolBarHelp);
        t.setTitle("Pomoć i napomene");
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);
    }
}
