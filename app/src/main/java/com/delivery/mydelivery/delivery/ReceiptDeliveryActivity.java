package com.delivery.mydelivery.delivery;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptDeliveryActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    // 레이아웃
    LinearLayout emptyLayout;
    NestedScrollView deliveryScrollView;

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView deliveryListRecyclerView;
    ReceiptDeliveryListAdapter receiptDeliveryListAdapter;
    List<RecruitVO> recruitList;

    // 레트로핏, api
    RetrofitService retrofitService;
    DeliveryApi deliveryApi;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_receipt);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.deliveryToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 초기화
        emptyLayout = findViewById(R.id.emptyLayout);
        deliveryScrollView = findViewById(R.id.deliveryScrollView);

        // 리사이클러뷰 설정
        deliveryListRecyclerView = findViewById(R.id.deliveryListRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        deliveryListRecyclerView.setLayoutManager(manager);
        deliveryListRecyclerView.setHasFixedSize(true);

        // 접수가능한 배달 목록 추가
        setDeliveryList();
    }

    private void setDeliveryList() {
        retrofitService = new RetrofitService();
        deliveryApi = retrofitService.getRetrofit().create(DeliveryApi.class);

        deliveryApi.getDeliveryList()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RecruitVO>> call, @NonNull Response<List<RecruitVO>> response) {
                        recruitList = response.body();

                        if (Objects.requireNonNull(recruitList).isEmpty()) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            deliveryScrollView.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                            deliveryScrollView.setVisibility(View.VISIBLE);

                            receiptDeliveryListAdapter = new ReceiptDeliveryListAdapter(recruitList, context);
                            deliveryListRecyclerView.setAdapter(receiptDeliveryListAdapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RecruitVO>> call, @NonNull Throwable t) {

                    }
                });
    }

}
