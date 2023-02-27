package com.delivery.mydelivery.point;

import android.annotation.SuppressLint;
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

public class PointHistoryActivity extends AppCompatActivity {

    // 툴바, 뒤로가기 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    // 리사이클러뷰, 어댑터, 리스트, 레이아웃
    RecyclerView pointHistoryRecyclerView;
    PointHistoryAdapter pointHistoryAdapter;
    List<PointHistoryVO> pointHistoryList;
    LinearLayout emptyLayout;

    // 레트로핏, api
    RetrofitService retrofitService;
    PointApi pointApi;

    Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_history);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.pointHistoryToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 리사이클러뷰 설정
        pointHistoryRecyclerView = findViewById(R.id.pointHistoryRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        pointHistoryRecyclerView.setLayoutManager(manager);
        pointHistoryRecyclerView.setHasFixedSize(true);

        // 초기화
        emptyLayout = findViewById(R.id.emptyLayout);

        // 포인트 이용내역 추가
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);
        int userId = user.getUserId();
        getPointHistory(userId);

    }

    private void getPointHistory(int userId) {
        retrofitService = new RetrofitService();
        pointApi = retrofitService.getRetrofit().create(PointApi.class);

        pointApi.getPointHistory(userId)
                .enqueue(new Callback<List<PointHistoryVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<PointHistoryVO>> call, @NonNull Response<List<PointHistoryVO>> response) {
                        pointHistoryList = response.body();

                        if (Objects.requireNonNull(pointHistoryList).isEmpty()) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            pointHistoryRecyclerView.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                            pointHistoryRecyclerView.setVisibility(View.VISIBLE);

                            pointHistoryAdapter = new PointHistoryAdapter(pointHistoryList, context);
                            pointHistoryRecyclerView.setAdapter(pointHistoryAdapter);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<PointHistoryVO>> call, @NonNull Throwable t) {

                    }
                });

    }
}
