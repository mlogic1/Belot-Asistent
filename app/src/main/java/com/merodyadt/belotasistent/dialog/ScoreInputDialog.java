package com.merodyadt.belotasistent.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.merodyadt.belotasistent.R;
import com.merodyadt.belotasistent.data.RoundData;

public class ScoreInputDialog extends AlertDialog
{
	private Context m_context;
	private AlertDialog.Builder m_alertDialogBuilder = null;
	private RoundData m_result = null;

	private RadioButton m_radioButtonCallingTeamA = null;
	private RadioButton m_radioButtonCallingTeamB = null;

	private EditText m_editTextScoreTeamA = null;
	private EditText m_editTextScoreTeamB = null;
	private EditText m_editTextCallsTeamA = null;
	private EditText m_editTextCallsTeamB = null;

	private CheckBox m_checkboxBelaTeamA = null;
	private CheckBox m_checkboxBelaTeamB = null;

	public ScoreInputDialog(@NonNull Context context)
	{
		super(context);
		m_context = context;
	}

	@Override
	public void show()
	{
		LayoutInflater layoutInflater = LayoutInflater.from(m_context);
		View dialogContent = layoutInflater.inflate(R.layout.popup_score_input, null);

		m_alertDialogBuilder = new Builder(m_context);
		m_alertDialogBuilder.setTitle(R.string.score_input_title);
		m_alertDialogBuilder.setView(dialogContent);

		// setup all references
		m_radioButtonCallingTeamA = dialogContent.findViewById(R.id.score_input_radio_calling_team_a);
		m_radioButtonCallingTeamB = dialogContent.findViewById(R.id.score_input_radio_calling_team_b);

		m_alertDialogBuilder.setPositiveButton(R.string.score_input_button_ok, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				// TODO handle this
			}
		});

		m_alertDialogBuilder.setNegativeButton(R.string.score_input_button_cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				cancel();
			}
		});

		m_alertDialogBuilder.create().show();
	}
}
