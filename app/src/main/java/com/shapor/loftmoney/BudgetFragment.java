package com.shapor.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shapor.loftmoney.cells.money.MoneyAdapter;
import com.shapor.loftmoney.cells.money.MoneyAdapterClick;
import com.shapor.loftmoney.cells.money.MoneyCellModel;
import com.shapor.loftmoney.remote.MoneyItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment extends Fragment {

    private static final String ARG_SECTION_NAME = "section_name";
    public String fragmentType;
    public static final int REQUEST_CODE = 100;
    private MoneyAdapter moneyAdapter;
    ImageButton callAddButton;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    /*
    public BudgetFragment(String fragmentType) {
        this.fragmentType = fragmentType;
    }

     */

    public static BudgetFragment newInstance(String fragmentType) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_NAME, fragmentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() == null) {
            return;
        }

        fragmentType = getArguments().getString("section_name");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget,null);
        //callAddButton = view.findViewById(R.id.call_add_item_activity);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.costsRecyclerView);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (fragmentType.equals("expense")) {
                    generateExpenses();
                } else if (fragmentType.equals("income")) {
                    generateIncome();
                }
            }
        });

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




        return view;
    }



    private void generateExpenses() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();

        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getMoney(token,"expense")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyItems) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);

                        for (MoneyItem moneyItem : moneyItems) {
                            moneyCellModels.add(MoneyCellModel.getInstance(moneyItem));
                        }

                        moneyAdapter.setData(moneyCellModels);

                        Log.e("TAG", "Success " + moneyItems);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("TAG", "Error " + throwable);
                    }
                });

        compositeDisposable.add(disposable);

    }

    private void generateIncome() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();

        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getMoney(token,"income")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyItems) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        for (MoneyItem moneyItem : moneyItems) {
                            moneyCellModels.add(MoneyCellModel.getInstance(moneyItem));
                        }

                        moneyAdapter.setData(moneyCellModels);

                        Log.e("TAG", "Success " + moneyItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("TAG", "Error " + throwable);
                    }
                });

        compositeDisposable.add(disposable);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (fragmentType.equals("expense")) {
            generateExpenses();
        } else if (fragmentType.equals("income")) {
            generateIncome();
        }

    }


}
