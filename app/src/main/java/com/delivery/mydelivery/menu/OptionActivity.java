package com.delivery.mydelivery.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionActivity extends AppCompatActivity {

    ImageView menuIV;
    TextView menuNameTV;
    TextView menuInfoTV;

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView optionRecyclerView;
    OptionAdapter optionAdapter;
    List<OptionVO> optionList;

    // 레트로핏, api
    RetrofitService retrofitService;
    MenuApi api;

    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_option);
        context = this; // context 지정

        // 전 액티비티에서 넘겨받은 값
        Intent intent = getIntent();
        int menuId = intent.getIntExtra("menuId", 0); // 메뉴Id
        String menuImage = intent.getStringExtra("menuImageUrl"); // 메뉴 이미지 주소
        String menuName = intent.getStringExtra("menuName"); // 메뉴 이름
        String menuInfo = intent.getStringExtra("menuInfo"); // 메뉴 정보

        // xml 변수 초기화
        menuIV = findViewById(R.id.menuIV);
        menuNameTV = findViewById(R.id.menuNameTV);
        menuInfoTV = findViewById(R.id.menuInfoTV);

        // 메뉴 이미지, 이름, 설명 삽입
        Glide.with(this).load(menuImage).placeholder(R.drawable.ic_launcher_background).override(500, 500).into(menuIV);
        menuNameTV.setText(menuName);
        menuInfoTV.setText(menuInfo);

        // 리사이클러뷰 설정
        optionRecyclerView = findViewById(R.id.optionRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        optionRecyclerView.setLayoutManager(manager);
        optionRecyclerView.setHasFixedSize(true);

        // 데이터 추가, 어댑터 적용
        setOption(menuId);
    }

    // 옵션 목록을 가져오는 api
    private void setOption(int menuId) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(MenuApi.class);

        optionList = new ArrayList<>();

        api.getMenuOptionList(menuId)
                .enqueue(new Callback<List<OptionVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<OptionVO>> call, @NonNull Response<List<OptionVO>> response) {
                        optionList = response.body();
                        optionAdapter = new OptionAdapter(optionList, context);
                        optionRecyclerView.setAdapter(optionAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<OptionVO>> call, @NonNull Throwable t) {
                    }
                });
    }
}
