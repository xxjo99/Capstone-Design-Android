package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.order.OrderListActivity;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.user.UserVO;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.Objects;

// 홈화면 액티비티
public class HomeActivity extends AppCompatActivity {

    // 보여질 프래그먼트
    private HomeFragment homeFragment;
    private RecruitListFragment recruitListFragment;
    private MyPostFragment myPostFragment;
    private SearchFragment searchFragment;
    private MyInfoFragment myInfoFragment;

    // 툴바, 툴바 텍스트
    Toolbar toolbar;
    TextView infoTV;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_home);

        // 프래그먼트 초기화
        homeFragment = new HomeFragment();
        recruitListFragment = new RecruitListFragment();
        myPostFragment = new MyPostFragment();
        searchFragment = new SearchFragment();
        myInfoFragment = new MyInfoFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, homeFragment).commit(); // 처음 보여질 프래그먼트

        // 유저 정보
        String loginInfo = PreferenceManager.getLoginInfo(this);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 툴바 설정
        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        infoTV = findViewById(R.id.infoTV);
        String school = user.getSchool();
        infoTV.setText(school);

        NavigationBarView navigation = findViewById(R.id.bottomNavigationView); // 하단바

        // 네비게이션 클릭 이벤트
        navigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_home_home: // 카테고리 이동
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, homeFragment).commit();
                    toolbar.setVisibility(View.VISIBLE);
                    infoTV.setText(school);
                    return true;

                case R.id.menu_home_post: // 모집글 리스트 이동
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, recruitListFragment).commit();
                    toolbar.setVisibility(View.VISIBLE);
                    infoTV.setText(school);
                    return true;

                case R.id.menu_home_myPost: // 나의 모집 / 등록글 이동
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, myPostFragment).commit();
                    toolbar.setVisibility(View.VISIBLE);
                    infoTV.setText(school);
                    return true;

                case R.id.menu_home_search: // 검색 이동
                    FragmentTransaction fT = getSupportFragmentManager().beginTransaction();
                    fT.replace(R.id.homeFragmentFrame, searchFragment);
                    fT.addToBackStack(null);
                    fT.commit();
                    toolbar.setVisibility(View.GONE);
                    return true;

                case R.id.menu_home_myInfo: // 내 정보 이동
                    getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, myInfoFragment).commit();
                    toolbar.setVisibility(View.VISIBLE);
                    infoTV.setText("내정보");
                    return true;
            }
            return false;
        });
    }

    // 툴바 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar, menu);
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
