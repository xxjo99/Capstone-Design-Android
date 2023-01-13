package com.delivery.mydelivery.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.MainActivity;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.home.HomeActivity;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.register.UserVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 로그인 액티비티
public class LoginActivity extends AppCompatActivity {

    EditText emailET;
    EditText pwET;
    Button loginBtn;

    RetrofitService retrofitService;
    LoginApi api;
    Gson gson;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this; // context 지정

        // xml 변수 초기화
        emailET = findViewById(R.id.emailET);
        pwET = findViewById(R.id.pwET);
        loginBtn = findViewById(R.id.loginBtn);

        // 로그인 버튼 이벤트
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String pw = pwET.getText().toString();

                // 유효성 검사
                if (email.isEmpty() && pw.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pw.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else { // 유효성 검사 통과시 로그인 진행
                    login(email, pw);
                }
            }
        });
    }

    // 로그인 api 호출
    private void login(String email, String pw) {
        // 레트로핏, api 초기화
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(LoginApi.class);

        api.login(email, pw)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                        UserVO userInfo = response.body(); // 유저 정보를 받아옴

                        // api를 통해 받아온 유저정보가 들어있는 객체를 json으로 변환
                        gson = new GsonBuilder().create();
                        String userInfoJson = gson.toJson(userInfo, UserVO.class);

                        // 변환된 데이터를 sharedPreference에 저장 -> 로그인 유지 기능
                        PreferenceManager.setLoginInfo(context, userInfoJson);

                        // 어플 홈화면 진입후 로그인 종료
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;
                        mainActivity.finish();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) { // 로그인 실패시
                        Toast.makeText(LoginActivity.this, "이메일 또는 비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
