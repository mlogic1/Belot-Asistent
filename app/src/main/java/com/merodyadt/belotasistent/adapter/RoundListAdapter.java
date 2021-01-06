package com.merodyadt.belotasistent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.merodyadt.belotasistent.R;
import com.merodyadt.belotasistent.data.RoundData;
import java.util.ArrayList;

public class RoundListAdapter extends ArrayAdapter<RoundData>
{
	private Context m_context = null;
	private int m_resource;

	public RoundListAdapter(Context context, int resource, ArrayList<RoundData> objects)
	{
		super(context, resource, objects);
		m_context = context;
		m_resource = resource;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(m_context);
		convertView = layoutInflater.inflate(m_resource, parent, false);
		TextView scoreTeamA =  convertView.findViewById(R.id.scoreTeamA);
		TextView scoreTeamB =  convertView.findViewById(R.id.scoreTeamB);
		scoreTeamA.setText(Integer.toString(getItem(position).GetScoreTeamA()));
		scoreTeamB.setText(Integer.toString(getItem(position).GetScoreTeamB()));

		return convertView;
	}
}
