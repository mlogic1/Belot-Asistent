package com.merodyadt.belotasistent;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar t = (Toolbar)findViewById(R.id.toolBarMenu);
        t.setTitle("Belot Asistent");
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);
    }


    public void ShowAboutMessage(View view){
        Toast.makeText(getApplicationContext(), "Izradio: Filip Radic",
                Toast.LENGTH_LONG).show();
    }

    public void ExitApplication(View view){
        finish();
        System.exit(0);
    }


    public void Start501Game(View view){
        // Start a new game
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("WinScore", 501);
        this.startActivity(i);
    }

    public void Start1001Game(View view){
        // Start a new game
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("WinScore", 1001);
        this.startActivity(i);
    }

    public void StartTournamentGame(View view){
        Toast.makeText(getApplicationContext(), "Turnirski mod je trenutno u izradi",
                Toast.LENGTH_LONG).show();
    }
}
