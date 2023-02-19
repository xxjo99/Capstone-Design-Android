package com.delivery.mydelivery.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.menu.MenuListActivity;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.store.StoreActivity;
import com.delivery.mydelivery.user.UserVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class OrderListActivity extends AppCompatActivity {

    // 매장 아이디, 이름
    public static int storeId;
    @SuppressLint("StaticFieldLeak")
    public static TextView storeNameTV;

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

    // 레이아웃, 매장 리스트 이동 버튼슬라이딩패널 펼치기 / 닫기 버튼
    LinearLayout emptyLayout;
    Button storeListBtn;
    SlidingUpPanelLayout slidingUpPanelLayout;
    Button slidingOpenBtn;

    // 시간, 인원, 장소선택, 등록 버튼
    @SuppressLint("StaticFieldLeak")
    public static TextView selectTimeTV;
    Button datePickerBtn;
    TextView selectPersonTV;
    Button minusPersonBtn;
    Button addPersonBtn;
    EditText selectPlaceET;
    Button registerBtn;

    // 시간 선택 다이얼로그
    DatePickerDialog datePickerDialog;

    // 초기 인원수
    int person = 1;

    // 레트로핏, api
    RetrofitService retrofitService;
    OrderApi orderApi;
    RecruitApi recruitApi;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_order_list);
        context = this; // context 지정

        // 초기화
        storeNameTV = findViewById(R.id.storeNameTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        slidingUpPanelLayout = findViewById(R.id.slidingUpPanelLayout);
        emptyLayout = findViewById(R.id.emptyLayout);
        storeListBtn = findViewById(R.id.storeListBtn);
        slidingOpenBtn = findViewById(R.id.slidingOpenBtn);

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

        // 담은 메뉴 가져옴
        setOrder(userId);

        // 메뉴 추가 버튼, 해당 매장 액티비티로 이동
        addMenuBtn = findViewById(R.id.addMenuBtn);
        addMenuBtn.setOnClickListener(view -> {
            Intent intent = new Intent(OrderListActivity.this, MenuListActivity.class);
            intent.putExtra("storeId", storeId);
            intent.putExtra("participantType", "등록자"); // 등록자 or 참가자인지 확인
            startActivity(intent);
        });

        // 모집글 등록 펼치기 버튼
        slidingOpenBtn.setOnClickListener(view -> {
            if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                slidingOpenBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                slidingOpenBtn.setText("닫기");
            } else {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                slidingOpenBtn.setBackgroundResource(R.drawable.btn_fill_mint);
                slidingOpenBtn.setText("글 등록하기");
            }
        });

        // 초기화
        selectTimeTV = findViewById(R.id.selectTimeTV);
        datePickerBtn = findViewById(R.id.datePickerBtn);
        selectPersonTV = findViewById(R.id.selectPersonTV);
        minusPersonBtn = findViewById(R.id.minusBtn);
        addPersonBtn = findViewById(R.id.addBtn);
        selectPlaceET = findViewById(R.id.selectPlaceET);
        registerBtn = findViewById(R.id.registerBtn);

        // 초기인원수 지정
        selectPersonTV.setText(person + "명");

        // 날짜 선택, datePicker 다이얼로그
        datePickerDialog = new DatePickerDialog(context);
        datePickerBtn.setOnClickListener(view -> datePickerDialog.callDialog());

        // 인원 추가, 감소
        minusPersonBtn.setOnClickListener(view -> {
            if (person != 1) {
                person--;
                selectPersonTV.setText(person + "명");
            }
        });

        addPersonBtn.setOnClickListener(view -> {
            if (person >= 4) {
                Toast.makeText(context, "최대 4명", Toast.LENGTH_SHORT).show();
            } else {
                person++;
                selectPersonTV.setText(person + "명");
            }
        });

        // 모집글 등록
        registerBtn.setOnClickListener(view -> {

            if (selectTimeTV.getText().toString().equals("배달 받을 시간을 선택해주세요.")) {
                Toast.makeText(context, "배달 받을 시간을 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else if (selectPlaceET.length() == 0) {
                Toast.makeText(context, "배달 받을 장소를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (selectTimeTV.getText().toString().equals("배달 받을 시간을 선택해주세요.") && selectPlaceET.length() == 0) {
                Toast.makeText(context, "배달 받을 시간과 장소를 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 객체 생성, 객체에 필요한 데이터 추가
                RecruitVO recruit = new RecruitVO();

                recruit.setUserId(userId); // 회원 아이디
                recruit.setRegistrantPlace(user.getSchool()); // 등록자 위치
                recruit.setStoreId(storeId); // 매장 아이디
                recruit.setPlace(selectPlaceET.getText().toString()); // 배달장소
                recruit.setPerson(person); // 모집인원
                recruit.setDeliveryTime(selectTimeTV.getText().toString());

                findRecruit(userId, recruit);// 모집글 등록
            }
        });

    }

    // 장바구니 목록 생성
    private void setOrder(int userId) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.getOrderList(userId)
                .enqueue(new Callback<List<OrderVO>>() {
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

                            orderListAdapter = new OrderListAdapter(orderList, context);
                            orderRecyclerView.setAdapter(orderListAdapter);

                            // 총 금액 계산
                            totalPrice = 0;
                            for (OrderVO order : orderList) {
                                totalPrice += order.getTotalPrice();
                            }
                            totalPriceTV.setText(totalPrice + "원");
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<OrderVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 해당 사용자의 등록글이 있는지 검색 후 없다면 글 등록
    public void findRecruit(int userId, RecruitVO recruit) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.findRecruit(userId)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        boolean flag = Boolean.TRUE.equals(response.body());

                        if (flag) {
                            registerRecruit(recruit);
                        } else {
                            Toast.makeText(context, "하나의 글만 등록가능", Toast.LENGTH_SHORT).show();
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

        orderApi.registerRecruit(recruit)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Toast.makeText(context, "등록 완료", Toast.LENGTH_SHORT).show();
                        finish();
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