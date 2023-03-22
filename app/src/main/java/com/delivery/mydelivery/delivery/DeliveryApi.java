package com.delivery.mydelivery.delivery;

import com.delivery.mydelivery.recruit.RecruitVO;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DeliveryApi {

    // 접수 가능한 주문 리스트
    @GET("/delivery/deliveryList")
    Call<List<RecruitVO>> getDeliveryList();

    // 배달 시작 알림 전송, 모집글 배달 상태 변경
    @POST("/delivery/start")
    Call<Void> receiptDelivery(@Query("recruitId") int recruitId);

    // 배달이 출발한 모집글 리스트
    @GET("/delivery/started/deliveryList")
    Call<List<RecruitVO>> getStartedDeliveryList();
    
    // 배달 완료 알림 전송, 배달 완료 처리
    @Multipart
    @POST("/delivery/complete")
    Call<Void> completeDelivery(@Query("recruitId") int recruitId, @Part MultipartBody.Part image);

}
