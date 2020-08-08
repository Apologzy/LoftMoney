package com.shapor.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class MoneyItem {
//теперь моделька сходится с ответом
  //  [{"id":3050,"price":100,"name":"abc","type":"expense","created_at":"2020-08-06 05:18:56","updated_at":"2020-08-06 05:18:56","user_id":320}]
    @SerializedName("id") private int id;
    @SerializedName("name") private String name;
    @SerializedName("price") private int price;
    @SerializedName("type") private String type;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("user_id") private int userId;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public String  getCreatedAt() {
        return createdAt;
    }

    public String  getApdatedAt() {
        return updatedAt;
    }

    public int  getUserId() {
        return userId;
    }

}
