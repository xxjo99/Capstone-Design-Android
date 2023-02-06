package com.delivery.mydelivery.recruit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecruitApi {

    // 모든 모집글 조회
    @GET("/recruit/getRecruitList/{registrantPlace}")
    Call<List<RecruitVO>> getRecruitList(@Path("registrantPlace") String registrantPlace);

    // 해당 사용자의 등록글 존재여부 검색
    @GET("/recruit/findRecruit/user/{userId}")
    Call<Boolean> findRecruit(@Path("userId") int userId);

    @GET("/recruit/getParticipantCount/{recruitId}")
    Call<Integer> getParticipantCount(@Path("recruitId") int recruitId);

    @GET("/recruit/findUser/{recruitId}/{userId}")
    Call<Boolean> findUserInRecruit(@Path("recruitId") int recruitId, @Path("userId") int userId);

    @POST("/recruit/participate")
    Call<ParticipantVO> participate(@Body ParticipantVO participant);

    // 해당 유저가 참가한 글 검색
    @GET("/recruit/findRecruitList/{userId}")
    Call<List<RecruitVO>> findRecruitList(@Path("userId") int userId);

    // 해당 글에 참가한 구성원 리스트 반환
    @GET("/recruit/getParticipantList/{recruitId}")
    Call<List<ParticipantVO>> getParticipantList(@Path("recruitId") int recruitId);

    // 해당 글의 사용자가 담은 메뉴의 총 금액 반환
    @GET("/recruit/getOrdersTotalPrice/{recruitId}/{participantId}")
    Call<Integer> getOrdersTotalPrice(@Path("recruitId") int recruitId, @Path("participantId") int participantId);

    // 자신을 제외한 나머지 참가자 리스트 반환
    @GET("/recruit/getParticipantListExceptMine/{recruitId}/{userId}")
    Call<List<ParticipantVO>> getParticipantListExceptMine(@Path("recruitId") int recruitId, @Path("userId") int userId);

    // 최종결제금액
    @GET("/recruit/finalPayment/{recruitId}/{storeId}/{userId}")
    Call<Integer> getFinalPayment(@Path("recruitId") int recruitId, @Path("storeId") int storeId, @Path("userId") int userId);

    // 해당 글에서 해당 유저가 담은 메뉴 반환
    @GET("/recruit/getOrderList/{recruitId}/{participantId}")
    Call<List<ParticipantOrderVO>> getOrderList(@Path("recruitId") int recruitId, @Path("participantId") int participantId);

    // 개수 수정
    @POST("/recruit/modifyAmount")
    Call<ParticipantOrderVO> modifyAmount(@Body ParticipantOrderVO order);

    // 삭제
    @DELETE("/recruit/delete/{participantOrderId}")
    Call<Void> deleteOrder(@Path("participantOrderId") int participantOrderId);

    // 장바구니에 메뉴 추가
    @POST("/recruit/save")
    Call<ParticipantOrderVO> addMenu(@Body ParticipantOrderVO order);

    // 모집글 검색
    @GET("/recruit/search/{keyword}/{deliveryAvailablePlace}")
    Call<List<RecruitVO>> searchRecruit(@Path("keyword") String keyword, @Path("deliveryAvailablePlace") String deliveryAvailablePlace);
}
