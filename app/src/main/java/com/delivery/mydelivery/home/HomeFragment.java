package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
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
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView categoryView;
    HomeCategoryAdapter homeCategoryAdapter;
    List<CategoryVO> categoryList;

    // 레트로핏, api
    RetrofitService retrofitService;
    HomeApi api;

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

        // 카테고리 뷰 그리드뷰로 설정
        categoryView = view.findViewById(R.id.categoryRecyclerView);
        categoryView.setLayoutManager(new GridLayoutManager(context, 5));

        // 어댑터 추가, 카테고리 추가
        homeCategoryAdapter = new HomeCategoryAdapter();
        categoryView.setAdapter(homeCategoryAdapter);
        setCategory();

        return view;
    }

    // 카테고리 리스트 가져오는 api
    private void setCategory() {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(HomeApi.class);

        api.getCategoryList()
                .enqueue(new Callback<List<CategoryVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<CategoryVO>> call, @NonNull Response<List<CategoryVO>> response) {
                        categoryList = response.body();

                        // 이미지 추가
                        int[] categoryImgList = {
                                R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat,
                                R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat,
                                R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat, R.drawable.meat
                        };


                       homeCategoryAdapter.setCategoryList(categoryList, categoryImgList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<CategoryVO>> call, @NonNull Throwable t) {
                    }
                });
    }

}
