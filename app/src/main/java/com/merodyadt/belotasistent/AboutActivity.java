package com.merodyadt.belotasistent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar)findViewById(R.id.toolBarAbout);
        toolbar.setTitle("O Aplikaciji");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }


    public void OpenGitHubHyperlink(View view){
        Uri uri = Uri.parse("https://github.com/mlogic1/Belot-asistent");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void OpenCC4LicenseHyperlink(View view){
        Uri uri = Uri.parse("https://creativecommons.org/licenses/by/4.0/legalcode");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
