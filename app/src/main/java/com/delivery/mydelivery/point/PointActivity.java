package com.delivery.mydelivery.point;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
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

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class PointActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    EditText pointET;
    TextView pointCkTV;
    Button addPointBtn;

    Button add1Btn;
    Button add3Btn;
    Button add5Btn;

    LinearLayout afterPointLayout;
    TextView afterPointTV;

    int userPoint; // 보유중인 포인트

    String applicationId = "63e5e127755e27001f59de55"; // 부트페이 id

    // retrofit, api, gson
    RetrofitService retrofitService;
    UserApi userApi;
    PointApi pointApi;
    Gson gson;

    Context context; // context

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_point);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.pointToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        pointET = findViewById(R.id.pointET);
        pointCkTV = findViewById(R.id.pointCkTV);
        addPointBtn = findViewById(R.id.addPointBtn);

        add1Btn = findViewById(R.id.add1Btn);
        add3Btn = findViewById(R.id.add3Btn);
        add5Btn = findViewById(R.id.add5Btn);

        afterPointLayout = findViewById(R.id.afterPointLayout);
        afterPointTV = findViewById(R.id.afterPointTV);

        // 현재 보유중인 포인트
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);
        userPoint = user.getPoint();

        afterPointTV.setText(userPoint + "P");

        // 전 액티비티에서 넘어온 값
        Intent intent = getIntent();
        int addPoint = intent.getIntExtra("addPoint", 0);

        if (addPoint != 0) {
            pointET.setText(addPoint + "");
            addPointBtn.setBackgroundResource(R.drawable.btn_fill_green);
            addPointBtn.setEnabled(true);
        }

        pointCk(); // 입력한 금액 검사

        // 금액 삭제
        pointET.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (pointET.getRight() - pointET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    pointET.setText(null);
                    return true;
                }
            }
            return false;
        });

        // 금액 추가
        add1Btn.setOnClickListener(view -> {
            int point = 0;

            if (!pointET.getText().toString().isEmpty()) {
                point = Integer.parseInt(pointET.getText().toString());
            }

            point += 10000;
            pointET.setText(Integer.toString(point));
        });

        add3Btn.setOnClickListener(view -> {
            int point = 0;

            if (!pointET.getText().toString().isEmpty()) {
                point = Integer.parseInt(pointET.getText().toString());
            }

            point += 30000;
            pointET.setText(Integer.toString(point));
        });

        add5Btn.setOnClickListener(view -> {
            int point = 0;

            if (!pointET.getText().toString().isEmpty()) {
                point = Integer.parseInt(pointET.getText().toString());
            }

            point += 50000;
            pointET.setText(Integer.toString(point));
        });

        // 포인트 충전
        addPointBtn.setOnClickListener(view -> {

            if (pointET.getText().toString().isEmpty()) {
                Toast.makeText(context, "금액 입력", Toast.LENGTH_SHORT).show();
            } else {
                int point = Integer.parseInt(pointET.getText().toString());
                goRequest(point);
            }
        });
    }

    // 금액 검사, 1000원 미만일시 버튼 비활성화, 색변경
    private void pointCk() {
        pointET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int point = 0;

                if (!pointET.getText().toString().isEmpty()) {
                    point = Integer.parseInt(pointET.getText().toString());
                }
                boolean flag = point >= 1000;

                if (flag) { // 입력한 금액 1000원 이상
                    pointET.setBackgroundResource(R.drawable.et_border2);
                    pointCkTV.setVisibility(View.GONE);
                    addPointBtn.setBackgroundResource(R.drawable.btn_fill_green);
                    addPointBtn.setEnabled(true);

                    // 충전 후 잔액 변경
                    afterPointLayout.setVisibility(View.VISIBLE);
                    int afterPoint = userPoint + point;
                    afterPointTV.setText(afterPoint + "P");
                } else { // 1000원 미만
                    pointET.setBackgroundResource(R.drawable.et_border_red_bottom);
                    pointCkTV.setVisibility(View.VISIBLE);
                    addPointBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                    addPointBtn.setEnabled(false);

                    afterPointLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 결제
    public void goRequest(double point) {
        // 사용자정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 결제자 정보 삽입
        BootUser bootUser = new BootUser();
        bootUser.setUsername(user.getName());

        String orderName = (int) point + "P";

        Payload payload = new Payload();
        payload.setApplicationId(applicationId)
                .setOrderName(orderName) // 상품 이름
                .setPrice(point) // 가격
                .setUser(bootUser) // 사용자
                .setOrderId(user.getUserId() + " : " + orderName); // 주문번호


        Bootpay.init(getSupportFragmentManager(), getApplicationContext())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Toast.makeText(context, "결제창 닫기", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("error", "error" + " " + data);
                    }

                    @Override
                    public void onClose() {
                        Log.d("close", "close");
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("issued", "issued" + " " + data);
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("confirm", "confirm" + " " + data);
                        return true;
                    }

                    @Override
                    public void onDone(String data) {
                        // 사용자 정보
                        String loginInfo = PreferenceManager.getLoginInfo(context);
                        Gson gson = new Gson();
                        UserVO user = gson.fromJson(loginInfo, UserVO.class);

                        // 포인트 수정
                        int userPoint = user.getPoint() + (int) point;
                        user.setPoint(userPoint);

                        // 포인트 충전 내역 추가
                        PointHistoryVO pointHistory = new PointHistoryVO();
                        pointHistory.setUserId(user.getUserId());
                        pointHistory.setPoint((int) point);
                        pointHistory.setType("충전");
                        pointHistory.setBalance(userPoint);
                        pointHistory.setContent("포인트 충전");

                        addPoint(user, pointHistory);

                        Intent pointCompleteIntent = new Intent(PointActivity.this, PointCompleteActivity.class);
                        pointCompleteIntent.putExtra("addPoint", (int) point);
                        pointCompleteIntent.putExtra("afterPoint", userPoint);
                        startActivity(pointCompleteIntent);

                        finish();
                    }

                }).requestPayment();
    }

    private void addPoint(UserVO user, PointHistoryVO pointHistory) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);
        pointApi = retrofitService.getRetrofit().create(PointApi.class);

        // 보유 포인트 변경
        userApi.modify(user)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {
                    }
                });

        // 포인트 이용내역 추가
        pointApi.addPointHistory(pointHistory)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        gson = new GsonBuilder().create();
                        String userInfoJson = gson.toJson(user, UserVO.class);
                        PreferenceManager.setLoginInfo(context, userInfoJson);
                        Toast.makeText(context, "포인트 충전 완료", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }

}