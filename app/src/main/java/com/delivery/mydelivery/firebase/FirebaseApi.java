package com.delivery.mydelivery.firebase;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FirebaseApi {

    // 알림 전송
    @POST("/firebase/fcm")
    Call<ResponseBody> pushMessage(@Body MessageDTO message);

}
