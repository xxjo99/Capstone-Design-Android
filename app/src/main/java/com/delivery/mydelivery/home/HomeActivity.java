package com.delivery.mydelivery.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.R;
import com.google.android.material.navigation.NavigationBarView;

// 홈화면 액티비티
public class HomeActivity extends AppCompatActivity {

    // 보여질 프래그먼트
    private CategoryFragment categoryFragment;
    private PostFragment postFragment;
    private MyPostFragment myPostFragment;
    private MyInfoFragment myInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 프래그먼트 초기화
        categoryFragment = new CategoryFragment();
        postFragment = new PostFragment();
        myPostFragment = new MyPostFragment();
        myInfoFragment = new MyInfoFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, categoryFragment).commit(); // 처음 보여질 프래그먼트

        NavigationBarView navigation = findViewById(R.id.bottomNavigationView); // 하단바

        // 네비게이션 클릭 이벤트
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_category: // 카테고리 이동
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, categoryFragment).commit();
                        return true;

                    case R.id.menu_post: // 모집글 리스트 이동
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, postFragment).commit();
                        return true;

                    case R.id.menu_myPost: // 내 모집글 이동
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, myPostFragment).commit();
                        return true;

                    case R.id.menu_myInfo: // 내 정보 이동
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentFrame, myInfoFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
