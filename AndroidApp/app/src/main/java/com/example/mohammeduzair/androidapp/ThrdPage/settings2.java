package com.example.mohammeduzair.androidapp.ThrdPage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.mohammeduzair.androidapp.Home.Homepage;
import com.example.mohammeduzair.androidapp.Home.heartrate;
import com.example.mohammeduzair.androidapp.R;
import com.example.mohammeduzair.androidapp.utility.savepref;

public class settings2 extends AppCompatActivity {
    private Switch tSwitch;
    savepref savePref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savePref = new savepref(this);
        if(savePref.loadDarkTheme()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tSwitch =(Switch)findViewById(R.id.tSwitch);
        if (savePref.loadDarkTheme()==true) {
            tSwitch.setChecked(true);
        }
        tSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    savePref.setDarkTheme(true);
                    restartApp();
                }
                else {
                    savePref.setDarkTheme(false);
                    restartApp();
                }
            }
        });
    }
    public void restartApp () {
        Intent i = new Intent(getApplicationContext(), settings2.class);
        startActivity(i);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(settings2.this, menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
