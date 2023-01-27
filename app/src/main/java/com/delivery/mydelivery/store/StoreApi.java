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

    // 식별자를 통해 해당 매장검색
    @GET("/store/detail/{storeId}")
    Call<StoreVO> getStore(@Path("storeId") int storeId);
}
