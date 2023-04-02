package com.delivery.mydelivery.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.order.OrderListActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class StoreActivity extends AppCompatActivity {

    // 툴바, 뒤로가기 버튼, 학교
    Toolbar toolbar;
    ImageButton backBtn;
    @SuppressLint("StaticFieldLeak")
    public static TextView categoryTV;

    // 탭 레이아웃, 뷰페이저
    TabLayout categoryTab;
    ViewPager2 storeListViewPager;

    // 뷰페이저 어댑터
    StoreViewPagerAdapter storeViewPagerAdapter;

    // context
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_store);
        context = this; // context 지정

        // 툴바
        toolbar = findViewById(R.id.storeToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());

        // 사용자 학교이름 지정
        categoryTV = findViewById(R.id.categoryTV);

        // 탭 레이아웃, 뷰페이저 초기화
        categoryTab = findViewById(R.id.categoryTab);
        storeListViewPager = findViewById(R.id.storeListViewPager);

        // 뷰페이저 어댑터 초기화
        storeViewPagerAdapter = new StoreViewPagerAdapter(this);

        // 뷰페이저 설정
        storeListViewPager.setAdapter(storeViewPagerAdapter);

        // 전 액티비티에서 누른 카테고리로 이동
        Intent intent = getIntent();
        int categoryPosition = intent.getIntExtra("categoryPosition", 0);
        storeListViewPager.setCurrentItem(categoryPosition);
        storeListViewPager.setOffscreenPageLimit(1);

        // 탭 추가
        new TabLayoutMediator(categoryTab, storeListViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("한식");
                    categoryTV.setText("한식");
                    break;

                case 1:
                    tab.setText("일식");
                    categoryTV.setText("일식");
                    break;

                case 2:
                    tab.setText("중식");
                    categoryTV.setText("중식");
                    break;

                case 3:
                    tab.setText("양식");
                    categoryTV.setText("양식");
                    break;

                case 4:
                    tab.setText("고기");
                    categoryTV.setText("고기");
                    break;

                case 5:
                    tab.setText("치킨");
                    categoryTV.setText("치킨");
                    break;

                case 6:
                    tab.setText("햄버거");
                    categoryTV.setText("햄버거");
                    break;

                case 7:
                    tab.setText("피자");
                    categoryTV.setText("피자");
                    break;

                case 8:
                    tab.setText("아시안");
                    categoryTV.setText("아시안");
                    break;

                case 9:
                    tab.setText("분식");
                    categoryTV.setText("분식");
                    break;
            }
        }).attach();

        storeListViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        categoryTV.setText("한식");
                        break;

                    case 1:
                        categoryTV.setText("일식");
                        break;

                    case 2:
                        categoryTV.setText("중식");
                        break;

                    case 3:
                        categoryTV.setText("양식");
                        break;

                    case 4:
                        categoryTV.setText("고기");
                        break;

                    case 5:
                        categoryTV.setText("치킨");
                        break;

                    case 6:
                        categoryTV.setText("햄버거");
                        break;

                    case 7:
                        categoryTV.setText("피자");
                        break;

                    case 8:
                        categoryTV.setText("아시안");
                        break;

                    case 9:
                        categoryTV.setText("분식");
                        break;
                }

                super.onPageSelected(position);
            }
        });
    }

    // 툴바 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_toolbar, menu);
        return true;
    }

    // 툴바 메뉴 버튼 이벤트
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.cartBtn) {
            Intent intent = new Intent(this, OrderListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
