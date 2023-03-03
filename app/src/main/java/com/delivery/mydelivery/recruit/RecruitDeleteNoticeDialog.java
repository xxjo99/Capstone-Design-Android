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

import com.delivery.mydelivery.R;

@SuppressLint("SetTextI18n")
public class RecruitDeleteNoticeDialog {

    private final Context context;

    public RecruitDeleteNoticeDialog(Context context) {
        this.context = context;
    }

    public void callDialog(int type) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_delete_notice);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        dialog.show();

        TextView penaltyTV = dialog.findViewById(R.id.penaltyTV);
        Button confirmBtn = dialog.findViewById(R.id.confirmBtn);

        // 패널티 유무 구분
        if (type == 0) {
            penaltyTV.setText("삭제가 완료되었습니다.");
        }

        confirmBtn.setOnClickListener(view -> {
            dialog.dismiss();
            ((Activity) context).finish();
        });
    }
}