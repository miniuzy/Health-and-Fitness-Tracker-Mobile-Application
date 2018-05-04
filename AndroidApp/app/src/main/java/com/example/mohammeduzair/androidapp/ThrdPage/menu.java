package com.example.mohammeduzair.androidapp.ThrdPage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mohammeduzair.androidapp.Home.Homepage;
import com.example.mohammeduzair.androidapp.Home.caloriecounter;
import com.example.mohammeduzair.androidapp.Home.heartrate;
import com.example.mohammeduzair.androidapp.Home.stepcounter;
import com.example.mohammeduzair.androidapp.R;

import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mohammeduzair.androidapp.utility.BotNavHelp;
import com.example.mohammeduzair.androidapp.utility.savepref;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class menu extends AppCompatActivity implements View.OnClickListener {
    private static final int NUM = 2;
    private CardView settingOne, settingTwo, settingThree;
    savepref savePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savePref = new savepref(this);
        if(savePref.loadDarkTheme()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setupBottomNavView();
        settingOne = (CardView) findViewById(R.id.setting1);
        settingTwo = (CardView) findViewById(R.id.setting2);
        settingThree = (CardView) findViewById(R.id.setting3);
        settingOne.setOnClickListener(this);
        settingTwo.setOnClickListener(this);
        settingThree.setOnClickListener(this);
    }

    private void setupBottomNavView() {
        //navigating the bottom menu
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavBar);
        BotNavHelp.setupBottomNavView(bottomNavigationViewEx);
        BotNavHelp.enableNav(menu.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(NUM);
        menuItem.setChecked(true);
    }

    public void onClick(View view) {
        Intent i;
        //switch statements to click into the cards created above
        switch (view.getId()) {
            case R.id.setting1:
                i = new Intent(menu.this, settings1.class);
                startActivity(i);
                break;
            case R.id.setting2:
                i = new Intent(menu.this, settings2.class);
                startActivity(i);
                break;
            case R.id.setting3:
                i = new Intent(menu.this, settings3.class);
                startActivity(i);
                break;
                default:
                    break;
        }
    }
}