package com.shapor.loftmoney;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class AddItemActivity extends AppCompatActivity {
    EditText mNameEditText;
    EditText mPriceEditText;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEditText = findViewById(R.id.name_edittext);
        mPriceEditText = findViewById(R.id.price_edittext);
        addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();
                String price = mPriceEditText.getText().toString();
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price)) {
                    try {
                        setResult(
                                RESULT_OK,
                                new Intent().putExtra("name", name).putExtra("price", price)
                        );
                        finish();
                    } catch (Exception e) {
                        Log.e("TAG E", e.getMessage());
                    }

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch(Exception e) {
            //default
            Log.e("TAG E", e.getMessage());
        }
    }
}