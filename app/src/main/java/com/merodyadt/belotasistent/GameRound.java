package com.merodyadt.belotasistent;

/**
 * Created by Filip on 17.7.2016..
 */
public class GameRound {
    private int ScoreA; // Score TeamA
    private int ScoreB; // Score TeamB


    public GameRound(int TeamScoreA, int TeamScoreB){
        ScoreA = TeamScoreA;
        ScoreB = TeamScoreB;
    }


    public int GetScoreA(){
        return ScoreA;
    }

    public int GetScoreB(){
        return ScoreB;
    }

}
