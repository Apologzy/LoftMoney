package com.shapor.loftmoney.remote;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoneyApi {

    // https://verdant-violet.glitch.me/items?type=expense
    @GET("./items")
    Single<List<MoneyItem>> getMoney(@Query("auth-token") String token, @Query("type") String type);

    // https://verdant-violet.glitch.me/items/add?name=xleb&price=100
    @POST("./items/add")
    @FormUrlEncoded
    Completable addMoney(@Field("auth-token") String token,
                         @Field("price") String price,
                         @Field("name") String name,
                         @Field("type") String type);

    @POST ("./items/remove")
    @FormUrlEncoded
    Completable removeMoney(@Field("auth-token") String token,
                            @Field("id") String id);

}
