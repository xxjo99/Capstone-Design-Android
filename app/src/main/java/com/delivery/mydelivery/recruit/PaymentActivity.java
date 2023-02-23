package com.delivery.mydelivery.recruit;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;

import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    TextView placeTV;
    TextView phoneNumTV;
    TextView orderPriceTV;
    TextView beforeDeliveryTipTV;
    TextView finalDeliveryTipTV;
    TextView finalPaymentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_payment);

        // 툴바
        toolbar = findViewById(R.id.paymentToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 전 액티비티에서 넘겨받은 값
        Intent intent = getIntent();
        String place = intent.getStringExtra("place");
        String phoneNum = intent.getStringExtra("phoneNum");
        String orderPrice = intent.getStringExtra("orderPrice");
        String deliveryTip = intent.getStringExtra("deliveryTip");
        String finalDeliveryTip = intent.getStringExtra("finalDeliveryTip");
        String finalPayment = intent.getStringExtra("finalPayment");

        System.out.println(place + " " + phoneNum + " " + orderPrice + " " + deliveryTip + " " + finalDeliveryTip + " " + finalPayment);

        // 초기화
        placeTV = findViewById(R.id.placeTV);
        phoneNumTV = findViewById(R.id.phoneNumTV);
        orderPriceTV = findViewById(R.id.orderPriceTV);
        beforeDeliveryTipTV = findViewById(R.id.beforeDeliveryTipTV);
        finalDeliveryTipTV = findViewById(R.id.finalDeliveryTipTV);
        finalPaymentTV = findViewById(R.id.finalPaymentTV);

        // 데이터 추가
        placeTV.setText(place);
        phoneNumTV.setText(phoneNum);
        orderPriceTV.setText(orderPrice);
        beforeDeliveryTipTV.setText(deliveryTip);
        beforeDeliveryTipTV.setPaintFlags(beforeDeliveryTipTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        finalDeliveryTipTV.setText(finalDeliveryTip);
        finalPaymentTV.setText(finalPayment);
    }
}
