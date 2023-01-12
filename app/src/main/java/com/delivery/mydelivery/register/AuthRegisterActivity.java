package com.delivery.mydelivery.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 이메일 인증 액티비티
public class AuthRegisterActivity extends AppCompatActivity {

    TextView emailTV;
    EditText authNumET;
    Button sendAuthNumBtn;
    Button checkAuthNumBtn;
    Button nextBtn;

    // 레트로핏, api
    RetrofitService retrofitService;
    RegisterApi api;

    String sentAuthNum; // 전송된 인증번호

    UserVO userVO; // 데이터를 담을 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_auth);

        userVO = (UserVO) getIntent().getSerializableExtra("userVO"); // 전 액티비티에서 넘어온 객체를 받아서 새로 생성
        String email = userVO.getEmail(); // 전 액티비티에서 넘어온 이메일을 가져옴

        // xml 변수 초기화
        emailTV = findViewById(R.id.emailTV);
        authNumET = findViewById(R.id.authNumET);
        sendAuthNumBtn = findViewById(R.id.sendAuthNumBtn);
        checkAuthNumBtn = findViewById(R.id.checkAuthNumBtn);
        nextBtn = findViewById(R.id.nextBtn);

        // emailTV 받아온 이메일 출력
        emailTV.setText(email);

        // 인증번호 전송
        sendAuthNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAuthNum(email); // 해당 이메일로 인증번호 전송
            }
        });

        // 인증번호 일치 여부 확인
        checkAuthNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authNum = authNumET.getText().toString();

                if (authNum.isEmpty()) { // 공백
                    Toast.makeText(AuthRegisterActivity.this, "인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (authNum.equals(sentAuthNum)) { // 인증에 성공시
                        Toast.makeText(AuthRegisterActivity.this, "인증에 성공했습니다.", Toast.LENGTH_SHORT).show();

                        authNumET.setEnabled(false); // 인증번호를 입력하지 못하도록 입력창 비활성화
                        checkAuthNumBtn.setEnabled(false); // 인증번호 검사버튼 비활성화
                        nextBtn.setVisibility(View.VISIBLE); // 다음 액티비티 이동버튼 활성화
                    } else { // 인증에 실패시
                        Toast.makeText(AuthRegisterActivity.this, "인증번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 다음페이지로 이동
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userVO = (UserVO) getIntent().getSerializableExtra("userVO"); // 전 액티비티에서 넘어온 객체를 받아서 새로 생성

                Intent intent = new Intent(AuthRegisterActivity.this, PasswordRegisterActivity.class);
                intent.putExtra("userVO", userVO);
                startActivity(intent);
                finish();
            }
        });
    }

    // 이메일 전송 api 호출
    private void sendAuthNum(String email) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(RegisterApi.class);

        sendAuthNumBtn = findViewById(R.id.sendAuthNumBtn);
        authNumET = findViewById(R.id.authNumET);
        checkAuthNumBtn = findViewById(R.id.checkAuthNumBtn);

        api.sendAuthNum(email)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(AuthRegisterActivity.this, "인증번호를 전송했습니다.", Toast.LENGTH_SHORT).show();

                        // 인증번호 전송버튼 막고 인증번호 입력, 검증 버튼 활성화
                        sendAuthNumBtn.setEnabled(false);
                        authNumET.setEnabled(true);
                        checkAuthNumBtn.setEnabled(true);

                        sentAuthNum = response.body();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
    }
}
