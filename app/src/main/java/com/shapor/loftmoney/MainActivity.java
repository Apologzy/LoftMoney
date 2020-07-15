package com.shapor.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView helloWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helloWorld = (TextView) findViewById(R.id.tv_hello_world);
        helloWorld.setText("Мы победим:" + "\n" + "Титры");
    }
}