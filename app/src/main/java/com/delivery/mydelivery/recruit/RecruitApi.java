package com.delivery.mydelivery.recruit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecruitApi {

    // 모든 모집글 조회
    @GET("/recruit/getAllRecruit")
    Call<List<RecruitVO>> getRecruitList();

    // 해당 사용자의 등록글 존재여부 검색
    @GET("/recruit/findRecruit/user/{userId}")
    Call<Boolean> findRecruit(@Path("userId") int userId);

    @GET("/recruit/getParticipantCount/{recruitId}")
    Call<Integer> getParticipantCount(@Path("recruitId") int recruitId);

}
