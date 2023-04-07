package com.delivery.mydelivery.myInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.menu.MenuApi;
import com.delivery.mydelivery.menu.MenuVO;
import com.delivery.mydelivery.order.OrderApi;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetTextI18n")
public class OrderHistoryDetailAdapter extends RecyclerView.Adapter<OrderHistoryDetailAdapter.ViewHolder> {

    private final List<OrderHistoryDetailVO> orderHistoryDetailList; // 주문 리스트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    MenuApi menuApi;
    OrderApi orderApi;

    // 생성자
    public OrderHistoryDetailAdapter(List<OrderHistoryDetailVO> orderHistoryDetailList, Context context) {
        this.orderHistoryDetailList = orderHistoryDetailList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_myinfo_order_history_detail, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull OrderHistoryDetailAdapter.ViewHolder holder, int position) {
        OrderHistoryDetailVO orderHistoryDetail = orderHistoryDetailList.get(position);

        setMenuName(orderHistoryDetail.getMenuId(), holder);
        setContentNameList(orderHistoryDetail.getSelectOption(), holder);

        NumberFormat numberFormat = NumberFormat.getInstance();
        String menuTotalPrice = numberFormat.format(orderHistoryDetail.getTotalPrice());
        holder.menuPriceTV.setText(menuTotalPrice + "원");

        holder.amountTV.setText(orderHistoryDetail.getAmount() + "개");
    }

    @Override
    public int getItemCount() {
        if (orderHistoryDetailList != null) {
            return orderHistoryDetailList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView menuNameTV;
        TextView optionListTV;
        TextView menuPriceTV;
        TextView amountTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menuNameTV = itemView.findViewById(R.id.menuNameTV);
            optionListTV = itemView.findViewById(R.id.optionListTV);
            menuPriceTV = itemView.findViewById(R.id.menuPriceTV);
            amountTV = itemView.findViewById(R.id.amountTV);
        }
    }

    // 메뉴이름 지정
    private void setMenuName(int menuId, OrderHistoryDetailAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        menuApi = retrofitService.getRetrofit().create(MenuApi.class);

        menuApi.getMenu(menuId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<MenuVO> call, @NonNull Response<MenuVO> response) {
                        MenuVO menu = response.body();
                        assert menu != null;
                        holder.menuNameTV.setText(menu.getMenuName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<MenuVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 선택한 옵션 내용
    private void setContentNameList(String contentNameList, @NonNull OrderHistoryDetailAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.getContentNameList(contentNameList)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                        List<String> contentNameResult = response.body();

                        holder.optionListTV.setText("");

                        if (contentNameResult != null) {
                            for (int i = 0; i < Objects.requireNonNull(contentNameResult).size(); i++) {
                                if (i == contentNameResult.size() - 1) {
                                    holder.optionListTV.append(contentNameResult.get(i));
                                } else {
                                    holder.optionListTV.append(contentNameResult.get(i) + ", ");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                    }
                });
    }

}
