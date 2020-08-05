package com.shapor.loftmoney.screens.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shapor.loftmoney.LoftApp;
import com.shapor.loftmoney.MainScreen;
import com.shapor.loftmoney.R;
import com.shapor.loftmoney.remote.AuthResponse;

import java.util.Random;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements LoginView {

    protected Button loginButtonView;
    private long backPressedTime;
    private Toast backToast;
    private LoginPresenter loginPresenter = new LoginPresenterImpl();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButtonView = (Button) findViewById(R.id.loginButtonView);
        loginPresenter.attachViewState(this);

        configureButton();

    }

    @Override
    protected void onPause() {
        loginPresenter.disposeRequests();
        super.onPause();
    }

    private void configureButton() {
        loginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.performLogin(((LoftApp) getApplication()).getAuthApi());
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast =  Toast.makeText(getBaseContext(), "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();

    }


    @Override
    public void toggleSending(boolean isActive) {
        loginButtonView.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String token) {

        Toast.makeText(getApplicationContext(), "User was login successfully", Toast.LENGTH_SHORT).show();

        ((LoftApp) getApplication()).getSharedPreferences().edit().putString(LoftApp.TOKEN_KEY, token).apply();

        Intent mainIntent = new Intent(getApplicationContext(), MainScreen.class);
        startActivity(mainIntent);

    }
}