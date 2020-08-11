package com.shapor.loftmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.shapor.loftmoney.screens.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected Toolbar mToolbar;
    protected FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        List<Fragment> fragments = new ArrayList<>(2);
        fragments.add(BudgetFragment.newInstance("expense"));
        fragments.add(BudgetFragment.newInstance("income"));
        fragments.add(BalanceFragment.newInstance("balance"));



        floatingActionButton = findViewById(R.id.fab);
        tabLayout = findViewById(R.id.tabs);
        mToolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.expenses);
        tabLayout.getTabAt(1).setText(R.string.income);
        tabLayout.getTabAt(2).setText("Balance");



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 2) {
                    floatingActionButton.setVisibility(View.GONE);
                } else {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int activeFragmentIndex = viewPager.getCurrentItem();
                String test = Integer.toString(activeFragmentIndex);
                Intent intent = new Intent(MainScreen.this, AddItemActivity.class);
                intent.putExtra("activeIndex", test);
                try {
                    startActivity(intent);
                    Log.e("TAG TRUE", "WE ARE !!!");
                    finish();
                }catch (Exception e) {
                    Log.e("TAG ERR", e.getMessage());
                }


            }
        });

    }


    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue));
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue));
        floatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightish_blue));
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.lightish_blue));
        floatingActionButton.setVisibility(View.VISIBLE);
    }




    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(MainScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch(Exception e) {
            //default
            Log.e("TAG E", e.getMessage());
        }
    }

}