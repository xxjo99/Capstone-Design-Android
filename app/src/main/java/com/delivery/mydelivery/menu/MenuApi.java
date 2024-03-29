package com.delivery.mydelivery.menu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MenuApi {

    // storeId를 통해 매장의 메뉴카테고리 검색
    @GET("/menu/category/{storeId}")
    Call<List<MenuCategoryVO>> getMenuCategoryList(@Path("storeId") int storeId);

    // storeId, menuCategoryId를 통해 매장의 메뉴 검색
    @GET("/menu/{storeId}/{menuCategoryId}")
    Call<List<MenuVO>> getMenuList(@Path("storeId") int storeId, @Path("menuCategoryId") int menuCategoryId);

    // menuId를 통해 해당 메뉴의 옵션 종류를 검색
    @GET("/menu/option/{menuId}")
    Call<List<OptionVO>> getMenuOptionList(@Path("menuId") int menuId);

    // menuId를 통해 옵션의 내용을 가져옴
    @GET("/menu/option/content/{menuOptionId}")
    Call<List<OptionContentVO>> getMenuOptionContentList(@Path("menuOptionId") int menuOptionId);

    @GET("/menu/menus/{menuId}")
    Call<MenuVO> getMenu(@Path("menuId") int menuId);

}
