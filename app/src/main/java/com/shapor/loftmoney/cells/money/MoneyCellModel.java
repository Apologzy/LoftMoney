package com.shapor.loftmoney.cells.money;


import com.shapor.loftmoney.R;
import com.shapor.loftmoney.remote.MoneyApi;
import com.shapor.loftmoney.remote.MoneyItem;

public class MoneyCellModel {
    private String name;
    private String value;
    private Integer color;
    private int id;

    public MoneyCellModel(String name, String value, Integer color, int id) {
        this.name = name;
        this.value = value;
        this.color = color;
        this.id = id;
    }

    public static MoneyCellModel getInstance(MoneyItem moneyItem) {
        return new MoneyCellModel(moneyItem.getName(),
                moneyItem.getPrice() + " ₽",
                moneyItem.getType().equals("expense") ? R.color.expenseColor : R.color.incomeColor,
                moneyItem.getId());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Integer getColor() {
        return color;
    }
}
