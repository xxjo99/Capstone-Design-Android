package com.delivery.mydelivery.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;

import java.util.List;

// 장바구니 어댑터
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<OrderVO> orderList; // 주문 리스트
    Context context; // context

    // 생성자
    public OrderListAdapter(List<OrderVO> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_order_list, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        OrderVO order = orderList.get(position);

        int menuPrice = order.getTotalPrice();

        holder.menuPriceTV.setText(menuPrice + "");
    }

    @Override
    public int getItemCount() {
        if (orderList != null) {
            return orderList.size();
        } else {
            return -1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView menuNameTV;
        ImageView menuIV;
        TextView menuPriceTV;
        TextView optionTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menuNameTV = itemView.findViewById(R.id.menuNameTV);
            menuIV = itemView.findViewById(R.id.menuIV);
            menuPriceTV = itemView.findViewById(R.id.menuPriceTV);
            optionTV = itemView.findViewById(R.id.optionTV);
        }

    }
}