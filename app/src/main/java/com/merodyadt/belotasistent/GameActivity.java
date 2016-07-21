package com.merodyadt.belotasistent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ArrayAdapter<GameRound> adapter = null; // Array adapter that updates the listview scoreboard according to gameRounds arraylist
    private ArrayList<GameRound> gameRounds;        // Game scores are stored as GameRound objects (they only contain two integer scores from each round)
    private  int WinScore;                          // Constant defining how much score is needed to win
    private boolean scoreBoardHasAtLeastOneEntry;   // Boolean defining if at least one entry is present in the scoreboard, this is only used to prevent user from accidentally exiting activity

    private ListView listViewScoreboard;            // Gui listview
    private TextView textViewScoreTeamA;
    private TextView textViewScoreTeamB;
    private TextView textViewNameTeamA;
    private TextView textViewNameTeamB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get score that's needed to win and set it up
        Intent i = getIntent();
        WinScore = i.getIntExtra("WinScore", 0);

        // Set up toolbar
        SetupToolbar();

        // Initialize scoreboard, scoreboard variables, custom adapter  and set it up
        SetupListView();

        // Set up textviews on the bottom
        SetupTextViews();

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
    }

    @Override
    public void onBackPressed(){
        // Check if there is at least one entry on the scoreboard the user can't accidentally press back button
        if(!scoreBoardHasAtLeastOneEntry){
            finish();   // There is nothing on the scoreboard, exit the activity as usual
            return;
        }

        // At least one entry is in the scoreboard, promt the user to confirm
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Izlaz");
        builder.setMessage("U aplikaciju su unešene odigrane runde, izlaskom će se izgubiti sve što ste unijeli.\nSigurno želite izaći?");

        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void SetupToolbar(){
        Toolbar t = (Toolbar)findViewById(R.id.toolBarGame);
        t.setTitle("Belot Asistent");
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);
    }

    private void SetupListView(){
        listViewScoreboard = (ListView) findViewById(R.id.ListViewScoreBoard);
        gameRounds = new ArrayList<GameRound>();

        adapter = new ScoreboardAdapter(this, gameRounds);
        listViewScoreboard.setAdapter(adapter);

        // Listener for Long click on any of the game matches
        // The menu should open and offer user to delete a row (in case of a mistake)
        listViewScoreboard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int rowIndex = position;
                Log.d("Listview", "Long press on index " + Integer.toString(rowIndex));
                // TODO
                // this isn't opening any dialogs, it should open a dialog and offer the user to delete an entry
                return false;
            }
        });

        // Recalculate total score and update listview

    }

    private void SetupTextViews(){
        textViewScoreTeamA = (TextView)findViewById(R.id.TextViewScoreTeamA);
        textViewScoreTeamB = (TextView)findViewById(R.id.TextViewScoreTeamB);
        textViewNameTeamA = (TextView)findViewById(R.id.TextViewNameTeamA);
        textViewNameTeamB = (TextView)findViewById(R.id.TextViewNameTeamB);


        textViewNameTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenChangeTeamNameDialog(v);
            }
        });

        textViewNameTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenChangeTeamNameDialog(v);
            }
        });


    }


    public void OpenChangeTeamNameDialog(View view){
        final TextView clickedView = (TextView)view;
        Log.d("Old Name", clickedView.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Izmjena naziva ekipe");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});      // Set maximum number of characters to 10
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickedView.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void OpenInputDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(GameActivity.this);
        View promtView = layoutInflater.inflate(R.layout.dialog_score_input, null);


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
        final EditText scoreAdditionalTeamB = (EditText)promtView.findViewById(R.id.EditTextAdditionalScoreTeamB);


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


                        // Additional score must be manually checked since the user can press OK without typing anything in
                        if(scoreAdditionalTeamA.getText().toString().equals("")){
                            additionalScoreTeamA = 0;
                        }else{
                            additionalScoreTeamA = Integer.parseInt(scoreAdditionalTeamA.getText().toString());
                        }

                        if(scoreAdditionalTeamB.getText().toString().equals("")){
                            additionalScoreTeamB = 0;
                        }else{
                            additionalScoreTeamB = Integer.parseInt(scoreAdditionalTeamB.getText().toString());
                        }

                        if(checkTeamA.isChecked()){
                            belaTeamA = true;
                            belaTeamB = false;
                        }

                        if(checkTeamB.isChecked()){
                            belaTeamA = false;
                            belaTeamB = true;
                        }

                        Log.d("Additional A", Integer.toString(additionalScoreTeamA));
                        Log.d("Additional B", Integer.toString(additionalScoreTeamB));

                        CalculateFinalScores(callingTeam, baseScoreTeamA, baseScoreTeamB, additionalScoreTeamA, additionalScoreTeamB, belaTeamA, belaTeamB);
                        CalculateAndUpdateGui();

                    }
                })
                .setNegativeButton("Prekid", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, user canceled
                    }
                });

        // Dialog window
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // Hide the CK button, listeners will display it again when it's ready to be displayed
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        dialog.show();



        // Radio button listeners used to prevent user from checking both radio buttons
        // (Radiolayout is NOT used so this is needed)

        radioTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioTeamB.setChecked(false);

                // Unlock the confirm button if the conditions are met
                if(CanUnlockConfirmButton(radioTeamA.isChecked(), radioTeamB.isChecked(), scoreTeamA.getText().toString(), scoreTeamB.getText().toString())){
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });


        radioTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioTeamA.setChecked(false);

                // Unlock the confirm button if the conditions are met
                if(CanUnlockConfirmButton(radioTeamA.isChecked(), radioTeamB.isChecked(), scoreTeamA.getText().toString(), scoreTeamB.getText().toString())){
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
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


        // Textchanged listeners for EditTexts
        scoreTeamA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if the user is focused in the edittext (if they're not, it means the edittext should not be touched)
                // This prevents the edittext from interacting back when the user types something in the other edittext
                if(!scoreTeamA.isFocused()){
                    // It's not focused, don't do anything
                    return;
                }

                // Check if text is empty string, if it is, empty the other team's score edittext, disable confirm button if it was enabled and return
                if(s.toString().equals("")){
                    scoreTeamB.setText("");
                    Log.d("OnTextChanged", "String is empty");
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    return;
                }

                // Check if the user didn't write something bigger than 162
                int userInput = Integer.parseInt(s.toString());
                if(userInput > 162){
                    scoreTeamA.setText(Integer.toString(162));
                    scoreTeamB.setText(Integer.toString(0));
                    return;
                }


                // Calculate the other team's points and set text in edittext
                final int standardGamePoints = 162;
                int pointsInEditText = Integer.parseInt(s.toString());
                int otherTeamPoints = standardGamePoints - pointsInEditText;
                scoreTeamB.setText(Integer.toString(otherTeamPoints));

                // Unlock the confirm button if the conditions are met
                if(CanUnlockConfirmButton(radioTeamA.isChecked(), radioTeamB.isChecked(), scoreTeamA.getText().toString(), scoreTeamB.getText().toString())){
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        scoreTeamB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if the user is focused in the edittext (if they're not, it means the edittext should not be touched)
                // This prevents the edittext from interacting back when the user types something in the other edittext
                if(!scoreTeamB.isFocused()){
                    // It's not focused, don't do anything
                    return;
                }

                // Check if text is empty string, if it is, empty the other team's score edittext, disable confirm button if it was enabled and return
                if(s.toString().equals("")){
                    scoreTeamA.setText("");
                    Log.d("OnTextChanged", "String is empty");
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    return;
                }

                // Check if the user didn't write something bigger than 162
                int userInput = Integer.parseInt(s.toString());
                if(userInput > 162){
                    scoreTeamB.setText(Integer.toString(162));
                    scoreTeamA.setText(Integer.toString(0));
                    return;
                }


                // Calculate the other team's points and set text in edittext
                final int standardGamePoints = 162;
                int pointsInEditText = Integer.parseInt(s.toString());
                int otherTeamPoints = standardGamePoints - pointsInEditText;
                scoreTeamA.setText(Integer.toString(otherTeamPoints));

                // Unlock the confirm button if the conditions are met
                if(CanUnlockConfirmButton(radioTeamA.isChecked(), radioTeamB.isChecked(), scoreTeamA.getText().toString(), scoreTeamB.getText().toString())){
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void OpenEndGameDialog(final String winnerName, final int winnerScore, final int loserScore){
        LayoutInflater layoutInflater = LayoutInflater.from(GameActivity.this);
        View promtView = layoutInflater.inflate(R.layout.dialog_end_game, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setView(promtView);
        builder.setTitle("Runda je završila");
        builder.setIcon(R.drawable.trophy_gold);
        final AlertDialog dialog;


        // All gui objects declarations and string which displays winner name and score
        final TextView textView = (TextView) promtView.findViewById(R.id.TextViewEndGameDialogTeamName);
        String endGameDialogText = "Ekipa " + winnerName + " pobijedila je sa rezultatom " + Integer.toString(winnerScore) + ":" + Integer.toString(loserScore);
        textView.setText(endGameDialogText);

        // Display the dialog with just OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss dialog and restart the game
                dialog.dismiss();
                gameRounds.clear();
                adapter.notifyDataSetChanged();
                CalculateAndUpdateGui();
            }
        });

        dialog = builder.create();
        dialog.show();

    }


    public boolean CanUnlockConfirmButton(boolean radioTeamA, boolean radioTeamB, String scoreTeamA, String scoreTeamB){
        if(radioTeamA == false && radioTeamB == false){
            return false;
        }

        if(scoreTeamA.equals("") || scoreTeamB.equals("")){
            return false;
        }

        return true;
    }

    public void CalculateFinalScores(boolean callingTeam, int scoreTeamA, int scoreTeamB, int zvanjeTeamA, int zvanjeTeamB, boolean belaTeamA, boolean belaTeamB){
        // calling team: false is teamA, true is teamB

        Log.d("Jake calling team", String.valueOf(callingTeam));
        Log.d("Jake base a", Integer.toString(scoreTeamA));
        Log.d("Jake base b", Integer.toString(scoreTeamB));
        Log.d("Jake add a", Integer.toString(zvanjeTeamA));
        Log.d("Jake add b", Integer.toString(zvanjeTeamB));
        Log.d("Jake bela a", String.valueOf(belaTeamA));
        Log.d("Jake bela b", String.valueOf(belaTeamB));

        int ukupno = 162;
        ukupno = ukupno + zvanjeTeamA + zvanjeTeamB;
        if(belaTeamB)
        {
            scoreTeamB = scoreTeamB + 20;
            ukupno = ukupno + 20;
        }
        if (belaTeamA)
        {
            scoreTeamA = scoreTeamA + 20;
            ukupno = ukupno + 20;
        }
        scoreTeamB = scoreTeamB + zvanjeTeamB;
        scoreTeamA = scoreTeamA + zvanjeTeamA;

        if (callingTeam) //true -> Team B je zval
        {
            if (ukupno/2 >= scoreTeamB)
            {
                Log.d("Jay's Func", "Team B pada");
                scoreTeamB = 0;
                scoreTeamA = ukupno;
            }
        }
        else // zval je Team A
        {
            if(ukupno/2 >= scoreTeamA)
            {
                Log.d("Jay's Func", "Team A pada");
                scoreTeamA = 0;
                scoreTeamB = ukupno;
            }
        }

        Log.d("Final Team A score", Integer.toString(scoreTeamA));
        Log.d("Final Team B score", Integer.toString(scoreTeamB));

        GameRound g = new GameRound(scoreTeamA, scoreTeamB);
        gameRounds.add(g);
        adapter.notifyDataSetChanged();

    }

    private void CalculateAndUpdateGui(){
        int totalScoreA = 0;
        int totalScoreB = 0;
        int totalRounds = gameRounds.size();

        // Check if there's 0 rounds played (can happen if user deletes all entries
        if(totalRounds == 0){
            // Set text to total score in bottom textviews to 0
            textViewScoreTeamA.setText("0");
            textViewScoreTeamB.setText("0");
            scoreBoardHasAtLeastOneEntry = false;   // no entries means no dialog on back button press
            return;
        }

        for(int i=0 ; i<totalRounds ; i++){
            // Calculate total team A score
            totalScoreA += gameRounds.get(i).GetScoreA();

            // Calculate total team B score
            totalScoreB += gameRounds.get(i).GetScoreB();
        }

        // Set text to total score in bottom textviews
        textViewScoreTeamA.setText(Integer.toString(totalScoreA));
        textViewScoreTeamB.setText(Integer.toString(totalScoreB));

        scoreBoardHasAtLeastOneEntry = true;       // prevent user from accidentally pressing back if there is something on the scoreboard



        // Check if any of the teams won
        if(totalScoreA > WinScore){
            // Team A won
            String winTeamName = textViewNameTeamA.getText().toString();
            OpenEndGameDialog( winTeamName, totalScoreA, totalScoreB);
        }else if(totalScoreB > WinScore){
            // Team B won
            String winTeamName = textViewNameTeamB.getText().toString();
            OpenEndGameDialog( winTeamName, totalScoreB, totalScoreA);
        }else{
            // Nobody won, do nothing
        }

    }

}
