package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
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

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class RecruitActivity extends AppCompatActivity {

    // dialog
    RecruitDeleteDialog recruitDeleteDialog;
    RecruitLeaveDialog recruitLeaveDialog;
    RecruitPaymentNoticeDialog recruitPaymentNoticeDialog;

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;
    TextView deleteBtn;

    // 매장 정보
    ImageView storeIV;
    TextView storeNameTV;
    TextView deliveryTimeTV;
    TextView deliveryTipTV;
    TextView minimumDeliveryPriceTV;

    // 레이아웃, 리사이클러뷰, 어댑터, 리스트
    RecyclerView memberRecyclerView;
    MemberAdapter memberAdapter;
    List<ParticipantVO> participantList;
    LinearLayout noParticipantLayout;
    RecyclerView memberDeliveryInfoRecyclerView;
    MemberDeliveryInfoAdapter memberDeliveryInfoAdapter;
    List<ParticipantVO> participantDeliveryInfoList;

    int participantCount; // 참가자수

    // 나의 배달 정보, 메뉴 확인 텍스트버튼
    ImageView userIV;
    TextView userNameTV;
    TextView totalPriceTV;
    TextView orderListTV;

    // 결제금액계산
    TextView orderPriceTV;
    TextView beforeDeliveryTipTV;
    TextView finalDeliveryTipTV;
    TextView finalPaymentTV;

    // 결제 버튼
    Button paymentBtn;

    // 배달 상태에 따라 보여질 레이아웃, 텍스트, 메뉴 확인 버튼
    LinearLayout receiptLayout;
    TextView deliveryStateTV;
    Button checkOrderBtn;
    NestedScrollView recruitScrollView;
    LinearLayout paymentLayout;
    TextView surchargeTV;

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

        // 전 액티비티에서 넘어온 값
        Intent intent = getIntent();
        int storeId = intent.getIntExtra("storeId", 0);
        int recruitId = intent.getIntExtra("recruitId", 0);

        // 결제를 완료하지 않은 인원이 있다면 강퇴
        checkParticipantPaymentStatus(recruitId);

        // 사용자정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 툴바
        toolbar = findViewById(R.id.recruitToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 모집글 삭제 버튼
        deleteBtn = findViewById(R.id.deleteBtn);

        // 등록자, 참가자에 따른 텍스트 변경
        setDeleteBtnVisibility(recruitId, user.getUserId());

        // 삭제 or 탈퇴
        deleteBtn.setOnClickListener(view -> {
            if (deleteBtn.getText().toString().equals("삭제하기")) {
                createDeleteDialog(recruitId);
            } else {
                createLeaveDialog(recruitId);
            }
        });

        // 레이아웃, 버튼 초기화
        receiptLayout = findViewById(R.id.receiptLayout);
        deliveryStateTV = findViewById(R.id.deliveryStateTV);
        checkOrderBtn = findViewById(R.id.checkOrderBtn);
        recruitScrollView = findViewById(R.id.recruitScrollView);
        paymentLayout = findViewById(R.id.paymentLayout);
        surchargeTV = findViewById(R.id.surchargeTV);

        checkDeliveryState(recruitId); // 배달상태 확인 (접수 전, 후, 배달 시작)

        // 담은 메뉴 확인 액티비티 이동
        checkOrderBtn.setOnClickListener(view -> moveOrderListActivity(recruitId, user.getUserId(), storeId));

        // 추가요금 텍스트 설정


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
        userIV = findViewById(R.id.userIV);
        userNameTV = findViewById(R.id.userNameTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        orderListTV = findViewById(R.id.orderListTV);

        // 담은 메뉴 확인 이동 버튼
        orderListTV.setOnClickListener(view -> moveOrderListActivity(recruitId, user.getUserId(), storeId));

        setUserImage(recruitId, user.getUserId()); // 이미지
        userNameTV.setText(user.getName()); // 사용자 이름

        getOrdersTotalPrice(recruitId, user.getUserId()); // 총 주문금액

        // 참가한 파티원이 없을경우 보여질 레이아웃 초기화
        noParticipantLayout = findViewById(R.id.noParticipantLayout);

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

        // 최종금액계산 추가, 탈퇴 다이얼로그 추가
        setPayment(storeId, recruitId, user.getUserId());

        // 결제 완료 검사
        checkPaymentComplete(recruitId, user.getUserId());

        // 결제 페이지 이동 버튼
        paymentBtn = findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(view -> {
            String phoneNum = user.getPhoneNum();
            movePayment(recruitId, phoneNum);
        });
    }

    // 삭제 or 탈퇴 결정
    public void setDeleteBtnVisibility(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.findRegistrant(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ParticipantVO> call, @NonNull Response<ParticipantVO> response) {
                        ParticipantVO participant = response.body();

                        assert participant != null;
                        if (participant.getUserId() == userId) {
                            deleteBtn.setText("삭제하기");
                        } else {
                            deleteBtn.setText("탈퇴하기");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ParticipantVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 삭제 다이얼로그 생성
    private void createDeleteDialog(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipantCount(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        Integer participantCount = response.body();

                        if (participantCount < 2) { // 참가자수 2명 미만일경우 패널티 부과 x
                            recruitDeleteDialog.callDialog(0);
                        } else { // 그렇지 않다면 패널티 부과
                            recruitDeleteDialog.callDialog(1);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });
    }

    // 탈퇴 다이얼로그 생성
    private void createLeaveDialog(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getRecruit(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<RecruitVO> call, @NonNull Response<RecruitVO> response) {
                        RecruitVO recruit = response.body();

                        LocalDateTime currentTime = LocalDateTime.now(); // 현재 시간
                        // 배달시간
                        Timestamp timestamp = Objects.requireNonNull(recruit).getDeliveryTime();
                        Instant instant = timestamp.toInstant();
                        LocalDateTime deliveryTime = instant.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

                        // 현재시간과 배달시간 비교
                        if (!deliveryTime.isAfter(currentTime)) { // 배달시간이 현재시간 이후일경우
                            recruitLeaveDialog.callDialog(1);
                        } else { // 배달시간이 현재시간 이전일경우
                            recruitLeaveDialog.callDialog(0);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RecruitVO> call, @NonNull Throwable t) {
                    }
                });
    }

    // 배달 상태에 따른 레이아웃 변경
    private void checkDeliveryState(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getRecruit(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<RecruitVO> call, @NonNull Response<RecruitVO> response) {
                        RecruitVO recruit = response.body();
                        int receiptState = Objects.requireNonNull(recruit).getReceiptState();

                        // 레이아웃, 버튼 초기화
                        receiptLayout = findViewById(R.id.receiptLayout);
                        deliveryStateTV = findViewById(R.id.deliveryStateTV);
                        checkOrderBtn = findViewById(R.id.checkOrderBtn);
                        recruitScrollView = findViewById(R.id.recruitScrollView);
                        paymentLayout = findViewById(R.id.paymentLayout);

                        if (receiptState == 0) { // 배달 접수 전
                            receiptLayout.setVisibility(View.GONE);

                            recruitScrollView.setVisibility(View.VISIBLE);
                            paymentLayout.setVisibility(View.VISIBLE);
                        } else if (receiptState == 1) { // 배달 접수
                            receiptLayout.setVisibility(View.VISIBLE);
                            deliveryStateTV.setText("배달이 접수되었습니다.");
                            checkOrderBtn.setVisibility(View.VISIBLE);

                            recruitScrollView.setVisibility(View.GONE);
                            paymentLayout.setVisibility(View.GONE);
                            deleteBtn.setVisibility(View.GONE);

                            checkSurcharge(recruitId); // 추가요금발생 체크
                        } else { // 배달 출발 (2)
                            receiptLayout.setVisibility(View.VISIBLE);
                            deliveryStateTV.setText("배달이 시작되었습니다.");
                            checkOrderBtn.setVisibility(View.VISIBLE);

                            recruitScrollView.setVisibility(View.GONE);
                            paymentLayout.setVisibility(View.GONE);
                            deleteBtn.setVisibility(View.GONE);

                            checkSurcharge(recruitId); // 추가요금발생 체크
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RecruitVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 추가 요금 발생시 텍스트 추가
    private void checkSurcharge(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getSurcharge(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        Integer surcharge = response.body();

                        if (surcharge == 0) {
                            surchargeTV.setVisibility(View.GONE);
                        } else {
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            String surchargeFormat = numberFormat.format(surcharge);
                            surchargeTV.setText("인원이 모두 모이지 않아 추가 요금 " + surchargeFormat + "원이 발생했습니다.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });
    }

    // 매장정보 생성
    private void setStore(int storeId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();

                        assert store != null;
                        Glide.with(context).load(store.getStoreImageUrl()).placeholder(R.drawable.ic_launcher_background).into(storeIV);
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

    // 참가자 리스트 생성
    private void setMemberList(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipantList(recruitId)
                .enqueue(new Callback<>() {
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

    // 사용자 이미지 설정
    private void setUserImage(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipant(recruitId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ParticipantVO> call, @NonNull Response<ParticipantVO> response) {
                        ParticipantVO participant = response.body();

                        if (Objects.requireNonNull(participant).getParticipantType().equals("registrant")) {
                            userIV.setImageResource(R.drawable.icon_user_mint);
                        } else {
                            userIV.setImageResource(R.drawable.icon_user_green);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ParticipantVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 총 주문금액
    private void getOrdersTotalPrice(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getOrdersTotalPrice(recruitId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        Integer totalPrice = response.body();

                        if (totalPrice == null || totalPrice == 0) {
                            totalPriceTV.setText("담은 메뉴 없음");
                        } else {
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            String totalPriceFormat = numberFormat.format(totalPrice);
                            totalPriceTV.setText("총 주문금액 " + totalPriceFormat + "원");
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
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ParticipantVO>> call, @NonNull Response<List<ParticipantVO>> response) {
                        participantDeliveryInfoList = response.body();

                        // 참가한 유저가 있는지 확인
                        if (Objects.requireNonNull(participantDeliveryInfoList).isEmpty()) {
                            noParticipantLayout.setVisibility(View.VISIBLE);
                            memberDeliveryInfoRecyclerView.setVisibility(View.GONE);
                        } else {
                            noParticipantLayout.setVisibility(View.GONE);
                            memberDeliveryInfoRecyclerView.setVisibility(View.VISIBLE);

                            memberDeliveryInfoAdapter = new MemberDeliveryInfoAdapter(participantDeliveryInfoList, context);
                            memberDeliveryInfoRecyclerView.setAdapter(memberDeliveryInfoAdapter);
                        }
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
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        String orderPrice = numberFormat.format(response.body());
                        orderPriceTV.setText(orderPrice + "원");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });

        // 배달팁
        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();
                        assert store != null;
                        NumberFormat numberFormat = NumberFormat.getInstance();

                        String deliveryTip = store.getDeliveryTip();

                        String deliveryTipFormat = numberFormat.format(Integer.parseInt(deliveryTip));
                        beforeDeliveryTipTV.setText(deliveryTipFormat + "원");
                        beforeDeliveryTipTV.setPaintFlags(beforeDeliveryTipTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        if (participantCount == 0) {
                            participantCount = 1;
                        }

                        int finalDeliveryTipResult = Integer.parseInt(deliveryTip) / participantCount;

                        // 삭제, 탈퇴 다이얼로그 추가
                        recruitLeaveDialog = new RecruitLeaveDialog(context, recruitId, userId, finalDeliveryTipResult);
                        recruitDeleteDialog = new RecruitDeleteDialog(context, recruitId, userId, participantCount);
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {

                    }
                });

        // 예상 배달비
        recruitApi.getDeliveryTip(recruitId)
                        .enqueue(new Callback<>() {
                            @Override
                            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                                Integer deliveryTip = response.body();
                                NumberFormat numberFormat = NumberFormat.getInstance();
                                String finalDeliveryTipFormat = numberFormat.format(deliveryTip);
                                finalDeliveryTipTV.setText(finalDeliveryTipFormat + "원");
                            }

                            @Override
                            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                            }
                        });

        // 최종결제금액
        recruitApi.getFinalPayment(recruitId, storeId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        String finalPayment = numberFormat.format(response.body());
                        finalPaymentTV.setText(finalPayment + "원");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });
    }

    // 장바구니 이동
    private void moveOrderListActivity(int recruitId, int userId, int storeId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipant(recruitId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ParticipantVO> call, @NonNull Response<ParticipantVO> response) {
                        ParticipantVO participant = response.body();
                        int paymentStatus = Objects.requireNonNull(participant).getPaymentStatus();

                        Intent checkMenuIntent = new Intent(context, RecruitOrderListActivity.class);

                        checkMenuIntent.putExtra("recruitId", recruitId);
                        checkMenuIntent.putExtra("storeId", storeId);
                        checkMenuIntent.putExtra("userId", userId);
                        checkMenuIntent.putExtra("paymentStatus", paymentStatus);

                        startActivity(checkMenuIntent);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ParticipantVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 결제 완료 검사
    private void checkPaymentComplete(int recruitId, int userId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipant(recruitId, userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ParticipantVO> call, @NonNull Response<ParticipantVO> response) {
                        ParticipantVO participant = response.body();
                        int paymentStatus = Objects.requireNonNull(participant).getPaymentStatus();

                        if (paymentStatus == 0) {
                            checkDeliveryTime(recruitId);
                        } else {
                            paymentBtn.setText("결제완료");
                            paymentBtn.setEnabled(false);
                            paymentBtn.setBackgroundResource(R.drawable.btn_fill1_gray);
                            deleteBtn.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<ParticipantVO> call, @NonNull Throwable t) {
                    }
                });
    }

    /* 배달 시간 검사
       배달 시간이 되었다면 결제 페이지 이동 버튼 활성화 */
    private void checkDeliveryTime(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getRecruit(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<RecruitVO> call, @NonNull Response<RecruitVO> response) {
                        RecruitVO recruit = response.body();

                        LocalDateTime currentTime = LocalDateTime.now(); // 현재 시간
                        // 배달시간
                        Timestamp timestamp = Objects.requireNonNull(recruit).getDeliveryTime();
                        Instant instant = timestamp.toInstant();
                        LocalDateTime deliveryTime = instant.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

                        // 현재시간과 배달시간 비교
                        if (!deliveryTime.isAfter(currentTime)) { // 배달시간이 현재시간 이후일경우 결제버튼 활성화

                            if (deleteBtn.getText().toString().equals("삭제하기")) { // 등록자일경우 참가인원 수에 따라 탈퇴, 삭제 버튼 변경
                                isMoreThanTwo(recruitId);
                            } else {
                                paymentBtn.setEnabled(true);
                                paymentBtn.setBackgroundResource(R.drawable.btn_fill1_mint);
                                paymentBtn.setText("결제하기");
                                deleteBtn.setText("탈퇴하기");
                            }

                            LocalDateTime paymentDeadline = deliveryTime.plusMinutes(10); // 결제마감시간 = 배달시간 + 10분

                            // 결제 안내 다이얼로그 생성
                            if (!currentTime.isAfter(paymentDeadline)) {
                                recruitPaymentNoticeDialog = new RecruitPaymentNoticeDialog(paymentDeadline, context);
                                recruitPaymentNoticeDialog.callDialog();
                            }

                        } else {
                            paymentBtn.setEnabled(true);
                            paymentBtn.setBackgroundResource(R.drawable.btn_fill1_mint);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<RecruitVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 참가자 수가 2명 이상 인지 검사, 2명 미만일 경우 삭제, 이상일 경우 탈퇴 버튼으로 변경
    private void isMoreThanTwo(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipantCount(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        Integer participantCount = response.body();

                        if (participantCount < 2) { // 참가자수 2명 미만일경우 삭제로 변경
                            paymentBtn.setEnabled(false);
                            paymentBtn.setBackgroundResource(R.drawable.btn_fill1_gray);
                            paymentBtn.setText("2명미만, 배달불가");
                            deleteBtn.setText("삭제하기");
                        } else { // 2명 이상일경우 탈퇴
                            paymentBtn.setEnabled(true);
                            paymentBtn.setBackgroundResource(R.drawable.btn_fill1_mint);
                            paymentBtn.setText("결제하기");
                            deleteBtn.setText("탈퇴하기");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });
    }

    // 결제페이지 이동
    private void movePayment(int recruitId, String phoneNum) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getRecruit(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<RecruitVO> call, @NonNull Response<RecruitVO> response) {
                        RecruitVO recruit = response.body();
                        String place = Objects.requireNonNull(recruit).getPlace();

                        Intent paymentIntent = new Intent(RecruitActivity.this, PaymentActivity.class);

                        paymentIntent.putExtra("recruitId", recruitId);
                        paymentIntent.putExtra("storeName", storeNameTV.getText().toString()); // 매장
                        paymentIntent.putExtra("place", place); // 장소
                        paymentIntent.putExtra("phoneNum", phoneNum); // 휴대폰번호
                        paymentIntent.putExtra("orderPrice", orderPriceTV.getText().toString()); // 상품금액
                        paymentIntent.putExtra("deliveryTip", beforeDeliveryTipTV.getText()); // 배달비
                        paymentIntent.putExtra("finalDeliveryTip", finalDeliveryTipTV.getText().toString()); // 할인된 배달비
                        paymentIntent.putExtra("finalPayment", finalPaymentTV.getText().toString()); // 최종결제금액

                        startActivity(paymentIntent);
                    }

                    @Override
                    public void onFailure(@NonNull Call<RecruitVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 마감시간이 지나고, 2명 이상일 경우 결제가 완료되지 않은 유저가 있다면 포인트 차감 후 강퇴
    private void checkParticipantPaymentStatus(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getRecruit(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<RecruitVO> call, @NonNull Response<RecruitVO> response) {
                        RecruitVO recruit = response.body();

                        LocalDateTime currentTime = LocalDateTime.now(); // 현재시간
                        // 배달시간
                        Timestamp timestamp = Objects.requireNonNull(recruit).getDeliveryTime();
                        Instant instant = timestamp.toInstant();
                        LocalDateTime deliveryTime = instant.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

                        // 결제마감시간이 지났을 때 참가인원 수 검사
                        if (currentTime.isAfter(deliveryTime)) {
                            checkParticipantCount(recruitId);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RecruitVO> call, @NonNull Throwable t) {
                    }
                });
    }

    // 참가인원 수 검사
    private void checkParticipantCount(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getParticipantCount(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        Integer participantCount = response.body();

                        // 2명 이상일 경우 결제하지 않은 인원 강퇴
                        if (participantCount >= 2) {
                            kickUser(recruitId);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });
    }

    // 결제가 완료되지 않은 유저가 있다면 포인트 차감 후 강퇴
    private void kickUser(int recruitId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.checkParticipantPaymentStatus(recruitId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        // 강퇴 후 배달승인대기로 변경
                        paymentBtn.setText("배달승인대기중");
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }

    // 재시작
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