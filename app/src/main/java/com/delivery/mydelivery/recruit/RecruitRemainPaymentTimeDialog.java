package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.delivery.mydelivery.R;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@SuppressLint("SetTextI18n")
public class RecruitRemainPaymentTimeDialog {

    TextView remainPaymentTimeTV;

    RecruitVO recruit;
    private final Context context;

    Long remainTime;

    public RecruitRemainPaymentTimeDialog(RecruitVO recruit, Context context) {
        this.recruit = recruit;
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_remain_payment_time);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        dialog.show();

        remainPaymentTimeTV = dialog.findViewById(R.id.remainPaymentTimeTV);
        Button confirmBtn = dialog.findViewById(R.id.confirmBtn);

        remainTime = getRemainTime(); // 남은시간 밀리초
        startTimer();

        // 다이얼로그 종료, 현재 액티비티 종료
        confirmBtn.setOnClickListener(view -> {
            dialog.dismiss();
            ((Activity) context).finish();
        });
    }

    // 남은시간 밀리초로 변환해서 반환
    private long getRemainTime() {
        Timestamp timestamp = recruit.getDeliveryTime();
        LocalDateTime deliveryTime = timestamp.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime(); // 배달시간
        LocalDateTime currentTime = LocalDateTime.now(); // 현재시간

        // 남은시간, 분, 초를 밀리초로 변경 후 반환
        Duration remainPaymentTime = Duration.between(currentTime, deliveryTime);
        long minute = remainPaymentTime.toMinutes();
        long seconds = (remainPaymentTime.toMillis() / 1000) % 60;
        return (minute * 60 * 1000) + (seconds * 1000);
    }

    // 남은시간 타이머
    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(remainTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainTime = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    //시간업데이트
    private void updateTimer() {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainTime); // 밀리초를 분으로 변환
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainTime) % 60; // 밀리초를 초로 변환 후,

        String timeLeftText = "결제 마감까지\n" + minutes + "분 " + seconds + "초 남았습니다.";

        remainPaymentTimeTV.setText(timeLeftText);
    }
}