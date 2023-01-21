package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 모집글 리스트 어댑터
@SuppressLint("SetTextI18n")
public class RecruitListAdapter extends RecyclerView.Adapter<RecruitListAdapter.ViewHolder> {

    private final List<RecruitVO> recruitList; // 모집글 리스트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    UserApi userApi;

    // 생성자
    public RecruitListAdapter(List<RecruitVO> recruitList, Context context) {
        this.recruitList = recruitList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home_recruit_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecruitListAdapter.ViewHolder holder, int position) {
        RecruitVO recruit = recruitList.get(position);

        int storeId = recruit.getStoreId();
        int userId = recruit.getUserId();
        String deliveryTime = recruit.getDeliveryTime();
        int person = recruit.getPerson();
        String place = recruit.getPlace();

        setStoreName(storeId, holder); // 매장이름
        setRegistrantName(userId, holder); // 등록자 이름
        holder.deliveryTimeTV.setText(deliveryTime);
        holder.recruitPersonTV.setText(person + "명");
        holder.placeTV.setText(place);
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
        TextView storeNameTv;
        TextView registrantTV;
        TextView deliveryTimeTV;
        TextView recruitPersonTV;
        TextView placeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeNameTv = itemView.findViewById(R.id.storeNameTV);
            registrantTV = itemView.findViewById(R.id.registrantTV);
            deliveryTimeTV = itemView.findViewById(R.id.deliveryTimeTV);
            recruitPersonTV = itemView.findViewById(R.id.recruitPersonTV);
            placeTV = itemView.findViewById(R.id.placeTV);
            
            // 클릭 이벤트
            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                Toast.makeText(itemView.getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
            });
        }
    }

    // 매장이름 지정
    private void setStoreName(int storeId, RecruitListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<StoreVO>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();
                        assert store != null;
                        holder.storeNameTv.setText(store.getStoreName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 등록자 지정
    private void setRegistrantName(int userId, RecruitListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getUser(userId)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        UserVO user = response.body();
                        assert user != null;
                        holder.registrantTV.setText(user.getName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {

                    }
                });

    }
}
