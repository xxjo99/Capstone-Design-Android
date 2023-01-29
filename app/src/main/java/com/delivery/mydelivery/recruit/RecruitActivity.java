package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

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
    RecyclerView memberDeliveryInfoRecyclerView;
    MemberDeliveryInfoAdapter memberDeliveryInfoAdapter;
    List<ParticipantVO> participantDeliveryInfoList;

    int participantCount; // 참가자수

    // 나의 배달 정보, 메뉴 확인 텍스트
    TextView userNameTV;
    TextView totalPriceTV;
    TextView checkMenuTV;

    // 결제금액계산
    TextView orderPriceTV;
    TextView beforeDeliveryTipTV;
    TextView finalDeliveryTipTV;
    TextView finalPaymentTV;

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

        // 사용자정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 전 액티비티에서 넘어온 값
        Intent intent = getIntent();
        int storeId = intent.getIntExtra("storeId", 0);
        int recruitId = intent.getIntExtra("recruitId", 0);

        // 매장정보 초기화
        storeIV = findViewById(R.id.storeIV);
        storeNameTV = findViewById(R.id.storeNameTV);
        deliveryTimeTV = findViewById(R.id.deliveryTimeTV);
        deliveryTipTV = findViewById(R.id.deliveryTipTV);
        minimumDeliveryPriceTV = findViewById(R.id.minimumDeliveryPriceTV);

        // 매장정보 등록
        setStore(storeId);

        // 리사이클러뷰 설정
        memberRecyclerView = findViewById(R.id.memberRecyclerView);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        memberRecyclerView.setLayoutManager(gridLayoutManager);
        memberRecyclerView.setHasFixedSize(true);

        // 참가자 목록 추가
        setMemberList(recruitId);

        // 나의 배달 정보 초기화
        userNameTV = findViewById(R.id.userNameTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        checkMenuTV = findViewById(R.id.checkMenuTV);

        userNameTV.setText(user.getName()); // 사용자 이름

        getOrdersTotalPrice(recruitId, user.getUserId()); // 총 주문금액

        // 리사이클러뷰 설정
        memberDeliveryInfoRecyclerView = findViewById(R.id.memberDeliveryInfoRecyclerView);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        memberDeliveryInfoRecyclerView.setLayoutManager(linearLayoutManager);
        memberDeliveryInfoRecyclerView.setHasFixedSize(true);

        // 자신을 제외한 참가자 정보
        getParticipantListExceptMine(recruitId, user.getUserId());

        // 결제금액계산 초기화
        orderPriceTV = findViewById(R.id.orderPriceTV);
        beforeDeliveryTipTV = findViewById(R.id.beforeDeliveryTipTV);
        finalDeliveryTipTV = findViewById(R.id.finalDeliveryTipTV);
        finalPaymentTV = findViewById(R.id.finalPaymentTV);

        // 최종금액계산 추가
        setPayment(storeId, recruitId, user.getUserId());
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
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size.x;
    }

    // 참가자 리스트 생성
    private void setMemberList(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipantList(recruitId)
                .enqueue(new Callback<List<ParticipantVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ParticipantVO>> call, @NonNull Response<List<ParticipantVO>> response) {
                        participantList = response.body();

                        // 참가자 수 추가
                        assert participantList != null;
                        participantCount = participantList.size();

                        memberAdapter = new MemberAdapter(participantList, context);
                        memberRecyclerView.setAdapter(memberAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ParticipantVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 총 주문금액
    private void getOrdersTotalPrice(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getOrdersTotalPrice(recruitId, userId)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        Integer totalPrice = response.body();

                        if (totalPrice == null || totalPrice == 0) {
                            totalPriceTV.setText("담은 메뉴 없음");
                        } else {
                            totalPriceTV.setText("총 주문금액 " + totalPrice + "원");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });
    }

    // 자신을 제외한 나머지 참가자 리스트 반환
    private void getParticipantListExceptMine(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipantListExceptMine(recruitId, userId)
                .enqueue(new Callback<List<ParticipantVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ParticipantVO>> call, @NonNull Response<List<ParticipantVO>> response) {
                        participantDeliveryInfoList = response.body();
                        memberDeliveryInfoAdapter = new MemberDeliveryInfoAdapter(participantDeliveryInfoList, context);
                        memberDeliveryInfoRecyclerView.setAdapter(memberDeliveryInfoAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ParticipantVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 결제금액계산 세팅
    private void setPayment(int storeId, int recruitId, int userId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        // 상품금액
        recruitApi.getOrdersTotalPrice(recruitId, userId)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        Integer orderPriceResult = response.body();
                        orderPriceTV.setText(orderPriceResult + "원");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });

        // 배달팁
        storeApi.getStore(storeId)
                .enqueue(new Callback<StoreVO>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();

                        assert store != null;
                        String deliveryTip = store.getDeliveryTip();
                        beforeDeliveryTipTV.setText(deliveryTip + "원");
                        beforeDeliveryTipTV.setPaintFlags(beforeDeliveryTipTV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                        int finalDeliveryTipResult = Integer.parseInt(deliveryTip) / participantCount;
                        finalDeliveryTipTV.setText(finalDeliveryTipResult + "원");
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {

                    }
                });

    }

}