package com.merodyadt.belotasistent;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// TODO: research if its possible to move this whole logic to cpp

public class MatchParser
{
    private final String CURRENT_MATCH_FILE_NAME = "CurrentMatch.json";
    private Context m_context = null;

    public MatchParser(Context context)
    {
        m_context = context;
    }

    public JSONObject LoadCurrentMatch()
    {
        // TODO: CHECK FILE SPACE
        String path = m_context.getFilesDir().getPath();

        String fileContent = "";
        FileInputStream fis = null;
        try
        {
            fis = m_context.openFileInput(CURRENT_MATCH_FILE_NAME);
        }
        catch (FileNotFoundException e)
        {
            Log.i("BeloteAsistent", "Current match file does not exist");
            return null;
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader))
        {
            String line = reader.readLine();
            while (line != null)
            {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        }
        catch (IOException e)
        {
            Log.i("BeloteAsistent", "Error occurred when opening raw file for reading. Opening empty match");
            return null;
        } finally {
            fileContent = stringBuilder.toString();
        }

        JSONObject currentMatchObj = null;
        try {
            currentMatchObj = new JSONObject(fileContent);
        }
        catch (JSONException e)
        {
            Log.e("BeloteAsistent", "Corrupted current match data, opening empty match");
            e.printStackTrace();
            return null;
        }

        return currentMatchObj;
    }

    public JSONObject LoadMatch(final String matchTimeStamp)
    {
        // TODO: implement this, for statistical purposes
        return null;
    }
}
