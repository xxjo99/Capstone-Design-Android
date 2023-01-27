package com.delivery.mydelivery.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.user.UserVO;

import java.util.regex.Pattern;

// 비밀번호 등록 액티비티
public class PasswordRegisterActivity extends AppCompatActivity {

    EditText pwET;
    EditText pwCkET;
    Button nextBtn;

    UserVO userVO; // 데이터를 담을 객체

    RegisterDialog registerDialog;


    Boolean pwFlag = false; // 비밀번호 검사 통과
    Boolean pwCkFlag = false; // 비밀번호확인 검사 통과

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        // xml변수 초기화
        pwET = findViewById(R.id.pwET);
        nextBtn = findViewById(R.id.nextBtn);

        registerDialog = new RegisterDialog(this);

        pwRegExCk(); // 실시간 비밀번호 정규식 검사 메소드
        pwMatchingCk(); // 실시간 비밀번호 일치 검사 메소드

        // 다음 페이지 이동
        nextBtn.setOnClickListener(view -> {
            String pw = pwET.getText().toString(); // 입력한 비밀번호 받아옴

            userVO = (UserVO) getIntent().getSerializableExtra("userVO"); // 전 액티비티에서 넘어온 객체를 받아서 새로 생성
            userVO.setPw(pw); // 객체에 비밀번호 저장

            // 객체 데이터 받아서 다음 액티비티로 전달
            Intent intent = new Intent(PasswordRegisterActivity.this, PrivacyRegisterActivity.class);
            intent.putExtra("userVO", userVO);
            startActivity(intent);
            finish();
        });
    }

    // 실시간 비밀번호 정규식 검사 메소드
    private void pwRegExCk() {
        pwET = findViewById(R.id.pwET);
        pwCkET = findViewById(R.id.pwCkET);

        pwET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 입력한 비밀번호와 비밀번호 확인 텍스트를 받아옴
                String pw = pwET.getText().toString();
                String pwCk = pwCkET.getText().toString();

                boolean flag = ckPwRegEx(pw); // 정규식 검사 성공 여부 확인 메소드

                if (flag) { // 정규식 검사 성공시 초록색으로 색상 변경, 비밀번호 확인 입력창 가능하게 변경, pwFlag를 true로 설정
                    pwET.setBackgroundResource(R.drawable.edit_text_border_green);
                    pwCkET.setEnabled(true);
                    pwFlag = true;
                } else { // 정규식 검사 성공시 빨간색으로 색상 변경, 비밀번호 확인 입력창 불가능하게 변경, pwFlag를 false로 설정
                    pwET.setBackgroundResource(R.drawable.edit_text_border_red);
                    pwCkET.setEnabled(false);
                    pwFlag = false;
                }

                // 비밀번호와 비밀번호 확인이 다르다면 다음페이지 이동버튼 보이지 않도록 설정
                if (!pw.equals(pwCk)) {
                    nextBtn.setVisibility(View.GONE);
                }

                // 정규식 검사 통과, 비밀번호 일치시 이동버튼 보이도록 설정
                if (pwFlag && pwCkFlag) {
                    nextBtn.setVisibility(View.VISIBLE);
                } else {
                    nextBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 비밀번호 정규식 검사
    private boolean ckPwRegEx(String pw) {
        if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", pw)) {
            return false;
        } else {
            return true;
        }
    }

    // 실시간 비밀번호 확인 일치 검사 메소드
    private void pwMatchingCk() {
        pwET = findViewById(R.id.pwET);
        pwCkET = findViewById(R.id.pwCkET);

        nextBtn = findViewById(R.id.nextBtn);

        pwCkET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String pw = pwET.getText().toString();
                String pwCk = pwCkET.getText().toString();

                if (pwCk.equals(pw)) { // 비밀번호가 일치한다면
                    pwCkET.setBackgroundResource(R.drawable.edit_text_border_green);
                    pwCkFlag = true;
                } else { // 일치하지 않는다면
                    pwCkET.setBackgroundResource(R.drawable.edit_text_border_red);
                    pwCkFlag = false;
                }

                if (pwFlag && pwCkFlag) {
                    nextBtn.setVisibility(View.VISIBLE);
                } else {
                    nextBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 뒤로가기시 팝업창 출력
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

        if(keycode ==KeyEvent.KEYCODE_BACK) {
            registerDialog.callDialog();
            return true;
        }

        return false;
    }
}
