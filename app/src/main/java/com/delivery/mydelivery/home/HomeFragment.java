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
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView categoryRecyclerView;
    HomeCategoryAdapter homeCategoryAdapter;
    List<CategoryVO> categoryList;

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

        // 리사이클러뷰설정
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.addItemDecoration(new ItemDecoration()); // 여백 설정

        // 어댑터 추가, 카테고리 추가
        homeCategoryAdapter = new HomeCategoryAdapter();
        categoryRecyclerView.setAdapter(homeCategoryAdapter);
        setCategory();

        return view;
    }

    // 카테고리 리스트 가져오는 api
    private void setCategory() {
        retrofitService = new RetrofitService();
        homeApi = retrofitService.getRetrofit().create(HomeApi.class);

        homeApi.getCategoryList()
                .enqueue(new Callback<List<CategoryVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<CategoryVO>> call, @NonNull Response<List<CategoryVO>> response) {
                        categoryList = response.body();

                        // 이미지 추가
                        int[] categoryImgList = {
                                R.drawable.category_icon_meat, R.drawable.category_icon_lunch_box, R.drawable.category_icon_japanese_food, R.drawable.category_icon_baekban, R.drawable.category_icon_snack,
                                R.drawable.category_icon_asian, R.drawable.category_icon_midnight_snack, R.drawable.category_icon_western_food, R.drawable.category_icon_jokbal_bossam, R.drawable.category_icon_chinese,
                                R.drawable.category_icon_stew, R.drawable.category_icon_chicken, R.drawable.category_icon_cafe_dessert, R.drawable.category_icon_fast_food, R.drawable.category_icon_pizza
                        };


                        homeCategoryAdapter.setCategoryList(categoryList, categoryImgList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<CategoryVO>> call, @NonNull Throwable t) {
                    }
                });
    }

    // 여백 설정
    public static class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = 40;
            outRect.right = 40;
            outRect.bottom = 60;
        }
    }

}
