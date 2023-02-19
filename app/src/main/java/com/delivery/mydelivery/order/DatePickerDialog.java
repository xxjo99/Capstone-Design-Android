package com.delivery.mydelivery.order;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.delivery.mydelivery.R;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@SuppressLint("SetTextI18n")
public class DatePickerDialog {

    private final Context context;

    NumberPicker datePicker;
    TimePicker timePicker;
    Button confirmBtn;

    String date; // 선택된 날짜

    public DatePickerDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_date_picker);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // 크기 지정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 초기화
        datePicker = dialog.findViewById(R.id.datePicker);
        timePicker = dialog.findViewById(R.id.timePicker);
        confirmBtn = dialog.findViewById(R.id.confirmBtn);

        // 오늘
        LocalDateTime today = LocalDateTime.now();
        int todayMonth = today.getMonthValue();
        int todayDay = today.getDayOfMonth();
        DayOfWeek daysOfWeek = today.getDayOfWeek();
        String dayOfWeek = daysOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        // 내일
        LocalDateTime nextDate = today.plusDays(1);
        int nextMonth = nextDate.getMonthValue();
        int nextDay = nextDate.getDayOfMonth();
        DayOfWeek daysOfWeekNext = nextDate.getDayOfWeek();
        String dayOfWeekNext = daysOfWeekNext.getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        String todayDate = todayMonth + "/" + todayDay + "(" + dayOfWeek + ")";
        String nextDateString = nextMonth + "/" + nextDay + "(" + dayOfWeekNext + ")";

        // datePicker 설정
        String[] dateArr = {todayDate, nextDateString};
        datePicker.setMinValue(0);
        datePicker.setMaxValue(1);
        datePicker.setDisplayedValues(dateArr);
        datePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // 타임피커 설정
        timePicker.setIs24HourView(true);
        timePicker.setHour(today.getHour());

        // datePicker 이벤트, 날짜 지정
        date = dateArr[0];
        datePicker.setOnValueChangedListener((picker, oldVal, newVal) -> date = dateArr[newVal]);

        dialog.show();

        // 확인 버튼
        confirmBtn.setOnClickListener(view -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String time = hour + "시 " + minute + "분";

            OrderListActivity.selectTimeTV.setText(date + " " + time);
            System.out.println(date + " " + time);
            OrderListActivity.selectTimeTV.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });
    }

}
