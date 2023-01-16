package com.delivery.mydelivery.order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderApi {

    // 사용자id, 매장id를 통해 장바구니에 다른 매장의 메뉴가 들어있는지 확인
    // 들어있다면 true
    @GET("/order/findStore/{userId}/{storeId}")
    Call<List<OrderVO>> findStoreInCart(@Path("userId") int userId, @Path("storeId") int storeId);

    // 장바구니에 메뉴 추가
    @POST("/order/save")
    Call<OrderVO> addMenu(@Body OrderVO order);

    // 장바구니에 담긴 메뉴 가져옴
    @GET("/order/getOrderList/{userId}")
    Call<List<OrderVO>> getOrderList(@Path("userId") int userId);

    @GET("/order/contentNameList/{contentIdList}")
    Call<List<String>> getContentNameList(@Path("contentIdList") String contentIdList);
}
