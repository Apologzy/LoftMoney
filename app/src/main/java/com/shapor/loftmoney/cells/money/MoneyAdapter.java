package com.shapor.loftmoney.cells.money;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shapor.loftmoney.R;

import java.util.ArrayList;
import java.util.List;


public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.MoneyViewHolder> {

    private List<MoneyCellModel> moneyCellModels = new ArrayList<>();
    private MoneyAdapterClick moneyAdapterClick;

    public void setMoneyAdapterClick(MoneyAdapterClick moneyAdapterClick) {
        this.moneyAdapterClick = moneyAdapterClick;
    }

    public void setData(List<MoneyCellModel> moneyCellModels) {
        this.moneyCellModels.clear();
        this.moneyCellModels.addAll(moneyCellModels);
        notifyDataSetChanged();
    }

    public void clearData() {
        moneyCellModels.clear();
        notifyDataSetChanged();
    }

    public void addData(List<MoneyCellModel> moneyCellModels) {
        this.moneyCellModels.addAll(moneyCellModels);
        notifyDataSetChanged();
    }

    public void addItem (MoneyCellModel moneyCellModels) {
        this.moneyCellModels.add(moneyCellModels);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoneyViewHolder(moneyAdapterClick, LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_money, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyViewHolder holder, int position) {
        holder.bind(moneyCellModels.get(position));
    }

    @Override
    public int getItemCount() {
        return moneyCellModels.size();
    }

    public static class MoneyViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView valueView;
        MoneyAdapterClick moneyAdapterClick;

        public MoneyViewHolder(MoneyAdapterClick moneyAdapterClick, @NonNull View itemView) {
            super(itemView);
            this.moneyAdapterClick = moneyAdapterClick;
            nameView = itemView.findViewById(R.id.cellMoneyNameView);
            valueView = itemView.findViewById(R.id.cellMoneyValueView);
        }

        public void bind(final MoneyCellModel moneyCellModel) {
            nameView.setText(moneyCellModel.getName());
            valueView.setText(moneyCellModel.getValue());
            valueView.setTextColor(ContextCompat.getColor(valueView.getContext(), moneyCellModel.getColor()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (moneyAdapterClick != null) {
                        moneyAdapterClick.onMoneyClick(moneyCellModel);
                    }
                }
            });
        }

    }

}
