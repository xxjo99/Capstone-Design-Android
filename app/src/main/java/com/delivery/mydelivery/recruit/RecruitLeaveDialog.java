package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class RecruitLeaveDialog {

    int recruitId; // 모집글 아이디
    int userId; // 사용자 아이디
    private final Context context;

    // 레트로핏, api
    RetrofitService retrofitService;
    RecruitApi recruitApi;

    public RecruitLeaveDialog(Context context, int recruitId, int userId) {
        this.context = context;
        this.recruitId = recruitId;
        this.userId = userId;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_leave);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        Button leaveBtn = dialog.findViewById(R.id.leaveBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

        leaveBtn.setOnClickListener(view -> {
            leaveRecruit(recruitId, userId);
            dialog.dismiss();
            ((Activity) context).finish();
        });

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }

    public void leaveRecruit(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.leaveRecruit(recruitId, userId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Toast.makeText(context, "탈퇴완료", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });

    }
}
