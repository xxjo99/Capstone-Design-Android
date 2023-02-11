package com.delivery.mydelivery.point;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointActivity extends AppCompatActivity {

    EditText pointET;
    TextView pointCkTV;
    Button addPointBtn;

    Button add1Btn;
    Button add3Btn;
    Button add5Btn;

    String applicationId = "63e5e127755e27001f59de55"; // 부트페이 id

    // retrofit, api, gson
    RetrofitService retrofitService;
    UserApi userApi;
    Gson gson;

    Context context; // context

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_point);
        context = this;

        pointET = findViewById(R.id.pointET);
        pointCkTV = findViewById(R.id.pointCkTV);
        addPointBtn = findViewById(R.id.addPointBtn);

        add1Btn = findViewById(R.id.add1Btn);
        add3Btn = findViewById(R.id.add3Btn);
        add5Btn = findViewById(R.id.add5Btn);

        pointCk(); // 입력한 금액 검사

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
                goRequest((double) point);
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
                    pointET.setBackgroundResource(R.drawable.point_edit_text_border);
                    pointCkTV.setVisibility(View.GONE);
                    addPointBtn.setBackgroundResource(R.drawable.btn_border_round_green);
                    addPointBtn.setEnabled(true);
                } else { // 1000원 미만
                    pointET.setBackgroundResource(R.drawable.point_edit_text_border_red);
                    pointCkTV.setVisibility(View.VISIBLE);
                    addPointBtn.setBackgroundResource(R.drawable.btn_border_round_gray);
                    addPointBtn.setEnabled(false);
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
                        Toast.makeText(context, "포인트 충전 완료", Toast.LENGTH_SHORT).show();

                        // 사용자 정보
                        String loginInfo = PreferenceManager.getLoginInfo(context);
                        Gson gson = new Gson();
                        UserVO user = gson.fromJson(loginInfo, UserVO.class);

                        // 포인트 수정
                        int userPoint = user.getPoint() + (int) point;
                        user.setPoint(userPoint);
                        modifyPoint(user);
                    }

                }).requestPayment();
    }

    private void modifyPoint(UserVO user) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.modifyPoint(user)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                        // api를 통해 받아온 유저정보가 들어있는 객체를 json으로 변환
                        gson = new GsonBuilder().create();
                        String userInfoJson = gson.toJson(user, UserVO.class);

                        // 변환된 데이터를 sharedPreference에 저장 -> 로그인 유지 기능
                        PreferenceManager.setLoginInfo(context, userInfoJson);
                    }

                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {

                    }
                });
    }

}