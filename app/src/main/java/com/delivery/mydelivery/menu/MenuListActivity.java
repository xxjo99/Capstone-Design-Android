package com.delivery.mydelivery.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.order.OrderListActivity;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 메뉴 리스트 액티비티
@SuppressLint("SetTextI18n")
public class MenuListActivity extends AppCompatActivity {

    // 매장 정보
    ImageView storeIV;
    TextView storeNameTV;
    TextView deliveryTimeTV;
    TextView deliveryTipTV;
    TextView minimumDeliveryPriceTV;

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView menuRecyclerView;
    MenuCategoryListAdapter menuCategoryListAdapter;
    List<MenuCategoryVO> menuCategoryList;

    // 툴바, 툴바 텍스트 버튼
    Toolbar toolbar;
    ImageButton backBtn;
    TextView toolbarStoreNameTV;

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    MenuApi menuApi;

    public static String participantType; // 등록자, 참가자 구분
    public static int recruitId; // 모집글 아이디

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_menu_list);
        context = this; // context 지정

        // 툴바
        toolbar = findViewById(R.id.menuToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 매장Id를 받아와서 매장 검색, 정보 출력
        Intent intent = getIntent();
        int storeId = intent.getIntExtra("storeId", 0);
        participantType = intent.getStringExtra("participantType");
        recruitId = intent.getIntExtra("recruitId", 0);
        setStore(storeId);

        // 리사이클러뷰 설정
        menuRecyclerView = findViewById(R.id.menuListRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // 어댑터, 메뉴 추가
        menuCategoryListAdapter = new MenuCategoryListAdapter();
        menuRecyclerView.setAdapter(menuCategoryListAdapter);
        setMenuCategory(storeId);
    }

    // 매장 상세정보 Api
    private void setStore(int storeId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();

                        storeIV = findViewById(R.id.storeIV);
                        storeNameTV = findViewById(R.id.storeNameTV);
                        toolbarStoreNameTV = findViewById(R.id.toolbarStoreNameTV);
                        deliveryTimeTV = findViewById(R.id.deliveryTimeTV);
                        deliveryTipTV = findViewById(R.id.deliveryTipTV);
                        minimumDeliveryPriceTV = findViewById(R.id.minimumDeliveryPriceTV);

                        assert store != null;

                        Glide.with(context).load(store.getStoreImageUrl()).placeholder(R.drawable.ic_launcher_background).into(storeIV);
                        storeNameTV.setText(store.getStoreName());
                        toolbarStoreNameTV.setText(store.getStoreName());
                        deliveryTimeTV.setText(store.getDeliveryTime() + "분");
                        deliveryTipTV.setText(store.getDeliveryTip() + "원");
                        minimumDeliveryPriceTV.setText(store.getMinimumDeliveryPrice() + "원");
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {
                    }
                });
    }

    // 매장의 메뉴 카테고리 리스트 api
    private void setMenuCategory(int storeId) {
        retrofitService = new RetrofitService();
        menuApi = retrofitService.getRetrofit().create(MenuApi.class);

        menuApi.getMenuCategoryList(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<MenuCategoryVO>> call, @NonNull Response<List<MenuCategoryVO>> response) {
                        menuCategoryList = response.body();
                        menuCategoryListAdapter.setMenuCategoryList(menuCategoryList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<MenuCategoryVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 툴바 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // 툴바 메뉴 버튼 이벤트
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.cartBtn) {
            Intent intent = new Intent(this, OrderListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}