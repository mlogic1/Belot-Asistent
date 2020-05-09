package com.merodyadt.belotasistent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ArrayAdapter<GameRound> adapter = null; // Array adapter that updates the listview scoreboard according to gameRounds arraylist
    private ArrayList<GameRound> gameRounds;        // Game scores are stored as GameRound objects (they only contain two integer scores from each round)
    private boolean scoreBoardHasAtLeastOneEntry;   // Boolean defining if at least one entry is present in the scoreboard, this is only used to prevent user from accidentally exiting activity

    private ListView listViewScoreboard;            // Gui listview
    private TextView textViewScoreTeamA;
    private TextView textViewScoreTeamB;
    private TextView textViewNameTeamA;
    private TextView textViewNameTeamB;



    // Application settings
    private SharedPreferences preferences;
    private int WinScore;                          // Constant defining how much score is needed to win
    private boolean countRounds;
    private String TeamNameA;
    private String TeamNameB;
    private String appThemeColor;

    private Toolbar toolBar;
    private String RoundsCount = "";                     // String that's added to the toolbar title, if the user wants rounds to be counted, this will be to right of app name text in toolbar
    private int roundsWonTeamA = 0;
    private int roundsWonTeamB = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Load saved settings
        LoadSettings();

        // Set up toolbar
        SetupToolbar();
        SetupActivityBackground();

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
        builder.setTitle(R.string.ActivityGameExitDialogTitle);
        builder.setMessage(R.string.ActivityGameExitDialogText);

        builder.setPositiveButton(R.string.ActivityGameYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton(R.string.ActivityGameNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void LoadSettings(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        WinScore = Integer.parseInt(preferences.getString("roundWinScore", ""));
        countRounds = preferences.getBoolean("countRounds", true);
        TeamNameA = preferences.getString("defaultTeamNameA", "");
        TeamNameB = preferences.getString("defaultTeamNameB", "");
        appThemeColor = preferences.getString("themeColor", "");
    }

    private void SetupToolbar(){
        toolBar = (Toolbar)findViewById(R.id.toolBarGame);
        toolBar.setTitle(R.string.ActivityGameToolBarText);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setBackgroundColor(Color.parseColor(appThemeColor));
        setSupportActionBar(toolBar);



        // User wants round counter to be enabled
        if(countRounds){
            RoundsCount = " (" + roundsWonTeamA + " : " + roundsWonTeamB + ")";
            toolBar.setTitle(getResources().getString(R.string.ActivityGameToolBarText) + RoundsCount);
        }
    }

    private void SetupActivityBackground(){
        RelativeLayout gameLayout = (RelativeLayout)findViewById(R.id.ActivityGameMasterLayout);
        switch (appThemeColor){
            case "#4CAF50":
                assert gameLayout != null;
                gameLayout.setBackgroundColor(Color.parseColor("#E8F5E9"));
                break;

            case "#607D8B":
                assert gameLayout != null;
                gameLayout.setBackgroundColor(Color.parseColor("#ECEFF1"));
                break;

            case "#FF5722":
                assert gameLayout != null;
                gameLayout.setBackgroundColor(Color.parseColor("#FBE9E7"));
                break;

            case "#2196F3":
                assert gameLayout != null;
                gameLayout.setBackgroundColor(Color.parseColor("#E3F2FD"));
                break;

            default:
                assert gameLayout != null;
                gameLayout.setBackgroundColor(Color.parseColor("#E3F2FD"));
                break;
        }
    }

    private void UpdateToolBar(){
        // User wants round counter to be enabled
        if(countRounds){
            RoundsCount = " (" + roundsWonTeamA + " : " + roundsWonTeamB + ")";
            toolBar.setTitle(getResources().getString(R.string.ActivityGameToolBarText) + RoundsCount);
        }
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
                OpenLongHoldDialog(rowIndex);
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

        // Set team names from settings
        textViewNameTeamA.setText(TeamNameA);
        textViewNameTeamB.setText(TeamNameB);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(R.string.ActivityGameTeamNameChangeDialogTitle);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});      // Set maximum number of characters to 10
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(R.string.ActivityGameOk, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickedView.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.ActivityGameCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void OpenLongHoldDialog(final int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.ActivityGameOptions);
        builder.setItems(R.array.scoreboardlongclickoptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                gameRounds.remove(index);
                adapter.notifyDataSetChanged();
                CalculateAndUpdateGui();
            }
        });
        builder.show();
    }


    private void OpenInputDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(GameActivity.this);
        View promtView = layoutInflater.inflate(R.layout.dialog_score_input, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setView(promtView);
        builder.setTitle(R.string.ActivityGameInputDialogTitle);


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
                .setPositiveButton(R.string.ActivityGameOk, new DialogInterface.OnClickListener() {
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
                        CalculateFinalScores(callingTeam, baseScoreTeamA, baseScoreTeamB, additionalScoreTeamA, additionalScoreTeamB, belaTeamA, belaTeamB);
                        CalculateAndUpdateGui();

                    }
                })
                .setNegativeButton(R.string.ActivityGameCancel, new DialogInterface.OnClickListener() {
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
        builder.setTitle(R.string.ActivityGameEndDialogTitle);
        builder.setIcon(R.drawable.trophy_gold);
        final AlertDialog dialog;


        // All gui objects declarations and string which displays winner name and score
        final TextView textView = (TextView) promtView.findViewById(R.id.TextViewEndGameDialogTeamName);
        String endGameDialogText = "Ekipa " + winnerName + " pobijedila je sa rezultatom " + Integer.toString(winnerScore) + ":" + Integer.toString(loserScore);
        textView.setText(endGameDialogText);

        // Display the dialog with just OK button
        builder.setPositiveButton(R.string.ActivityGameOk, new DialogInterface.OnClickListener() {
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

        int ukupno = 162;
        ukupno += zvanjeTeamA + zvanjeTeamB;
        if(belaTeamB)
        {
            scoreTeamB = scoreTeamB + 20;
            ukupno += 20;
        }
        if (belaTeamA)
        {
            scoreTeamA = scoreTeamA + 20;
            ukupno += 20;
        }
        scoreTeamB += zvanjeTeamB;
        scoreTeamA += zvanjeTeamA;

        if (callingTeam) //true -> Team B je zval
        {
            if (ukupno/2 >= scoreTeamB)
            {
                Toast.makeText(getApplicationContext(), R.string.ActivityGameCallingTeamFailed,
                        Toast.LENGTH_LONG).show();
                scoreTeamB = 0;
                scoreTeamA = ukupno;
            }
        }
        else // zval je Team A
        {
            if(ukupno/2 >= scoreTeamA)
            {
                Toast.makeText(getApplicationContext(), R.string.ActivityGameCallingTeamFailed,
                        Toast.LENGTH_LONG).show();
                scoreTeamA = 0;
                scoreTeamB = ukupno;
            }
        }

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
        if(totalScoreA >= WinScore){
            // Team A won
            String winTeamName = textViewNameTeamA.getText().toString();
            OpenEndGameDialog( winTeamName, totalScoreA, totalScoreB);
            roundsWonTeamA++;
            UpdateToolBar();
        }else if(totalScoreB >= WinScore){
            // Team B won
            String winTeamName = textViewNameTeamB.getText().toString();
            OpenEndGameDialog( winTeamName, totalScoreB, totalScoreA);
            roundsWonTeamB++;
            UpdateToolBar();
        }else{
            // Nobody won, do nothing
        }

    }

}
