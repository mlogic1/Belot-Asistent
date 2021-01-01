package com.merodyadt.belotasistent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.merodyadt.belotasistent.data.RoundData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    // Used to load the 'native-lib' library on application startup.
    static
    {
        System.loadLibrary("native-lib");
    }

    private Toolbar m_toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_toolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(m_toolBar);

        InitBeloteController();
        RefreshScoreboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    private void RefreshScoreboard()
    {
        GetMatchRounds();
    }

    public native void InitBeloteController();
    public native ArrayList<RoundData> GetMatchRounds();
}