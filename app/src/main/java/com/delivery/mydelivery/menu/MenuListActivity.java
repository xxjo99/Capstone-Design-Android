package com.delivery.mydelivery.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 메뉴 리스트 액티비티
public class MenuListActivity extends AppCompatActivity {
    TextView storeDetailTV; // 매장 상세정보

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView menuRecyclerView;
    MenuListAdapter menuListAdapter;
    List<MenuVO> menuList;

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    MenuApi menuApi;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        context = this; // context 지정

        // 매장Id를 받아와서 매장 검색, 정보 출력
        Intent intent = getIntent();
        int storeId = intent.getIntExtra("storeId", 0);
        setStore(storeId);

        // 리사이클러뷰 설정
        menuRecyclerView = findViewById(R.id.menuListRecyclerView);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // 어댑터, 메뉴 추가
        menuListAdapter = new MenuListAdapter();
        menuRecyclerView.setAdapter(menuListAdapter);
        setMenu(storeId);
    }

    // 매장 상세정보 가져오는 Api
    private void setStore(int storeId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<StoreVO>() {
                    @Override
                    public void onResponse(Call<StoreVO> call, Response<StoreVO> response) {
                        StoreVO store = response.body();
                        storeDetailTV = findViewById(R.id.storeDetailTV);
                        storeDetailTV.setText(store.toString());
                    }

                    @Override
                    public void onFailure(Call<StoreVO> call, Throwable t) {
                    }
                });
    }

    // 매장의 메뉴 리스트를 가져오는 api
    private void setMenu(int storeId) {
        retrofitService = new RetrofitService();
        menuApi = retrofitService.getRetrofit().create(MenuApi.class);

        menuApi.getMenuList(storeId)
                .enqueue(new Callback<List<MenuVO>>() {
                    @Override
                    public void onResponse(Call<List<MenuVO>> call, Response<List<MenuVO>> response) {
                        menuList = response.body();
                        menuListAdapter.setMenuList(menuList);
                    }

                    @Override
                    public void onFailure(Call<List<MenuVO>> call, Throwable t) {
                    }
                });
    }

}