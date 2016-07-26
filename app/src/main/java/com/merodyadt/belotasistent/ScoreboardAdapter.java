package com.merodyadt.belotasistent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Filip on 17.7.2016..
 */
public class ScoreboardAdapter extends ArrayAdapter<GameRound> {

    TextView textViewScoreTeamA;
    TextView textViewScoreTeamB;

    //private static LayoutInflater inflater=null;

    public ScoreboardAdapter(Context context, ArrayList<GameRound> gameRounds){
        super(context, 0, gameRounds);
        //inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        GameRound g = getItem(position);

        if(convertView == null){
            vi = LayoutInflater.from(getContext()).inflate(R.layout.list_row_score_round, parent, false);
        }

        int ScoreA = g.GetScoreA();
        int ScoreB = g.GetScoreB();


        TextView textViewTeamA = (TextView)vi.findViewById(R.id.TextViewRoundScoreTeamA);
        TextView textViewTeamB = (TextView)vi.findViewById(R.id.TextViewRoundScoreTeamB);

        textViewTeamA.setText(Integer.toString(ScoreA));
        textViewTeamB.setText(Integer.toString(ScoreB));

        return vi;
    }
}
