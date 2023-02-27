package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class MemberDeliveryInfoAdapter extends RecyclerView.Adapter<MemberDeliveryInfoAdapter.ViewHolder> {

    private final List<ParticipantVO> participantList; // 참가자 리스트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    UserApi userApi;
    RecruitApi recruitApi;

    // 생성자
    public MemberDeliveryInfoAdapter(List<ParticipantVO> participantList, Context context) {
        this.participantList = participantList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recruit_member_delivery_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberDeliveryInfoAdapter.ViewHolder holder, int position) {
        ParticipantVO participant = participantList.get(position);

        // 유저 이름
        int userId = participant.getUserId();
        setParticipantName(userId, holder);

        // 해당 유저의 총 주문금액
        int recruitId = participant.getRecruitId();
        serParticipantTotalPrice(recruitId, userId, holder);

        // 결제 상태
        int paymentStatus = participant.getPaymentStatus();
        if (paymentStatus == 0) {
            holder.paymentStatusTV.setText("결제대기");
            holder.paymentStatusTV.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.red));
        } else {
            holder.paymentStatusTV.setText("결제완료");
            holder.paymentStatusTV.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.mint));
        }
    }

    @Override
    public int getItemCount() {
        if (participantList != null) {
            return participantList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTV;
        TextView totalPriceTV;
        TextView paymentStatusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTV = itemView.findViewById(R.id.userNameTV);
            totalPriceTV = itemView.findViewById(R.id.totalPriceTV);
            paymentStatusTV = itemView.findViewById(R.id.paymentStatusTV);
        }
    }

    private void setParticipantName(int userId, MemberDeliveryInfoAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getUser(userId)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        UserVO user = response.body();
                        assert user != null;
                        holder.userNameTV.setText(user.getName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 총 주문금액
    private void serParticipantTotalPrice(int recruitId, int userId, MemberDeliveryInfoAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.getOrdersTotalPrice(recruitId, userId)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        Integer totalPrice = response.body();

                        if (totalPrice == null || totalPrice == 0) {
                            holder.totalPriceTV.setText("담은 메뉴 없음");
                        } else {
                            holder.totalPriceTV.setText("총 주문금액 " + totalPrice + "원");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {

                    }
                });
    }

}
