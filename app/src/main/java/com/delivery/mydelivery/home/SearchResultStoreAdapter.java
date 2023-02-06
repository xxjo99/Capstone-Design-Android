package com.delivery.mydelivery.home;

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
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.util.List;

@SuppressLint("SetTextI18n")
public class SearchResultStoreAdapter extends RecyclerView.Adapter<SearchResultStoreAdapter.ViewHolder> {

    private final List<StoreVO> storeList; // 모집글 리스트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;

    // 생성자
    public SearchResultStoreAdapter(List<StoreVO> storeList, Context context) {
        this.storeList = storeList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_search_store_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultStoreAdapter.ViewHolder holder, int position) {
        StoreVO store = storeList.get(position);

        String text = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png";
        String storeDeliveryPrice = "최소주문 " + store.getMinimumDeliveryPrice() + "원, 배달팁 " + store.getDeliveryTip() + "원";

        // store의 지정된 데이터들을 뷰에 세팅
        Glide.with(context).load(/*store.getStoreImageUrl()*/ text).placeholder(R.drawable.ic_launcher_background).override(100, 100).into(holder.storeIV);
        holder.storeNameTV.setText(store.getStoreName());
        holder.storeInfoTV.setText(store.getStoreInfo());
        holder.storeDeliveryPriceTV.setText(storeDeliveryPrice);
        holder.deliveryTimeTV.setText(store.getDeliveryTime() + "분");

        // 매장 이동
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MenuListActivity.class);

            intent.putExtra("storeId", store.getStoreId());
            intent.putExtra("participantType", "등록자");
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
