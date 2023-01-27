package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class RecruitActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    // 매장 정보
    ImageView storeIV;
    TextView storeNameTV;
    TextView deliveryTimeTV;
    TextView deliveryTipTV;
    TextView minimumDeliveryPriceTV;

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView memberRecyclerView;
    MemberAdapter memberAdapter;
    List<ParticipantVO> participantList;

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    RecruitApi recruitApi;

    // context
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_recruit);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.recruitToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 전 액티비티에서 넘어온 값
        Intent intent = getIntent();
        int storeId = intent.getIntExtra("storeId", 0);
        int recruitId = intent.getIntExtra("recruitId", 0);

        // 초기화
        storeIV = findViewById(R.id.storeIV);
        storeNameTV = findViewById(R.id.storeNameTV);
        deliveryTimeTV = findViewById(R.id.deliveryTimeTV);
        deliveryTipTV = findViewById(R.id.deliveryTipTV);
        minimumDeliveryPriceTV = findViewById(R.id.minimumDeliveryPriceTV);

        // 매장정보 등록
        setStore(storeId);

        // 리사이클러뷰 설정
        memberRecyclerView = findViewById(R.id.memberRecyclerView);
        RecyclerView.LayoutManager manager = new GridLayoutManager(context, 4);
        memberRecyclerView.setLayoutManager(manager);
        memberRecyclerView.setHasFixedSize(true);

        // 참가자 목록
        setMemberList(recruitId);

    }

    // 매장정보 생성
    private void setStore(int storeId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<StoreVO>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();

                        // 이미지
                        String text = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png";
                        int width = getWidth((Activity) context);
                        Glide.with(context).load(/*menuImage*/text).placeholder(R.drawable.ic_launcher_background).override(width, 600).into(storeIV);

                        assert store != null;
                        storeNameTV.setText(store.getStoreName());
                        deliveryTimeTV.setText(store.getDeliveryTime() + "분");
                        deliveryTipTV.setText(store.getDeliveryTip() + "원");
                        minimumDeliveryPriceTV.setText(store.getMinimumDeliveryPrice() + "원");
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 디바이스 넓이 구하기
    private int getWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();  // in Activity
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        return size.x;
    }

    // 참가자 리스트 생성
    private void setMemberList(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipantList(recruitId)
                .enqueue(new Callback<List<ParticipantVO>>() {
                    @Override
                    public void onResponse(Call<List<ParticipantVO>> call, Response<List<ParticipantVO>> response) {
                        participantList = response.body();
                        memberAdapter = new MemberAdapter(participantList, context);
                        memberRecyclerView.setAdapter(memberAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<ParticipantVO>> call, Throwable t) {

                    }
                });
    }
}