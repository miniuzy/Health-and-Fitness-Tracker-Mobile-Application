package com.example.mohammeduzair.androidapp.Home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.sensorextension.Ssensor;
import com.samsung.android.sdk.sensorextension.SsensorEvent;
import com.samsung.android.sdk.sensorextension.SsensorEventListener;
import com.samsung.android.sdk.sensorextension.SsensorExtension;
import com.samsung.android.sdk.sensorextension.SsensorManager;

import com.example.mohammeduzair.androidapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class heartrate extends AppCompatActivity {
    Ssensor red = null;
    private Boolean sensorRunning;
    TextView tRED = null;
    SSListener mSSListener = null;
    SsensorManager mSSensorManager = null;
    SsensorExtension mSsensorExtension = null;
    Activity mContext;
    public static float heartLED;

    ToggleButton btn_start = null;
    private Button saveBtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @TargetApi(23)@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);
        btn_start = (ToggleButton) findViewById(R.id.btn_start);
        mContext = this;
        mAuth = FirebaseAuth.getInstance();
        saveBtn = (Button) findViewById(R.id.savebutton);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tRED = (TextView) findViewById(R.id.tRED);
        mSSListener = new SSListener();
        if (btn_start != null) {
            btn_start.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    btn_start.setSelected(!btn_start.isSelected());

                    try {
                        if (!btn_start.isSelected()) {
                            // HRM OFF
                            btn_start.setText(btn_start.getTextOff());
                            if (mSSensorManager != null) {
                                mSSensorManager.unregisterListener(mSSListener,red);
                            }
                        } else {
                            mSsensorExtension = new SsensorExtension();
                            try {
                                mSsensorExtension.initialize(mContext);
                                mSSensorManager = new SsensorManager(mContext, mSsensorExtension);
                                red = mSSensorManager.getDefaultSensor(Ssensor.TYPE_HRM_LED_RED);
                            } catch (SsdkUnsupportedException e) {
                                Toast.makeText(mContext, e.getMessage(),Toast.LENGTH_LONG).show();
                                mContext.finish();
                            } catch (IllegalArgumentException e) {
                                Toast.makeText(mContext, e.getMessage(),Toast.LENGTH_SHORT).show();
                                mContext.finish();
                            } catch (SecurityException e) {
                                Toast.makeText(mContext, e.getMessage(),Toast.LENGTH_SHORT).show();
                                mContext.finish();
                            }
                            // HRM ON
                            btn_start.setText(btn_start.getTextOn());
                            if (mSSensorManager != null) {
                                mSSensorManager.registerListener(mSSListener, red, SensorManager.SENSOR_DELAY_NORMAL);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        ErrorToast(e);
                    }
                }
            });
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (shouldShowRequestPermissionRationale(Manifest.permission.BODY_SENSORS)) {
                        // Explain to the user why we need to read the contacts
                    }

                    requestPermissions(new String[] { Manifest.permission.BODY_SENSORS }, 101);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant

                    return;
                }
            }
        }
        //create back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });
    }
    private void saveUserInfo() {
        String heartrate = tRED.getText().toString().trim();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child(user.getUid()).child("HeartLED").setValue(heartrate);
        databaseReference.child(user.getUid()).child("Date").setValue(strDate);
        databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());

        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
    }

    public void ErrorToast(IllegalArgumentException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }

    @Override
    protected void onResume() {
        sensorRunning = true;
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            if (red != null) {
                mSSensorManager.unregisterListener(mSSListener, red);
                tRED.setText("");
            }
            btn_start.setSelected(false);
            btn_start.setText(btn_start.getTextOff());
        } catch (IllegalArgumentException e) {
            this.finish();
        } catch (IllegalStateException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    private class SSListener implements SsensorEventListener {
        @Override
        public void OnSensorChanged(SsensorEvent event) {
            // TODO Auto-generated method stub

            if (sensorRunning) {
                heartLED = event.values[0];
                tRED.setText(String.valueOf(heartLED));
            }
            else {
                event.values[0]=0;
            }
        }

        @Override
        public void OnAccuracyChanged(Ssensor ssensor, int i) {

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(heartrate.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //added back button after testing originally was going to rely on the ones android phones have built in
}
