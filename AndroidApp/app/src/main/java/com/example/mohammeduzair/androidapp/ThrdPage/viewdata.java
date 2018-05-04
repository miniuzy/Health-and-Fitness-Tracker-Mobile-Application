package com.example.mohammeduzair.androidapp.ThrdPage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mohammeduzair.androidapp.Home.Homepage;
import com.example.mohammeduzair.androidapp.Home.heartrate;
import com.example.mohammeduzair.androidapp.R;
import com.example.mohammeduzair.androidapp.utility.Userinformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewdata extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdata);
        mListView = (ListView) findViewById(R.id.listview);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // ...
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(viewdata.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Put back button here

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Userinformation uInfo = new Userinformation();
            uInfo.setEmail(ds.child(userID).getValue(Userinformation.class).getEmail()); //set the email
            uInfo.setBMI(ds.child(userID).getValue(Userinformation.class).getBMI()); //set the name
            uInfo.setCalories(ds.child(userID).getValue(Userinformation.class)
                    .getCalories()); //set the name
            uInfo.setCalories_Burned(ds.child(userID).getValue(Userinformation.class)
                    .getCalories_Burned()); //
            uInfo.setDate(ds.child(userID).getValue(Userinformation.class)
                    .getDate());
            uInfo.setDistance(ds.child(userID).getValue(Userinformation.class)
                    .getDistance());
            uInfo.setFeeling(ds.child(userID).getValue(Userinformation.class)
                    .getFeeling());
            uInfo.setTime_Slept(ds.child(userID).getValue(Userinformation.class)
                    .getTime_Slept());
            uInfo.setSteps(ds.child(userID).getValue(Userinformation.class)
                    .getSteps());
            uInfo.setHeartLED(ds.child(userID).getValue(Userinformation.class)
                    .getHeartLED());

            //display all the information

            ArrayList<String> array  = new ArrayList<>();
            array.add("Email:" + uInfo.getEmail());
            array.add("Date:" + uInfo.getDate());
            array.add("BMI:" + uInfo.getBMI());
            array.add("Calories:" + uInfo.getCalories());
            array.add("Calories Burned:" + uInfo.getCalories_Burned());
            array.add("Steps:" + uInfo.getSteps());
            array.add("Distance:" + uInfo.getDistance());
            array.add("Time Slept:" + uInfo.getTime_Slept());
            array.add("Feeling:" + uInfo.getFeeling());
            array.add("Heart Rate:" + uInfo.getHeartLED());

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
