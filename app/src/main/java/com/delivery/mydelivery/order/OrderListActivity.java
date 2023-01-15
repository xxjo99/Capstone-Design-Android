package com.delivery.mydelivery.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.register.UserVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListActivity extends AppCompatActivity {
    TextView storeDetailTV; // 매장 상세정보

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView orderRecyclerView;
    OrderListAdapter orderListAdapter;
    List<OrderVO> orderList;

    // 레트로핏, api
    RetrofitService retrofitService;
    OrderApi api;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        context = this; // context 지정

        // 리사이클러뷰 설정
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        orderRecyclerView.setLayoutManager(manager);
        orderRecyclerView.setHasFixedSize(true);

        // 유저 id를 통해 해당 유저의 장바구니 리스트 가져옴
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);
        int userId = user.getUserId();

        // 담은 메뉴 가져옴
        setOrder(userId);
    }

    private void setOrder(int userId) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(OrderApi.class);

        api.getOrderList(userId)
                .enqueue(new Callback<List<OrderVO>>() {
                    @Override
                    public void onResponse(Call<List<OrderVO>> call, Response<List<OrderVO>> response) {
                        orderList = response.body();
                        orderListAdapter = new OrderListAdapter(orderList, context);
                        orderRecyclerView.setAdapter(orderListAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<OrderVO>> call, Throwable t) {

                    }
                });
    }
}