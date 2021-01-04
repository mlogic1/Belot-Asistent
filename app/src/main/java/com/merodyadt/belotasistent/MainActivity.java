package com.merodyadt.belotasistent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.merodyadt.belotasistent.data.RoundData;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    // Used to load the 'native-lib' library on application startup.
    static
    {
        System.loadLibrary("native-lib");
    }

    private Toolbar m_toolBar;
    private MatchParser m_matchParser = null;
    private LinearLayout m_scoreBoardLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_toolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(m_toolBar);
        m_scoreBoardLayout = findViewById(R.id.main_linear_layout);
        m_matchParser = new MatchParser(this);
        JSONObject currentMatchObj = m_matchParser.LoadCurrentMatch();

        if (currentMatchObj == null)
        {
            // TODO load this from preferences activity
            InitBeloteControllerNew(1001);
        }
        else
        {
            InitBeloteControllerData(currentMatchObj.toString());
        }

        RefreshScoreboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    private void RefreshScoreboard()
    {
        ArrayList<RoundData> data = GetMatchRounds();
        m_scoreBoardLayout.removeAllViews();

        // TODO for each data entry instantiate 1 view
    }

    public native void InitBeloteControllerNew(int targetScore);
    public native void InitBeloteControllerData(String data);
    public native ArrayList<RoundData> GetMatchRounds();
}