package com.merodyadt.belotasistent.preferences;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.merodyadt.belotasistent.R;

/**
 * Created by Filip on 25.7.2016..
 */
public class GamePrefActivity extends AppCompatActivity {

    private Toolbar toolbar;

    // Application settings
    SharedPreferences preferences;
    int winScore;
    boolean countRounds;
    String TeamNameA;
    String TeamNameB;
    String appThemeColor;

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_preferences);

        LoadSettings();

        // Setup toolbar
        SetupToolBar();

        getFragmentManager().beginTransaction()
                .replace(R.id.preferences_content_frame, new GamePrefFragment())
                .commit();
    }

    private void LoadSettings(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        winScore = Integer.parseInt(preferences.getString("roundWinScore", ""));
        countRounds = preferences.getBoolean("countRounds", true);
        TeamNameA = preferences.getString("defaultTeamNameA", "");
        TeamNameB = preferences.getString("defaultTeamNameB", "");
        appThemeColor = preferences.getString("themeColor", "");
    }

    private void SetupToolBar(){
        // Set up toolbar
        toolbar = (Toolbar)findViewById(R.id.toolBarPreferences);
        toolbar.setTitle(R.string.SettingsToolBarName);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.parseColor(appThemeColor));
        setSupportActionBar(toolbar);
    }


    public void UpdateThemeColor(){
        LoadSettings();
        toolbar.setBackgroundColor(Color.parseColor(appThemeColor));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
