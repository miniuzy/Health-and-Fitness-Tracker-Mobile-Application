package com.example.mohammeduzair.androidapp.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.R;

import java.util.Locale;

import static android.view.WindowManager.*;

public class settimer extends AppCompatActivity implements View.OnClickListener{

    private Button startButton, stopButton, resetButton, startCountDown, resetCountDown;
    private Chronometer chronometer;
    private long lastPause;
    LinearLayout AlertDialogLayout;
    LinearLayout.LayoutParams layoutParams;
    private CardView inputTimer;

    EditText inputValue, changeInput;
    AlertDialog.Builder builder;
    private int countdown = 0;
    private CountDownTimer countDownTimer;
    private long numMinSec;
    //remove card view and make the 00 clickable to set a value


    private TextView countertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settimer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputTimer = (CardView) findViewById(R.id.inputsetting);
        builder = new AlertDialog.Builder(this);
        inputValue = new EditText(this);
        inputTimer.setOnClickListener(this);
        countertext = (TextView) findViewById(R.id.countertext);
        resetCountDown = (Button) findViewById(R.id.countdownBtnReset);
        startCountDown = (Button) findViewById(R.id.countdownBtn);
        startButton = (Button) findViewById(R.id.startBtn);
        stopButton = (Button) findViewById(R.id.stopBtn);
        resetButton = (Button) findViewById(R.id.resetBtn);
        chronometer = (Chronometer) findViewById(R.id.appchronometer);

        resetCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownStop();
                startCountDown.setText(getString(R.string.start));
                countertext.setText("00");
                resetCountDown.setEnabled(false);
                startCountDown.setEnabled(true);
            }
        });
        startCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer == null) {
                    String getSeconds = countertext.getText().toString();
                    //Check to see if the edittext is valid to use or not
                    if (!getSeconds.equals("") && getSeconds.length() > 0) {
                        int noOfSecs = Integer.parseInt(getSeconds) * 1000;
                        startTimer(noOfSecs);
                        startCountDown.setEnabled(false);
                        resetCountDown.setEnabled(true);
                    } else
                        Toast.makeText(settimer.this, "Please enter a value for seconds", Toast.LENGTH_SHORT).show();
                } else {
                    countdownStop();
                    startCountDown.setText(getString(R.string.start));
                }
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
                if (lastPause != 0) {
                    chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }

                chronometer.start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                lastPause = 0;
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });


    }

    private void countdownStop() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                numMinSec = millisUntilFinished;
                int minutes = (int) (numMinSec / 1000) / 60;
                int seconds = (int) (numMinSec / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                countertext.setText(timeLeftFormatted);
            }
            public void onFinish() {
                countertext.setText("00");
                Toast.makeText(settimer.this, "Timer is finished", Toast.LENGTH_LONG).show();
                countDownTimer = null;
                startCountDown.setText(getString(R.string.start));
            }
        }.start();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(settimer.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void createAlert() {
        builder = new AlertDialog.Builder(this);

        AlertDialogLayout = new LinearLayout(this);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        AlertDialogLayout.setOrientation(LinearLayout.VERTICAL);
        AlertDialogLayout.setLayoutParams(layoutParams);


        AlertDialogLayout.setGravity(Gravity.CENTER);

        changeInput = new EditText(this);
        changeInput.setHint("Example: 60");
        changeInput.setInputType(InputType.TYPE_CLASS_DATETIME);


        AlertDialogLayout.addView(changeInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        builder.setView(AlertDialogLayout);
        builder.setCancelable(true);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (changeInput.length() > 0) {
                    countdown += Integer.parseInt(changeInput.getText().toString());
                    countertext.setText((String.valueOf(countdown)));
                    countdown *=1000;
                } else {
                    changeInput.setError("Enter valid number");
                }
            }


        });
        builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        builder.setTitle("Input a value:");

        AlertDialog alertDialog = builder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.inputsetting:
                createAlert();
                break;
            default:
                break;
        }

    }
}
