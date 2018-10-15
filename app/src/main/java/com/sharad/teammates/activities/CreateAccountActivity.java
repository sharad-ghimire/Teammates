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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sharad.teammates.R;
import com.sharad.teammates.models.Student;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class CreateAccountActivity extends AppCompatActivity {

    Button mRegister;
    ProgressBar mProgressBar;
    EditText mCreateAccountEmailET, mCreateAccountPasswordET, mCreateAccountPasswordET2, mCreateAccountStudentIDET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        mRegister = findViewById(R.id.register);
        mProgressBar = findViewById(R.id.pBarSignUp);
        mCreateAccountEmailET = findViewById(R.id.createAccountEmailET);
        mCreateAccountPasswordET = findViewById(R.id.createAccountPasswordET);
        mCreateAccountPasswordET2 = findViewById(R.id.createAccountPasswordET2);
        mCreateAccountStudentIDET = findViewById(R.id.createAccountStudentIDET);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for null values edit text field
                if(!isEmpty(mCreateAccountEmailET.getText().toString()) && !isEmpty(mCreateAccountPasswordET.getText().toString())
                        && !isEmpty(mCreateAccountPasswordET2.getText().toString()) && !isEmpty(mCreateAccountStudentIDET.getText().toString())){
                    //check if password match
                    if(doStringMatch(mCreateAccountPasswordET.getText().toString(),mCreateAccountPasswordET2.getText().toString())){
                        registerNewEmail(mCreateAccountEmailET.getText().toString(), mCreateAccountPasswordET.getText().toString(), mCreateAccountStudentIDET.getText().toString());
                    } else {
                        Toast.makeText(CreateAccountActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CreateAccountActivity.this, "Fill out all forms", Toast.LENGTH_SHORT).show();
                }


            }
        });
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private boolean doStringMatch(String s1, String s2) {return s1.equals(s2); }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void registerNewEmail(final String email, String password, final String studentID) {
        showDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    Student student = new Student();
                    student.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    student.setName("");
                    student.setAge("");
                    student.setEmail(email.substring(0, email.indexOf("@")));
                    student.setUni_id("");
                    student.setMajor_id("");
                    student.setSubjects("");
                    student.setStudent_id("");

                    //Set a default Image while creating new user
                    student.setProfile_image(getString(R.string.default_image));
                    student.setIs_coordinator("false");

                    student.setBio("");
                    student.setInterest("");
                    student.setSkills("");

                    //Add Above student to the database
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(getString(R.string.dbnode_students))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Sign out the student
                            FirebaseAuth.getInstance().signOut();
                            //redirects the user
                            redirectToLoginUser();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Sign out the student
                            FirebaseAuth.getInstance().signOut();
                            //redirects the user to Login Screen
                            redirectToLoginUser();
                            Toast.makeText(CreateAccountActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                } else {
                    Toast.makeText(CreateAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        });


    }

    private void redirectToLoginUser() {
        startActivity(new Intent(CreateAccountActivity.this, SignInActivity.class));
        Toast.makeText(CreateAccountActivity.this, "Login to Continue", Toast.LENGTH_SHORT).show();
    }
}
