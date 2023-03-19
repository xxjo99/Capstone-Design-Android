package com.delivery.mydelivery.delivery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.recruit.RecruitVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 모집글 리스트 어댑터
@SuppressLint("SetTextI18n")
public class ReceivedDeliveryListAdapter extends RecyclerView.Adapter<ReceivedDeliveryListAdapter.ViewHolder> {

    private final List<RecruitVO> recruitList; // 모집글 리스트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    UserApi userApi;

    // 생성자
    public ReceivedDeliveryListAdapter(List<RecruitVO> recruitList, Context context) {
        this.recruitList = recruitList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_delivery_received, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedDeliveryListAdapter.ViewHolder holder, int position) {
        RecruitVO recruit = recruitList.get(position);

        // 매장이름 지정
        int storeId = recruit.getStoreId();
        setStoreName(storeId, holder);

        // 배달 장소 지정
        int userId = recruit.getUserId();
        String place = recruit.getPlace();
        setPlace(userId, place, holder);

        // 알림전송 액티비티 이동
        holder.sendCompleteMessageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, SendCompleteMessageActivity.class);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (recruitList != null) {
            return recruitList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView storeNameTV;
        TextView placeTV;
        Button sendCompleteMessageBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeNameTV = itemView.findViewById(R.id.storeNameTV);
            placeTV = itemView.findViewById(R.id.placeTV);
            sendCompleteMessageBtn = itemView.findViewById(R.id.sendCompleteMessageBtn);
        }
    }

    // 매장이름 지정
    private void setStoreName(int storeId, ReceivedDeliveryListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();
                        assert store != null;
                        holder.storeNameTV.setText(store.getStoreName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 장소 지정
    private void setPlace(int userId, String place, ReceivedDeliveryListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getUser(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        UserVO user = response.body();
                        String school = Objects.requireNonNull(user).getSchool();

                        holder.placeTV.setText(school + " " + place);
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {

                    }
                });
    }

}
