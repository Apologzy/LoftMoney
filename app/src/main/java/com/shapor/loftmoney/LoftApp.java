package com.shapor.loftmoney;

import android.app.Application;
import android.content.SharedPreferences;

import com.shapor.loftmoney.remote.AuthApi;
import com.shapor.loftmoney.remote.MoneyApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoftApp extends Application {

    private MoneyApi moneyApi;
    private AuthApi authApi;
    public static String TOKEN_KEY = "token";


    @Override
    public void onCreate() {
        super.onCreate();

        configureNetwork();
    }

    private void configureNetwork() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://loftschool.com/android-api/basic/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        moneyApi = retrofit.create(MoneyApi.class);
        authApi = retrofit.create(AuthApi.class);
    }


    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(getString(R.string.app_name), 0);
    }

    public MoneyApi getMoneyApi() {
        return moneyApi;
    }

    public AuthApi getAuthApi() {
        return authApi;
    }

}
