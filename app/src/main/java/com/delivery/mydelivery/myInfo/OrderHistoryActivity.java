package com.delivery.mydelivery.myInfo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    // 툴바, 뒤로가기 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    // 리사이클러뷰, 어댑터, 리스트, 레이아웃
    RecyclerView orderHistoryRecyclerView;
    OrderHistoryAdapter orderHistoryAdapter;
    List<OrderHistoryVO2> orderHistoryList;
    LinearLayout emptyLayout;

    // 레트로핏, api
    RetrofitService retrofitService;
    OrderHistoryApi orderHistoryApi;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_order_history);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.orderHistoryToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 리사이클러뷰 설정
        orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        orderHistoryRecyclerView.setLayoutManager(manager);
        orderHistoryRecyclerView.setHasFixedSize(true);

        // 초기화
        emptyLayout = findViewById(R.id.emptyLayout);

        // 주문내역 추가
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);
        int userId = user.getUserId();
        getOrderHistory(userId);
    }

    private void getOrderHistory(int userId) {
        retrofitService = new RetrofitService();
        orderHistoryApi = retrofitService.getRetrofit().create(OrderHistoryApi.class);

        orderHistoryApi.getOrderHistory(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<OrderHistoryVO2>> call, @NonNull Response<List<OrderHistoryVO2>> response) {
                        orderHistoryList = response.body();

                        if (Objects.requireNonNull(orderHistoryList).isEmpty()) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            orderHistoryRecyclerView.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                            orderHistoryRecyclerView.setVisibility(View.VISIBLE);

                            orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryList, context);
                            orderHistoryRecyclerView.setAdapter(orderHistoryAdapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<OrderHistoryVO2>> call, @NonNull Throwable t) {
                    }
                });

    }

}