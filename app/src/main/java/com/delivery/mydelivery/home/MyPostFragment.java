package com.delivery.mydelivery.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostFragment extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView myPostListRecyclerView;
    MyPostAdapter myPostAdapter;
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
        view = inflater.inflate(R.layout.fragment_home_my_post, container, false); // view 초기화
        assert container != null;
        context = container.getContext();

        // 리사이클러뷰 설정
        myPostListRecyclerView = view.findViewById(R.id.myPostListRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        myPostListRecyclerView.setLayoutManager(manager);
        myPostListRecyclerView.setHasFixedSize(true);

        // 유저id
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);
        int userId = user.getUserId();

        // 등록 또는 참여한 모집글 목록 불러옴
        setRecruitList(userId);

        return view;
    }

    private void setRecruitList(int userId) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(RecruitApi.class);

        api.findRecruitList(userId)
                .enqueue(new Callback<List<RecruitVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RecruitVO>> call, @NonNull Response<List<RecruitVO>> response) {
                        recruitList = response.body();
                        myPostAdapter = new MyPostAdapter(recruitList, context);
                        myPostListRecyclerView.setAdapter(myPostAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RecruitVO>> call, @NonNull Throwable t) {

                    }
                });
    }
}
