package com.shapor.loftmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.shapor.loftmoney.screens.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        List<Fragment> fragments = new ArrayList<>(2);
        fragments.add(BudgetFragment.newInstance("expense"));
        fragments.add(BudgetFragment.newInstance("income"));
        //fragments.add(new BudgetFragment("expense"));
        //fragments.add(new BudgetFragment("income"));



        floatingActionButton = findViewById(R.id.fab);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,fragments));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.expenses);
        tabLayout.getTabAt(1).setText(R.string.income);

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