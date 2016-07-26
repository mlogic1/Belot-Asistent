package com.merodyadt.belotasistent.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.merodyadt.belotasistent.R;

import java.util.List;

/**
 * Created by Filip on 25.7.2016..
 */
public class GamePrefFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference ListPrefWinScore;
    private EditTextPreference EditPrefTeamAName;
    private EditTextPreference EditPrefTeamBName;

    // Application settings
    private SharedPreferences preferences;
    private String winScore;                          // Constant defining how much score is needed to win
    private String TeamNameA;
    private String TeamNameB;

    @Override
    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        addPreferencesFromResource(R.xml.preferences);

        LoadSettings();
        InitializeGui();
        SetSummaries();

    }

    private void LoadSettings(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        winScore = preferences.getString("roundWinScore", "");
        TeamNameA = preferences.getString("defaultTeamNameA", "");
        TeamNameB = preferences.getString("defaultTeamNameB", "");
    }

    private void InitializeGui(){
        ListPrefWinScore = (ListPreference)findPreference("roundWinScore");
        EditPrefTeamAName = (EditTextPreference)findPreference("defaultTeamNameA");
        EditPrefTeamBName = (EditTextPreference)findPreference("defaultTeamNameB");
    }

    private void SetSummaries(){
        ListPrefWinScore.setSummary(winScore);
        EditPrefTeamAName.setSummary(TeamNameA);
        EditPrefTeamBName.setSummary(TeamNameB);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        if(key.equals(getString(R.string.themeColor))){
            ((GamePrefActivity)getActivity()).UpdateThemeColor();   // Call parent activity and apply theme changes
        }

        // When user changes a setting, load the new settings and change summaries
        LoadSettings();
        SetSummaries();
    }
}
