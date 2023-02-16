package com.delivery.mydelivery.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.register.RegisterApi;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPwActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    EditText emailET;
    Button sendAuthNumBtn;
    EditText authNumET;
    Button checkAuthNumBtn;
    Button modifyPwBtn;

    String sentAuthNum; // 인증번호

    // 레트로핏, api
    RetrofitService retrofitService;
    RegisterApi registerApi;
    UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_find_pw);

        // 툴바
        toolbar = findViewById(R.id.findPwToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 초기화
        emailET = findViewById(R.id.emailET);
        sendAuthNumBtn = findViewById(R.id.sendAuthNumBtn);
        authNumET = findViewById(R.id.authNumET);
        checkAuthNumBtn = findViewById(R.id.checkAuthNumBtn);
        modifyPwBtn = findViewById(R.id.modifyPwBtn);

        // 인증번호 전송
        sendAuthNumBtn.setOnClickListener(view -> {
            String email = emailET.getText().toString();

            if (email.isEmpty()) { // 공백
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                modifyPwBtn.setEnabled(false);
                modifyPwBtn.setBackgroundResource(R.drawable.btn_fill2_gray);
            } else { // 인증번호 전송
                sendAuthNum(email);
                Toast.makeText(this, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 입력한 인증번호 비교
        checkAuthNumBtn.setOnClickListener(view -> {
            String authNum = authNumET.getText().toString();

            if (authNum.equals(sentAuthNum)) { // 입력한 인증번호와 전송된 인증번호가 같을경우
                Toast.makeText(this, "인증에 성공했습니다.", Toast.LENGTH_SHORT).show();

                authNumET.setEnabled(false);
                checkAuthNumBtn.setEnabled(false);
                checkAuthNumBtn.setBackgroundResource(R.drawable.btn_fill2_gray);

                modifyPwBtn.setEnabled(true);
                modifyPwBtn.setBackgroundResource(R.drawable.btn_fill2_green);
            } else {
                Toast.makeText(this, "인증번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                modifyPwBtn.setEnabled(false);
                modifyPwBtn.setBackgroundResource(R.drawable.btn_fill2_gray);
            }
        });

        // 비밀번호 수정 이동
        modifyPwBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FindPwActivity.this, ModifyPwActivity.class);
            intent.putExtra("email", emailET.getText().toString());
            startActivity(intent);
            finish();
        });

    }

    // 인증번호 전송
    private void sendAuthNum(String email) {
        retrofitService = new RetrofitService();
        registerApi = retrofitService.getRetrofit().create(RegisterApi.class);
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        // 등록된 이메일인지 확인
        registerApi.duplicateEmailCk(email)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        Boolean emailCheck = response.body();

                        if (Boolean.TRUE.equals(emailCheck)) { // true일경우 등록된 이메일 없음
                            Toast.makeText(FindPwActivity.this, "이메일 주소를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        } else { // 등록된 이메일이 있을경우 입력한 이메일로 인증번호 전송

                            userApi.sendAuthNum(email)
                                    .enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                            sentAuthNum = response.body();

                                            // 입력한 이메일 변경불가, 인증번호 전송불가, 색 변경
                                            emailET.setEnabled(false);
                                            sendAuthNumBtn.setEnabled(false);
                                            sendAuthNumBtn.setBackgroundResource(R.drawable.btn_fill2_gray);

                                            // 인증번호 입력, 비교버튼 활성화, 색 변경
                                            authNumET.setEnabled(true);
                                            checkAuthNumBtn.setEnabled(true);
                                            checkAuthNumBtn.setBackgroundResource(R.drawable.btn_fill2_green);
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    }
                });
    }
}
