package com.delivery.mydelivery.myInfo;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

import java.util.List;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInfoActivity extends AppCompatActivity {

    // 툴바, 뒤로가기 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    EditText emailET;
    EditText nameET;
    EditText phoneNumET;
    AutoCompleteTextView schoolAutoCompleteTV;
    Button modifyBtn;

    // 사용자 정보
    String name;
    String phoneNum;
    String school;

    // 변경사항 체크 플래그
    boolean nameFlag = false;
    boolean phoneNumFlag = false;
    boolean schoolFlag = false;

    private List<String> schoolList; // 학교 리스트

    Context context;

    // 레트로핏, api, gson
    RetrofitService retrofitService;
    UserApi userApi;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_myinfo);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.myInfoToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 초기화
        emailET = findViewById(R.id.emailET);
        nameET = findViewById(R.id.nameET);
        phoneNumET = findViewById(R.id.phoneNumET);
        schoolAutoCompleteTV = findViewById(R.id.schoolAutoCompleteTV);
        modifyBtn = findViewById(R.id.modifyBtn);

        // 하이픈 자동 입력
        phoneNumET.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // 학교리스트 추가, 어댑터 연결
        setSchoolAutoCompleteTV();

        // 사용자 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        name = user.getName();
        phoneNum = user.getPhoneNum();
        school = user.getSchool();

        // 정보 추가
        emailET.setText(user.getEmail());
        nameET.setText(name);
        phoneNumET.setText(phoneNum);
        schoolAutoCompleteTV.setText(school);

        // 변경사항 있는지 확인
        changeNameCk();
        changePhoneNumCk();
        changeSchoolCk();

        // 변경
        modifyBtn.setOnClickListener(view -> {
            user.setName(nameET.getText().toString());
            user.setPhoneNum(phoneNumET.getText().toString());
            user.setSchool(schoolAutoCompleteTV.getText().toString());
            modifyAmount(user);
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

    // 변경사항 있는지 확인
    private void changeNameCk() {
        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String changedName = nameET.getText().toString();

                if (changedName.isEmpty()) {
                    modifyBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                    modifyBtn.setEnabled(false);
                    nameFlag = false;
                } else if (changedName.equals(name)) {
                    modifyBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                    modifyBtn.setEnabled(false);
                    nameFlag = false;
                } else {
                    nameFlag = true;
                    if (phoneNumET.length() != 0) {
                        modifyBtn.setBackgroundResource(R.drawable.btn_fill_green);
                        modifyBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void changePhoneNumCk() {
        phoneNumET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String changedPhoneNum = phoneNumET.getText().toString();

                if (changedPhoneNum.isEmpty()) {
                    modifyBtn.setEnabled(false);
                    phoneNumFlag = false;
                } else if (changedPhoneNum.equals(phoneNum)) {
                    modifyBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                    modifyBtn.setEnabled(false);
                    phoneNumFlag = false;
                } else {
                    phoneNumFlag = true;
                    if (nameET.length() != 0) {
                        modifyBtn.setBackgroundResource(R.drawable.btn_fill_green);
                        modifyBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void changeSchoolCk() {
        phoneNumET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String changedSchool = schoolAutoCompleteTV.getText().toString();

                if (changedSchool.isEmpty()) {
                    modifyBtn.setEnabled(false);
                    schoolFlag = false;
                } else if (changedSchool.equals(phoneNum)) {
                    modifyBtn.setBackgroundResource(R.drawable.btn_fill_gray);
                    modifyBtn.setEnabled(false);
                    schoolFlag = false;
                } else {
                    schoolFlag = true;
                    if (schoolAutoCompleteTV.length() != 0) {
                        modifyBtn.setBackgroundResource(R.drawable.btn_fill_green);
                        modifyBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 정보 변경
    private void modifyAmount(UserVO user) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.modify(user)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        gson = new GsonBuilder().create();
                        String userInfoJson = gson.toJson(user, UserVO.class);
                        PreferenceManager.setLoginInfo(context, userInfoJson);
                        StyleableToast.makeText(context, "변경이 완료되었습니다.", R.style.successToast).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {
                    }
                });
    }

}
