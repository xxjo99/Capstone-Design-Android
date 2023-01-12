package com.delivery.mydelivery.login;

import com.delivery.mydelivery.register.UserVO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// 로그인 Api
public interface LoginApi {

    // 아이디와 비밀번호로 로그인
    @GET("/login/{email}/{pw}")
    Call<UserVO> login(@Path("email") String email, @Path("pw") String pw);

}
