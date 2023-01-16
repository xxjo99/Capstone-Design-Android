package com.delivery.mydelivery.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    // 매장 이름
    @SuppressLint("StaticFieldLeak")
    public static TextView storeNameTV;

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView orderRecyclerView;
    OrderListAdapter orderListAdapter;
    List<OrderVO> orderList;

    // 총 금액 텍스트뷰, 글 등록 이동 버튼
    public static TextView totalPriceTV;
    Button uploadBtn;

    public static int totalPrice;

    // 레트로핏, api
    RetrofitService retrofitService;
    OrderApi api;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        context = this; // context 지정

        // 매장이름, 총 주문금액, 버튼
        storeNameTV = findViewById(R.id.storeNameTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        uploadBtn = findViewById(R.id.uploadBtn);

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

    // 장바구니 목록 불러옴
    private void setOrder(int userId) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(OrderApi.class);

        api.getOrderList(userId)
                .enqueue(new Callback<List<OrderVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<OrderVO>> call, @NonNull Response<List<OrderVO>> response) {
                        orderList = response.body();
                        orderListAdapter = new OrderListAdapter(orderList, context);
                        orderRecyclerView.setAdapter(orderListAdapter);

                        // 선택한 메뉴의 총 가격을 계산
                        totalPrice = 0;
                        for (OrderVO order : orderList) {
                            totalPrice += order.getTotalPrice();
                        }
                        totalPriceTV.setText(totalPrice + "원");
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<OrderVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 총 가격 수정
    public static void modifyPrice(int modifyPrice) {

    }
}