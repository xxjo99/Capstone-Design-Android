package com.delivery.mydelivery.myInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private final List<OrderHistoryVO> orderHistoryList; // 주문 리스트
    Context context; // context

    RetrofitService retrofitService;
    StoreApi storeApi;

    // 생성자
    public OrderHistoryAdapter(List<OrderHistoryVO> orderHistoryList, Context context) {
        this.orderHistoryList = orderHistoryList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_myinfo_order_history, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderHistoryVO orderHistory = orderHistoryList.get(position);

        // 매장 이미지, 이름
        getStoreInfo(orderHistory.getStoreId(), holder);

        // 배달 일자
        Timestamp dateTime = orderHistory.getDeliveryDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd (E요일)", Locale.KOREAN);
        String time = dateFormat.format(dateTime);
        holder.deliveryDateTV.setText(time);

        holder.participantCountTV.setText(orderHistory.getParticipantCount() + "명"); // 참여인원
        holder.paymentMoneyTV.setText(orderHistory.getPaymentMoney() + "P"); // 지불금액

        System.out.println(orderHistory.getDeliveryCompletePicture());

        // 상세주문내역으로 이동
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, OrderHistoryDetailActivity.class);
            intent.putExtra("recruitId", orderHistory.getRecruitId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (orderHistoryList != null) {
            return orderHistoryList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView storeIV;
        TextView deliveryDateTV;
        TextView storeNameTV;
        TextView participantCountTV;
        TextView paymentMoneyTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeIV = itemView.findViewById(R.id.storeIV);
            deliveryDateTV = itemView.findViewById(R.id.deliveryDateTV);
            storeNameTV = itemView.findViewById(R.id.storeNameTV);
            participantCountTV = itemView.findViewById(R.id.participantCountTV);
            paymentMoneyTV = itemView.findViewById(R.id.paymentMoneyTV);
        }
    }

    private void getStoreInfo(int storeId, @NonNull OrderHistoryAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();

                        String text = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png";
                        Glide.with(context).load(/*storeImage*/ text).placeholder(R.drawable.ic_launcher_background).override(100, 100).into(holder.storeIV);
                        holder.storeNameTV.setText(Objects.requireNonNull(store).getStoreName());

                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {

                    }
                });
    }

}
