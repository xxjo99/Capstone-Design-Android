package com.delivery.mydelivery.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.login.LoginActivity;

public class CompleteActivity extends AppCompatActivity {

    Button loginBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);

        loginBtn = findViewById(R.id.loginBtn);

        // 로그인 이동
        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CompleteActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

}
