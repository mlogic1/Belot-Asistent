package com.merodyadt.belotasistent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity
{
	private Toolbar m_toolBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		if (savedInstanceState == null)
		{
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.settings, new SettingsFragment())
					.commit();
		}

		m_toolBar = findViewById(R.id.settings_toolbar);
		setSupportActionBar(m_toolBar);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowHomeEnabled(true);
		}
	}

	@Override
	public boolean onSupportNavigateUp()
	{
		onBackPressed();
		return true;
	}

	public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
	{
		private ListPreference m_prefPointsInRound = null;
		private EditTextPreference m_prefTeamNameA = null;
		private EditTextPreference m_prefTeamNameB = null;
		private SharedPreferences m_preferences = null;

		@Override
		public void onCreate(@Nullable Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			getChildFragmentManager().executePendingTransactions();
			m_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			m_prefPointsInRound = (ListPreference)findPreference(getString(R.string.settings_key_points_in_round));
			m_prefTeamNameA = (EditTextPreference)findPreference(getString(R.string.settings_key_team_name_A));
			m_prefTeamNameB = (EditTextPreference)findPreference(getString(R.string.settings_key_team_name_B));

			// emptyTeamNameValidator prevents setting an empty team name
			Preference.OnPreferenceChangeListener emptyTeamNameValidator = new Preference.OnPreferenceChangeListener()
			{
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue)
				{
					if (newValue.toString().length() == 0)
						return false;
					return true;
				}
			};

			m_prefTeamNameA.setOnPreferenceChangeListener(emptyTeamNameValidator);
			m_prefTeamNameB.setOnPreferenceChangeListener(emptyTeamNameValidator);
			RefreshView();
		}

		private void RefreshView()
		{
			String pointsInRound = m_preferences.getString(getString(R.string.settings_key_points_in_round), "");
			m_prefPointsInRound.setSummary(pointsInRound);
		}

		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
		{
			setPreferencesFromResource(R.xml.root_preferences, rootKey);
		}

		@Override
		public void onResume()
		{
			super.onResume();
			m_preferences.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause()
		{
			super.onPause();
			m_preferences.unregisterOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
		{
			RefreshView();
		}
	}
}