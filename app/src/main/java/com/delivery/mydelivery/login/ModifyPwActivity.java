package com.delivery.mydelivery.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPwActivity extends AppCompatActivity {

    // 변경완료 dialog
    ModifyPwDialog modifyPwDialog;
    ModifyPwCancelDialog modifyPwCancelDialog;

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton closeBtn;

    EditText pwET;
    EditText pwCkET;
    Button modifyPwBtn;

    Boolean pwFlag = false; // 비밀번호 검사 통과
    Boolean pwCkFlag = false; // 비밀번호확인 검사 통과

    // 레트로핏, api, gson
    RetrofitService retrofitService;
    UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_modify_pw);

        // dialog
        modifyPwDialog = new ModifyPwDialog(this);
        modifyPwCancelDialog = new ModifyPwCancelDialog(this);

        // 툴바
        toolbar = findViewById(R.id.modifyPwToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 수정종료버튼
        closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(view -> modifyPwCancelDialog.callDialog());

        // 초기화
        pwET = findViewById(R.id.pwET);
        pwCkET = findViewById(R.id.pwCkET);
        modifyPwBtn = findViewById(R.id.modifyPwBtn);

        // 입력한 이메일
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        pwRegExCk(); // 실시간 비밀번호 정규식 검사
        pwMatchingCk(); // 실시간 비밀번호 일치 검사

        // 비밀번호 수정
        modifyPwBtn.setOnClickListener(view -> modifyPw(email, pwET.getText().toString()));
    }

    // 실시간 비밀번호 정규식 검사 메소드
    private void pwRegExCk() {
        pwET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 비밀번호, 비밀번호 확인 텍스트
                String pw = pwET.getText().toString();
                String pwCk = pwCkET.getText().toString();

                boolean flag = ckPwRegEx(pw); // 정규식 검사 성공 여부 확인

                if (flag) { // 정규식 검사 성공시 초록색으로 색상 변경, 비밀번호 확인 입력창 활성화, pwFlag를 true로 설정
                    pwET.setBackgroundResource(R.drawable.et_border_green);
                    pwCkET.setEnabled(true);
                    pwFlag = true;
                } else { // 정규식 검사 실패시 빨간색으로 색상 변경, 비밀번호 확인 입력창 비활성화, pwFlag를 false로 설정
                    pwET.setBackgroundResource(R.drawable.et_border_red);
                    pwCkET.setEnabled(false);
                    pwFlag = false;
                }

                // 비밀번호와 비밀번호 확인이 다르다면 버튼 비활성화
                if (!pw.equals(pwCk)) {
                    modifyPwBtn.setEnabled(false);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill2_gray);
                }

                if (pwFlag && pwCkFlag) {
                    modifyPwBtn.setEnabled(true);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill2_mint);
                } else {
                    modifyPwBtn.setEnabled(false);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill2_gray);
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

        pwCkET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pw = pwET.getText().toString();
                String pwCk = pwCkET.getText().toString();

                if (pwCk.equals(pw)) { // 비밀번호 일치
                    pwCkET.setBackgroundResource(R.drawable.et_border_green);
                    pwCkFlag = true;
                } else { // 불일치
                    pwCkET.setBackgroundResource(R.drawable.et_border_red);
                    pwCkFlag = false;
                }

                if (pwFlag && pwCkFlag) {
                    modifyPwBtn.setEnabled(true);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill2_mint);
                } else {
                    modifyPwBtn.setEnabled(false);
                    modifyPwBtn.setBackgroundResource(R.drawable.btn_fill2_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 비밀번호 수정
    private void modifyPw(String email, String pw) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.findUser(email)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        UserVO user = response.body();
                        assert user != null;
                        user.setPw(pw);

                        userApi.modify(user)
                                .enqueue(new Callback<UserVO>() {
                                    @Override
                                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                                        modifyPwDialog.callDialog();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {
                                    }
                                });
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {
                    }
                });
    }

    // 뒤로가기시 팝업창 출력
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

        if (keycode == KeyEvent.KEYCODE_BACK) {
            modifyPwCancelDialog.callDialog();
            return true;
        }

        return false;
    }
}
