package com.delivery.mydelivery.home;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.delivery.mydelivery.R;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

public class ParticipationRestrictionDialog {

    private final Timestamp restrictionPeriod;
    private final Context context;

    public ParticipationRestrictionDialog(Timestamp restrictionPeriod, Context context) {
        this.restrictionPeriod = restrictionPeriod;
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_recruit_restriction);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        dialog.show();

        TextView restrictionTV = dialog.findViewById(R.id.restrictionTV);
        Button confirmBtn = dialog.findViewById(R.id.confirmBtn);

        // 시간 변환
        String restrictionPeriodString = changeDeliveryTime(restrictionPeriod) + " 까지\n이용이 불가능합니다.";
        restrictionTV.setText(restrictionPeriodString);

        confirmBtn.setOnClickListener(view -> dialog.dismiss());

    }

    // 시간 변환
    private String changeDeliveryTime(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        int month = localDateTime.getMonthValue(); // 월
        int day = localDateTime.getDayOfMonth(); // 일
        int hour = localDateTime.getHour(); // 시간
        int minute = localDateTime.getMinute(); // 분

        // 요일
        int dayOfWeek = localDateTime.getDayOfWeek().getValue();
        String dayOfWeekStr = "";
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeekStr = "일";
                break;
            case Calendar.MONDAY:
                dayOfWeekStr = "월";
                break;
            case Calendar.TUESDAY:
                dayOfWeekStr = "화";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekStr = "수";
                break;
            case Calendar.THURSDAY:
                dayOfWeekStr = "목";
                break;
            case Calendar.FRIDAY:
                dayOfWeekStr = "금";
                break;
            case Calendar.SATURDAY:
                dayOfWeekStr = "토";
                break;
        }

        return month + "/" + day + "(" + dayOfWeekStr + ") " + hour + "시 " + minute + "분";
    }
}
