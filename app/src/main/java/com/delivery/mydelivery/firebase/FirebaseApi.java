package com.delivery.mydelivery.firebase;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FirebaseApi {

    // 알림 전송
    @POST("/firebase/fcm")
    Call<ResponseBody> pushMessage(@Body MessageDTO message);

    // 삭제 알림 전송
    @POST("/fcm/send/deliveryReception")
    Call<Void> sendMessageDeleteRecruit(@Query("recruitId") int recruitId);
}