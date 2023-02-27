package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class RecruitLeaveDialog {

    int recruitId; // 모집글 아이디
    int userId; // 사용자 아이디
    int deductPoint; // 배달비만큼의 차감될 포인트
    private final Context context;

    // 레트로핏, api
    RetrofitService retrofitService;
    RecruitApi recruitApi;
    UserApi userApi;

    public RecruitLeaveDialog(Context context, int recruitId, int userId, int deductPoint) {
        this.context = context;
        this.recruitId = recruitId;
        this.userId = userId;
        this.deductPoint = deductPoint;
    }

    public void callDialog(int leaveType) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_leave);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        TextView penaltyTV = dialog.findViewById(R.id.penaltyTV);
        if (leaveType == 1) { // 현재시간이 배달시간 이후일경우, 패널티는 포인트 차감
            penaltyTV.setText("탈퇴하시겠습니까?\n탈퇴 시 배달팁 만큼의\n포인트가 차감됩니다.");
        } else { // 현재시간이 배달시간 이전일경우, 패널티는 이용제한
            penaltyTV.setText("탈퇴하시겠습니까?\n탈퇴 후 24시간 동안\n참가와 등록이 제한됩니다.");
        }

        dialog.show();

        Button leaveBtn = dialog.findViewById(R.id.leaveBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

        leaveBtn.setOnClickListener(view -> leaveRecruit(recruitId, userId, deductPoint, leaveType, dialog));

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }

    public void leaveRecruit(int recruitId, int userId, int deductPoint, int leaveType, Dialog dialog) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.leaveRecruit(recruitId, userId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                        if (leaveType == 0) { // 참가제한
                            setParticipationRestriction(userId);
                        } else { // 포인트 차감
                            deductPoint(userId, deductPoint);
                        }

                        Toast.makeText(context, "탈퇴완료", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        ((Activity) context).finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
    }

    // 참가제한 생성
    public void setParticipationRestriction(int userId) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.setParticipationRestriction(userId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }

    // 포인트 차감
    public void deductPoint(int userId, int deductPoint) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.deductPoint(userId, deductPoint)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }
}
