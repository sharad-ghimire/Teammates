package com.sharad.teammates.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sharad.teammates.R;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void getStartedClickHandler(View view) {
        startActivity(new Intent(HomePageActivity.this, CreateAccountActivity.class));
        finish();
    }

    public void loginHandler(View view) {
        startActivity(new Intent(HomePageActivity.this, SignInActivity.class));
        finish();
    }
}
