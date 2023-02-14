package com.delivery.mydelivery.point;

import com.delivery.mydelivery.user.UserVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PointApi {

    // 충전내역 추가
    @POST("/point/add/history")
    Call<Void> addPointHistory(@Body PointHistoryVO pointHistory);

}
