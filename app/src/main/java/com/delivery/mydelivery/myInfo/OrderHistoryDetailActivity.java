package com.delivery.mydelivery.myInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryDetailActivity extends AppCompatActivity {

    // 툴바, 뒤로가기 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    ImageView pictureIV;

    // 레트로핏, api
    RetrofitService retrofitService;
    OrderHistoryApi orderHistoryApi;

    Context context;

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

        // 상세 주문내역 추가
        Intent intent = getIntent();
        int recruitId = intent.getIntExtra("recruitId", 0);

        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);
        int userId = user.getUserId();
        getOrderHistoryDetail(recruitId, userId);
    }

    // 배달 완료 이미지 추가
    public void getOrderHistoryDetail(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        orderHistoryApi = retrofitService.getRetrofit().create(OrderHistoryApi.class);

        orderHistoryApi.getImage(recruitId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            byte[] byteImageArray = Objects.requireNonNull(response.body()).bytes();
                            Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteImageArray,0, byteImageArray.length);
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


}