package com.example.mohammeduzair.androidapp.Home;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.R;
import com.example.mohammeduzair.androidapp.utility.BotNavHelp;
import com.example.mohammeduzair.androidapp.utility.savepref;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class Homepage extends AppCompatActivity implements View.OnClickListener{
    private CardView heartRate,stepCounter, calorieCounter, locationTracker, sleepTracker, setTimer, mainMenu;
    private static final int NUM = 0;
    savepref savePref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savePref = new savepref(this);
        if(savePref.loadDarkTheme()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // Defining what you want your cards to be
        heartRate =(CardView) findViewById(R.id.heart_rate);
        stepCounter =(CardView) findViewById(R.id.step_counter);
        calorieCounter =(CardView) findViewById(R.id.calorie_counter);
        locationTracker =(CardView) findViewById(R.id.location_tracker);
        sleepTracker =(CardView) findViewById(R.id.sleep_tracker);
        setTimer=(CardView) findViewById(R.id.set_time);

        setupBottomNavView();
        // Setting up onclick listeners to the cards
        heartRate.setOnClickListener(this);
        stepCounter.setOnClickListener(this);
        calorieCounter.setOnClickListener(this);
        locationTracker.setOnClickListener(this);
        sleepTracker.setOnClickListener(this);
        setTimer.setOnClickListener(this);


}
    private void setupBottomNavView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavBar);
        BotNavHelp.setupBottomNavView(bottomNavigationViewEx);
        BotNavHelp.enableNav(Homepage.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(NUM);
        menuItem.setChecked(true);
    }
    //
    @Override
    public void onClick(View view) {
        Intent i;
        //switch statements to click into the cards created above
        switch (view.getId()) {
            case R.id.heart_rate:
                i = new Intent(Homepage.this, heartrate.class);
                startActivity(i);
                break;
            case R.id.step_counter:
                i = new Intent (Homepage.this,stepcounter.class);
                startActivity(i);
                break;
            case R.id.calorie_counter:
                i = new Intent (Homepage.this, caloriecounter.class);
                startActivity(i);
                break;
            case R.id.location_tracker:
                i = new Intent (Homepage.this, locationtracker.class);
                startActivity(i);
                break;
            case R.id.sleep_tracker:
                i = new Intent(Homepage.this, sleeptracker.class);
                startActivity(i);
                break;
            case R.id.set_time:
                i = new Intent (Homepage.this, settimer.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.challengemenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.challenges:
                i = new Intent (Homepage.this, WeeklyChallenge.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
