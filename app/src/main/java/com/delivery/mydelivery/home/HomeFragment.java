package com.delivery.mydelivery.home;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    // 슬라이드 배너, 배너 리스트, 어댑터
    ViewPager2 slideViewPager;
    SlideViewPagerAdapter slideViewPagerAdapter;
    CircleIndicator3 indicator;
    int pageCount = 2;

    // 카테고리 리사이클러뷰, 어댑터, 리스트
    RecyclerView categoryRecyclerView;
    HomeCategoryAdapter homeCategoryAdapter;
    List<CategoryVO> categoryList;

    // 모집글 리스트 리사이클러뷰, 어댑터, 리스트
    RecyclerView recruitRecyclerView;
    HomeRecruitListAdapter homeRecruitListAdapter;
    List<RecruitVO> recruitList;

    // 레트로핏, api
    RetrofitService retrofitService;
    HomeApi homeApi;
    RecruitApi recruitApi;

    // view, context
    View view;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragment_home_home, container, false);
        assert container != null;
        context = container.getContext();

        // 유저 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 뷰페이저
        slideViewPager = view.findViewById(R.id.slideViewPager);
        // 어댑터 연결
        slideViewPagerAdapter = new SlideViewPagerAdapter(requireActivity(), pageCount);
        slideViewPager.setAdapter(slideViewPagerAdapter);
        // Indicator
        indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(slideViewPager);
        indicator.createIndicators(pageCount, 0);
        // 뷰페이저 설정
        slideViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        slideViewPager.setCurrentItem(1000); //시작 지점
        slideViewPager.setOffscreenPageLimit(2); //최대 이미지 수

        // 페이지 슬라이드 시 Indicator 변경, 마지막 이미지에서 슬라이드할 경우 첫 슬라이드로 이동
        slideViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    slideViewPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position % pageCount);
            }
        });

        // 카테고리 리사이클러뷰 설정
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        categoryRecyclerView.setHasFixedSize(true);

        // 어댑터 추가, 카테고리 추가
        homeCategoryAdapter = new HomeCategoryAdapter();
        categoryRecyclerView.setAdapter(homeCategoryAdapter);
        setCategory();

        // 모집글 리스트 리사이클러뷰 설정
        recruitRecyclerView = view.findViewById(R.id.recruitRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recruitRecyclerView.setLayoutManager(manager);
        recruitRecyclerView.setHasFixedSize(true);

        // 모집글 리스트 추가
        setRecruitList(user.getSchool());

        return view;
    }

    // 카테고리 리스트 추가
    private void setCategory() {
        retrofitService = new RetrofitService();
        homeApi = retrofitService.getRetrofit().create(HomeApi.class);

        homeApi.getCategoryList()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<CategoryVO>> call, @NonNull Response<List<CategoryVO>> response) {
                        categoryList = response.body();

                        // 이미지 추가
                        int[] categoryImgList = {
                                R.drawable.img_korean, R.drawable.img_chicken, R.drawable.img_chicken, R.drawable.img_chicken, R.drawable.img_chicken,
                                R.drawable.img_chicken, R.drawable.img_chicken, R.drawable.img_chicken, R.drawable.img_chicken, R.drawable.img_chicken
                        };


                        homeCategoryAdapter.setCategoryList(categoryList, categoryImgList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<CategoryVO>> call, @NonNull Throwable t) {
                    }
                });
    }

    // 모집글 목록 생성
    private void setRecruitList(String registrantPlace) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getRecruitListOrder(registrantPlace)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RecruitVO>> call, @NonNull Response<List<RecruitVO>> response) {
                        recruitList = response.body();

                        homeRecruitListAdapter = new HomeRecruitListAdapter(recruitList, context);
                        recruitRecyclerView.setAdapter(homeRecruitListAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RecruitVO>> call, @NonNull Throwable t) {

                    }
                });
    }

}
