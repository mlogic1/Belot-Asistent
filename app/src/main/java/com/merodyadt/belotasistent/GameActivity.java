package com.merodyadt.belotasistent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class GameActivity extends AppCompatActivity {

    private ArrayAdapter<GameRound> adapter = null; // Array adapter that updates the listview scoreboard according to gameRounds arraylist
    private ArrayList<GameRound> gameRounds;        // Game scores are stored as GameRound objects (they only contain two integer scores from each round)
    private  int WinScore;                           // Constant defining how much score is needed to win

    private ListView listViewScoreboard;            // Gui listview



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get score that's needed to win and set it up
        Intent i = getIntent();
        WinScore = i.getIntExtra("WinScore", 0);

        // Set up toolbar
        Toolbar t = (Toolbar)findViewById(R.id.toolBarGame);
        t.setTitle("Belot Asistent");
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);

        // Initialize scoreboard and scoreboard variables
        listViewScoreboard = (ListView) findViewById(R.id.ListViewScoreBoard);
        gameRounds = new ArrayList<GameRound>();

        // Initialize custom adapter for the scoreboard and set it up
        adapter = new ScoreboardAdapter(this, gameRounds);
        listViewScoreboard.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_add_score:
                Toast.makeText(getApplicationContext(), "Add score dialog!",
                        Toast.LENGTH_LONG).show();
                OpenInputDialog();


            default:
                return super.onOptionsItemSelected(item);
        }
        //return true;
    }


    private void OpenInputDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(GameActivity.this);

        View promtView = layoutInflater.inflate(R.layout.score_input_dialog, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setView(promtView);
        builder.setTitle("Unos bodova");


        // All gui objects declarations
        final RadioButton radioTeamA = (RadioButton)promtView.findViewById(R.id.radioButtonTeamA);
        final RadioButton radioTeamB = (RadioButton)promtView.findViewById(R.id.radioButtonTeamB);

        final CheckBox checkTeamA = (CheckBox)promtView.findViewById(R.id.checkBoxBelaTeamA);
        final CheckBox checkTeamB = (CheckBox)promtView.findViewById(R.id.checkBoxBelaTeamB);

        final EditText scoreTeamA = (EditText)promtView.findViewById(R.id.EditTextScoreTeamA);
        final EditText scoreTeamB = (EditText)promtView.findViewById(R.id.EditTextScoreTeamB);
        final EditText scoreAdditionalTeamA = (EditText)promtView.findViewById(R.id.EditTextAdditionalScoreTeamA);
        final EditText scoreAdditionalTeamB = (EditText)promtView.findViewById(R.id.EditTextAdditionalScoreTeamA);



        // Radio button listeners used to prevent user from checking both radio buttons
        // (Radiolayout is NOT used so this is needed)

        radioTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioTeamB.setChecked(false);
            }
        });


        radioTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioTeamA.setChecked(false);
            }
        });



        // Checkboxes also can't be both enabled at the same time. 1 is max
        checkTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user clicks checkbox A
                // If both checkboxes are enabled disable checkbox B
                if(checkTeamA.isChecked() && checkTeamB.isChecked()){
                    checkTeamB.setChecked(false);
                }
            }
        });


        checkTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user clicks checkbox B
                // If both checkboxes are enabled disable checkbox B
                if(checkTeamA.isChecked() && checkTeamB.isChecked()){
                    checkTeamA.setChecked(false);
                }
            }
        });


        // TODO
        // Finish click listeners for EditTexts so score automatically calculates

        // Click listeners for EditTexts
        scoreTeamA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if text is empty string, if it is, do nothing
                if(count == 0){
                    Log.d("OnTextChanged", s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("After", s.toString());
            }
        });



        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Confirm button

                        int x = Integer.parseInt(scoreTeamA.getText().toString());


                        // Variables needed to calculate score
                        boolean callingTeam;   // false is teamA , true is TeamB
                        int baseScoreTeamA;
                        int baseScoreTeamB;
                        int additionalScoreTeamA;
                        int additionalScoreTeamB;
                        boolean belaTeamA = false;
                        boolean belaTeamB = false;


                        if(radioTeamA.isChecked()){
                            callingTeam = false;
                        }else{
                            callingTeam = true;
                        }

                        baseScoreTeamA = Integer.parseInt(scoreTeamA.getText().toString());
                        baseScoreTeamB = Integer.parseInt(scoreTeamB.getText().toString());
                        additionalScoreTeamA = Integer.parseInt(scoreAdditionalTeamA.getText().toString());
                        additionalScoreTeamB = Integer.parseInt(scoreAdditionalTeamB.getText().toString());

                        if(checkTeamA.isChecked()){
                            belaTeamA = true;
                            belaTeamB = false;
                        }

                        if(checkTeamB.isChecked()){
                            belaTeamA = false;
                            belaTeamB = true;
                        }


                        Log.d("Zvanje", String.valueOf(callingTeam));
                        Log.d("Base score A", Integer.toString(baseScoreTeamA));
                        Log.d("Base score B", Integer.toString(baseScoreTeamB));
                        Log.d("dodatni score B", Integer.toString(additionalScoreTeamA));
                        Log.d("dodatni score B", Integer.toString(additionalScoreTeamB));

                        Log.d("Bela A", String.valueOf(belaTeamA));
                        Log.d("Bela B", String.valueOf(belaTeamB));


                        // TODO
                        // Call Dejk's calculations function


                        /* This goes to Dejk's calculation function
                        GameRound g = new GameRound(50,130);
                        gameRounds.add(g);
                        */

                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Prekid", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, user canceled
                    }
                });


        // Dialog window
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // Hide the CK button, action listeners will display it again when it's ready to be displayed
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        dialog.show();

    }



    public void CheckForErrorsInDialog(){

    }

}
