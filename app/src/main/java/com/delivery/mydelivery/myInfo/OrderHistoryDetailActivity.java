package com.delivery.mydelivery.myInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryDetailActivity extends AppCompatActivity {

    // 툴바, 뒤로가기 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    TextView deliveryDateTV;
    ImageView pictureIV;
    TextView storeNameTV;
    TextView participantCountTV;
    TextView paymentMoneyTV;
    TextView saveMoneyTV;

    // 리사이클러뷰, 어댑터, 리스트, 레이아웃
    RecyclerView orderHistoryRecyclerView;
    OrderHistoryDetailAdapter orderHistoryDetailAdapter;
    List<OrderHistoryDetailVO> orderHistoryDetailList;

    // 레트로핏, api
    RetrofitService retrofitService;
    OrderHistoryApi orderHistoryApi;

    Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_order_history_detail);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.orderHistoryToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 초기화
        pictureIV = findViewById(R.id.pictureIV);
        deliveryDateTV = findViewById(R.id.deliveryDateTV);
        storeNameTV = findViewById(R.id.storeNameTV);
        participantCountTV = findViewById(R.id.participantCountTV);
        paymentMoneyTV = findViewById(R.id.paymentMoneyTV);
        saveMoneyTV = findViewById(R.id.saveMoneyTV);

        // 매장정보, 상세내역 추가
        Intent intent = getIntent();
        int recruitId = intent.getIntExtra("recruitId", 0);
        String deliveryDate = intent.getStringExtra("deliveryDate");
        String storeName = intent.getStringExtra("storeName");
        int participantCount = intent.getIntExtra("participantCount", 0);
        String paymentMoney = intent.getStringExtra("paymentMoney");

        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);
        int userId = user.getUserId();

        getOrderHistoryImage(recruitId, userId); // 배달 완료 이미지
        deliveryDateTV.setText(deliveryDate + " 배달완료");
        storeNameTV.setText(storeName);
        participantCountTV.setText(participantCount + "명");
        paymentMoneyTV.setText(paymentMoney + "P");

        setSaveMoney(recruitId, userId); // 절약된 배달비

        // 리사이클러뷰 설정
        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        orderHistoryRecyclerView.setLayoutManager(manager);
        orderHistoryRecyclerView.setHasFixedSize(true);

        setOrderHistoryDetail(recruitId, userId);
    }

    // 배달 완료 이미지 추가
    private void getOrderHistoryImage(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        orderHistoryApi = retrofitService.getRetrofit().create(OrderHistoryApi.class);

        orderHistoryApi.getImage(recruitId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            byte[] byteImageArray = Objects.requireNonNull(response.body()).bytes();
                            Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteImageArray, 0, byteImageArray.length);
                            pictureIV.setImageBitmap(imageBitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    }
                });
    }

    // 절약한 배달비
    private void setSaveMoney(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        orderHistoryApi = retrofitService.getRetrofit().create(OrderHistoryApi.class);

        orderHistoryApi.getSaveMoney(recruitId, userId)
                .enqueue(new Callback<>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        String saveMoney = numberFormat.format(response.body());
                        saveMoneyTV.setText(saveMoney + "원");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });
    }

    // 상세주문내역 추가
    private void setOrderHistoryDetail(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        orderHistoryApi = retrofitService.getRetrofit().create(OrderHistoryApi.class);

        orderHistoryApi.getOrderHistoryDetail(recruitId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<OrderHistoryDetailVO>> call, @NonNull Response<List<OrderHistoryDetailVO>> response) {
                        orderHistoryDetailList = response.body();
                        orderHistoryDetailAdapter = new OrderHistoryDetailAdapter(orderHistoryDetailList, context);
                        orderHistoryRecyclerView.setAdapter(orderHistoryDetailAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<OrderHistoryDetailVO>> call, @NonNull Throwable t) {

                    }
                });
    }


}