package com.delivery.mydelivery.store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// 매장 Api
public interface StoreApi {

    // 카테고리를 통해 해당 카테고리에 해당하는 모든 매장 검색
    @GET("/store/{category}")
    Call<List<StoreVO>> getStoreList(@Path("category") String category);

    // 식별자를 통해 해당 매장의 상세정보 불러옴
    @GET("/store/detail/{storeId}")
    Call<StoreVO> getStore(@Path("storeId") int storeId);
}
