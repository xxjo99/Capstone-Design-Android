package com.delivery.mydelivery.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.MainActivity;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.firebase.FirebaseMessagingService;
import com.delivery.mydelivery.home.HomeActivity;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.register.EmailRegisterActivity;
import com.delivery.mydelivery.user.UserVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 로그인 액티비티
public class LoginActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    EditText emailET;
    EditText pwET;
    Button loginBtn;

    TextView registerTV;
    TextView findPwTV;

    RetrofitService retrofitService;
    LoginApi loginApi;
    Gson gson;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_login);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 초기화
        emailET = findViewById(R.id.emailET);
        pwET = findViewById(R.id.pwET);
        loginBtn = findViewById(R.id.loginBtn);

        pwET.setTransformationMethod(new CustomPasswordTransformationMethod()); // 마스킹문자 변경

        // 로그인 버튼 이벤트
        loginBtn.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            String pw = pwET.getText().toString();

            // 유효성 검사
            if (email.isEmpty() && pw.isEmpty()) {
                StyleableToast.makeText(context, "이메일과 비밀번호를 입력해주세요.", R.style.warningToast).show();
            } else if (email.isEmpty()) {
                StyleableToast.makeText(context, "이메일을 입력해주세요.", R.style.warningToast).show();
            } else if (pw.isEmpty()) {
                StyleableToast.makeText(context, "비밀번호를 입력해주세요.", R.style.warningToast).show();
            } else { // 유효성 검사 통과시 로그인 진행
                login(email, pw);
            }
        });

        // 회원가입 이동
        registerTV = findViewById(R.id.registerTV);
        registerTV.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, EmailRegisterActivity.class);
            startActivity(intent);
            finish();
        });

        // 비밀번호 찾기 이동
        findPwTV = findViewById(R.id.findPwTV);
        findPwTV.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, FindPwActivity.class);
            startActivity(intent);
        });
    }

    // 로그인 api 호출
    private void login(String email, String pw) {
        // 레트로핏, api 초기화
        retrofitService = new RetrofitService();
        loginApi = retrofitService.getRetrofit().create(LoginApi.class);

        loginApi.login(email, pw)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        UserVO userInfo = response.body(); // 유저 정보를 받아옴

                        // api를 통해 받아온 유저정보가 들어있는 객체를 json으로 변환
                        gson = new GsonBuilder().create();
                        String userInfoJson = gson.toJson(userInfo, UserVO.class);

                        // 변환된 데이터를 sharedPreference에 저장 -> 로그인 유지 기능
                        PreferenceManager.setLoginInfo(context, userInfoJson);

                        FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService(email);

                        // 어플 홈화면 진입후 로그인 종료
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        MainActivity mainActivity = MainActivity.mainActivity;
                        mainActivity.finish();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) { // 로그인 실패시
                        StyleableToast.makeText(context, "이메일 또는 비밀번호를 다시 입력해주세요.", R.style.errorToast).show();
                    }
                });
    }

}
