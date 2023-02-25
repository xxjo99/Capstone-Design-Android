package com.delivery.mydelivery.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.order.OrderListActivity;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.recruit.RecruitApi;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreActivity;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitListFragment extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트, 레이아웃
    RecyclerView recruitListRecyclerView;
    RecruitListAdapter recruitListAdapter;
    List<RecruitVO> recruitList;
    LinearLayout emptyLayout;
    NestedScrollView recruitLayout;

    Button storeListBtn; // 매장 이동 버튼

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

        // 유저 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 초기화
        emptyLayout = view.findViewById(R.id.emptyLayout);
        recruitLayout = view.findViewById(R.id.recruitLayout);
        storeListBtn = view.findViewById(R.id.storeListBtn);

        // 리사이클러뷰 설정
        recruitListRecyclerView = view.findViewById(R.id.recruitListRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        recruitListRecyclerView.setLayoutManager(manager);
        recruitListRecyclerView.setHasFixedSize(true);

        // 모집글 목록 불러옴
        setRecruitList(user.getSchool());

        // 매장 이동 버튼
        storeListBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), StoreActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // 모집글 목록 생성
    private void setRecruitList(String registrantPlace) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(RecruitApi.class);

        api.getRecruitList(registrantPlace)
                .enqueue(new Callback<List<RecruitVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RecruitVO>> call, @NonNull Response<List<RecruitVO>> response) {
                        recruitList = response.body();

                        if (Objects.requireNonNull(recruitList).isEmpty()) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            recruitLayout.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                            recruitLayout.setVisibility(View.VISIBLE);

                            recruitListAdapter = new RecruitListAdapter(recruitList, context);
                            recruitListRecyclerView.setAdapter(recruitListAdapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RecruitVO>> call, @NonNull Throwable t) {

                    }
                });
    }

}
