package com.delivery.mydelivery.myInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderHistoryApi {

    // 주문내역
    @GET("/order/history/{userId}")
    Call<List<OrderHistoryVO2>> getOrderHistory(@Path("userId") int userId);

    // 주문 내역 상세
    @GET("/order/history/detail/{recruitId}/{userId}")
    Call<List<OrderHistoryDetailVO>> getOrderHistoryDetail(@Path("recruitId") int recruitId, @Path("userId") int userId);

    // 이미지
    @GET("/order/image/{recruitId}/{userId}")
    Call<ResponseBody> getImage(@Path("recruitId") int recruitId, @Path("userId") int userId);
}
