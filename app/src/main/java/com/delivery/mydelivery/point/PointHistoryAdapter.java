package com.delivery.mydelivery.point;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@SuppressLint("SetTextI18n")
public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.ViewHolder> {

    private final List<PointHistoryVO> pointHistoryList; // 주문 리스트
    Context context; // context

    // 생성자
    public PointHistoryAdapter(List<PointHistoryVO> pointHistoryList, Context context) {
        this.pointHistoryList = pointHistoryList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_point_history, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull PointHistoryAdapter.ViewHolder holder, int position) {
        PointHistoryVO pointHistory = pointHistoryList.get(position);

        String type = pointHistory.getType();
        int point = pointHistory.getPoint();
        Timestamp dateTime = pointHistory.getDateTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd\nHH:mm", Locale.getDefault());
        String time = dateFormat.format(dateTime);
        int balance = pointHistory.getBalance();

        holder.pointHistoryTV.setText(type);
        if (type.equals("충전")) {
            holder.pointTV.setText("+" + point + "P");
        } else {
            holder.pointTV.setText("-" + point + "P");
        }
        holder.dateTimeTV.setText(time);
        holder.balanceTV.setText("잔액 " + balance + "P");
    }

    @Override
    public int getItemCount() {
        if (pointHistoryList != null) {
            return pointHistoryList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pointHistoryTV;
        TextView pointTV;
        TextView dateTimeTV;
        TextView balanceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pointHistoryTV = itemView.findViewById(R.id.pointHistoryTV);
            pointTV = itemView.findViewById(R.id.pointTV);
            dateTimeTV = itemView.findViewById(R.id.dateTimeTV);
            balanceTV = itemView.findViewById(R.id.balanceTV);
        }
    }
}
