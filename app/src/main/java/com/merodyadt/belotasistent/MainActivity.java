package com.merodyadt.belotasistent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.merodyadt.belotasistent.preferences.GamePrefActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolBar;

    // Application settings
    SharedPreferences preferences;
    String appThemeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        LoadSettings();
        SetupToolBar();

    }



    // This method gets called when the user presses Back from some of the activities and comes back to menu
    // Its needed when the user changes the theme and comes back to the menu, so the new theme is applied
    @Override
    protected void onRestart(){
        super.onRestart();
        appThemeColor = preferences.getString("themeColor", "");
        toolBar.setBackgroundColor(Color.parseColor(appThemeColor));
    }

    private void LoadSettings(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appThemeColor = preferences.getString("themeColor", "");
    }

    private void SetupToolBar(){
        toolBar = (Toolbar)findViewById(R.id.toolBarMenu);
        toolBar.setTitle(R.string.app_name);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setBackgroundColor(Color.parseColor(appThemeColor));
        setSupportActionBar(toolBar);
    }


    public void StartGame(View view){
        // Start a new game
        Intent i = new Intent(this, GameActivity.class);
        this.startActivity(i);
    }

    public void StartTournamentGame(View view){
        Toast.makeText(getApplicationContext(), "Turnirski mod je trenutno u izradi",
                Toast.LENGTH_LONG).show();
    }

    public void OpenSettings(View view){

        // Open settings activity
        Intent i = new Intent(this, GamePrefActivity.class);
        startActivity(i);
    }

    public void ShowAboutDialog(View view){
        //Intent i = new Intent(this, AboutActivity.class);
        Intent i = new Intent(this, MenuTestActivity.class);
        this.startActivity(i);
    }



    public void ExitApplication(View view){
        finish();
        System.exit(0);
    }

}
