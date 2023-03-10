package com.delivery.mydelivery.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public FirebaseMessagingService(String email) {
        super();
        Task<String> tokenObject = FirebaseMessaging.getInstance().getToken();
        tokenObject.addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Log.d("FCM Token", task.getResult());
                setToken(email, task.getResult());
            }

        });
    }

    // 푸시메시지 수신시 할 작업 작성
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    // 새로운 토큰 생성되면 호출
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    public void setToken(String email, String token) {
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.saveToken(email, token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
    }

}
