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

import com.delivery.mydelivery.R;

import java.time.LocalDateTime;

@SuppressLint("SetTextI18n")
public class RecruitPaymentNoticeDialog {

    LocalDateTime paymentDeadline;
    private final Context context;

    public RecruitPaymentNoticeDialog(LocalDateTime paymentDeadline, Context context) {
        this.paymentDeadline = paymentDeadline;
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_paymnet_notice);
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

        String paymentDeadlineStr = changeDeliveryTime();
        penaltyTV.setText(paymentDeadlineStr);

        confirmBtn.setOnClickListener(view -> dialog.dismiss());
    }

    // 결제마감시간 변환
    private String changeDeliveryTime() {
        int hour = paymentDeadline.getHour(); // 시간
        int minute = paymentDeadline.getMinute(); // 분

        return hour + "시 " + minute + "분 까지 결제를 완료해주세요.";
    }
}