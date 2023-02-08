package com.delivery.mydelivery.store;

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
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListJapaneseFragment extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView storeListRecyclerView;
    StoreListAdapter storeListAdapter;
    List<StoreVO> storeList;

    // view, context
    View view;
    Context context;

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragment_store_store_list, container, false);
        context = getContext();

        // 유저 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 리사이클러뷰 설정
        storeListRecyclerView = view.findViewById(R.id.storeListRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        storeListRecyclerView.setLayoutManager(manager);
        storeListRecyclerView.setHasFixedSize(true);

        // 리스트에 데이터 추가, 어댑터 연결
        setStoreList(user.getSchool());

        return view;
    }

    // 매장리스트 추가, 어댑터 연결
    private void setStoreList(String deliveryAvailablePlace) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStoreList("돈까스, 회, 일식", deliveryAvailablePlace)
                .enqueue(new Callback<List<StoreVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<StoreVO>> call, @NonNull Response<List<StoreVO>> response) {
                        storeList = response.body();
                        storeListAdapter = new StoreListAdapter(storeList, context);
                        storeListRecyclerView.setAdapter(storeListAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<StoreVO>> call, @NonNull Throwable t) {

                    }
                });
    }

}