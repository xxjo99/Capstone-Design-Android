package com.delivery.mydelivery.keyword;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.delivery.mydelivery.R;

import java.util.Objects;

public class KeywordActivity extends AppCompatActivity {

    // 툴바, 툴바 버튼
    Toolbar toolbar;
    ImageButton backBtn;

    LinearLayout koreanLayout, japaneseLayout, chineseLayout, westernLayout, meatLayout, chickenLayout, pizzaLayout, snackLayout, asianLayout, hamburgerLayout;
    TextView koreanTV, japaneseTV, chineseTV, westernTV, meatTV, chickenTV, pizzaTV, snackTV, asianTV, hamburgerTV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_keyword);

        // 툴바
        toolbar = findViewById(R.id.keywordToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

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

        koreanLayout.setOnClickListener(view -> {
            if (koreanLayout.isSelected()) {
                // 선택이 해제된 경우
                koreanLayout.setSelected(false);
                koreanLayout.setBackgroundResource(R.drawable.layout_border_gray);
                koreanTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                koreanLayout.setSelected(true);
                koreanLayout.setBackgroundResource(R.drawable.layout_border_mint);
                koreanTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        japaneseLayout.setOnClickListener(view -> {
            if (japaneseLayout.isSelected()) {
                // 선택이 해제된 경우
                japaneseLayout.setSelected(false);
                japaneseLayout.setBackgroundResource(R.drawable.layout_border_gray);
                japaneseTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                japaneseLayout.setSelected(true);
                japaneseLayout.setBackgroundResource(R.drawable.layout_border_mint);
                japaneseTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        chineseLayout.setOnClickListener(view -> {
            if (chineseLayout.isSelected()) {
                // 선택이 해제된 경우
                chineseLayout.setSelected(false);
                chineseLayout.setBackgroundResource(R.drawable.layout_border_gray);
                chineseTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                chineseLayout.setSelected(true);
                chineseLayout.setBackgroundResource(R.drawable.layout_border_mint);
                chineseTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        westernLayout.setOnClickListener(view -> {
            if (westernLayout.isSelected()) {
                // 선택이 해제된 경우
                westernLayout.setSelected(false);
                westernLayout.setBackgroundResource(R.drawable.layout_border_gray);
                westernTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                westernLayout.setSelected(true);
                westernLayout.setBackgroundResource(R.drawable.layout_border_mint);
                westernTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        meatLayout.setOnClickListener(view -> {
            if (meatLayout.isSelected()) {
                // 선택이 해제된 경우
                meatLayout.setSelected(false);
                meatLayout.setBackgroundResource(R.drawable.layout_border_gray);
                meatTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                meatLayout.setSelected(true);
                meatLayout.setBackgroundResource(R.drawable.layout_border_mint);
                meatTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        chickenLayout.setOnClickListener(view -> {
            if (chickenLayout.isSelected()) {
                // 선택이 해제된 경우
                chickenLayout.setSelected(false);
                chickenLayout.setBackgroundResource(R.drawable.layout_border_gray);
                chickenTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                chickenLayout.setSelected(true);
                chickenLayout.setBackgroundResource(R.drawable.layout_border_mint);
                chickenTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        pizzaLayout.setOnClickListener(view -> {
            if (pizzaLayout.isSelected()) {
                // 선택이 해제된 경우
                pizzaLayout.setSelected(false);
                pizzaLayout.setBackgroundResource(R.drawable.layout_border_gray);
                pizzaTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                pizzaLayout.setSelected(true);
                pizzaLayout.setBackgroundResource(R.drawable.layout_border_mint);
                pizzaTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        snackLayout.setOnClickListener(view -> {
            if (snackLayout.isSelected()) {
                // 선택이 해제된 경우
                snackLayout.setSelected(false);
                snackLayout.setBackgroundResource(R.drawable.layout_border_gray);
                snackTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                snackLayout.setSelected(true);
                snackLayout.setBackgroundResource(R.drawable.layout_border_mint);
                snackTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        asianLayout.setOnClickListener(view -> {
            if (asianLayout.isSelected()) {
                // 선택이 해제된 경우
                asianLayout.setSelected(false);
                asianLayout.setBackgroundResource(R.drawable.layout_border_gray);
                asianTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                asianLayout.setSelected(true);
                asianLayout.setBackgroundResource(R.drawable.layout_border_mint);
                asianTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });

        hamburgerLayout.setOnClickListener(view -> {
            if (hamburgerLayout.isSelected()) {
                // 선택이 해제된 경우
                hamburgerLayout.setSelected(false);
                hamburgerLayout.setBackgroundResource(R.drawable.layout_border_gray);
                hamburgerTV.setTextColor(ContextCompat.getColor(this, R.color.gray2));
            } else {
                // 선택된 경우
                hamburgerLayout.setSelected(true);
                hamburgerLayout.setBackgroundResource(R.drawable.layout_border_mint);
                hamburgerTV.setTextColor(ContextCompat.getColor(this, R.color.black2));
            }
        });
    }
}
