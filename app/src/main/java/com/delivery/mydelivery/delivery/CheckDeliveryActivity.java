package com.delivery.mydelivery.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;

import java.util.Objects;

public class CheckDeliveryActivity extends AppCompatActivity {


    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    Button checkDeliveryBtn; // 배달 확인
    Button receivedDeliveryBtn; // 접수한 배달 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_check_delivery);

        // 툴바
        toolbar = findViewById(R.id.deliveryToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 초기화
        checkDeliveryBtn = findViewById(R.id.checkDeliveryBtn);
        receivedDeliveryBtn = findViewById(R.id.receivedDeliveryBtn);

        // 배달 리스트 액티비티 이동
        checkDeliveryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CheckDeliveryActivity.this, ReceiptDeliveryActivity.class);
            startActivity(intent);
        });

        // 접수한 배달 리스트 액티비티 이동
        receivedDeliveryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CheckDeliveryActivity.this, ReceivedDeliveryActivity.class);
            startActivity(intent);
        });

    }
}
