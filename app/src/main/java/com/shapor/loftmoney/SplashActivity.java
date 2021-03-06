package com.shapor.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.shapor.loftmoney.screens.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkToken();

    }

    private void checkToken() {
        String token = ((LoftApp) getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        if (TextUtils.isEmpty(token)) {
            roadToLogin();
        } else {
            roadToMain();
        }

    }

    private void roadToMain() {
        Intent mainIntent = new Intent(SplashActivity.this, MainScreen.class);
        startActivity(mainIntent);
    }

    private void roadToLogin() {
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

}