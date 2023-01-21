package com.delivery.mydelivery.user.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.user.UserVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class OrderListActivity extends AppCompatActivity {

    // 매장 아이디, 이름
    public static int storeId;
    @SuppressLint("StaticFieldLeak")
    public static TextView storeNameTV;

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView orderRecyclerView;
    OrderListAdapter orderListAdapter;
    List<OrderVO> orderList;

    // 총 금액 텍스트뷰
    @SuppressLint("StaticFieldLeak")
    public static TextView totalPriceTV;
    public static int totalPrice;

    // 모집글 등록 레이아웃, 슬라이딩패널 펼치기 / 닫기 버튼
    SlidingUpPanelLayout slidingUpPanelLayout;
    Button slidingOpenBtn;

    // 시간, 인원, 장소선택, 등록 버튼
    TextView selectTimeTV;
    TextView selectPersonTV;
    Button minusPersonBtn;
    Button addPersonBtn;
    EditText selectPlaceET;
    Button registerBtn;

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

        // 매장이름, 총 주문금액, 레이아웃, 버튼
        storeNameTV = findViewById(R.id.storeNameTV);
        totalPriceTV = findViewById(R.id.totalPriceTV);
        slidingUpPanelLayout = findViewById(R.id.slidingUpPanelLayout);
        slidingOpenBtn = findViewById(R.id.slidingOpenBtn);

        // 슬라이딩패널 설정
        slidingUpPanelLayout.setTouchEnabled(false);

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

        // 모집글 등록 펼치기 버튼
        slidingOpenBtn.setOnClickListener(view -> {
            if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                slidingOpenBtn.setText("닫기");
            } else {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                slidingOpenBtn.setText("글 등록하기");
            }
        });

        selectTimeTV = findViewById(R.id.selectTimeTV);
        selectPersonTV = findViewById(R.id.selectPersonTV);
        minusPersonBtn = findViewById(R.id.minusPersonBtn);
        addPersonBtn = findViewById(R.id.addPersonBtn);
        selectPlaceET = findViewById(R.id.selectPlaceET);
        registerBtn = findViewById(R.id.registerBtn);

        // 초기인원수 지정
        selectPersonTV.setText(person + "명");

        // 인원 추가, 감소
        minusPersonBtn.setOnClickListener(view -> {
            if (person != 1) {
                person--;
                selectPersonTV.setText(person + "명");
            }
        });

        addPersonBtn.setOnClickListener(view -> {
            person++;
            selectPersonTV.setText(person + "명");
        });

        // 모집글 등록
        registerBtn.setOnClickListener(view -> {

            if (selectPlaceET.getText().toString().length() == 0) {
                Toast.makeText(context, "장소를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                // 객체 생성, 객체에 필요한 데이터 추가
                RecruitVO recruit = new RecruitVO();

                recruit.setUserId(userId); // 회원 아이디
                recruit.setStoreId(storeId); // 매장 아이디
                recruit.setDeliveryTime(selectTimeTV.getText().toString()); // 시간
                recruit.setPlace(selectPlaceET.getText().toString()); // 장소
                recruit.setPerson(person); // 모집인원

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
                        orderListAdapter = new OrderListAdapter(orderList, context);
                        orderRecyclerView.setAdapter(orderListAdapter);

                        // 선택한 메뉴의 총 가격을 계산
                        totalPrice = 0;
                        for (OrderVO order : orderList) {
                            totalPrice += order.getTotalPrice();
                        }
                        totalPriceTV.setText(totalPrice + "원");
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
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context, "등록 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }

}