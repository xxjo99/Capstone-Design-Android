package com.delivery.mydelivery.store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// 매장 Api
public interface StoreApi {

    // 카테고리를 통해 해당 카테고리에 해당하는 모든 매장 검색
    @GET("/store/{category}/{deliveryAvailablePlace}")
    Call<List<StoreVO>> getStoreList(@Path("category") String category, @Path("deliveryAvailablePlace") String deliveryAvailablePlace);

    // 매장 상세정보
    @GET("/store/detail/{storeId}")
    Call<StoreVO> getStore(@Path("storeId") int storeId);

    // 매장 검색
    @GET("/store/search/{keyword}/{deliveryAvailablePlace}")
    Call<List<StoreVO>> searchStore(@Path("keyword") String keyword, @Path("deliveryAvailablePlace") String deliveryAvailablePlace);
}
