package com.sharad.teammates.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sharad.teammates.R;

import static android.text.TextUtils.isEmpty;

public class SignInActivity extends AppCompatActivity {

    //firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    Button mSIgnIn;
    ProgressBar mProgressBar;
    EditText emailSignIn, passwordSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSIgnIn = findViewById(R.id.buttonSignIn);
        mProgressBar = findViewById(R.id.signInPBar);
        emailSignIn = findViewById(R.id.emailSignIn);
        passwordSignIn = findViewById(R.id.passwordSignIn);

        //Setup Firebase Auth
        setUpFirebaseAuth();

        mSIgnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if value is empty
                if(!isEmpty(emailSignIn.getText().toString()) && !isEmpty(passwordSignIn.getText().toString())){
                    showDialog();

                    //Sign in the user
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailSignIn.getText().toString(), passwordSignIn.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideDialog();
                            if(task.isSuccessful()){
                                startActivity(new Intent(SignInActivity.this, AddUniversityDetails.class));
                            } else {
                                Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(SignInActivity.this, "Fill both fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void createOneHandler(View view) {
        startActivity(new Intent(SignInActivity.this, CreateAccountActivity.class));
    }

    public void signInHandler(View view) {
        startActivity(new Intent(SignInActivity.this, MainPageAfterLoginActivity.class));
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //Firebase Auth Setup
    private void setUpFirebaseAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //Then we have a user that is autheticated

                } else {

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        //OnStart we need to add our Auth Listener
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //onStop we want to remove the Auth State Listener
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);

        }
    }

}
