package com.merodyadt.belotasistent.data;

public class RoundData
{
    private int RoundIndex;
    private int ScoreTeamA;
    private int ScoreTeamB;

    public RoundData(int roundIndex, int scoreTeamA, int scoreTeamB)
    {
        RoundIndex = roundIndex;
        ScoreTeamA = scoreTeamA;
        ScoreTeamB = scoreTeamB;
    }

    public int GetRoundIndex() {
        return RoundIndex;
    }

    public int GetScoreTeamA() {
        return ScoreTeamA;
    }

    public int GetScoreTeamB() {
        return ScoreTeamB;
    }
}
