package com.shapor.loftmoney;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shapor.loftmoney.cells.money.ItemsAdapterListener;
import com.shapor.loftmoney.cells.money.MoneyAdapter;
import com.shapor.loftmoney.cells.money.MoneyAdapterClick;
import com.shapor.loftmoney.cells.money.MoneyCellModel;
import com.shapor.loftmoney.remote.MoneyItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment extends Fragment implements ItemsAdapterListener, ActionMode.Callback {

    private static final String ARG_SECTION_NAME = "section_name";
    public String fragmentType;
    public static final int REQUEST_CODE = 100;
    private MoneyAdapter moneyAdapter;
    private ActionMode mActionMode;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CompositeDisposable compositeDisposable = new CompositeDisposable();



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


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.costsRecyclerView);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generateExpenses();
            }
        });

        moneyAdapter = new MoneyAdapter();
        moneyAdapter.setListener(this);
        /*
        moneyAdapter.setMoneyAdapterClick(new MoneyAdapterClick() {
            @Override
            public void onMoneyClick(MoneyCellModel moneyCellModel) {
                Intent intent = new Intent(getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

         */

        recyclerView.setAdapter(moneyAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));


        return view;
    }



    private void generateExpenses() {
        List<MoneyCellModel> moneyCellModels = new ArrayList<>();

        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().getMoney(token, fragmentType)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MoneyItem>>() {
                    @Override
                    public void accept(List<MoneyItem> moneyItems) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);

                        for (MoneyItem moneyItem : moneyItems) {
                            moneyCellModels.add(MoneyCellModel.getInstance(moneyItem));
                        }
                        //Collections.reverse(moneyCellModels);
                        moneyAdapter.setData(moneyCellModels);

                        Log.e("TAG", "Success " + moneyItems);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("TAG", "Error " + throwable.getLocalizedMessage());
                    }
                });

        compositeDisposable.add(disposable);

    }


    @Override
    public void onResume() {
        super.onResume();

        generateExpenses();

    }


    @Override
    public void onItemClick(MoneyCellModel item, int position) {
//        if(moneyAdapter.isSelected(position)) {
//            moneyAdapter.clearItem(position);
//        }

        if(mActionMode != null) {
            //mActionMode.setTitle(getString(R.string.selected, String.valueOf(moneyAdapter.getSelectedSize())));
            if (!moneyAdapter.isSelected(position)) {
                moneyAdapter.toggleItem(position);
                mActionMode.setTitle(getString(R.string.selected, String.valueOf(moneyAdapter.getSelectedSize())));
            } else {
                moneyAdapter.clearItem(position);
                //moneyAdapter.itemSelectedDelete(position);
                mActionMode.setTitle(getString(R.string.selected, String.valueOf(moneyAdapter.getSelectedSize())));

            }
        }

    }

    @Override
    public void onItemLongClick(MoneyCellModel item, int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        moneyAdapter.toggleItem(position);
        if(mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(moneyAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.remove) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeItems();
                            actionMode.finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
        return true;
    }

    private void removeItems() {
        String token = ((LoftApp) getActivity().getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");
        List<Integer> selectedItems = moneyAdapter.getSelectedItemIds();
        for (Integer itemId : selectedItems) {
            Disposable disposable = ((LoftApp) getActivity().getApplication()).getMoneyApi().removeMoney(String.valueOf(itemId.intValue()), token)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            Log.e("TAG COMPLETE", "Complete");

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e("TAG ERROR", throwable.getLocalizedMessage());
                        }
                    });
            compositeDisposable.add(disposable);
        }
        generateExpenses();
        moneyAdapter.clearSelections();

    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        moneyAdapter.clearSelections();
    }
}
