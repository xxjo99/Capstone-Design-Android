package com.delivery.mydelivery.register;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;

import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 이메일 인증 액티비티
public class AuthRegisterActivity extends AppCompatActivity {

    // 회원가입 종료 dialog
    RegisterCancelDialog registerCancelDialog;

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton closeBtn;

    EditText emailET;
    EditText authNumET;
    Button sendAuthNumBtn;
    Button checkAuthNumBtn;
    Button nextBtn;

    // 레트로핏, api
    RetrofitService retrofitService;
    RegisterApi api;

    String sentAuthNum; // 전송된 인증번호

    UserVO userVO; // 데이터를 담을 객체

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_auth);
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

        // 이전 액티비티에서 넘어온 값을 객체에 저장
        userVO = (UserVO) getIntent().getSerializableExtra("userVO");
        String email = userVO.getEmail(); // 이메일

        // 초기화
        emailET = findViewById(R.id.emailET);
        authNumET = findViewById(R.id.authNumET);
        sendAuthNumBtn = findViewById(R.id.sendAuthNumBtn);
        checkAuthNumBtn = findViewById(R.id.checkAuthNumBtn);
        nextBtn = findViewById(R.id.nextBtn);

        // 이메일 출력
        emailET.setText(email);

        // 인증번호 전송
        sendAuthNumBtn.setOnClickListener(view -> {
            sendAuthNum(email); // 해당 이메일로 인증번호 전송
        });

        // 인증번호 일치 여부 확인
        checkAuthNumBtn.setOnClickListener(view -> {
            String authNum = authNumET.getText().toString();

            if (authNum.isEmpty()) { // 공백
                StyleableToast.makeText(context, "인증번호를 입력해주세요.", R.style.warningToast).show();
            } else {
                if (authNum.equals(sentAuthNum)) { // 인증 성공
                    StyleableToast.makeText(context, "인증에 성공했습니다.", R.style.successToast).show();

                    authNumET.setEnabled(false); // 인증번호를 입력하지 못하도록 입력창 비활성화

                    checkAuthNumBtn.setEnabled(false); // 인증번호 검사버튼 비활성화
                    checkAuthNumBtn.setBackgroundColor(getColor(R.color.gray2));

                    nextBtn.setEnabled(true);
                    nextBtn.setBackgroundColor(getColor(R.color.mint));
                } else { // 인증 실패
                    StyleableToast.makeText(context, "인증번호를 확인해주세요.", R.style.errorToast).show();
                }
            }
        });

        // 다음 액티비티로 이동
        nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AuthRegisterActivity.this, PasswordRegisterActivity.class);
            intent.putExtra("userVO", userVO);
            startActivity(intent);
            finish();
        });
    }

    // 이메일 전송
    private void sendAuthNum(String email) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(RegisterApi.class);

        sendAuthNumBtn = findViewById(R.id.sendAuthNumBtn);
        authNumET = findViewById(R.id.authNumET);
        checkAuthNumBtn = findViewById(R.id.checkAuthNumBtn);

        // 로딩바 구현
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(progressBar)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        api.sendAuthNum(email)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        StyleableToast.makeText(context, "이메일이 전송되었습니다.", R.style.messageToast).show();
                        sentAuthNum = response.body();

                        // 인증번호 입력, 전송 비활성화, 인증번호 검증버튼 활성화, 색 변경
                        authNumET.setEnabled(true);
                        sendAuthNumBtn.setEnabled(false);
                        sendAuthNumBtn.setBackgroundColor(getColor(R.color.gray2));

                        checkAuthNumBtn.setEnabled(true);
                        checkAuthNumBtn.setBackgroundColor(getColor(R.color.mint));

                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        dialog.dismiss();
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
