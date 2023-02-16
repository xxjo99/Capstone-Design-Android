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
    Button modifyBtn;

    // 사용자 정보
    String name;
    String phoneNum;

    // 변경사항 체크 플래그
    boolean nameFlag = false;
    boolean phoneNumFlag = false;

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
        modifyBtn = findViewById(R.id.modifyBtn);

        // 사용자 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        name = user.getName();
        phoneNum = user.getPhoneNum();

        // 정보 추가
        emailET.setText(user.getEmail());
        nameET.setText(name);
        phoneNumET.setText(phoneNum);

        // 변경사항 있는지 확인
        changeNameCk();
        changePhoneNumCk();

        // 변경
        modifyBtn.setOnClickListener(view -> {
            user.setName(nameET.getText().toString());
            user.setPhoneNum(phoneNumET.getText().toString());
            modifyAmount(user);
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

    // 정보 변경
    private void modifyAmount(UserVO user) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.modify(user)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        gson = new GsonBuilder().create();
                        String userInfoJson = gson.toJson(user, UserVO.class);
                        PreferenceManager.setLoginInfo(context, userInfoJson);

                        Toast.makeText(context, "변경 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {
                    }
                });
    }

}
