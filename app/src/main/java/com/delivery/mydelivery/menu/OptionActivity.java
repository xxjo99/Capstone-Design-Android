package com.delivery.mydelivery.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.order.OrderApi;
import com.delivery.mydelivery.order.OrderVO;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.recruit.ParticipantOrderVO;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.user.UserVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class OptionActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    // 메뉴정보
    ImageView menuIV;
    TextView menuNameTV;
    TextView menuPriceTV;

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView optionRecyclerView;
    OptionAdapter optionAdapter;
    List<OptionVO> optionList;

    public static int finalMenuPrice; // 메뉴의 총 가격
    @SuppressLint("StaticFieldLeak")
    public static Button addMenuBtn; // 장바구니 담기 버튼
    public static List<Integer> selectOptionList; // 선택한 옵션의 리스트
    public static List<Boolean> selectedOptionFlagList; // 모든 옵션이 선택됐는지 확인

    // 개수 텍스트뷰, 증가, 감소 버튼
    TextView amountTV;
    ImageButton increaseBtn;
    ImageButton decreaseBtn;

    int amount; // 현재 메뉴 개수

    // 레트로핏, api
    RetrofitService retrofitService;
    MenuApi menuApi;
    OrderApi orderApi;
    RecruitApi recruitApi;

    Context context;

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_option);
        context = this; // context 지정

        // 툴바
        toolbar = findViewById(R.id.optionToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 사용자 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 전 액티비티에서 넘겨받은 값
        Intent intent = getIntent();
        int storeId = intent.getIntExtra("storeId", 0); // 매장 id
        int menuId = intent.getIntExtra("menuId", 0); // 메뉴Id
        String menuImage = intent.getStringExtra("menuImageUrl"); // 메뉴 이미지 주소
        String menuName = intent.getStringExtra("menuName"); // 메뉴 이름
        int menuPrice = intent.getIntExtra("menuPrice", 0);
        String participantType = intent.getStringExtra("participantType"); // 참가 타입
        int recruitId = intent.getIntExtra("recruitId", 0); // 모집글 아이디

        // 초기화
        menuIV = findViewById(R.id.menuIV);
        menuNameTV = findViewById(R.id.menuNameTV);
        menuPriceTV = findViewById(R.id.menuPriceTV);
        amountTV = findViewById(R.id.amountTV);
        decreaseBtn = findViewById(R.id.decreaseBtn);
        increaseBtn = findViewById(R.id.increaseBtn);
        decreaseBtn.setEnabled(false);

        // 메뉴 이미지, 이름, 가격
        Glide.with(this).load(menuImage).placeholder(R.drawable.ic_launcher_background).into(menuIV);
        menuNameTV.setText(menuName);
        NumberFormat numberFormat = NumberFormat.getInstance();
        String deliveryTip = numberFormat.format(menuPrice);
        menuPriceTV.setText(deliveryTip + "원");

        // 리사이클러뷰 설정
        optionRecyclerView = findViewById(R.id.optionRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        optionRecyclerView.setLayoutManager(manager);
        optionRecyclerView.setHasFixedSize(true);

        selectedOptionFlagList = new ArrayList<>(); // 초기화
        setOption(menuId); // 어댑터 추가

        // 수량 초기화
        amount = 1;
        amountTV.setText(amount + "개");

        // 장바구니 담기 버튼 -> 버튼의 텍스트를 가격으로 변환
        finalMenuPrice = intent.getIntExtra("menuPrice", 0);
        addMenuBtn = findViewById(R.id.addMenuBtn);
        addMenuBtn.setText(finalMenuPrice + "원 담기");

        selectOptionList = new ArrayList<>();

        // 개수 증가, 감소 이벤트
        decreaseBtn.setOnClickListener(view -> {
            int price = finalMenuPrice / amount;
            finalMenuPrice -= price;
            addMenuBtn.setText(finalMenuPrice + "원 담기");

            amount -= 1;
            amountTV.setText(amount + "개");

            if (amount == 1) {
                decreaseBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_minus_gray));
                decreaseBtn.setEnabled(false);
            }

        });

        increaseBtn.setOnClickListener(view -> {
            decreaseBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_minus));
            decreaseBtn.setEnabled(true);

            int price = finalMenuPrice / amount;
            finalMenuPrice += price;
            addMenuBtn.setText(finalMenuPrice + "원 담기");

            amount += 1;
            amountTV.setText(amount + "개");
        });

        // 장바구니 추가 버튼
        addMenuBtn.setOnClickListener(view -> {
            // 유저 아이디
            int userId = user.getUserId();

            // 등록자, 참가자 구분
            if (participantType.equals("등록자")) {
                OrderVO order = new OrderVO(); // 객체 생성

                // 객체에 필요한 데이터 추가
                order.setStoreId(storeId);
                order.setUserId(userId); // 사용자 아이디
                order.setMenuId(menuId); // 메뉴id
                order.setAmount(amount); // 메뉴 개수
                order.setTotalPrice(finalMenuPrice); // 최종 메뉴 가격

                // 선택한 옵션을 텍스트로 변환후 저장
                String selectOptionStr = String.join(",", selectOptionList.stream().map(String::valueOf).toArray(String[]::new));
                order.setSelectOption(selectOptionStr);

                // 장바구니에 다른 매장의 메뉴가 들어있는지 확인후 없다면 장바구니에 메뉴 추가
                addMenu(userId, storeId, order);
            } else {
                ParticipantOrderVO order = new ParticipantOrderVO();

                order.setRecruitId(recruitId);
                order.setParticipantId(user.getUserId());
                order.setStoreId(storeId);
                order.setMenuId(menuId);
                order.setAmount(amount);
                order.setTotalPrice(finalMenuPrice);

                String selectOptionStr = String.join(",", selectOptionList.stream().map(String::valueOf).toArray(String[]::new));
                order.setSelectOption(selectOptionStr);

                addMenuInRecruit(order);
            }
        });
    }

    // 옵션 목록을 가져오는 api
    private void setOption(int menuId) {
        retrofitService = new RetrofitService();
        menuApi = retrofitService.getRetrofit().create(MenuApi.class);

        optionList = new ArrayList<>();

        menuApi.getMenuOptionList(menuId)
                .enqueue(new Callback<>() {
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

    // 가격 수정
    public static void modifyPrice(int modifyPrice, int optionContentId, int flag) {
        finalMenuPrice = modifyPrice;
        addMenuBtn.setText(finalMenuPrice + "원 담기");

        // 옵션 추가
        if (flag == 1) {
            selectOptionList.add(optionContentId);
        } else { // 옵션 제거
            selectOptionList.remove(Integer.valueOf(optionContentId));
            for (int i = 0; i < selectOptionList.size(); i++) {
                System.out.println(selectOptionList.get(i));
            }
        }

    }

    // 장바구니에 메뉴 추가 api 호출
    private void addMenu(int userId, int storeId, OrderVO order) {
        // 레트로핏, api 초기화
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.findStoreInCart(userId, storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<OrderVO>> call, @NonNull Response<List<OrderVO>> response) {
                        List<OrderVO> orderList = response.body();

                        assert orderList != null;
                        if (orderList.size() != 0) {
                            StyleableToast.makeText(context, "장바구니에 다른 매장의 메뉴가 담겨있습니다.", R.style.errorToast).show();
                        } else {
                            orderApi.addMenu(order)
                                    .enqueue(new Callback<>() {
                                        @Override
                                        public void onResponse(@NonNull Call<OrderVO> call, @NonNull Response<OrderVO> response) {
                                            StyleableToast.makeText(context, "장바구니에 추가되었습니다.", R.style.successToast).show();
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<OrderVO> call, @NonNull Throwable t) {
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<OrderVO>> call, @NonNull Throwable t) {
                    }
                });
    }

    // 모집글 장바구니에 추가
    private void addMenuInRecruit(ParticipantOrderVO order) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.addMenu(order)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ParticipantOrderVO> call, @NonNull Response<ParticipantOrderVO> response) {
                        StyleableToast.makeText(context, "장바구니에 추가되었습니다.", R.style.successToast).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ParticipantOrderVO> call, @NonNull Throwable t) {
                    }
                });
    }
}
