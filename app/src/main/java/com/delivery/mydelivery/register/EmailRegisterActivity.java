package com.delivery.mydelivery.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 이메일 입력 액티비티
public class EmailRegisterActivity extends AppCompatActivity {

    // 회원가입 종료 dialog
    RegisterCancelDialog registerCancelDialog;

    // 툴바, 버튼, 진행뷰
    Toolbar toolbar;
    ImageButton closeBtn;

    EditText emailET;
    Button duplicationCkBtn;
    Button nextBtn;

    // 레트로핏, api
    RetrofitService retrofitService;
    RegisterApi api;

    Context context;

    UserVO userVO; // 데이터를 담을 객체

    Boolean regExFlag = false; // 정규식 검사 성공 여부

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        context = this;

        // dialog
        registerCancelDialog = new RegisterCancelDialog(this);

        // 툴바
        toolbar = findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 회원가입 종료 버튼
        closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(view -> registerCancelDialog.callDialog());

        // 초기화
        emailET = findViewById(R.id.emailET);
        duplicationCkBtn = findViewById(R.id.duplicationCkBtn);
        nextBtn = findViewById(R.id.nextBtn);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        emailRegExCk(); // 실시간 이메일 정규식 검사

        // 중복검사
        duplicationCkBtn.setOnClickListener(view -> {
            String email = emailET.getText().toString();

            if (email.isEmpty()) { // 공백
                StyleableToast.makeText(context, "이메일을 입력해주세요.", R.style.warningToast).show();
                nextBtn.setEnabled(false);
                nextBtn.setBackgroundColor(getColor(R.color.gray2));
            } else if (!regExFlag) { // 정규식 검사 실패
                StyleableToast.makeText(context, "이메일 형식에 맞게 입력해주세요.", R.style.errorToast).show();
                nextBtn.setEnabled(false);
                nextBtn.setBackgroundColor(getColor(R.color.gray2));
            } else { // 정규식 통과
                callDuplicateCkApi(email); // 중복검사 api 호출
            }
        });

        // 다음 액티비티로 이동
        nextBtn.setOnClickListener(view -> {
            String email = emailET.getText().toString();

            // registerVO에 입력한 이메일을 저장
            userVO = new UserVO();
            userVO.setEmail(email);

            Intent intent = new Intent(EmailRegisterActivity.this, AuthRegisterActivity.class);
            intent.putExtra("userVO", userVO);
            startActivity(intent);
            finish();
        });

    }

    // 실시간 이메일 정규식 검사
    private void emailRegExCk() {
        emailET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = emailET.getText().toString();
                boolean flag = ckEmailRegEx(email);

                if (flag) { // 정규식 검사 성공시 색상 변경, regExFlag를 true로 설정
                    emailET.setBackgroundResource(R.drawable.et_border_green);
                    regExFlag = true;
                } else { // 실패시
                    emailET.setBackgroundResource(R.drawable.et_border_red);
                    regExFlag = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 이메일 정규식 검사
    private boolean ckEmailRegEx(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);

        // 이메일 형식이 맞을경우 true 반환
        return matcher.find();
    }

    // 중복검사
    private void callDuplicateCkApi(String email) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(RegisterApi.class);

        nextBtn = findViewById(R.id.nextBtn);

        api.duplicateEmailCk(email)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        boolean duplicateCkResult = Boolean.TRUE.equals(response.body()); // 중복여부 저장

                        if (duplicateCkResult) { // 중복되지 않은 이메일
                            StyleableToast.makeText(context, "사용가능한 이메일입니다.", R.style.successToast).show();
                            nextBtn.setEnabled(true);
                            nextBtn.setBackgroundColor(getColor(R.color.mint1));
                            imm.hideSoftInputFromWindow(emailET.getWindowToken(), 0);
                        } else {
                            StyleableToast.makeText(context, "중복된 이메일입니다.", R.style.errorToast).show();
                            nextBtn.setEnabled(false);
                            nextBtn.setBackgroundColor(getColor(R.color.gray));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    }
                });
    }

    // 뒤로가기시 팝업창 출력
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

        if (keycode == KeyEvent.KEYCODE_BACK) {
            registerCancelDialog.callDialog();
            return true;
        }

        return false;
    }

}

