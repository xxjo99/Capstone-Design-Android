package com.delivery.mydelivery.myInfo;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPwActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    EditText currentPwET;
    EditText modifyPwET;
    EditText modifyPwCkET;
    Button modifyPwBtn;

    Boolean pwFlag = false; // 비밀번호 검사 통과
    Boolean pwCkFlag = false; // 비밀번호확인 검사 통과

    // 레트로핏, api, gson
    RetrofitService retrofitService;
    UserApi userApi;
    Gson gson;

    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_modify_pw);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.modifyPwToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 사용자 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 초기화
        currentPwET = findViewById(R.id.currentPwET);
        modifyPwET = findViewById(R.id.modifyPwET);
        modifyPwCkET = findViewById(R.id.modifyPwCkET);
        modifyPwBtn = findViewById(R.id.modifyPwBtn);

        pwRegExCk(); // 실시간 비밀번호 정규식 검사
        pwMatchingCk(); // 실시간 비밀번호 일치 검사

        modifyPwBtn.setOnClickListener(view -> {
            // 비밀번호 변경
            if (!currentPwET.getText().toString().equals(user.getPw())) {
                Toast.makeText(context, "비밀번호를 정확하게 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                user.setPw(modifyPwET.getText().toString());
                modifyPw(user);
            }
        });
    }

    // 실시간 비밀번호 정규식 검사 메소드
    private void pwRegExCk() {
        modifyPwET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 비밀번호, 비밀번호 확인 텍스트
                String pw = modifyPwET.getText().toString();
                String pwCk = modifyPwCkET.getText().toString();

                boolean flag = ckPwRegEx(pw); // 정규식 검사 성공 여부 확인

                if (flag) { // 정규식 검사 성공시 초록색으로 색상 변경, 비밀번호 확인 입력창 활성화, pwFlag를 true로 설정
                    modifyPwET.setBackgroundResource(R.drawable.et_border_green);
                    modifyPwCkET.setEnabled(true);
                    pwFlag = true;
                } else { // 정규식 검사 실패시 빨간색으로 색상 변경, 비밀번호 확인 입력창 비활성화, pwFlag를 false로 설정
                    modifyPwET.setBackgroundResource(R.drawable.et_border_red);
                    modifyPwCkET.setEnabled(false);
                    pwFlag = false;
                }

                // 비밀번호와 비밀번호 확인이 다르다면 버튼 비활성화
                if (!pw.equals(pwCk)) {
                    modifyPwBtn.setEnabled(false);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                }

                if (pwFlag && pwCkFlag) {
                    modifyPwBtn.setEnabled(true);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill_green);
                } else {
                    modifyPwBtn.setEnabled(false);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 비밀번호 정규식 검사
    private boolean ckPwRegEx(String pw) {
        return Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", pw);
    }

    // 실시간 비밀번호 확인 일치 검사
    private void pwMatchingCk() {
        modifyPwCkET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pw = modifyPwET.getText().toString();
                String pwCk = modifyPwCkET.getText().toString();

                if (pwCk.equals(pw)) { // 비밀번호 일치
                    modifyPwCkET.setBackgroundResource(R.drawable.et_border_green);
                    pwCkFlag = true;
                } else { // 불일치
                    modifyPwCkET.setBackgroundResource(R.drawable.et_border_red);
                    pwCkFlag = false;
                }

                if (pwFlag && pwCkFlag) {
                    modifyPwBtn.setEnabled(true);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill_green);
                } else {
                    modifyPwBtn.setEnabled(false);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 비밀번호 수정
    private void modifyPw(UserVO user) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.modify(user)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        gson = new GsonBuilder().create();
                        String userInfoJson = gson.toJson(user, UserVO.class);
                        PreferenceManager.setLoginInfo(context, userInfoJson);

                        Toast.makeText(ModifyPwActivity.this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {

                    }
                });
    }
}
