package com.example.mohammeduzair.androidapp.ThrdPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.example.mohammeduzair.androidapp.Login.login;
import com.example.mohammeduzair.androidapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class settings1 extends AppCompatActivity implements View.OnClickListener {
    private Button logoutBtn, saveBtn, viewBtn;
    private TextView result;
    AlertDialog.Builder builder;
    EditText inputValue, changeInput;
    LinearLayout AlertDialogLayout;
    LinearLayout.LayoutParams layoutParams;
    private int heightValue = 0;
    private int weightValue = 0;
    private TextView bmiHeight, bmiWeight;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        sharedPreferences = getSharedPreferences("BMIValue", android.content.Context.MODE_PRIVATE);

        logoutBtn = (Button) findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(settings1.this, login.class));
            }
        });
        viewBtn = (Button) findViewById(R.id.viewbutton);
        viewBtn.setOnClickListener(this);
        inputValue = new EditText(this);
        bmiHeight = (TextView) findViewById(R.id.height);
        bmiWeight = (TextView) findViewById(R.id.weight);
        bmiWeight.setOnClickListener(this);
        bmiHeight.setOnClickListener(this);
        result = (TextView) findViewById(R.id.result);
        saveBtn = (Button) findViewById(R.id.savebutton);
        mAuth = FirebaseAuth.getInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });


    }



    private void saveUserInfo() {
        String bmi = result.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();

        databaseReference.child(user.getUid()).child("BMI").setValue(bmi);
        databaseReference.child(user.getUid()).child("Date").setValue(strDate);
        databaseReference.child(user.getUid()).child("Email").setValue(user.getEmail());

        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(settings1.this, menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void heightAlert() {
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
                    heightValue += Integer.parseInt(changeInput.getText().toString());
                    bmiHeight.setText((String.valueOf(heightValue)));
                } else {
                    changeInput.setError("Enter valid number");
                }
            }
        });
        builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        builder.setTitle("Input height:");

        AlertDialog alertDialog = builder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void weightAlert() {
        builder = new AlertDialog.Builder(this);

        AlertDialogLayout = new LinearLayout(this);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        AlertDialogLayout.setOrientation(LinearLayout.VERTICAL);
        AlertDialogLayout.setLayoutParams(layoutParams);


        AlertDialogLayout.setGravity(Gravity.CENTER);

        changeInput = new EditText(this);
        changeInput.setHint("Example: 75");
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
                    weightValue += Integer.parseInt(changeInput.getText().toString());
                    bmiWeight.setText((String.valueOf(weightValue)));
                } else {
                    changeInput.setError("Enter valid number");
                }
            }
        });
        builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        builder.setTitle("Input weight:");

        AlertDialog alertDialog = builder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void calculateBMIValue(View view) {
        String heightString = bmiHeight.getText().toString();
        String weightString = bmiWeight.getText().toString();
        if (heightString != null && !"".equals(heightString)
                && weightString != null && !"".equals(weightString)) {
            float heightValue = Float.parseFloat(heightString) / 100;
            float weightValue = Float.parseFloat(weightString);


            float bmi = weightValue / (heightValue * heightValue);
            displayBMIValue(bmi);
        }
        else { Toast.makeText(this, "Please enter some values...", Toast.LENGTH_LONG).show();

        }
    }


    private void displayBMIValue(float bmi) {
        String bmiOutput = "";

        if (Float.compare(bmi, 15f) <= 0) {
            bmiOutput = getString(R.string.underweight);
        } else if (Float.compare(bmi, 15f) > 0 && Float.compare(bmi, 18.5f) <= 0) {
            bmiOutput = getString(R.string.underweight);
        } else if (Float.compare(bmi, 18.5f) > 0 && Float.compare(bmi, 25f) <= 0) {
            bmiOutput = getString(R.string.normal);
        } else if (Float.compare(bmi, 25f) > 0 && Float.compare(bmi, 30f) <= 0) {
            bmiOutput = getString(R.string.overweight);
        } else if (Float.compare(bmi, 30f) > 0 && Float.compare(bmi, 40f) <= 0) {
            bmiOutput = getString(R.string.obese);
        } else {
            bmiOutput = getString(R.string.obese);
        }

        bmiOutput = bmi + "\n" + bmiOutput;
        result.setText(bmiOutput);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.weight:
                weightAlert();
                break;
            case R.id.height:
                heightAlert();
                break;
            case R.id.viewbutton:
                i = new Intent(settings1.this, viewdata.class);
                startActivity(i);
                break;
            default:
                break;

        }
    }
}


//to retrieve it i need to look, but look am gonna go dinner and come back till t
//search for it or let me put a vid on

//
