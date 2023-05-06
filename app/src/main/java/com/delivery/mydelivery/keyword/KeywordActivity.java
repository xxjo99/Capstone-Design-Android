package com.delivery.mydelivery.keyword;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeywordActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    LinearLayout koreanLayout, japaneseLayout, chineseLayout, westernLayout, meatLayout, chickenLayout, pizzaLayout, snackLayout, asianLayout, hamburgerLayout;
    TextView koreanTV, japaneseTV, chineseTV, westernTV, meatTV, chickenTV, pizzaTV, snackTV, asianTV, hamburgerTV;
    Button saveBtn;

    boolean koreanFlag, japaneseFlag, chineseFlag, westernFlag, meatFlag, chickenFlag, pizzaFlag, snackFlag, asianFlag, hamburgerFlag = false;
    String[] categoryList = {"한식", "일식", "중식", "양식", "고기", "치킨", "피자", "분식", "아시안", "햄버거"};
    int selectCount = 0;

    // 레트로핏, api
    RetrofitService retrofitService;
    KeywordApi keywordApi;

    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_keyword);
        context = this;

        // 툴바
        toolbar = findViewById(R.id.keywordToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 초기화
        koreanLayout = findViewById(R.id.koreanLayout);
        japaneseLayout = findViewById(R.id.japaneseLayout);
        chineseLayout = findViewById(R.id.chineseLayout);
        westernLayout = findViewById(R.id.westernLayout);
        meatLayout = findViewById(R.id.meatLayout);
        chickenLayout = findViewById(R.id.chickenLayout);
        pizzaLayout = findViewById(R.id.pizzaLayout);
        snackLayout = findViewById(R.id.snackLayout);
        asianLayout = findViewById(R.id.asianLayout);
        hamburgerLayout = findViewById(R.id.hamburgerLayout);

        koreanTV = findViewById(R.id.koreanTV);
        japaneseTV = findViewById(R.id.japaneseTV);
        chineseTV = findViewById(R.id.chineseTV);
        westernTV = findViewById(R.id.westernTV);
        meatTV = findViewById(R.id.meatTV);
        chickenTV = findViewById(R.id.chickenTV);
        pizzaTV = findViewById(R.id.pizzaTV);
        snackTV = findViewById(R.id.snackTV);
        asianTV = findViewById(R.id.asianTV);
        hamburgerTV = findViewById(R.id.hamburgerTV);

        saveBtn = findViewById(R.id.saveBtn);

        // 사용자 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        getKeywordList(user.getUserId()); // 선택한 키워드 리스트

        for (String category : categoryList) {
            // 선택이 되었는지 확인
            switch (category) {

                case "한식":
                    if (koreanLayout.isSelected()) {
                        koreanFlag = true;
                        selectCount++;
                    }
                    break;

                case "일식":
                    if (japaneseLayout.isSelected()) {
                        japaneseFlag = true;
                        selectCount++;
                    }
                    break;

                case "중식":
                    if (chineseLayout.isSelected()) {
                        chineseFlag = true;
                        selectCount++;
                    }
                    break;

                case "양식":
                    if (westernLayout.isSelected()) {
                        westernFlag = true;
                        selectCount++;
                    }
                    break;

                case "고기":
                    if (meatLayout.isSelected()) {
                        meatFlag = true;
                        selectCount++;
                    }
                    break;

                case "치킨":
                    if (chickenLayout.isSelected()) {
                        chickenFlag = true;
                        selectCount++;
                    }
                    break;

                case "피자":
                    if (pizzaLayout.isSelected()) {
                        pizzaFlag = true;
                        selectCount++;
                    }
                    break;

                case "분식":
                    if (snackLayout.isSelected()) {
                        snackFlag = true;
                        selectCount++;
                    }
                    break;

                case "아시안":
                    if (asianLayout.isSelected()) {
                        asianFlag = true;
                        selectCount++;
                    }
                    break;

                case "햄버거":
                    if (hamburgerLayout.isSelected()) {
                        hamburgerFlag = true;
                        selectCount++;
                    }
                    break;
            }
        }

        koreanLayout.setOnClickListener(view -> {
            if (koreanLayout.isSelected()) {
                // 선택이 해제된 경우
                koreanLayout.setSelected(false);
                koreanLayout.setBackgroundResource(R.drawable.layout_border_gray);
                koreanTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                koreanFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    koreanLayout.setSelected(true);
                    koreanLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    koreanTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    koreanFlag = true;
                    selectCount++;
                }
            }
        });

        japaneseLayout.setOnClickListener(view -> {
            if (japaneseLayout.isSelected()) {
                // 선택이 해제된 경우
                japaneseLayout.setSelected(false);
                japaneseLayout.setBackgroundResource(R.drawable.layout_border_gray);
                japaneseTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                japaneseFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    japaneseLayout.setSelected(true);
                    japaneseLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    japaneseTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    japaneseFlag = true;
                    selectCount++;
                }
            }
        });

        chineseLayout.setOnClickListener(view -> {
            if (chineseLayout.isSelected()) {
                // 선택이 해제된 경우
                chineseLayout.setSelected(false);
                chineseLayout.setBackgroundResource(R.drawable.layout_border_gray);
                chineseTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                chineseFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    chineseLayout.setSelected(true);
                    chineseLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    chineseTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    chineseFlag = true;
                    selectCount++;
                }
            }
        });

        westernLayout.setOnClickListener(view -> {
            if (westernLayout.isSelected()) {
                // 선택이 해제된 경우
                westernLayout.setSelected(false);
                westernLayout.setBackgroundResource(R.drawable.layout_border_gray);
                westernTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                westernFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    westernLayout.setSelected(true);
                    westernLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    westernTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    westernFlag = true;
                    selectCount++;
                }
            }
        });

        meatLayout.setOnClickListener(view -> {
            if (meatLayout.isSelected()) {
                // 선택이 해제된 경우
                meatLayout.setSelected(false);
                meatLayout.setBackgroundResource(R.drawable.layout_border_gray);
                meatTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                meatFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    meatLayout.setSelected(true);
                    meatLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    meatTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    meatFlag = true;
                    selectCount++;
                }
            }
        });

        chickenLayout.setOnClickListener(view -> {
            if (chickenLayout.isSelected()) {
                // 선택이 해제된 경우
                chickenLayout.setSelected(false);
                chickenLayout.setBackgroundResource(R.drawable.layout_border_gray);
                chickenTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                chickenFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    chickenLayout.setSelected(true);
                    chickenLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    chickenTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    chickenFlag = true;
                    selectCount++;
                }
            }
        });

        pizzaLayout.setOnClickListener(view -> {
            if (pizzaLayout.isSelected()) {
                // 선택이 해제된 경우
                pizzaLayout.setSelected(false);
                pizzaLayout.setBackgroundResource(R.drawable.layout_border_gray);
                pizzaTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                pizzaFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    pizzaLayout.setSelected(true);
                    pizzaLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    pizzaTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    pizzaFlag = true;
                    selectCount++;
                }
            }
        });

        snackLayout.setOnClickListener(view -> {
            if (snackLayout.isSelected()) {
                // 선택이 해제된 경우
                snackLayout.setSelected(false);
                snackLayout.setBackgroundResource(R.drawable.layout_border_gray);
                snackTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                snackFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    snackLayout.setSelected(true);
                    snackLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    snackTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    snackFlag = true;
                    selectCount++;
                }
            }
        });

        asianLayout.setOnClickListener(view -> {
            if (asianLayout.isSelected()) {
                // 선택이 해제된 경우
                asianLayout.setSelected(false);
                asianLayout.setBackgroundResource(R.drawable.layout_border_gray);
                asianTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                asianFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    asianLayout.setSelected(true);
                    asianLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    asianTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    asianFlag = true;
                    selectCount++;
                }
            }
        });

        hamburgerLayout.setOnClickListener(view -> {
            if (hamburgerLayout.isSelected()) {
                // 선택이 해제된 경우
                hamburgerLayout.setSelected(false);
                hamburgerLayout.setBackgroundResource(R.drawable.layout_border_gray);
                hamburgerTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
                hamburgerFlag = false;
                selectCount--;
            } else {
                // 선택된 경우
                if (selectCount >= 3) {
                    Toast.makeText(context, "최대 3개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    hamburgerLayout.setSelected(true);
                    hamburgerLayout.setBackgroundResource(R.drawable.layout_border_mint);
                    hamburgerTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
                    hamburgerFlag = true;
                    selectCount++;
                }
            }
        });

        // 저장
        saveBtn.setOnClickListener(view -> setKeyword(user.getUserId()));
    }

    // 선택한 키워드 리스트
    private void getKeywordList(int userId) {
        retrofitService = new RetrofitService();
        keywordApi = retrofitService.getRetrofit().create(KeywordApi.class);

        keywordApi.getKeywordList(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<KeywordVO>> call, @NonNull Response<List<KeywordVO>> response) {
                        List<KeywordVO> keywordList = response.body();
                        selectCount = Objects.requireNonNull(keywordList).size();

                        for (KeywordVO keyword : Objects.requireNonNull(keywordList)) {

                            switch (keyword.getKeyword()) {

                                case "한식":
                                    koreanLayout.setSelected(true);
                                    koreanLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    koreanTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    koreanFlag = true;
                                    break;

                                case "일식":
                                    japaneseLayout.setSelected(true);
                                    japaneseLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    japaneseTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    japaneseFlag = true;
                                    break;

                                case "중식":
                                    chineseLayout.setSelected(true);
                                    chineseLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    chineseTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    chineseFlag = true;
                                    break;

                                case "양식":
                                    westernLayout.setSelected(true);
                                    westernLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    westernTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    westernFlag = true;
                                    break;

                                case "고기":
                                    meatLayout.setSelected(true);
                                    meatLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    meatTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    meatFlag = true;
                                    break;

                                case "치킨":
                                    chickenLayout.setSelected(true);
                                    chickenLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    chickenTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    chickenFlag = true;
                                    break;

                                case "피자":
                                    pizzaLayout.setSelected(true);
                                    pizzaLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    pizzaTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    pizzaFlag = true;
                                    break;

                                case "분식":
                                    snackLayout.setSelected(true);
                                    snackLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    snackTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    snackFlag = true;
                                    break;

                                case "아시안":
                                    asianLayout.setSelected(true);
                                    asianLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    asianTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    asianFlag = true;
                                    break;

                                case "햄버거":
                                    hamburgerLayout.setSelected(true);
                                    hamburgerLayout.setBackgroundResource(R.drawable.layout_border_mint);
                                    hamburgerTV.setTextColor(ContextCompat.getColor(context, R.color.black2));
                                    hamburgerFlag = true;
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<KeywordVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 키워드 등록 및 수정
    private void setKeyword(int userId) {
        retrofitService = new RetrofitService();
        keywordApi = retrofitService.getRetrofit().create(KeywordApi.class);

        keywordApi.getKeywordList(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<KeywordVO>> call, @NonNull Response<List<KeywordVO>> response) {
                        List<String> keywordList = new ArrayList<>();
                        List<String> removedKeywordList = new ArrayList<>();

                        for (KeywordVO keyword : Objects.requireNonNull(response.body())) {
                            keywordList.add(keyword.getKeyword());
                            removedKeywordList.add(keyword.getKeyword());
                        }

                        // 선택하지 않은 키워드는 삭제
                        for (String keyword : Objects.requireNonNull(keywordList)) {

                            switch (keyword) {

                                case "한식":
                                    if (!koreanFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "일식":
                                    if (!japaneseFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "중식":
                                    if (!chineseFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "양식":
                                    if (!westernFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "고기":
                                    if (!meatFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "치킨":
                                    if (!chickenFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "피자":
                                    if (!pizzaFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "분식":
                                    if (!snackFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "아시안":
                                    if (!asianFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;

                                case "햄버거":
                                    if (!hamburgerFlag) {
                                        deleteKeyword(userId, keyword);
                                        removedKeywordList.remove(keyword);
                                    }
                                    break;
                            }
                        }

                        // 키워드 저장, 이전에 이미 선택한 키워드는 제외
                        for (String category : categoryList) {

                            // 선택이 되었는지 확인
                            switch (category) {

                                case "한식":
                                    if (koreanFlag) {
                                        // 이전에 저장한 키워드가 아니라면 키워드 추가
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "일식":
                                    if (japaneseFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "중식":
                                    if (chineseFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "양식":
                                    if (westernFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "고기":
                                    if (meatFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "치킨":
                                    if (chickenFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "피자":
                                    if (pizzaFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "분식":
                                    if (snackFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "아시안":
                                    if (asianFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;

                                case "햄버거":
                                    if (hamburgerFlag) {
                                        if (!removedKeywordList.contains(category)) {
                                            addKeyword(userId, category);
                                        }
                                    }
                                    break;
                            }

                        }

                        StyleableToast.makeText(context, "저장이 완료되었습니다.", R.style.successToast).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<KeywordVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 키워드 추가
    private void addKeyword(int userId, String keyword) {
        retrofitService = new RetrofitService();
        keywordApi = retrofitService.getRetrofit().create(KeywordApi.class);

        keywordApi.addKeyword(userId, keyword)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }

    // 키워드 삭제
    private void deleteKeyword(int userId, String keyword) {
        retrofitService = new RetrofitService();
        keywordApi = retrofitService.getRetrofit().create(KeywordApi.class);

        keywordApi.deleteKeyword(userId, keyword)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }

}
