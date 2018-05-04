package com.example.mohammeduzair.androidapp.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class caloriecounter extends AppCompatActivity implements View.OnClickListener {
    private TextView counterTxtView;
    private int counterValue = 0;
    private CardView inputNewValue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button saveBtn;
    private DatabaseReference databaseReference;
    AlertDialog.Builder builder;
    EditText inputValue, changeInput;
    LinearLayout AlertDialogLayout;
    LinearLayout.LayoutParams layoutParams;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caloriecounter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        saveBtn = (Button) findViewById(R.id.savebutton);
        inputNewValue = (CardView) findViewById(R.id.inputsetting);
        counterTxtView = (TextView) findViewById(R.id.countertext);
        sharedPreferences = getSharedPreferences("counterValue", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        builder = new AlertDialog.Builder(this);
        inputValue=new EditText(this);
        loadValues();
        setCount();
        inputNewValue.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
                counterValue = 0;
                counterTxtView.setText(String.valueOf(counterValue));
                commitToSharedPreferences();
            }
        });
    }

    private void saveUserInfo() {
        String calories = counterTxtView.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();

        databaseReference.child(user.getUid()).child("Calories").setValue(calories);
        databaseReference.child(user.getUid()).child("Date").setValue(strDate);
        databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());

        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
    }

    private void setCount() {
        counterValue = sharedPreferences.getInt("counterValue", 0);

        if (counterValue == 0) {
            counterTxtView.setText("Zero");
        }
        else {
            counterTxtView.setText(Integer.toString(counterValue));
        }
    }

    private void loadValues() {
        sharedPreferences = getSharedPreferences("counterValue", Context.MODE_PRIVATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(caloriecounter.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void createAlert(){
        builder = new AlertDialog.Builder(this);

        AlertDialogLayout = new LinearLayout(this);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        AlertDialogLayout.setOrientation(LinearLayout.VERTICAL);
        AlertDialogLayout.setLayoutParams(layoutParams);


        AlertDialogLayout.setGravity(Gravity.CENTER);

        changeInput = new EditText(this);
        changeInput.setHint("Example: 150");
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
                    counterValue += Integer.parseInt(changeInput.getText().toString());
                    counterTxtView.setText((String.valueOf(counterValue)));
                    commitToSharedPreferences();
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

    public void commitToSharedPreferences(){
        editor.putInt("counterValue", counterValue);
        editor.commit();
    }

    public void plusBtnClick(View view) {
        counterValue+=100;
        counterTxtView.setText(String.valueOf(counterValue));
        commitToSharedPreferences();
    }

    public void minusBtnClick(View view) {
        counterValue-=100;
        counterTxtView.setText(String.valueOf(counterValue));
        commitToSharedPreferences();
    }


    public void coffeeInc(View view) {
        counterValue+=20;
        counterTxtView.setText(String.valueOf(counterValue));
        commitToSharedPreferences();
    }

    public void crispInc(View view) {
        counterValue+=123;
        counterTxtView.setText(String.valueOf(counterValue));
        commitToSharedPreferences();
    }

    public void chocoInc(View view) {
        counterValue+=224;
        counterTxtView.setText(String.valueOf(counterValue));
        commitToSharedPreferences();
    }

    public void biscuitInc(View view) {
        counterValue+=67;
        counterTxtView.setText(String.valueOf(counterValue));
        commitToSharedPreferences();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.inputsetting:
                createAlert();
            default:
                break;
        }

    }


}
