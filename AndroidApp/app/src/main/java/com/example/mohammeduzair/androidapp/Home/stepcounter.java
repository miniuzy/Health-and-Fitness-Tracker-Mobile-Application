package com.example.mohammeduzair.androidapp.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;


import com.example.mohammeduzair.androidapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class stepcounter extends AppCompatActivity implements SensorEventListener {
    private Boolean activityRunning;
    public static float stepstaken;
    private SensorManager sensorManager;
    private TextView textView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Button saveBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView distanceTxtView, calorieTxtView;

    // remove the two save buttons as its confusing and put one at the end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setContentView(R.layout.activity_stepcounter);
        textView = (TextView) findViewById(R.id.textRemaining);
        distanceTxtView = (TextView) findViewById(R.id.distancecovered);
        calorieTxtView = (TextView) findViewById(R.id.caloriesburnt);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        saveBtn = (Button) findViewById(R.id.savebutton);


        sharedPreferences = getSharedPreferences("Steps", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mAuth = FirebaseAuth.getInstance();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });

    }




    private void distancetravel() {
        float distance = (float) (stepstaken * 0.8);
        distanceTxtView.setText(String.valueOf(distance));
        }
    private void caloriesBurned() {
        float calories = stepstaken / 20;
        calorieTxtView.setText(String.valueOf(calories));
    }

    private void saveUserInfo() {
        String steps = textView.getText().toString().trim();
        String distance = distanceTxtView.getText().toString().trim();
        String caloriesburned = calorieTxtView.getText().toString().trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child(user.getUid()).child("Date").setValue(strDate);
        databaseReference.child(user.getUid()).child("Steps").setValue(steps);
        databaseReference.child(user.getUid()).child("Calories Burned").setValue(caloriesburned);
        databaseReference.child(user.getUid()).child("Distance").setValue(distance);
        databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());

        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(stepcounter.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            textView.setText(String.valueOf(stepstaken));
            distancetravel();
            caloriesBurned();
        }
        else {
            event.values[0]=0;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

