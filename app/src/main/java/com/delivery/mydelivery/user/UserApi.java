package com.delivery.mydelivery.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    // 유저검색
    @GET("/user/find/id/{userId}")
    Call<UserVO> getUser(@Path("userId") int userId);

    // 이름 검색, 중복된 이름 없다면 true 반환
    @GET("/user/check/name/{name}")
    Call<Boolean> findName(@Path("name") String name);

    // 학교 리스트 가져옴
    @GET("/user/schools")
    Call<List<String>> getAllSchool();

    // 인증번호 전송 - 비밀번호 찾기
    @GET("/user/pw/{email}")
    Call<String> sendAuthNum(@Path("email") String email);

    // 이메일로 유저 검색
    @GET("/user/find/email/{email}")
    Call<UserVO> findUser(@Path("email") String email);

    // 정보 수정
    @POST("/user/modify")
    Call<UserVO> modify(@Body UserVO user);

    // 토큰 저장
    @POST("/user/save/token")
    Call<Void> saveToken(@Query("email") String email, @Query("token") String token);

    // 토큰 삭제
    @POST("/user/delete/token")
    Call<Void> deleteToken(@Query("userId") int userId);

    // 이용제한 생성
    @POST("/user/restriction/set/period")
    Call<Void> setParticipationRestriction(@Query("userId") int userId);

    // 이용제한 반환
    @GET("/user/restriction/get/{userId}")
    Call<ParticipationRestrictionVO> getParticipationRestriction(@Path("userId") int userId);

    // 이용제한 지난지 확인, 지났으면 이용제한 제거
    @GET("/user/restriction/check/{userId}")
    Call<Boolean> checkRestriction(@Path("userId") int userId);

    // 포인트 차감
    @POST("/user/deduct/point")
    Call<Void> deductPoint(@Query("userId") int userId, @Query("deductPoint") int deductPoint);

}
