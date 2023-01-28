package com.delivery.mydelivery.recruit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 카테고리 어댑터
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private final List<ParticipantVO> participantList; // 구성원 리스트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    UserApi userApi;

    // 생성자
    public MemberAdapter(List<ParticipantVO> participantList, Context context) {
        this.participantList = participantList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recruit_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.ViewHolder holder, int position) {
        ParticipantVO participant = participantList.get(position);

        // 유저 이름
        int userId = participant.getUserId();
        setParticipantName(userId, holder);

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
        TextView memberNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            memberNameTV = itemView.findViewById(R.id.memberNameTV);
        }
    }

    private void setParticipantName(int userId, MemberAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getUser(userId)
                .enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        UserVO user = response.body();
                        assert user != null;
                        holder.memberNameTV.setText(user.getName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {

                    }
                });
    }

}