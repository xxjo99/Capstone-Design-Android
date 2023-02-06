package com.delivery.mydelivery.order;

import com.delivery.mydelivery.recruit.RecruitVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderApi {

    // 사용자id, 매장id를 통해 장바구니에 다른 매장의 메뉴가 들어있는지 확인
    @GET("/order/stores/{userId}/{storeId}")
    Call<List<OrderVO>> findStoreInCart(@Path("userId") int userId, @Path("storeId") int storeId);

    // 장바구니에 메뉴 추가
    @POST("/order/save")
    Call<OrderVO> addMenu(@Body OrderVO order);

    // 장바구니에 담긴 메뉴 가져옴
    @GET("/order/orders/{userId}")
    Call<List<OrderVO>> getOrderList(@Path("userId") int userId);

    // 옵션 내용 검색
    @GET("/order/contents/{contentIdList}")
    Call<List<String>> getContentNameList(@Path("contentIdList") String contentIdList);

    // 개수 수정
    @POST("/order/modify/amount")
    Call<OrderVO> modifyAmount(@Body OrderVO order);

    // 삭제
    @DELETE("/order/delete/{orderId}")
    Call<Void> deleteOrder(@Path("orderId") int orderId);

    // 모집글 등록
    @POST("/order/register/recruit")
    Call<Void> registerRecruit(@Body RecruitVO recruit);
}
