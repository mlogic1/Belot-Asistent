package com.merodyadt.belotasistent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.merodyadt.belotasistent.adapter.RoundListAdapter;
import com.merodyadt.belotasistent.data.RoundData;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final String BELOT_LOG_TAG = "BELOT-ASISTENT-LOG";
    // Used to load the 'native-lib' library on application startup.
    static
    {
        System.loadLibrary("native-lib");
    }

    private Toolbar m_toolBar;
    private MatchParser m_matchParser = null;
    private ListView m_scoreBoardView = null;
    private ArrayAdapter<RoundData> m_roundListAdapter = null;
    private FloatingActionButton m_addRoundFloatingButton = null;
    private View.OnClickListener m_addRoundClickListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_toolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(m_toolBar);
        m_scoreBoardView = findViewById(R.id.main_list_view);
        m_addRoundFloatingButton = findViewById(R.id.floatingButtonAddRound);
        m_addRoundClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddRoundButtonClick(v);
            }
        };
        m_addRoundFloatingButton.setOnClickListener(m_addRoundClickListener);
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
        switch (item.getItemId())
        {
            case R.id.main_menu_add:
            {
                AddRoundButtonClick(null);
                break;
            }
            case R.id.main_menu_card_dealer:
            {
                // TODO toast the card dealer
                break;
            }
            case R.id.main_menu_reset_round_counter:
            {
                // TODO reset the round counter
                int x = 1;
                break;
            }
            case R.id.main_menu_settings:
            {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            }
            default:
            {
                Log.v(BELOT_LOG_TAG, "Unsupported option clicked");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void RefreshScoreboard()
    {
        ArrayList<RoundData> data = new ArrayList<>();
        GetMatchRounds(data);
        if (m_roundListAdapter == null)
        {
            m_roundListAdapter = new RoundListAdapter(this, R.layout.adapter_round_view, data);
            m_scoreBoardView.setAdapter(m_roundListAdapter);
        }
        else
        {
            m_roundListAdapter.clear();
            m_roundListAdapter.addAll(data);
            m_roundListAdapter.notifyDataSetChanged();
        }

        // TODO update sum at the bottom
    }

    public void AddRoundButtonClick(View view)
    {
        Log.v(BELOT_LOG_TAG, "TODO add round input");
    }

    // native declarations
    public native void InitBeloteControllerNew(int targetScore);
    public native void InitBeloteControllerData(String data);
    public native void GetMatchRounds(ArrayList<RoundData> outRounds);
}