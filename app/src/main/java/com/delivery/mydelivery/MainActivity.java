package com.delivery.mydelivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.home.HomeActivity;
import com.delivery.mydelivery.home.ParticipateDialog;
import com.delivery.mydelivery.login.ModifyPwDialog;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.recruit.RecruitDeleteDialog;
import com.delivery.mydelivery.recruit.RecruitDeleteNoticeDialog;
import com.delivery.mydelivery.recruit.RecruitLeaveDialog;
import com.delivery.mydelivery.recruit.RecruitRemainPaymentTimeDialog;
import com.delivery.mydelivery.register.EmailRegisterActivity;
import com.delivery.mydelivery.login.LoginActivity;

import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity {

    Button registerFormBtn; // 회원가입 이동버튼
    Button loginFormBtn; // 로그인 이동 버튼

    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = MainActivity.this;

        // 로그인 정보가 있다면 어플 메인화면으로 이동
        if (!PreferenceManager.getLoginInfo(this).isEmpty()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        registerFormBtn = findViewById(R.id.registerFormBtn);
        loginFormBtn = findViewById(R.id.loginFormBtn);

        // 회원가입 액티비티 이동
        registerFormBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EmailRegisterActivity.class);
            startActivity(intent);
        });

        // 로그인 액티비티 이동
        loginFormBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}
