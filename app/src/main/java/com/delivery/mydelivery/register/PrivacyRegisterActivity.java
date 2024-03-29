package com.delivery.mydelivery.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.List;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 개인정보 등록 액티비티
public class PrivacyRegisterActivity extends AppCompatActivity {

    // 회원가입 종료 dialog
    RegisterCancelDialog registerCancelDialog;

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton closeBtn;

    EditText nameET;
    Button duplicationCkBtn;
    EditText phoneNumET;
    AutoCompleteTextView schoolAutoCompleteTV;
    Button registerBtn;

    Boolean duplicationCk = false; // 닉네임 중복검사 통과 확인
    private List<String> schoolList; // 학교 리스트
    UserVO userVO; // 데이터를 담을 객체

    // 레트로핏, api
    RetrofitService retrofitService;
    UserApi userApi;
    RegisterApi registerApi;

    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_privacy);
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
        nameET = findViewById(R.id.nameET);
        duplicationCkBtn = findViewById(R.id.duplicationCkBtn);
        phoneNumET = findViewById(R.id.phoneNumET);
        schoolAutoCompleteTV = findViewById(R.id.schoolAutoCompleteTV);
        registerBtn = findViewById(R.id.registerBtn);

        // 하이픈 자동 입력
        phoneNumET.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // 학교리스트 추가, 어댑터 연결
        setSchoolAutoCompleteTV();

        // 닉네임 중복검사
        duplicationCkBtn.setOnClickListener(view -> {

            if (nameET.getText().toString().isEmpty()) {
                StyleableToast.makeText(context, "닉네임을 입력해주세요.", R.style.warningToast).show();
            } else {
                String name = nameET.getText().toString();
                duplicationCk(name);
            }
        });

        // 회원가입 버튼
        registerBtn.setOnClickListener(view -> {
            String name = nameET.getText().toString();
            String phoneNum = phoneNumET.getText().toString();
            String school = schoolAutoCompleteTV.getText().toString();

            if (!duplicationCk) {
                StyleableToast.makeText(context, "닉네임 중복검사를 진행해주세요.", R.style.errorToast).show();
            } else {
                if (name.isEmpty() || phoneNum.isEmpty() || school.isEmpty()) { // 입력칸중 하나라도 비어있을경우
                    StyleableToast.makeText(context, "모든 입력값은 필수입니다.", R.style.errorToast).show();
                } else {
                    register(name, phoneNum, school);
                }
            }

        });
    }

    // 리스트 추가후 schoolAutoCompleteTV에 어댑터 연결
    private void setSchoolAutoCompleteTV() {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getAllSchool()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                        schoolList = response.body();
                        schoolAutoCompleteTV.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, schoolList));
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                    }
                });
    }

    // 닉네임 중복검사
    private void duplicationCk(String name) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.findName(name)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                        Boolean duplicationNameCk = response.body();

                        if (Boolean.TRUE.equals(duplicationNameCk)) { // 중복된 닉네임 없을경우
                            duplicationCk = true;
                            StyleableToast.makeText(context, "사용가능한 닉네임입니다.", R.style.successToast).show();
                        } else {
                            duplicationCk = false;
                            StyleableToast.makeText(context, "이미 존재하는 닉네임입니다.", R.style.errorToast).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

                    }
                });
    }

    // 회원가입
    private void register(String name, String phoneNum, String school) {
        userVO = (UserVO) getIntent().getSerializableExtra("userVO"); // 이전 액티비티에서 넘어온 데이터들을 생성한 객체에 저장

        // 객체에 이름, 생년월일, 휴대폰번호 저장
        userVO.setName(name);
        userVO.setPhoneNum(phoneNum);
        userVO.setSchool(school);

        // 레트로핏, api 초기화
        retrofitService = new RetrofitService();
        registerApi = retrofitService.getRetrofit().create(RegisterApi.class);

        registerApi.register(userVO)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) { // 회원가입 성공
                        // 회원가입 완료페이지 이동
                        Intent intent = new Intent(PrivacyRegisterActivity.this, CompleteActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) { // 회원가입 실패
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
