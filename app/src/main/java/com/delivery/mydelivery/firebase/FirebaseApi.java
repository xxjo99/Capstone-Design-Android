package com.delivery.mydelivery.firebase;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FirebaseApi {

    // 삭제 알림 전송
    @POST("/fcm/send/deliveryReception")
    Call<Void> sendMessageDeleteRecruit(@Query("recruitId") int recruitId);
}