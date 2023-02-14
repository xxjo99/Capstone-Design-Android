package com.delivery.mydelivery.point;

import com.delivery.mydelivery.order.OrderVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PointApi {

    // 충전내역 추가
    @POST("/point/add/history")
    Call<Void> addPointHistory(@Body PointHistoryVO pointHistory);

    // 포인트 이용내역
    @GET("/point/history/{userId}")
    Call<List<PointHistoryVO>> getPointHistory(@Path("userId") int userId);

}
