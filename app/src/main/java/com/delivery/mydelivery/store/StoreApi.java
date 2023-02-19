package com.delivery.mydelivery.store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// 매장 Api
public interface StoreApi {

    // 카테고리를 통해 해당 카테고리에 해당하는 오픈한 매장 검색
    @GET("/store/open/{category}/{deliveryAvailablePlace}")
    Call<List<StoreVO>> getOpenedStoreList(@Path("category") String category, @Path("deliveryAvailablePlace") String deliveryAvailablePlace);

    // 카테고리를 통해 해당 카테고리에 해당하는 마감한 매장 검색
    @GET("/store/close/{category}/{deliveryAvailablePlace}")
    Call<List<StoreVO>> getClosedStoreList(@Path("category") String category, @Path("deliveryAvailablePlace") String deliveryAvailablePlace);

    // 매장 상세정보
    @GET("/store/detail/{storeId}")
    Call<StoreVO> getStore(@Path("storeId") int storeId);

    // 오픈한 매장 검색
    @GET("/store/search/open/{keyword}/{deliveryAvailablePlace}")
    Call<List<StoreVO>> searchOpenedStore(@Path("keyword") String keyword, @Path("deliveryAvailablePlace") String deliveryAvailablePlace);
}
