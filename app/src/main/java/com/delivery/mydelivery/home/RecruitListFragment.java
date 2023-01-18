package com.delivery.mydelivery.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.order.OrderApi;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitListFragment extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView recruitListRecyclerView;
    RecruitListAdapter recruitListAdapter;
    List<RecruitVO> recruitList;

    // view, context
    View view;
    Context context;

    // 레트로핏, api
    RetrofitService retrofitService;
    RecruitApi api;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragment_home_recruit_list, container, false); // view 초기화
        assert container != null;
        context = container.getContext();

        // 리사이클러뷰 설정
        recruitListRecyclerView = view.findViewById(R.id.recruitListRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recruitListRecyclerView.setLayoutManager(manager);
        recruitListRecyclerView.setHasFixedSize(true);

        // 모집글 목록 불러옴
        setRecruitList();

        return view;
    }

    // 모집글 목록 생성
    private void setRecruitList() {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(RecruitApi.class);

        api.getRecruitList()
                .enqueue(new Callback<List<RecruitVO>>() {
                    @Override
                    public void onResponse(Call<List<RecruitVO>> call, Response<List<RecruitVO>> response) {
                        recruitList = response.body();
                        recruitListAdapter = new RecruitListAdapter(recruitList, context);
                        recruitListRecyclerView.setAdapter(recruitListAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<RecruitVO>> call, Throwable t) {

                    }
                });
    }
}
