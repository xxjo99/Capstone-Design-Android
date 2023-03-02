package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class RecruitDeleteDialog {

    int recruitId; // 모집글 아이디
    int userId; // 사용자 아이디
    int participantCount; // 참가자 수
    private final Context context;

    // 레트로핏, api
    RetrofitService retrofitService;
    RecruitApi recruitApi;
    UserApi userApi;

    public RecruitDeleteDialog(Context context, int recruitId, int userId, int participantCount) {
        this.context = context;
        this.recruitId = recruitId;
        this.userId = userId;
        this.participantCount = participantCount;
    }

    public void callDialog(int deleteType) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        TextView penaltyTV = dialog.findViewById(R.id.penaltyTV);
        if (deleteType == 0) { // 참가인원 2인 미만, 패널티 x
            penaltyTV.setText("삭제하시겠습니까?");
        } else { // 2인 이상, 패널티 o
            penaltyTV.setText("삭제하시겠습니까?\n삭제 후 24시간 동안\n참가와 등록이 제한됩니다.");
        }

        dialog.show();

        Button deleteBtn = dialog.findViewById(R.id.deleteBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

        deleteBtn.setOnClickListener(view -> deleteRecruit(recruitId, userId, deleteType, dialog));

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }

    public void deleteRecruit(int recruitId, int userId, int deleteType, Dialog dialog) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.deleteRecruit(recruitId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                        // 2인 이상일경우 참가제한 생성
                        if (deleteType != 0) {
                            setParticipationRestriction(userId);
                        }

                        dialog.dismiss();
                        createDialog(deleteType);
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

    // 삭제완료 안내 다이얼로그 생성, 패널티 구분
    private void createDialog(int type) {
        RecruitDeleteNoticeDialog dialog = new RecruitDeleteNoticeDialog(context);
        dialog.callDialog(type);
    }
}
