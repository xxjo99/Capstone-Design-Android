package com.delivery.mydelivery.register;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.delivery.mydelivery.MainActivity;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 개인정보 등록 액티비티
public class PrivacyRegisterActivity extends AppCompatActivity {

    EditText nameET;
    EditText birthET; // 생일
    EditText phoneNumET;
    Button registerBtn,birthBT;
    ImageButton go_back_password;


    UserVO userVO; // 데이터를 담을 객체

    RetrofitService retrofitService;
    RegisterApi registerApi;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_privacy);

        // xml 변수 초기화
        nameET = findViewById(R.id.nameET);
        birthET = findViewById(R.id.birthET);
        phoneNumET = findViewById(R.id.phoneNumET);
        registerBtn = findViewById(R.id.registerBtn);
        go_back_password = findViewById(R.id.go_back_password);
        birthBT = findViewById(R.id.birthBT);


        // 회원가입 버튼
        registerBtn.setOnClickListener(view -> {
            String name = nameET.getText().toString();
            String birth = birthET.getText().toString();
            String phoneNum = phoneNumET.getText().toString();

            if (name.isEmpty() || birth.isEmpty() || phoneNum.isEmpty()) { // 입력칸중 하나라도 비어있을경우
                Toast.makeText(PrivacyRegisterActivity.this, "빈칸 입력", Toast.LENGTH_SHORT).show();
            } else {
                register(name, birth, phoneNum);
            }
        });

        go_back_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_back_password = new Intent(PrivacyRegisterActivity.this, PasswordRegisterActivity.class);
                startActivity(go_back_password);
                finish();
            }
        });
    }

    // 회원가입 api 호출
    private void register(String name, String birth, String phoneNum) {
        userVO = (UserVO) getIntent().getSerializableExtra("userVO"); // 이전 액티비티에서 넘어온 데이터들을 생성한 객체에 저장

        // 객체에 이름, 생년월일, 휴대폰번호 저장
        userVO.setName(name);
        userVO.setBirth(birth);
        userVO.setPhoneNum(phoneNum);

        System.out.println(userVO.toString());

        // 레트로핏, api 초기화
        retrofitService = new RetrofitService();
        registerApi = retrofitService.getRetrofit().create(RegisterApi.class);

        registerApi.register(userVO)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) { // 회원가입 성공
                        Toast.makeText(PrivacyRegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                        // 메인액티비티로 넘어가고 종료
                        Intent intent = new Intent(PrivacyRegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) { // 회원가입 실패
                    }
                });
    }
    // 생일 표시
    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }
    // 생일 표시
    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);

        birthET.setText(year_string+"."+day_string+"."+month_string);
    }


}
