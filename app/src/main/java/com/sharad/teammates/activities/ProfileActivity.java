package com.sharad.teammates.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sharad.teammates.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


    }

    public void editProfileBtnHandler(View view) {
        startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
    }
}
