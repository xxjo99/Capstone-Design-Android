package com.delivery.mydelivery.user;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {

    @GET("/user/findUser/{userId}")
    Call<UserVO> getUser(@Path("userId") int userId);

}
