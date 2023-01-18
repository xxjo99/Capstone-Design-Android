package com.delivery.mydelivery.home;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecruitApi {

    // 아이디와 비밀번호로 로그인
    @GET("/recruit/getAllRecruit")
    Call<List<RecruitVO>> getRecruitList();

}
