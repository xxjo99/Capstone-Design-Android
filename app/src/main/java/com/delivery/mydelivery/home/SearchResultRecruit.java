package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class SearchResultRecruit extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트
    static RecyclerView recruitRecyclerView;
    static SearchResultRecruitAdapter searchResultRecruitAdapter;
    static List<RecruitVO> recruitList;

    // view, context
    static View view;
    static Context context;

    // 레트로핏, api
    static RetrofitService retrofitService;
    static RecruitApi recruitApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragment_home_search_result_recruit, container, false);
        context = getContext();

        // 리사이클러뷰 설정
        recruitRecyclerView = view.findViewById(R.id.recruitRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recruitRecyclerView.setLayoutManager(manager);
        recruitRecyclerView.setHasFixedSize(true);

        return view;
    }

    // 검색된 모집글 목록
    public static void searchRecruitResult(String keyword, String deliveryAvailablePlace) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.searchRecruit(keyword, deliveryAvailablePlace)
                .enqueue(new Callback<List<RecruitVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RecruitVO>> call, @NonNull Response<List<RecruitVO>> response) {
                        recruitList = response.body();
                        searchResultRecruitAdapter = new SearchResultRecruitAdapter(recruitList, context);
                        recruitRecyclerView.setAdapter(searchResultRecruitAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RecruitVO>> call, @NonNull Throwable t) {

                    }
                });
    }
}