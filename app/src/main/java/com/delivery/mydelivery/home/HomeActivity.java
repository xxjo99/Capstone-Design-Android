package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.user.order.OrderListActivity;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

// 홈화면 액티비티
public class HomeActivity extends AppCompatActivity {

    // 보여질 프래그먼트
    private HomeFragment homeFragment;
    private RecruitListFragment recruitListFragment;
    private SearchFragment searchFragment;
    private MyInfoFragment myInfoFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_home);

        // 프래그먼트 초기화
        homeFragment = new HomeFragment();
        recruitListFragment = new RecruitListFragment();
        searchFragment = new SearchFragment();
        myInfoFragment = new MyInfoFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, homeFragment).commit(); // 처음 보여질 프래그먼트

        // 툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        NavigationBarView navigation = findViewById(R.id.bottomNavigationView); // 하단바

        // 네비게이션 클릭 이벤트
        navigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_home_home: // 카테고리 이동
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, homeFragment).commit();
                    return true;

                case R.id.menu_home_post: // 모집글 리스트 이동
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, recruitListFragment).commit();
                    return true;

                case R.id.menu_home_search: // 검색 이동
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, searchFragment).commit();
                    return true;

                case R.id.menu_home_myInfo: // 내 정보 이동
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, myInfoFragment).commit();
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cartBtn:
                Intent intent = new Intent(this, OrderListActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
