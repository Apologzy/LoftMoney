package com.shapor.loftmoney;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shapor.loftmoney.cells.money.MoneyCellModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class AddItemActivity extends AppCompatActivity {
    EditText mNameEditText;
    EditText mPriceEditText;
    Button addButton;
    TextView loadingView;
    String fragType;
    String activeIndex;

    private final int REQUEST_CODE = 100;

    String name;
    String value;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEditText = findViewById(R.id.name_edittext);
        mPriceEditText = findViewById(R.id.price_edittext);
        addButton = findViewById(R.id.add_button);
        loadingView = findViewById(R.id.loadingView);

        try {
            Intent intent = getIntent();
            activeIndex = intent.getStringExtra("activeIndex");
            if(activeIndex.equals("0")) {
                fragType = "expense";
            } else if (activeIndex.equals("1")) {
                fragType = "income";
            }
        } catch (Exception e) {
            Log.e("TAG ERR", e.getMessage());
        }


        configureInputViews();

        if (fragType.equals("expense")) {
            configureExpenseAdding();
        } else if (fragType.equals("income")) {
            configureIncomeAdding();
        }


    }

    private void setUI(boolean isLoading) {
        addButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void configureExpenseAdding() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUI(true);
                String token = getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");

                Disposable disposable = ((LoftApp) getApplication()).getMoneyApi().addMoney(token, value, name, "expense")
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                setUI(false);
                                Log.e("TAG", "Complete");
                                Toast.makeText(getApplicationContext(), "Add successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddItemActivity.this, MainScreen.class));
                                finish();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("TAG", throwable.getLocalizedMessage());
                                Toast.makeText(getApplicationContext(), "Added was not successful because "
                                                + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                compositeDisposable.add(disposable);

            }
        });
    }

    private void configureIncomeAdding() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUI(true);
                String token = getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");

                Disposable disposable = ((LoftApp) getApplication()).getMoneyApi().addMoney(token, value, name, "income")
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                setUI(false);
                                Log.e("TAG", "Complete");
                                Toast.makeText(getApplicationContext(), "Add successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddItemActivity.this, MainScreen.class));
                                finish();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("TAG", throwable.getLocalizedMessage());
                                Toast.makeText(getApplicationContext(), "Added was not successful because "
                                        + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                compositeDisposable.add(disposable);

            }
        });
    }

    private void configureInputViews() {

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                name = charSequence.toString();
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                value = charSequence.toString();
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void checkInputs() {
        boolean isEnabled = value != null && !value.isEmpty() && name != null && !name.isEmpty();

        addButton.setEnabled(isEnabled);
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE) {
            try {
                activeIndex = data.getStringExtra("activeIndex");
                Log.e("TEST", activeIndex);
                if(activeIndex.equals("0")) {
                    fragType = "expense";
                } else if (activeIndex.equals("1")) {
                    fragType = "income";
                }

                Log.e("TEST", fragType);
            } catch (Exception e) {
                Log.e("TAG E", e.getMessage());
            }
        }



    }

     */




    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(AddItemActivity.this, MainScreen.class);
            startActivity(intent);
            finish();
        } catch(Exception e) {
            //default
            Log.e("TAG E", e.getMessage());
        }
    }
}