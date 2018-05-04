package com.example.mohammeduzair.androidapp.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.Home.Homepage;
import com.example.mohammeduzair.androidapp.Home.heartrate;
import com.example.mohammeduzair.androidapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A login screen that offers login via email/password.
 */
public class login extends AppCompatActivity{

    private EditText emailInput ,passwordInput;
    private Button signupBtn, loginBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SignInButton googleBtn;
    private final static int RC_SIGN_IN = 4 ;
    private GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mAuth = FirebaseAuth.getInstance();

        //use this to avoid login page
       mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(login.this, Homepage.class));
                }
            }
        };

        emailInput = (EditText) findViewById(R.id.email);
        passwordInput= (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.email_sign_in_button);
        signupBtn = (Button) findViewById(R.id.email_register_button);
        googleBtn = (SignInButton) findViewById(R.id.google_login_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, signup.class));
            }
        });
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent = new Intent (login.this, Homepage.class);
                                    startActivity(intent);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(login.this, "Logging in with:" + user.getEmail(),
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(login.this, "Incorrect Email/Password",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }



        });

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and if he is keep him signed in.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //   updateUI(user);
                            Intent intent = new Intent(login.this, Homepage.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(login.this, "Google sign in with:" + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                        }

                    }
                });
    }


}
