package com.delivery.mydelivery.user.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.menu.MenuApi;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 장바구니 어댑터
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private final List<OrderVO> orderList; // 주문 리스트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    MenuApi menuApi;
    OrderApi orderApi;

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
        View view = inflater.inflate(R.layout.item_order_order_list, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        OrderVO order = orderList.get(position);
        OrderListActivity.storeId = order.getStoreId();
        // 매장이름, 선택한 옵션, 가격, 개수 세팅
        setStoreName(order.getStoreId()); // 매장 이름
        setMenuName(order.getMenuId(), holder); // 메뉴 이름

        if (!order.getSelectOption().equals("")) {
            setContentNameList(order.getSelectOption(), holder);// 선택 옵션 리스트
        } else {
            holder.optionListTV.setVisibility(View.GONE);
        }

        holder.menuPriceTV.setText(order.getTotalPrice() + ""); // 메뉴 총 가격
        holder.amountTV.setText(order.getAmount() + ""); // 메뉴 개수

        // 메뉴 개수 수정 이벤트
        holder.decreaseBtn.setOnClickListener(view -> {
            if (order.getAmount() != 1) {
                int amount = order.getAmount() - 1;
                int price = order.getTotalPrice() - (order.getTotalPrice() / order.getAmount());

                order.setAmount(amount);
                order.setTotalPrice(price);

                modifyAmount(order);
                notifyItemChanged(position);

                OrderListActivity.totalPrice -= (order.getTotalPrice() / order.getAmount());
                OrderListActivity.totalPriceTV.setText(OrderListActivity.totalPrice + "원");
            }
        });

        holder.increaseBtn.setOnClickListener(view -> {
            int amount = order.getAmount() + 1;
            int price = order.getTotalPrice() + (order.getTotalPrice() / order.getAmount());

            order.setAmount(amount);
            order.setTotalPrice(price);

            modifyAmount(order);
            notifyItemChanged(position);

            OrderListActivity.totalPrice += (order.getTotalPrice() / order.getAmount());
            OrderListActivity.totalPriceTV.setText(OrderListActivity.totalPrice + "원");
        });

    }

    @Override
    public int getItemCount() {
        if (orderList != null) {
            return orderList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView menuNameTV;
        TextView optionListTV;
        TextView menuPriceTV;
        TextView amountTV;
        ImageButton decreaseBtn;
        ImageButton increaseBtn;
        Button deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menuNameTV = itemView.findViewById(R.id.menuNameTV);
            optionListTV = itemView.findViewById(R.id.optionListTV);
            menuPriceTV = itemView.findViewById(R.id.menuPriceTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
            increaseBtn = itemView.findViewById(R.id.increaseBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    // 매장이름 지정
    private void setStoreName(int storeId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<StoreVO>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();
                        assert store != null;
                        OrderListActivity.storeNameTV.setText(store.getStoreName());
                    }

                    @Override
                    public void onFailure(@NonNull Call<StoreVO> call, @NonNull Throwable t) {
                    }
                });
    }

    // 메뉴이름 지정
    private void setMenuName(int menuId, OrderListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        menuApi = retrofitService.getRetrofit().create(MenuApi.class);

        menuApi.getMenuName(menuId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        String menuName = response.body();
                        holder.menuNameTV.setText(menuName);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        System.out.println("call " + call + " T" + t);
                    }
                });
    }

    // 선택한 옵션의 내용들을 가져옴
    private void setContentNameList(String contentNameList, @NonNull OrderListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.getContentNameList(contentNameList)
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                        List<String> contentNameResult = response.body();

                        holder.optionListTV.setText("");
                        for (int i = 0; i < Objects.requireNonNull(contentNameResult).size(); i++) {

                            if (i == contentNameResult.size() - 1) {
                                holder.optionListTV.append(contentNameResult.get(i));
                            } else {
                                holder.optionListTV.append(contentNameResult.get(i) + ", ");
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                    }
                });
    }

    // 증가, 감소버튼 눌렀을경우 개수, 가격 변경
    private void modifyAmount(OrderVO order) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.modifyAmount(order)
                .enqueue(new Callback<OrderVO>() {
                    @Override
                    public void onResponse(@NonNull Call<OrderVO> call, @NonNull Response<OrderVO> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<OrderVO> call, @NonNull Throwable t) {

                    }
                });

    }

    // 메뉴 삭제
    private void deleteOrder(int orderId, int position) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.deleteOrder(orderId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
    }

}