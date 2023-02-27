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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class RecruitDeleteDialog {

    int recruitId; // 모집글 아이디
    int participantCount; // 참가자 수
    private final Context context;

    // 레트로핏, api
    RetrofitService retrofitService;
    RecruitApi recruitApi;

    public RecruitDeleteDialog(Context context, int recruitId, int participantCount) {
        this.context = context;
        this.recruitId = recruitId;
        this.participantCount = participantCount;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        dialog.show();

        Button deleteBtn = dialog.findViewById(R.id.deleteBtn);
        Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

        deleteBtn.setOnClickListener(view -> {
            deleteRecruit(recruitId);
            dialog.dismiss();
            ((Activity) context).finish();
        });

        cancelBtn.setOnClickListener(view -> dialog.dismiss());
    }

    public void deleteRecruit(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.deleteRecruit(recruitId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Toast.makeText(context, "삭제완료", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
    }
}
