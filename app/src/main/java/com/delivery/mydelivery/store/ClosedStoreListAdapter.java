package com.delivery.mydelivery.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;

import java.util.List;

public class ClosedStoreListAdapter extends RecyclerView.Adapter<ClosedStoreListAdapter.ViewHolder> {

    private final List<StoreVO> storeList; // 매장 리스트
    Context context; // context

    // 생성자
    public ClosedStoreListAdapter(List<StoreVO> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_store_store_close, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClosedStoreListAdapter.ViewHolder holder, int position) {
        StoreVO store = storeList.get(position);

        // 매장 정보들
        String storeImage = store.getStoreImageUrl();
        String storeName = store.getStoreName();
        String storeInfo = store.getStoreInfo();
        String deliveryPrice = "최소주문 " + store.getMinimumDeliveryPrice() + "원, 배달팁 " + store.getDeliveryTip() + "원";
        String deliveryTime = store.getDeliveryTime();

        String text = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png";

        // 매장정보 삽입
        Glide.with(context).load(/*storeImage*/ text).placeholder(R.drawable.ic_launcher_background).override(100, 100).into(holder.storeIV);
        holder.storeNameTV.setText(storeName);
        holder.storeInfoTV.setText(storeInfo);
        holder.storeDeliveryPriceTV.setText(deliveryPrice);
        holder.deliveryTimeTV.setText(deliveryTime + "분");
    }

    @Override
    public int getItemCount() {
        if (storeList != null) {
            return storeList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView storeIV;
        TextView storeNameTV;
        TextView storeInfoTV;
        TextView storeDeliveryPriceTV;
        TextView deliveryTimeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storeIV = itemView.findViewById(R.id.storeIV);
            storeNameTV = itemView.findViewById(R.id.storeNameTV);
            storeInfoTV = itemView.findViewById(R.id.storeInfoTV);
            storeDeliveryPriceTV = itemView.findViewById(R.id.storeDeliveryPriceTV);
            deliveryTimeTV = itemView.findViewById(R.id.deliveryTimeTV);
        }
    }
}
