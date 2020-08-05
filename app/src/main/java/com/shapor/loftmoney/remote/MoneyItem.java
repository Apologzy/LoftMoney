package com.shapor.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class MoneyItem {

    @SerializedName("id") private String ItemId;
    @SerializedName("name") private String name;
    @SerializedName("price") private int price;
    @SerializedName("type") private String type;
    @SerializedName("date") private String date;

    public String getItemId() {
        return ItemId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
