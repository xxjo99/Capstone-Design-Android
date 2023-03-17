package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.menu.MenuListActivity;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class RecruitOrderListActivity extends AppCompatActivity {

    // 툴바
    Toolbar toolbar;
    ImageButton backBtn;

    // 레이아웃
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout emptyLayout;
    @SuppressLint("StaticFieldLeak")
    public static NestedScrollView orderListLayout;

    // 매장이름, 리사이클러뷰, 메뉴 리스트 버튼, 메뉴 추가버튼, 총 금액
    TextView storeNameTV;
    RecyclerView recruitOrderListRecyclerView;
    Button menuListBtn;
    Button addMenuBtn;
    @SuppressLint("StaticFieldLeak")
    public static TextView totalPriceTV;

    public static int totalPrice;

    // 담은 메뉴 리스트, 어댑터
    List<ParticipantOrderVO> orderList;
    RecruitOrderListAdapter recruitOrderListAdapter;

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    RecruitApi recruitApi;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_order_list);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.recruitOrderListToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 모집글 아이디, 매장 아이디, 유저 아이디
        Intent intent = getIntent();
        int recruitId = intent.getIntExtra("recruitId", 0);
        int storeId = intent.getIntExtra("storeId", 0);
        int userId = intent.getIntExtra("userId", 0);
        int paymentStatus = intent.getIntExtra("paymentStatus", 0);

        // 초기화
        emptyLayout = findViewById(R.id.emptyLayout);
        orderListLayout = findViewById(R.id.orderListLayout);

        storeNameTV = findViewById(R.id.storeNameTV);
        recruitOrderListRecyclerView = findViewById(R.id.recruitOrderListRecyclerView);
        menuListBtn = findViewById(R.id.menuListBtn);
        addMenuBtn = findViewById(R.id.addMenuBtn);
        totalPriceTV = findViewById(R.id.totalPriceTV);

        // 매장이름 추가
        setStoreName(storeId);

        if (paymentStatus == 1) {
            addMenuBtn.setVisibility(View.GONE);
        }

        // 리사이클러뷰 설정
        recruitOrderListRecyclerView = findViewById(R.id.recruitOrderListRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recruitOrderListRecyclerView.setLayoutManager(manager);
        recruitOrderListRecyclerView.setHasFixedSize(true);

        // 해당 모집글에서 유저가 담은 메뉴 리스트 추가
        setOrder(recruitId, userId, paymentStatus);

        // 메뉴 리스트 이동 버튼
        menuListBtn.setOnClickListener(view -> {
            Intent storeMoveIntent = new Intent(RecruitOrderListActivity.this, MenuListActivity.class);
            storeMoveIntent.putExtra("participantType", "참가자");
            storeMoveIntent.putExtra("storeId", storeId);
            storeMoveIntent.putExtra("recruitId", recruitId);
            startActivity(storeMoveIntent);
        });

        // 메뉴 추가, 해당 매장으로 이동
        addMenuBtn.setOnClickListener(view -> {
            Intent storeMoveIntent = new Intent(RecruitOrderListActivity.this, MenuListActivity.class);
            storeMoveIntent.putExtra("participantType", "참가자");
            storeMoveIntent.putExtra("storeId", storeId);
            storeMoveIntent.putExtra("recruitId", recruitId);
            startActivity(storeMoveIntent);
        });

    }

    // 매장이름추가
    private void setStoreName(int storeId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();
                        assert store != null;
                        storeNameTV.setText(store.getStoreName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {
                    }
                });
    }

    // 리사이클러뷰에 메뉴 목록 추가
    private void setOrder(int recruitId, int userId, int paymentStatus) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getOrderList(recruitId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ParticipantOrderVO>> call, @NonNull Response<List<ParticipantOrderVO>> response) {
                        orderList = response.body();

                        // 장바구니가 비어있는지 아닌지 확인
                        assert orderList != null;
                        if (orderList.isEmpty()) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            recruitOrderListRecyclerView.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                            recruitOrderListRecyclerView.setVisibility(View.VISIBLE);

                            recruitOrderListAdapter = new RecruitOrderListAdapter(orderList, paymentStatus, context);
                            recruitOrderListRecyclerView.setAdapter(recruitOrderListAdapter);

                            // 총 금액 계산
                            totalPrice = 0;
                            for (ParticipantOrderVO participantOrderVO : orderList) {
                                totalPrice += participantOrderVO.getTotalPrice();
                            }
                            totalPriceTV.setText(totalPrice + "원");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ParticipantOrderVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 새로고침
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
