package com.delivery.mydelivery.recruit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecruitApi {

    // 모든 모집글 조회
    @GET("/recruit/recruits/{registrantPlace}")
    Call<List<RecruitVO>> getRecruitList(@Path("registrantPlace") String registrantPlace);

    // 해당 사용자의 등록글 존재여부 검색
    @GET("/recruit/find/user/{userId}")
    Call<Boolean> findRecruit(@Path("userId") int userId);

    // 모집글에 참가한 유저의 수
    @GET("/recruit/participants/count/{recruitId}")
    Call<Integer> getParticipantCount(@Path("recruitId") int recruitId);

    // 해당모집글에 해당 유저가 존재한다면 true 반환
    @GET("/recruit/find/user/{recruitId}/{userId}")
    Call<Boolean> findUserInRecruit(@Path("recruitId") int recruitId, @Path("userId") int userId);

    // 참가
    @POST("/recruit/participate")
    Call<ParticipantVO> participate(@Body ParticipantVO participant);

    // 모집글 정보
    @GET("/recruit/recruit/{recruitId}")
    Call<RecruitVO> getRecruit(@Path("recruitId") int recruitId);

    // 해당 유저가 참가한 글 검색
    @GET("/recruit/find/recruits/{userId}")
    Call<List<RecruitVO>> findRecruitList(@Path("userId") int userId);

    // 해당 글에 참가한 구성원 리스트 반환
    @GET("/recruit/participants/{recruitId}")
    Call<List<ParticipantVO>> getParticipantList(@Path("recruitId") int recruitId);

    // 해당 글의 사용자가 담은 메뉴의 총 금액 반환
    @GET("/recruit/price/{recruitId}/{participantId}")
    Call<Integer> getOrdersTotalPrice(@Path("recruitId") int recruitId, @Path("participantId") int participantId);

    // 자신을 제외한 나머지 참가자 리스트 반환
    @GET("/recruit/participants/except/{recruitId}/{userId}")
    Call<List<ParticipantVO>> getParticipantListExceptMine(@Path("recruitId") int recruitId, @Path("userId") int userId);

    // 최종결제금액
    @GET("/recruit/payment/{recruitId}/{storeId}/{userId}")
    Call<Integer> getFinalPayment(@Path("recruitId") int recruitId, @Path("storeId") int storeId, @Path("userId") int userId);

    // 해당 글에서 해당 유저가 담은 메뉴 반환
    @GET("/recruit/orders/{recruitId}/{participantId}")
    Call<List<ParticipantOrderVO>> getOrderList(@Path("recruitId") int recruitId, @Path("participantId") int participantId);

    // 개수 수정
    @POST("/recruit/modify/amount")
    Call<ParticipantOrderVO> modifyAmount(@Body ParticipantOrderVO order);

    // 삭제
    @DELETE("/recruit/delete/menu/{participantOrderId}")
    Call<Void> deleteOrder(@Path("participantOrderId") int participantOrderId);

    // 장바구니에 메뉴 추가
    @POST("/recruit/save")
    Call<ParticipantOrderVO> addMenu(@Body ParticipantOrderVO order);

    // 모집글 검색
    @GET("/recruit/search/{keyword}/{deliveryAvailablePlace}")
    Call<List<RecruitVO>> searchRecruit(@Path("keyword") String keyword, @Path("deliveryAvailablePlace") String deliveryAvailablePlace);

    // 모집글 삭제
    @DELETE("/recruit/delete/{recruitId}")
    Call<Void> deleteRecruit(@Path("recruitId") int recruitId);

    // 모집글 탈퇴
    @DELETE("/recruit/leave/{recruitId}/{userId}")
    Call<Void> leaveRecruit(@Path("recruitId") int recruitId, @Path("userId") int userId);

    // 등록자 검색
    @GET("/recruit/find/registrant/{recruitId}")
    Call<ParticipantVO> findRegistrant(@Path("recruitId") int recruitId);

    // 해당 모집글에 참가한 유저 반환
    @GET("recruit/get/participant/{recruitId}/{userId}")
    Call<ParticipantVO> getParticipant(@Path("recruitId") int recruitId, @Path("userId") int userId);

    // 결제하기
    @POST("/recruit/payment")
    Call<Void> payment(@Query("recruitId") int recruitId, @Query("userId") int userId, @Query("usedPoint") int usedPoint, @Query("content") String content);

    // 마감시간이 지나고, 결제가 완료되지 않은 유저가 있다면 포인트 차감 후 강퇴
    @POST("/recruit/check/paymentStatus")
    Call<Void> checkParticipantPaymentStatus(@Query("recruitId") int recruitId);

}
