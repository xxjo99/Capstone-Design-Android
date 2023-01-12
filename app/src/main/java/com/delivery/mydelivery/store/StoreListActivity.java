package com.delivery.mydelivery.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.home.CategoryVO;
import com.delivery.mydelivery.home.HomeApi;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 매장 리스트 액티비티
public class StoreListActivity extends AppCompatActivity {

    // 카테고리 리사이클러뷰, 어댑터, 리스트
    RecyclerView categoryView;
    StoreCategoryAdapter storeCategoryAdapter;
    List<CategoryVO> categoryList;

    // 매장 리사이클러뷰, 어댑터, 리스트
    ListView storeListView;
    StoreListAdapter storeListadapter;
    List<StoreVO> storeList;

    // 레트로핏, api
    RetrofitService retrofitService;
    HomeApi homeApi;
    StoreApi storeApi;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        context = this; // context 지정

        // 리사이클러뷰 가로뷰로 설정
        categoryView = findViewById(R.id.categoryRecyclerView);
        categoryView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        // 카테고리 어댑터 추가, 카테고리 리스트 출력
        storeCategoryAdapter = new StoreCategoryAdapter();
        categoryView.setAdapter(storeCategoryAdapter);
        setCategory();

        storeListView = findViewById(R.id.storeListView);

        // 전 액티비티에서 눌러진 카테고리 데이터를 받아와서 카테고리에 맞는 매장 리스트를 출력
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        setStoreList(category); // 매장 리스트 가져오는 메소드

        // 카테고리 클릭 이벤트 구현
        storeCategoryAdapter.setOnItemClickListener(new StoreCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                setStoreList(categoryList.get(position).getCategoryName());
            }
        });

    }

    // 카테고리 리스트 가져오는 api
    private void setCategory() {
        // 레트로핏, api 초기화
        retrofitService = new RetrofitService();
        homeApi = retrofitService.getRetrofit().create(HomeApi.class);

        homeApi.getCategoryList()
                .enqueue(new Callback<List<CategoryVO>>() {
                    @Override
                    public void onResponse(Call<List<CategoryVO>> call, Response<List<CategoryVO>> response) {
                        categoryList = response.body();
                        storeCategoryAdapter.setCategoryList(categoryList);
                    }

                    @Override
                    public void onFailure(Call<List<CategoryVO>> call, Throwable t) {
                    }
                });
    }

    // 매장 리스트 전환 메소드, api 호출
    public void setStoreList(String category) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStoreList(category)
                .enqueue(new Callback<List<StoreVO>>() {
                    @Override
                    public void onResponse(Call<List<StoreVO>> call, Response<List<StoreVO>> response) {
                        storeList = response.body(); // api를 통해 호출된 매장 리스트 저장
                        storeListadapter = new StoreListAdapter(context, storeList); // 어댑터에 매장 리스트 추가
                        storeListView.setAdapter(storeListadapter); // 리스트뷰에 어댑터 연결
                    }

                    @Override
                    public void onFailure(Call<List<StoreVO>> call, Throwable t) {

                    }
                });
    }
}
