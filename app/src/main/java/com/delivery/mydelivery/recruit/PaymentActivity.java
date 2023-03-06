package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.point.PointActivity;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class PaymentActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    // 남은 결제시간 다이얼로그
    RecruitRemainPaymentTimeDialog remainPaymentTimeDialog;

    TextView placeTV;
    TextView phoneNumTV;

    TextView orderPriceTV;
    TextView beforeDeliveryTipTV;
    TextView finalDeliveryTipTV;
    TextView finalPaymentTV;

    TextView pointTV;

    TextView paymentTV;
    LinearLayout addPointLayout;
    TextView addPointTV;
    TextView remainPointTV;

    Button paymentBtn;

    int addPoint; // 충전할 포인트

    Context context;

    // 레트로핏, api, gson
    RetrofitService retrofitService;
    RecruitApi recruitApi;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_payment);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.paymentToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        backBtn = findViewById(R.id.backBtn); // 뒤로가기 버튼

        // 전 액티비티에서 넘겨받은 값
        Intent intent = getIntent();
        int recruitId = intent.getIntExtra("recruitId", 0);
        String storeName = intent.getStringExtra("storeName");
        String place = intent.getStringExtra("place");
        String phoneNum = intent.getStringExtra("phoneNum");
        String orderPrice = intent.getStringExtra("orderPrice");
        String deliveryTip = intent.getStringExtra("deliveryTip");
        String finalDeliveryTip = intent.getStringExtra("finalDeliveryTip");
        String finalPayment = intent.getStringExtra("finalPayment");

        // 남은시간 다이얼로그 생성
        backBtn.setOnClickListener(view -> createRemainPaymentTimeDialog(recruitId));

        // 유저 정보
        String loginInfo = PreferenceManager.getLoginInfo(this);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 초기화
        placeTV = findViewById(R.id.placeTV);
        phoneNumTV = findViewById(R.id.phoneNumTV);

        orderPriceTV = findViewById(R.id.orderPriceTV);
        beforeDeliveryTipTV = findViewById(R.id.beforeDeliveryTipTV);
        finalDeliveryTipTV = findViewById(R.id.finalDeliveryTipTV);
        finalPaymentTV = findViewById(R.id.finalPaymentTV);

        pointTV = findViewById(R.id.pointTV);

        paymentTV = findViewById(R.id.paymentTV);
        addPointLayout = findViewById(R.id.addPointLayout);
        addPointTV = findViewById(R.id.addPointTV);
        remainPointTV = findViewById(R.id.remainPointTV);

        paymentBtn = findViewById(R.id.paymentBtn);

        // 데이터 추가
        placeTV.setText(place);
        phoneNumTV.setText(phoneNum);

        orderPriceTV.setText(orderPrice);
        beforeDeliveryTipTV.setText(deliveryTip);
        beforeDeliveryTipTV.setPaintFlags(beforeDeliveryTipTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        finalDeliveryTipTV.setText(finalDeliveryTip);
        finalPaymentTV.setText(finalPayment);

        pointTV.setText(user.getPoint() + "P");

        int finalPaymentInt = Integer.parseInt(finalPayment.substring(0, finalPayment.length() - 1));
        paymentTV.setText(finalPaymentInt + "P");

        if (user.getPoint() >= finalPaymentInt) {
            addPointLayout.setVisibility(View.GONE);
            remainPointTV.setText(user.getPoint() - finalPaymentInt + "P");

            paymentBtn.setText("결제하기");
        } else {
            addPointLayout.setVisibility(View.VISIBLE);
            addPoint = finalPaymentInt - user.getPoint();

            if (addPoint < 1000) {
                addPoint = 1000;
                addPointTV.setText(addPoint + "P");
            } else {
                addPointTV.setText(addPoint + "P");
            }
            remainPointTV.setText((user.getPoint() + addPoint) - finalPaymentInt + "P");

            paymentBtn.setText("충전하기");
        }

        // 결제, 충전 버튼
        paymentBtn.setOnClickListener(view -> {
            String type = paymentBtn.getText().toString();

            if (type.equals("충전하기")) {
                Intent paymentIntent = new Intent(PaymentActivity.this, PointActivity.class);
                paymentIntent.putExtra("addPoint", addPoint);
                startActivity(paymentIntent);
            } else { // 결제
                payment(recruitId, user.getUserId(), finalPaymentInt, storeName, user);
            }

        });

    }

    // 남은 결제 시간 다이얼로그 생성
    private void createRemainPaymentTimeDialog(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getRecruit(recruitId)
                .enqueue(new Callback<RecruitVO>() {
                    @Override
                    public void onResponse(@NonNull Call<RecruitVO> call, @NonNull Response<RecruitVO> response) {
                        RecruitVO recruit = response.body();
                        remainPaymentTimeDialog = new RecruitRemainPaymentTimeDialog(recruit, context);
                        remainPaymentTimeDialog.callDialog();
                    }

                    @Override
                    public void onFailure(@NonNull Call<RecruitVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 결제
    private void payment(int recruitId, int userId, int usedPoint, String content, UserVO user) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.payment(recruitId, userId, usedPoint, content)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        user.setPoint(user.getPoint() - usedPoint);
                        gson = new GsonBuilder().create();
                        String userInfoJson = gson.toJson(user, UserVO.class);
                        PreferenceManager.setLoginInfo(context, userInfoJson);

                        Intent completePaymentIntent = new Intent(PaymentActivity.this, CompletePaymentActivity.class);
                        startActivity(completePaymentIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });

    }

    // 재시작
    @Override
    protected void onRestart() {
        finish();
        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);

        super.onRestart();
    }
}