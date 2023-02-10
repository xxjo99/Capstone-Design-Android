package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.recruit.ParticipantVO;
import com.delivery.mydelivery.recruit.RecruitActivity;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class ParticipateDialog {

    private final Context context;

    private String storeName;
    private int currentPersonCount;
    private int recruitPerson;
    private String place;
    private String deliveryTime;
    private String deliveryTip;

    private int recruitId;
    private int userId;
    private int storeId;

    RetrofitService retrofitService;
    RecruitApi api;

    public ParticipateDialog(Context context) {
        this.context = context;
    }

    // 데이터 추가
    public void setData(String storeName, int currentPersonCount, int recruitPerson, String place, String deliveryTime, String deliveryTip, int recruitId, int userId, int storeId) {
        this.storeName = storeName;
        this.currentPersonCount = currentPersonCount;
        this.recruitPerson = recruitPerson;
        this.place = place;
        this.deliveryTime = deliveryTime;
        this.deliveryTip = deliveryTip;
        this.recruitId = recruitId;
        this.userId = userId;
        this.storeId = storeId;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        // dialog 설정
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_recruit_participate_dialog);
        dialog.show();

        // 초기화
        TextView storeNameTV = dialog.findViewById(R.id.storeNameTV);
        TextView recruitPersonTV = dialog.findViewById(R.id.recruitPersonTV);
        TextView placeTV = dialog.findViewById(R.id.placeTV);
        TextView deliveryTimeTV = dialog.findViewById(R.id.deliveryTimeTV);
        TextView deliveryTipTV = dialog.findViewById(R.id.deliveryTipTV);

        Button participateBtn = dialog.findViewById(R.id.participateBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

        LinearLayout participateBtnLayout = dialog.findViewById(R.id.participateBtnLayout);
        LinearLayout participateCompleteLayout = dialog.findViewById(R.id.participateCompleteLayout);

        // 데이터 셋팅
        storeNameTV.setText(storeName);
        recruitPersonTV.setText(currentPersonCount + " / " + recruitPerson);
        placeTV.setText(place);
        deliveryTimeTV.setText(deliveryTime);
        deliveryTipTV.setText(deliveryTip + "원");

        // 참가 가능 여부
        if (currentPersonCount == recruitPerson) {
            participateBtnLayout.setVisibility(View.GONE);
            participateCompleteLayout.setVisibility(View.VISIBLE);
        } else {
            participateBtnLayout.setVisibility(View.VISIBLE);
            participateCompleteLayout.setVisibility(View.GONE);
        }

        // 사용자 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 참가버튼
        participateBtn.setOnClickListener(view -> {

            int userPoint = user.getPoint();
            int deliveryTipInt = Integer.parseInt(deliveryTip);

            // 사용자가 가진 포인트가 해당 매장의 배달비보다 적다면 참여 불가
            if (userPoint < deliveryTipInt) {
                Toast.makeText(context, "참여불가", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                ParticipantVO participant = new ParticipantVO();

                participant.setRecruitId(recruitId);
                participant.setUserId(userId);
                participant.setParticipantType("participant");

                participate(participant, dialog);
            }
        });

        // 취소 버튼
        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }

    private void participate(ParticipantVO participant, Dialog dialog) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(RecruitApi.class);

        api.participate(participant)
                .enqueue(new Callback<ParticipantVO>() {
                    @Override
                    public void onResponse(@NonNull Call<ParticipantVO> call, @NonNull Response<ParticipantVO> response) {
                        Toast.makeText(context, "참가 완료", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        // 모집글 상세페이지 이동
                        Intent intent = new Intent(context, RecruitActivity.class);
                        intent.putExtra("recruitId", recruitId);
                        intent.putExtra("storeId", storeId);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ParticipantVO> call, @NonNull Throwable t) {

                    }
                });
    }
}
