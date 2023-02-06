package com.delivery.mydelivery.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    // 유저검색
    @GET("/user/findUser/{userId}")
    Call<UserVO> getUser(@Path("userId") int userId);

    // 학교 리스트 가져옴
    @GET("/user/getAllSchool")
    Call<List<String>> getAllSchool();

    // 인증번호 전송 - 비밀번호 찾기
    @GET("/user/findPw/{email}")
    Call<String> sendAuthNum(@Path("email") String email);

    // 이메일로 유저 검색
    @GET("/user/findUserByEmail/{email}")
    Call<UserVO> findUser(@Path("email") String email);

    // 비밀번호 수정
    @POST("/user/modifyPw")
    Call<UserVO> modifyPw(@Body UserVO user);
}
