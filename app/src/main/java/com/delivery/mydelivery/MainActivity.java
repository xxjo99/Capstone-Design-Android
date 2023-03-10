package com.delivery.mydelivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.home.HomeActivity;
import com.delivery.mydelivery.login.ModifyPwCancelDialog;
import com.delivery.mydelivery.login.ModifyPwDialog;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.register.EmailRegisterActivity;
import com.delivery.mydelivery.login.LoginActivity;
import com.delivery.mydelivery.register.PrivacyRegisterActivity;
import com.delivery.mydelivery.register.RegisterDialog;

public class MainActivity extends AppCompatActivity {

    Button registerFormBtn; // 회원가입 이동버튼
    Button loginFormBtn; // 로그인 이동 버튼

    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity; // 현재 액티비티를 전역변수로 설정

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
