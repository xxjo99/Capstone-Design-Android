package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.delivery.CheckDeliveryActivity;
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
    private MyPageFragment myPageFragment;

    // 툴바, 툴바 텍스트
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;
    TextView infoTV;

    // 배달 확인 버튼
    Button checkDeliveryBtn;

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
        myPageFragment = new MyPageFragment();

        // 제일 처음 띄워주는 프래그먼트
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.homeFragmentFrame, homeFragment, "home").commitAllowingStateLoss();

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

            FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.menu_home_home: // 카테고리 이동
                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag("home"))).commit();
                    } else {
                        fragmentManager.beginTransaction().add(R.id.homeFragmentFrame, homeFragment, "home").commit();
                    }

                    if (fragmentManager.findFragmentByTag("recruit") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("recruit"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("myPost") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPost"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("search") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("search"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("myPage") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPage"))).commit();
                    }

                    toolbar.setVisibility(View.VISIBLE);
                    infoTV.setText(school);
                    return true;

                case R.id.menu_home_post: // 모집글 리스트 이동
                    if (fragmentManager.findFragmentByTag("recruit") != null) {
                        fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag("recruit"))).commit();
                    } else {
                        fragmentManager.beginTransaction().add(R.id.homeFragmentFrame, recruitListFragment, "recruit").commit();
                    }

                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("home"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("myPost") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPost"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("search") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("search"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("myPage") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPage"))).commit();
                    }

                    toolbar.setVisibility(View.VISIBLE);
                    infoTV.setText(school);
                    return true;

                case R.id.menu_home_myPost: // 나의 모집 / 등록글 이동
                    if (fragmentManager.findFragmentByTag("myPost") != null) {
                        fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPost"))).commit();
                    } else {
                        fragmentManager.beginTransaction().add(R.id.homeFragmentFrame, myPostFragment, "myPost").commit();
                    }

                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("home"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("recruit") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("recruit"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("search") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("search"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("myPage") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPage"))).commit();
                    }

                    toolbar.setVisibility(View.VISIBLE);
                    infoTV.setText(school);
                    return true;

                case R.id.menu_home_search: // 검색 이동
                    if (fragmentManager.findFragmentByTag("search") != null) {
                        fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag("search"))).commit();
                    } else {
                        fragmentManager.beginTransaction().add(R.id.homeFragmentFrame, searchFragment, "search").commit();
                    }

                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("home"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("recruit") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("recruit"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("myPost") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPost"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("myPage") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPage"))).commit();
                    }

                    toolbar.setVisibility(View.GONE);
                    return true;

                case R.id.menu_home_myPage: // 마이페이지
                    if (fragmentManager.findFragmentByTag("myPage") != null) {
                        fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPage"))).commit();
                    } else {
                        fragmentManager.beginTransaction().add(R.id.homeFragmentFrame, myPageFragment, "myPage").commit();
                    }

                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("home"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("recruit") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("recruit"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("myPost") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("myPost"))).commit();
                    }
                    if (fragmentManager.findFragmentByTag("search") != null) {
                        fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentByTag("search"))).commit();
                    }

                    toolbar.setVisibility(View.VISIBLE);
                    infoTV.setText("마이페이지");
                    return true;

                default:
                    return false;
            }


        });

        // 배달 확인 페이지 이동
        checkDeliveryBtn = findViewById(R.id.checkDeliveryBtn);
        checkDeliveryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, CheckDeliveryActivity.class);
            startActivity(intent);
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

        if (item.getItemId() == R.id.cartBtn) {
            Intent intent = new Intent(this, OrderListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
