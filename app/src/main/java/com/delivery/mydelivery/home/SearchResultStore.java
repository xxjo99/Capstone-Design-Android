package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class SearchResultStore extends Fragment {

    // 리사이클러뷰, 어댑터, 리스트
    static RecyclerView storeRecyclerView;
    static SearchResultStoreAdapter searchResultStoreAdapter;
    static List<StoreVO> storeList;
    static LinearLayout emptyLayout;
    static NestedScrollView searchResultLayout;

    // view, context
    View view;
    static Context context;

    // 레트로핏, api
    static RetrofitService retrofitService;
    static StoreApi storeApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragment_home_search_result_store, container, false);
        context = getContext();

        // 초기화
        emptyLayout = view.findViewById(R.id.emptyLayout);
        searchResultLayout = view.findViewById(R.id.searchResultLayout);

        // 리사이클러뷰 설정
        storeRecyclerView = view.findViewById(R.id.storeRecyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        storeRecyclerView.setLayoutManager(manager);
        storeRecyclerView.setHasFixedSize(true);

        return view;
    }

    // 검색된 매장 목록
    public static void searchOpenStore(String keyword, String deliveryAvailablePlace) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.searchOpenedStore(keyword, deliveryAvailablePlace)
                .enqueue(new Callback<List<StoreVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<StoreVO>> call, @NonNull Response<List<StoreVO>> response) {
                        storeList = response.body();

                        if (Objects.requireNonNull(storeList).isEmpty()) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            searchResultLayout.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                            searchResultLayout.setVisibility(View.VISIBLE);

                            searchResultStoreAdapter = new SearchResultStoreAdapter(storeList, context);
                            storeRecyclerView.setAdapter(searchResultStoreAdapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<StoreVO>> call, @NonNull Throwable t) {

                    }
                });
    }
}