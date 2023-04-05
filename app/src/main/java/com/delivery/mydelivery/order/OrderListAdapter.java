package com.delivery.mydelivery.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.menu.MenuApi;
import com.delivery.mydelivery.menu.MenuVO;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 장바구니 어댑터
@SuppressLint("SetTextI18n")
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private final List<OrderVO> orderList; // 주문 리스트
    int point; // 유저 포인트
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    StoreApi storeApi;
    MenuApi menuApi;
    OrderApi orderApi;

    // 생성자
    public OrderListAdapter(List<OrderVO> orderList, int point, Context context) {
        this.orderList = orderList;
        this.point = point;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_order_order, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        OrderVO order = orderList.get(position);
        OrderListActivity.storeId = order.getStoreId();

        // 매장이름, 선택한 옵션, 가격, 개수 세팅
        setStoreName(order.getStoreId(), point); // 매장 이름
        setMenuName(order.getMenuId(), holder); // 메뉴 이름
        setContentNameList(order.getSelectOption(), holder);// 선택 옵션 리스트

        NumberFormat numberFormat = NumberFormat.getInstance();
        String menuTotalPrice = numberFormat.format(order.getTotalPrice());
        holder.menuPriceTV.setText(menuTotalPrice + "원"); // 메뉴 총 가격
        holder.amountTV.setText(order.getAmount() + "개"); // 메뉴 개수

        if (order.getAmount() == 1) {
            holder.decreaseBtn.setImageResource(R.drawable.icon_minus_gray);
        } else {
            holder.decreaseBtn.setImageResource(R.drawable.icon_minus);
        }

        // 메뉴 개수 수정
        holder.decreaseBtn.setOnClickListener(view -> {
            if (order.getAmount() > 1) {
                int amount = order.getAmount() - 1;
                int price = order.getTotalPrice() - (order.getTotalPrice() / order.getAmount());

                order.setAmount(amount);
                order.setTotalPrice(price);

                modifyAmount(order);
                notifyItemChanged(position);

                OrderListActivity.totalPrice -= (order.getTotalPrice() / order.getAmount());
                String totalPrice = numberFormat.format(OrderListActivity.totalPrice);
                OrderListActivity.totalPriceTV.setText(totalPrice + "원");
            }

            if (order.getAmount() == 1) {
                holder.decreaseBtn.setImageResource(R.drawable.icon_minus_gray);
            } else {
                holder.decreaseBtn.setImageResource(R.drawable.icon_minus);
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
            String totalPrice = numberFormat.format(OrderListActivity.totalPrice);
            OrderListActivity.totalPriceTV.setText(totalPrice + "원");

            if (order.getAmount() == 1) {
                holder.decreaseBtn.setImageResource(R.drawable.icon_minus_gray);
            } else {
                holder.decreaseBtn.setImageResource(R.drawable.icon_minus);
            }
        });

        // 메뉴 삭제
        holder.deleteBtn.setOnClickListener(view -> deleteOrder(order.getOrderId(), order.getTotalPrice(), position));
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
        ImageButton deleteBtn;

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

    // 매장정보 추가
    private void setStoreName(int storeId, int point) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<StoreVO> call, @NonNull Response<StoreVO> response) {
                        StoreVO store = response.body();
                        assert store != null;

                        String storeName = store.getStoreName();
                        String deliveryTipStr = store.getDeliveryTip();

                        OrderListActivity.storeNameTV.setText(storeName);
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        String deliveryTipFormat = numberFormat.format(Integer.parseInt(deliveryTipStr));
                        OrderListActivity.deliveryTipTV.setText(deliveryTipFormat + "원");

                        // 사용자가 가진 포인트보다 배달비가 더 높은지 확인
                        int deliveryTip = Integer.parseInt(deliveryTipStr);

                        OrderListActivity.deliveryAvailableFlag = deliveryTip <= point;
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

    // 선택한 옵션의 내용들을 가져옴
    private void setContentNameList(String contentNameList, @NonNull OrderListAdapter.ViewHolder holder) {
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

    // 증가, 감소버튼 눌렀을경우 개수, 가격 변경
    private void modifyAmount(OrderVO order) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.modifyAmount(order)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<OrderVO> call, @NonNull Response<OrderVO> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<OrderVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 메뉴 삭제
    private void deleteOrder(int orderId, int orderPrice, int position) {
        retrofitService = new RetrofitService();
        orderApi = retrofitService.getRetrofit().create(OrderApi.class);

        orderApi.deleteOrder(orderId)
                .enqueue(new Callback<>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        OrderListActivity.totalPrice -= orderPrice;
                        NumberFormat numberFormat = NumberFormat.getInstance();
                        String totalPrice = numberFormat.format(OrderListActivity.totalPrice);
                        OrderListActivity.totalPriceTV.setText(totalPrice + "원");
                        orderList.remove(position);
                        notifyDataSetChanged();

                        // 삭제 후 장바구니가 비어있다면 레이아웃 변경
                        if (orderList.size() == 0) {
                            OrderListActivity.emptyLayout.setVisibility(View.VISIBLE);
                            OrderListActivity.slidingUpPanelLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }

}