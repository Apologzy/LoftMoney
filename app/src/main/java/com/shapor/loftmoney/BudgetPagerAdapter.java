package com.shapor.loftmoney;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BudgetPagerAdapter extends FragmentPagerAdapter {

    public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return new BudgetFragment(position);

    }

    @Override
    public int getCount() {
        return 2;
    }
}
