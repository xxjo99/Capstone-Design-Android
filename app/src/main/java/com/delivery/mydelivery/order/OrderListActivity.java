package com.delivery.mydelivery.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.keyword.KeywordApi;
import com.delivery.mydelivery.menu.MenuListActivity;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.store.StoreActivity;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class OrderListActivity extends AppCompatActivity {

    // 매장 아이디, 이름, 최소주문금액, 배달팁, 배달 가능 확인 flag
    public static int storeId;
    @SuppressLint("StaticFieldLeak")
    public static TextView storeNameTV;
    @SuppressLint("StaticFieldLeak")
    public static TextView deliveryTipTV;
    public static boolean deliveryAvailableFlag = false;

    public static String dateTime; // db에 저장할 시간데이터

    // 포인트 충전 다이얼로그
    AddPointDialog addPointDialog;

    // 툴바, 뒤로가기 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView orderRecyclerView;
    OrderListAdapter orderListAdapter;
    List<OrderVO> orderList;

    // 총 금액 텍스트뷰
    @SuppressLint("StaticFieldLeak")
    public static TextView totalPriceTV;
    public static int totalPrice;

    // 메뉴 추가 버튼
    Button addMenuBtn;

    // 레이아웃, 매장 리스트 이동 버튼, 슬라이딩 패널, 펼치기 / 닫기 버튼
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout emptyLayout;
    Button storeListBtn;
    public static SlidingUpPanelLayout slidingUpPanelLayout;
    @SuppressLint("StaticFieldLeak")
    public static Button slidingOpenBtn;

    // 시간, 인원, 장소선택, 등록 버튼
    @SuppressLint("StaticFieldLeak")
    public static TextView selectTimeTV;
    ImageButton datePickerBtn;
    TextView selectPersonTV;
    Button minusPersonBtn;
    Button addPersonBtn;
    AutoCompleteTextView deliveryPlaceAutoCompleteTV;
    Button registerBtn;

    List<String> deliveryPlaceList; // 배달장소 리스트

    // 시간 선택 다이얼로그
    DeliveryTimePickerDialog deliveryTimePickerDialog;

    // 초기 인원수
    int person = 2;

    // 레트로핏, api
    RetrofitService retrofitService;
    OrderApi orderApi;
    RecruitApi recruitApi;
    UserApi userApi;
    StoreApi storeApi;
    KeywordApi keywordApi;

    Context context;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_order_list);
        context = this; // context 지정

        // 초기화
        storeNameTV = findViewById(R.id.storeNameTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        deliveryTipTV = findViewById(R.id.deliveryTipTV);
        slidingUpPanelLayout = findViewById(R.id.slidingUpPanelLayout);
        emptyLayout = findViewById(R.id.emptyLayout);
        storeListBtn = findViewById(R.id.storeListBtn);
        slidingOpenBtn = findViewById(R.id.slidingOpenBtn);
        addPointDialog = new AddPointDialog(context);

        // 매장 리스트 이동
        storeListBtn.setOnClickListener(view -> {
            Intent intent = new Intent(OrderListActivity.this, StoreActivity.class);
            startActivity(intent);
            finish();
        });

        // 슬라이딩패널 설정
        slidingUpPanelLayout.setTouchEnabled(false);

        // 툴바
        toolbar = findViewById(R.id.orderToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 리사이클러뷰 설정
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        orderRecyclerView.setLayoutManager(manager);
        orderRecyclerView.setHasFixedSize(true);

        // 유저 id를 통해 해당 유저의 장바구니 리스트 가져옴
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);
        int userId = user.getUserId();
        int point = user.getPoint();

        // 담은 메뉴 가져옴
        setOrder(userId, point);

        // 메뉴 추가 버튼, 해당 매장 액티비티로 이동
        addMenuBtn = findViewById(R.id.addMenuBtn);
        addMenuBtn.setOnClickListener(view -> {
            Intent intent = new Intent(OrderListActivity.this, MenuListActivity.class);
            intent.putExtra("storeId", storeId);
            intent.putExtra("participantType", "등록자"); // 등록자 or 참가자인지 확인
            startActivity(intent);
        });

        // 등록 가능한지 검사
        checkParticipate(userId);

        // 모집글 등록 펼치기 버튼
        slidingOpenBtn.setOnClickListener(view -> {

            // 등록 가능 여부 판별
            if (!deliveryAvailableFlag) { // 보유한 포인트가 배달비보다 적을경우
                addPointDialog.callDialog();
            } else {
                if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                    slidingOpenBtn.setBackgroundResource(R.drawable.btn_fill1_gray);
                    slidingOpenBtn.setText("닫기");
                } else {
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    slidingOpenBtn.setBackgroundResource(R.drawable.btn_fill1_mint);
                    slidingOpenBtn.setText("글 등록하기");
                }
            }
        });

        // 초기화
        selectTimeTV = findViewById(R.id.selectTimeTV);
        datePickerBtn = findViewById(R.id.datePickerBtn);
        selectPersonTV = findViewById(R.id.selectPersonTV);
        minusPersonBtn = findViewById(R.id.minusBtn);
        addPersonBtn = findViewById(R.id.addBtn);
        deliveryPlaceAutoCompleteTV = findViewById(R.id.deliveryPlaceAutoCompleteTV);
        registerBtn = findViewById(R.id.registerBtn);

        // 초기인원수 지정
        selectPersonTV.setText(person + "명");
        minusPersonBtn.setBackgroundResource(R.drawable.icon_minus_gray);
        minusPersonBtn.setEnabled(false);

        // 학교리스트 추가, 어댑터 연결
        setDeliveryPlace();

        // 배달시간 선택 다이얼로그
        datePickerBtn.setOnClickListener(view -> createDeliveryTimePickerDialog());

        // 인원 추가, 감소
        minusPersonBtn.setOnClickListener(view -> {
            if (person > 1) {
                person--;
                selectPersonTV.setText(person + "명");
            }

            if (person == 2) {
                minusPersonBtn.setBackgroundResource(R.drawable.icon_minus_gray);
                addPersonBtn.setBackgroundResource(R.drawable.icon_plus);

                minusPersonBtn.setEnabled(false);
                addPersonBtn.setEnabled(true);
            } else {
                addPersonBtn.setBackgroundResource(R.drawable.icon_plus);
                minusPersonBtn.setBackgroundResource(R.drawable.icon_minus);

                minusPersonBtn.setEnabled(true);
                addPersonBtn.setEnabled(true);
            }
        });

        addPersonBtn.setOnClickListener(view -> {
            if (person < 4) {
                person++;
                selectPersonTV.setText(person + "명");
            }

            if (person >= 4) {
                addPersonBtn.setBackgroundResource(R.drawable.icon_plus_gray);
                minusPersonBtn.setBackgroundResource(R.drawable.icon_minus);

                minusPersonBtn.setEnabled(true);
                addPersonBtn.setEnabled(false);
            } else {
                addPersonBtn.setBackgroundResource(R.drawable.icon_plus);
                minusPersonBtn.setBackgroundResource(R.drawable.icon_minus);

                minusPersonBtn.setEnabled(true);
                addPersonBtn.setEnabled(true);
            }
        });

        // 모집글 등록
        registerBtn.setOnClickListener(view -> {

            if (selectTimeTV.getText().toString().equals("시간 선택")) {
                StyleableToast.makeText(context, "배달 받을 시간을 선택해주세요.", R.style.warningToast).show();
            } else if (deliveryPlaceAutoCompleteTV.length() == 0) {
                StyleableToast.makeText(context, "배달 받을 장소를 입력해주세요.", R.style.warningToast).show();
            } else {
                checkDeliveryPlace(deliveryPlaceAutoCompleteTV.getText().toString(), user); // 배달장소를 정확히 입력했는지 확인
            }
        });

    }

    // 장바구니 목록 생성
    private void setOrder(int userId, int point) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.getOrderList(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<OrderVO>> call, @NonNull Response<List<OrderVO>> response) {
                        orderList = response.body();

                        // 장바구니가 비어있는지 아닌지 확인
                        assert orderList != null;
                        if (orderList.isEmpty()) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            slidingUpPanelLayout.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                            slidingUpPanelLayout.setVisibility(View.VISIBLE);

                            orderListAdapter = new OrderListAdapter(orderList, point, context);
                            orderRecyclerView.setAdapter(orderListAdapter);

                            // 총 금액 계산
                            totalPrice = 0;
                            for (OrderVO order : orderList) {
                                totalPrice += order.getTotalPrice();
                            }
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            String totalPriceFormat = numberFormat.format(totalPrice);
                            totalPriceTV.setText(totalPriceFormat + "원");
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<OrderVO>> call, @NonNull Throwable t) {
                    }
                });
    }

    // 이용제한 검사
    private void checkParticipate(int userId) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.checkRestriction(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        Boolean checkRestrictionFlag = response.body();

                        if (Boolean.TRUE.equals(checkRestrictionFlag)) { // 이용제한 없음, 해당 유저가 등록한 글이 있는지 검사
                            findRecruit(userId);
                        } else { // 등록불가
                            slidingOpenBtn.setBackgroundResource(R.drawable.btn_fill1_gray);
                            slidingOpenBtn.setText("이용제한");
                            slidingOpenBtn.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

                    }
                });
    }

    // 배달시간 선택 다이얼로그 생성
    private void createDeliveryTimePickerDialog() {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = new StoreVO();
                        Timestamp openTime = store.getOpenTime();
                        Timestamp closeTime = store.getCloseTime();

                        deliveryTimePickerDialog = new DeliveryTimePickerDialog(openTime, closeTime, context);
                        deliveryTimePickerDialog.callDialog();
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {
                    }
                });
    }

    // 배달장소 추가
    private void setDeliveryPlace() {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.getDeliveryPlaceList()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                        deliveryPlaceList = response.body();
                        deliveryPlaceAutoCompleteTV.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, deliveryPlaceList));
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                    }
                });
    }

    // 정확한 배달장소
    private void checkDeliveryPlace(String deliveryPlace, UserVO user) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.getDeliveryPlaceList()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                        deliveryPlaceList = response.body();

                        if (!Objects.requireNonNull(deliveryPlaceList).contains(deliveryPlace)) {
                            StyleableToast.makeText(context, "배달 장소를 정확하게 선택해주세요.", R.style.warningToast).show();
                        } else {
                            // 객체 생성, 객체에 필요한 데이터 추가
                            RecruitVO recruit = new RecruitVO();

                            recruit.setUserId(user.getUserId()); // 회원 아이디
                            recruit.setRegistrantPlace(user.getSchool()); // 등록자 위치
                            recruit.setStoreId(storeId); // 매장 아이디
                            recruit.setPlace(deliveryPlaceAutoCompleteTV.getText().toString()); // 배달장소
                            recruit.setPerson(person); // 모집인원
                            recruit.setReceiptState(0); // 배달 시작 구분, 초기값 0

                            registerRecruit(recruit);// 모집글 등록
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 해당 사용자의 등록글이 있는지 검색 후 없다면 등록
    private void findRecruit(int userId) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.findRecruit(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        boolean flag = Boolean.TRUE.equals(response.body());

                        if (flag) { // 등록된 글 없음, 등록 가능
                            slidingOpenBtn.setBackgroundResource(R.drawable.btn_fill1_mint);
                            slidingOpenBtn.setText("글 등록하기");
                            slidingOpenBtn.setEnabled(true);
                        } else { // 등록된 글 있음, 등록 불가
                            slidingOpenBtn.setBackgroundResource(R.drawable.btn_fill1_gray);
                            slidingOpenBtn.setText("하나의 글만 등록가능");
                            slidingOpenBtn.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

                    }
                });
    }

    // 모집글 등록
    private void registerRecruit(RecruitVO recruit) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.registerRecruit(recruit, dateTime)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        findStore(recruit.getStoreId()); // 매장 검색 -> 키워드 메시지 전송

                        StyleableToast.makeText(context, "모집글 등록 완료", R.style.successToast).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
    }

    // 매장 검색
    private void findStore(int storeId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();
                        String category = Objects.requireNonNull(store).getCategory();
                        String storeName = store.getStoreName();

                        sendKeywordMessage(category, storeName); // 키워드 메시지 전송
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 키워드 메시지 전송
    private void sendKeywordMessage(String keyword, String storeName) {
        retrofitService = new RetrofitService();
        keywordApi = retrofitService.getRetrofit().create(KeywordApi.class);

        keywordApi.sendKeywordMessage(keyword, storeName)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
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