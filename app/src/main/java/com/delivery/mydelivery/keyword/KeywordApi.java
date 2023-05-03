package com.delivery.mydelivery.keyword;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KeywordApi {

    // 키워드 등록
    @POST("/keyword/add/{userId}/{keyword}")
    Call<Void> addKeyword(@Path("userId") int userId, @Path("keyword") String keyword);

    // 키워드 삭제
    @DELETE("/keyword/delete/{userId}/{keyword}")
    Call<Void> deleteKeyword(@Path("userId") int userId, @Path("keyword") String keyword);

    // 등록한 키워드 조회
    @GET("/keyword/list/{userId}")
    Call<List<KeywordVO>> getKeywordList(@Path("userId") int userId);

}
