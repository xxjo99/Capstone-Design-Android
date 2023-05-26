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

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SetTextI18n")
public class DeliveryTimePickerDialog {

    // 매장오픈시간
    Timestamp openTime;
    Timestamp closeTime;

    private final Context context;

    NumberPicker datePicker;
    TimePicker timePicker;
    Button confirmBtn;

    String date; // 선택된 날짜
    int selectedVal; // 선택한 날짜의 인덱스

    public DeliveryTimePickerDialog(Timestamp openTime, Timestamp closeTime, Context context) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_time_picker);
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
        int currentHour = today.getHour();
        timePicker.setIs24HourView(true);
        timePicker.setHour(currentHour);

        // datePicker 이벤트, 날짜 지정
        date = dateArr[0];
        selectedVal = 0;
        datePicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            date = dateArr[newVal];
            selectedVal = newVal;
        });

        dialog.show();

        // 확인 버튼
        confirmBtn.setOnClickListener(view -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String time = hour + "시 " + minute + "분";

            // db에 저장할 데이터
            int selectedMonth, selectedDay;

            if (selectedVal == 0) { // 오늘
                selectedMonth = todayMonth;
                selectedDay = todayDay;
            } else { // 내일
                selectedMonth = nextMonth;
                selectedDay = nextDay;
            }

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int currentYear = year - 1900; // date에 저장할 년도, 1900년 부터 시작

            Date selectedDate = new Date(currentYear, selectedMonth - 1, selectedDay, hour, minute);
            OrderListActivity.dateTime = new Timestamp(selectedDate.getTime()).toString();

            OrderListActivity.selectTimeTV.setText(date + " " + time);
            OrderListActivity.selectTimeTV.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });
    }

}
