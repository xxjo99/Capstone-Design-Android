package com.delivery.mydelivery.store;

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
import com.delivery.mydelivery.menu.MenuListActivity;

import java.util.List;

public class OpenedStoreListAdapter extends RecyclerView.Adapter<OpenedStoreListAdapter.ViewHolder> {

    private final List<StoreVO> storeList; // 매장 리스트
    Context context; // context

    // 생성자
    public OpenedStoreListAdapter(List<StoreVO> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_store_store_open, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OpenedStoreListAdapter.ViewHolder holder, int position) {
        StoreVO store = storeList.get(position);

        // 매장 정보들
        String storeImage = store.getStoreImageUrl();
        String storeName = store.getStoreName();
        String storeInfo = store.getStoreInfo();
        String deliveryPrice = "배달팁 " + store.getDeliveryTip() + "원";
        String deliveryTime = store.getDeliveryTime();

        // 매장정보 삽입
        Glide.with(context).load(storeImage).placeholder(R.drawable.ic_launcher_background).into(holder.storeIV);
        holder.storeNameTV.setText(storeName);
        holder.storeInfoTV.setText(storeInfo);
        holder.storeDeliveryPriceTV.setText(deliveryPrice);
        holder.deliveryTimeTV.setText(deliveryTime + "분");

        // 해당 매장 이동
        holder.itemView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, MenuListActivity.class);

            intent.putExtra("storeId", store.getStoreId());
            intent.putExtra("participantType", "등록자"); // 등록자 or 참가자인지 확인
            intent.putExtra("recruitId", -1);

            context.startActivity(intent);
        });
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
