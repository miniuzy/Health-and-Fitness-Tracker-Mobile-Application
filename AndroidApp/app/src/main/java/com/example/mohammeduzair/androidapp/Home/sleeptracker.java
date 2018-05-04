package com.example.mohammeduzair.androidapp.Home;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sleeptracker extends AppCompatActivity {
    private Button startButton, stopButton, resetButton, saveButton;
    private Chronometer chronometer;
    private long lastPause;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ImageView neutralImage, happyImage, unhappyImage, veryUnhappyImage, veryHappyImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeptracker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveButton = (Button) findViewById(R.id.savebutton);
        startButton = (Button) findViewById(R.id.startBtn);
        stopButton = (Button) findViewById(R.id.stopBtn);
        resetButton = (Button) findViewById(R.id.resetBtn);
        chronometer = (Chronometer) findViewById(R.id.appchronometer);
        neutralImage = (ImageView) findViewById(R.id.neutralfeeling);
        happyImage = (ImageView) findViewById(R.id.happy);
        veryHappyImage = (ImageView) findViewById(R.id.veryhappy);
        unhappyImage = (ImageView) findViewById(R.id.unhappy);
        veryUnhappyImage = (ImageView) findViewById(R.id.veryunhappy);


        Chronometer c  = (Chronometer) findViewById(R.id.appchronometer);
        c.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cTick) {
                long time = SystemClock.elapsedRealtime() - cTick.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cTick.setText(hh+":"+mm+":"+ss);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        mAuth = FirebaseAuth.getInstance();
        neutralImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification();
                String feeling = "Neutral";
                FirebaseUser user = mAuth.getCurrentUser();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date).toString();
                databaseReference.child(user.getUid()).child("Feeling").setValue(feeling);
                databaseReference.child(user.getUid()).child("Date").setValue(strDate);
                databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());
            }
        });
        happyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification();
                String feeling = "Happy";
                FirebaseUser user = mAuth.getCurrentUser();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date).toString();
                databaseReference.child(user.getUid()).child("Feeling").setValue(feeling);
                databaseReference.child(user.getUid()).child("Date").setValue(strDate);
                databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());
            }
        });
        veryHappyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification();
                String feeling = "Very Happy";
                FirebaseUser user = mAuth.getCurrentUser();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date).toString();
                databaseReference.child(user.getUid()).child("Feeling").setValue(feeling);
                databaseReference.child(user.getUid()).child("Date").setValue(strDate);
                databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());
            }
        });
        unhappyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification();
                String feeling = "Unhappy";
                FirebaseUser user = mAuth.getCurrentUser();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date).toString();
                databaseReference.child(user.getUid()).child("Feeling").setValue(feeling);
                databaseReference.child(user.getUid()).child("Date").setValue(strDate);
                databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());
            }
        });
        veryUnhappyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification();
                String feeling = "Very Unhappy";
                FirebaseUser user = mAuth.getCurrentUser();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date).toString();
                databaseReference.child(user.getUid()).child("Feeling").setValue(feeling);
                databaseReference.child(user.getUid()).child("Date").setValue(strDate);
                databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTimer();
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                lastPause = 0;
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPause = SystemClock.elapsedRealtime();
                chronometer.stop();
                stopButton.setEnabled(false);
                startButton.setEnabled(true);

            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastPause != 0){
                    chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                }
                else{
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }

                chronometer.start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });

    }
    private void notification(){
        Toast.makeText(this, "Storing your Feelings...", Toast.LENGTH_SHORT).show();
    }

    private void saveTimer() {
        String time = chronometer.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();

        databaseReference.child(user.getUid()).child("Time_Slept").setValue(time);
        databaseReference.child(user.getUid()).child("Date").setValue(strDate);
        databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(sleeptracker.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
