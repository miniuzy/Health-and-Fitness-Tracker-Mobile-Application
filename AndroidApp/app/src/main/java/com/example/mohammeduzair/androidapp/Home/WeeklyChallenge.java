package com.example.mohammeduzair.androidapp.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.R;
import com.example.mohammeduzair.androidapp.utility.Userinformation;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class WeeklyChallenge extends AppCompatActivity implements SensorEventListener {
    private PieChart pg;
    private Boolean activityRunning;
    private PieModel weeklyGoal, dailySteps;
    private Button updateBtn;
    private int DEFAULT_GOAL = 600;
    public static float stepstaken;
    private SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_challenge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        updateBtn = (Button) findViewById(R.id.update);


        pg = (PieChart) findViewById(R.id.graph);
        dailySteps = new PieModel(" ", 0 , Color.parseColor("#99CC00"));
        pg.addPieSlice(dailySteps);

        weeklyGoal = new PieModel(" ",DEFAULT_GOAL,Color.parseColor("#CC0000"));
        pg.addPieSlice(weeklyGoal);
        pg.setInnerPadding(75);

        pg.setDrawValueInPie(false);
        pg.startAnimation();
        updatePie();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePie();
            }
        });

    }
   /* private void updateUserInfo(){

    }*/

    private void updatePie(){
        //total steps daily.
        int steps_today = (int) stepstaken;

        dailySteps.setValue(steps_today);
        //goal steps

        if(DEFAULT_GOAL - steps_today > 0){

            weeklyGoal.setValue(DEFAULT_GOAL - steps_today);

        }
        else{
            //goal reached
            pg.clearChart();
            pg.addPieSlice(dailySteps);
            Toast.makeText(this, "Congratulations, Challenge completed!", Toast.LENGTH_LONG).show();


        }
        pg.update();



    }
    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Step sensor is not available", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            stepstaken = event.values[0];
        }
        else {
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(WeeklyChallenge.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
