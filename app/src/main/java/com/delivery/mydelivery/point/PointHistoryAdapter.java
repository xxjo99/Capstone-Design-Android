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

        // 일자
        Timestamp dateTime = pointHistory.getDateTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd\nHH:mm", Locale.getDefault());
        String time = dateFormat.format(dateTime);

        String content = pointHistory.getContent(); // 내용
        int point = pointHistory.getPoint(); // 사용 or 충전 포인트
        String type = pointHistory.getType(); // 사용 or 충전
        int balance = pointHistory.getBalance(); // 잔액

        holder.dateTimeTV.setText(time); // 일자
        holder.pointHistoryTV.setText(content); // 내용

        // 포인트 이용 타입
        if (type.equals("사용")) { // 포인트 사용 (결제, 패널티 등)
            holder.pointTV.setText("-" + point + "P");
        } else { // 포인트 충전, 환불
            holder.pointTV.setText("+" + point + "P");
        }

        holder.typeTV.setText(type); // 포인트 이용 타입
        holder.balanceTV.setText("잔액 " + balance + "P"); // 포인트 이용 후 남은 잔액
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
        TextView dateTimeTV;
        TextView pointHistoryTV;
        TextView pointTV;
        TextView typeTV;
        TextView balanceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTimeTV = itemView.findViewById(R.id.dateTimeTV);
            pointHistoryTV = itemView.findViewById(R.id.pointHistoryTV);
            pointTV = itemView.findViewById(R.id.pointTV);
            typeTV = itemView.findViewById(R.id.typeTV);
            balanceTV = itemView.findViewById(R.id.balanceTV);
        }
    }
}
