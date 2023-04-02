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

public class StoreListHamburgerFragment extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트
    RecyclerView openedStoreListRecyclerView;
    RecyclerView closedStoreListRecyclerView;
    OpenedStoreListAdapter openedStoreListAdapter;
    ClosedStoreListAdapter closedStoreListAdapter;
    List<StoreVO> openedStoreList;
    List<StoreVO> closedStoreList;

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
        view = inflater.inflate(R.layout.fragment_store_store_list_open, container, false);
        context = getContext();

        // 유저 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 리사이클러뷰 설정
        openedStoreListRecyclerView = view.findViewById(R.id.openedStoreListRecyclerView);
        closedStoreListRecyclerView = view.findViewById(R.id.closedStoreListRecyclerView);
        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(context);
        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(context);
        openedStoreListRecyclerView.setLayoutManager(manager1);
        closedStoreListRecyclerView.setLayoutManager(manager2);
        openedStoreListRecyclerView.setHasFixedSize(true);
        closedStoreListRecyclerView.setHasFixedSize(true);

        // 리스트에 데이터 추가, 어댑터 연결
        setOpenedStoreList(user.getSchool());
        setClosedStoreList(user.getSchool());

        return view;
    }

    // 오픈한 매장리스트 추가, 어댑터 연결
    private void setOpenedStoreList(String deliveryAvailablePlace) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getOpenedStoreList("햄버거", deliveryAvailablePlace)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<StoreVO>> call, @NonNull Response<List<StoreVO>> response) {
                        openedStoreList = response.body();
                        openedStoreListAdapter = new OpenedStoreListAdapter(openedStoreList, context);
                        openedStoreListRecyclerView.setAdapter(openedStoreListAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<StoreVO>> call, @NonNull Throwable t) {

                    }
                });
    }

    // 마감한 매장리스트 추가, 어댑터 연결
    private void setClosedStoreList(String deliveryAvailablePlace) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getClosedStoreList("햄버거", deliveryAvailablePlace)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<StoreVO>> call, @NonNull Response<List<StoreVO>> response) {
                        closedStoreList = response.body();
                        closedStoreListAdapter = new ClosedStoreListAdapter(closedStoreList, context);
                        closedStoreListRecyclerView.setAdapter(closedStoreListAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<StoreVO>> call, @NonNull Throwable t) {

                    }
                });
    }

}