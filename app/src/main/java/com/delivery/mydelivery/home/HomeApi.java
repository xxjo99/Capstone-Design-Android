package com.delivery.mydelivery.home;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HomeApi {

    // 카테고리 목록을 가져옴
    @GET("/home/categoryList")
    Call<List<CategoryVO>> getCategoryList();

}
