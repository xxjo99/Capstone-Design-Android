package com.delivery.mydelivery.register;

import com.delivery.mydelivery.user.UserVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

// 회원가입 Api
public interface RegisterApi {

    // 이메일 중복검사
    @GET("/register/registerCk/{email}")
    Call<Boolean> duplicateEmailCk(@Path("email") String email);

    // 인증번호 전송
    @GET("/register/sendAuthNum/{email}")
    Call<String> sendAuthNum(@Path("email") String email);

    // 인증번호 전송 - 비밀번호 찾기
    @GET("findPw/{email}")
    Call<String> sendAuthNumFind(@Path("email") String email);

    // 회원가입
    @POST("/register")
    Call<UserVO> register(@Body UserVO user);
}