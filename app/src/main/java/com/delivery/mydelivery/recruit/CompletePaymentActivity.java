package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.R;

public class CompletePaymentActivity extends AppCompatActivity {

    Button backBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_complete_payment);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());
    }
}
