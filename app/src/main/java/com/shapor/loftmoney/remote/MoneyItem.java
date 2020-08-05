package com.shapor.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class MoneyItem {

    @SerializedName("id") private String id;
    @SerializedName("name") private String name;
    @SerializedName("price") private int price;
    @SerializedName("type") private String type;
    @SerializedName("date") private String date;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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
