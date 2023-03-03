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
public class RecruitLeaveNoticeDialog {

    private final Context context;

    public RecruitLeaveNoticeDialog(Context context) {
        this.context = context;
    }

    // type == 0 : 참가제한, type == 1 : 포인트 차감
    public void callDialog(int type, int deductPoint) {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_leave_notice);
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

        // 포인트 차감
        if (type == 1) {
            penaltyTV.setText("탈퇴가 완료되었습니다.\n" + deductPoint + "P가 차감되었습니다.");
        }

        confirmBtn.setOnClickListener(view -> {
            dialog.dismiss();
            ((Activity) context).finish();
        });
    }
}