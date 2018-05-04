package com.example.mohammeduzair.androidapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class savepref {

    SharedPreferences sharedPreferences;
    public savepref (Context context) {
        sharedPreferences = context.getSharedPreferences("filename",context.MODE_PRIVATE);
    }
    //Method to save dark theme with true and false

    public void setDarkTheme (Boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("DarkTheme", state);
        editor.commit();
    }

    //Method that will load the app in dark theme

    public boolean loadDarkTheme () {
        Boolean state = sharedPreferences.getBoolean("DarkTheme", false);
        return state;
    }
}
