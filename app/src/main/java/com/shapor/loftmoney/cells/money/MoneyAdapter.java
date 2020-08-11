package com.shapor.loftmoney.cells.money;

import android.util.SparseBooleanArray;
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
    private ItemsAdapterListener mListener;

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();

    public void clearSelections() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(int position) {
        mSelectedItems.put(position, !mSelectedItems.get(position));
        notifyDataSetChanged();
    }

    public  void itemSelectedDelete (int position) {
        mSelectedItems.delete(position);
    }

    public void clearItem(int position) {
        mSelectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public boolean isSelected (int position) {
       return mSelectedItems.get(position);
    }


    public int getSelectedSize() {
        int result = 0;

        for (int i = 0; i < moneyCellModels.size() ; i++) {
            if(mSelectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    public int getSelectedSizeOld(int position) {
        int result = 0;

        for (int i = 0; i < mSelectedItems.size() ; i++) {
            if(mSelectedItems.size() == 1) {
                if (mSelectedItems.get(position)) {
                    result++;
                }

            } else {
                if(mSelectedItems.get(i)) {
                    result++;
                }
            }

        }

        return result;
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (MoneyCellModel item: moneyCellModels) {
            if (mSelectedItems.get(i)) {
                result.add(item.getId());
            }
            i++;
        }
        return result;
    }

    public void setListener(ItemsAdapterListener listener) {
        mListener = listener;
    }

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
        holder.bind(moneyCellModels.get(position), mSelectedItems.get(position));
        holder.setListener(mListener, moneyCellModels.get(position), position);
    }

    @Override
    public int getItemCount() {
        return moneyCellModels.size();
    }

    public static class MoneyViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        TextView nameView;
        TextView valueView;
        MoneyAdapterClick moneyAdapterClick;

        public MoneyViewHolder(MoneyAdapterClick moneyAdapterClick, @NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            this.moneyAdapterClick = moneyAdapterClick;
            nameView = itemView.findViewById(R.id.cellMoneyNameView);
            valueView = itemView.findViewById(R.id.cellMoneyValueView);
        }

        public void bind(final MoneyCellModel moneyCellModel, boolean isSelected) {
            mItemView.setSelected(isSelected);
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

        public void setListener(ItemsAdapterListener listener, MoneyCellModel item, int position) {

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, position);
                }
            });

            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(item, position);
                    return false;
                }
            });

        }

    }

}
