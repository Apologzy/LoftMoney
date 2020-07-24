package com.shapor.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shapor.loftmoney.cells.money.MoneyAdapter;
import com.shapor.loftmoney.cells.money.MoneyAdapterClick;
import com.shapor.loftmoney.cells.money.MoneyCellModel;

import java.util.ArrayList;
import java.util.List;

public class IncomeFragment extends Fragment {
    private static final int REQUEST_CODE = 100;
    private MoneyAdapter moneyAdapter;
    ImageButton callAddButton;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget,null);
        callAddButton = view.findViewById(R.id.call_add_item_activity);

        callAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(new Intent(getActivity(), AddItemActivity.class), REQUEST_CODE);
                } catch (Exception e) {
                    Log.e("TAG E", e.getMessage());
                }
            }
        });

        recyclerView = view.findViewById(R.id.costsRecyclerView);
        moneyAdapter = new MoneyAdapter();
        moneyAdapter.setMoneyAdapterClick(new MoneyAdapterClick() {
            @Override
            public void onMoneyClick(MoneyCellModel moneyCellModel) {
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(moneyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));


        moneyAdapter.addData(generateIncome());

        return view;
    }


    private List<MoneyCellModel> generateIncome() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();
        moneyCellModels.add(new MoneyCellModel("Зарплата.Июнь", "70000 Р", R.color.incomeColor));
        moneyCellModels.add(new MoneyCellModel("Премия", "7000 Р", R.color.incomeColor));
        moneyCellModels.add(new MoneyCellModel("Олег наконец-то вернул долг", "300000 Р", R.color.incomeColor));
        return moneyCellModels;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String price;
        try {
            price = data.getStringExtra("price");
        } catch (NullPointerException e) {
            price = "0";
            Log.e("TAG E", e.getMessage());
        }

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            moneyAdapter.addItem(new MoneyCellModel(data.getStringExtra("name"), price, R.color.incomeColor));

        }
    }
}
