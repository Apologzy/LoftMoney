package com.shapor.loftmoney;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shapor.loftmoney.remote.MoneyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class BalanceFragment extends Fragment {

    private static final String ARG_SECTION_NAME = "section_name";
    public String fragmentType;
    public BalanceView balanceView;
    public TextView tvBalanceValue;
    public TextView tvExpenseValue;
    public TextView tvIncomeValue;
    public float expense;
    public float income;
    public float balance;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public static BalanceFragment newInstance(String fragmentType) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_NAME, fragmentType);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() == null) {
            return;
        }

        fragmentType = getArguments().getString("section_name");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_balance, null);



         balanceView = view.findViewById(R.id.balanceView);
         tvExpenseValue = view.findViewById(R.id.expenseValue);
         tvIncomeValue = view.findViewById(R.id.incomeValue);
         tvBalanceValue = view.findViewById(R.id.txtBalanceFinanceValue);


         totalIncome();
         totalExpense();

         return view;
    }



    private void initBalanceConfigure() {


        float expense = this.expense;
        float income =  this.income;
        float balance = expense + income;

        tvBalanceValue.setText(String.valueOf((int) balance)  + "₽");
        tvIncomeValue.setText(String.valueOf((int) income) + "₽");
        tvExpenseValue.setText(String.valueOf((int) expense) + "₽");

        balanceView.update(expense, income);

    }


    private void calculateExpense (List<Integer> totalExpenseList ) {

        float sumExpense = 0;
        for (int i = 0; i < totalExpenseList.size() ; i++) {
            sumExpense += (float) totalExpenseList.get(i);
        }

        this.expense = sumExpense;
    }

    private void calculateIncome (List<Integer> totalIncomeList ) {

        float sumIncome = 0;
        for (int i = 0; i < totalIncomeList.size() ; i++) {
            sumIncome += (float) totalIncomeList.get(i);
        }

        this.income = sumIncome;

    }

    private void totalExpense () {

        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getMoney(token, "expense")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyItems) throws Exception {
                        List<Integer> totalExpenseList = new ArrayList<>();
                        for (MoneyItem moneyItem : moneyItems) {
                            totalExpenseList.add(moneyItem.getPrice());

                        }
                        calculateExpense(totalExpenseList);
                        initBalanceConfigure();
                        Log.e("TAG", "Success " + moneyItems);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "Error " + throwable.getLocalizedMessage());
                    }
                });
        compositeDisposable.add(disposable);


    }

    private void totalIncome () {

        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getMoney(token, "income")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyItems) throws Exception {
                        List<Integer> totalIncomeList = new ArrayList<>();
                        for (MoneyItem moneyItem : moneyItems) {
                            totalIncomeList.add(moneyItem.getPrice());

                        }
                        Log.e("TAG", "Success " + moneyItems);
                        calculateIncome(totalIncomeList);
                        initBalanceConfigure();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "Error " + throwable.getLocalizedMessage());
                    }
                });
        compositeDisposable.add(disposable);

    }


    

}