package com.merodyadt.belotasistent.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.merodyadt.belotasistent.R;

class ScoreTextWatcher implements TextWatcher
{
	private EditText m_currentEditText = null;
	private EditText m_oposingEditText = null;
	private final int ROUND_TOTAL_SCORE = 162;

	public ScoreTextWatcher(EditText currentEditText, EditText oposingEditText)
	{
		m_currentEditText = currentEditText;
		m_oposingEditText = oposingEditText;
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{
		if (!m_currentEditText.isFocused())
		{
			return;
		}

		m_oposingEditText.setText("123");
	}

	@Override
	public void afterTextChanged(Editable editable)
	{

	}
}

public class ScoreInputDialog extends AlertDialog
{
	private Context m_context;
	private AlertDialog.Builder m_alertDialogBuilder = null;
	private ScoreInputDialogFinishedEventListener m_dialogFinishedListner;

	private RadioButton m_radioButtonCallingTeamA = null;
	private RadioButton m_radioButtonCallingTeamB = null;

	private EditText m_editTextScoreTeamA = null;
	private EditText m_editTextScoreTeamB = null;
	private EditText m_editTextCallsTeamA = null;
	private EditText m_editTextCallsTeamB = null;

	private CheckBox m_checkboxBelaTeamA = null;
	private CheckBox m_checkboxBelaTeamB = null;

	public ScoreInputDialog(@NonNull Context context, ScoreInputDialogFinishedEventListener dialogFinishedEventListner)
	{
		super(context);
		m_dialogFinishedListner = dialogFinishedEventListner;
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
		m_editTextScoreTeamA = dialogContent.findViewById(R.id.score_input_score_team_a);
		m_editTextScoreTeamB = dialogContent.findViewById(R.id.score_input_score_team_b);
		m_editTextCallsTeamA = dialogContent.findViewById(R.id.score_input_calls_team_a);
		m_editTextCallsTeamB = dialogContent.findViewById(R.id.score_input_calls_team_b);
		m_checkboxBelaTeamA = dialogContent.findViewById(R.id.score_input_bela_team_a);
		m_checkboxBelaTeamB = dialogContent.findViewById(R.id.score_input_bela_team_b);

		/*	Events	*/
		// Bela checkbox
		View.OnClickListener belaToggleEventListener = new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (view == m_checkboxBelaTeamA)
				{
					if (m_checkboxBelaTeamA.isChecked())
					{
						m_checkboxBelaTeamB.setChecked(false);
					}
				}

				if (view == m_checkboxBelaTeamB)
				{
					if (m_checkboxBelaTeamB.isChecked())
					{
						m_checkboxBelaTeamA.setChecked(false);
					}
				}

			}
		};

		m_editTextScoreTeamA.addTextChangedListener(new ScoreTextWatcher(m_editTextScoreTeamA, m_editTextScoreTeamB));

		m_checkboxBelaTeamA.setOnClickListener(belaToggleEventListener);
		m_checkboxBelaTeamB.setOnClickListener(belaToggleEventListener);

		m_alertDialogBuilder.setPositiveButton(R.string.score_input_button_ok, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				// TODO handle this
				if (m_dialogFinishedListner != null)
				{
					m_dialogFinishedListner.OnScoreInputDialogFinished(10,10,10,10);
				}
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

	private void OnScoreValueChanged()
	{

	}

}
