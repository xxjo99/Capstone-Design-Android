package com.delivery.mydelivery.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {

    @GET("/user/findUser/{userId}")
    Call<UserVO> getUser(@Path("userId") int userId);

    @GET("/user/getAllSchool")
    Call<List<SchoolVO>> getAllSchool();

}
