package com.delivery.mydelivery.recruit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.menu.MenuApi;
import com.delivery.mydelivery.menu.MenuVO;
import com.delivery.mydelivery.order.OrderApi;
import com.delivery.mydelivery.order.OrderListActivity;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 장바구니 어댑터
@SuppressLint("SetTextI18n")
public class RecruitOrderListAdapter extends RecyclerView.Adapter<RecruitOrderListAdapter.ViewHolder> {

    private final List<ParticipantOrderVO> orderList; // 담은 메뉴
    int paymentStatus; // 결제상태
    Context context; // context

    // 레트로핏, api
    RetrofitService retrofitService;
    MenuApi menuApi;
    OrderApi orderApi;
    RecruitApi recruitApi;

    // 생성자
    public RecruitOrderListAdapter(List<ParticipantOrderVO> orderList, int paymentStatus, Context context) {
        this.orderList = orderList;
        this.paymentStatus = paymentStatus;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recruit_order, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull RecruitOrderListAdapter.ViewHolder holder, int position) {
        ParticipantOrderVO order = orderList.get(position);

        int menuId = order.getMenuId(); // 선택한 메뉴의 id
        String selectedOptionList = order.getSelectOption(); // 선택한 옵션 리스트
        int price = order.getTotalPrice();
        int amount = order.getAmount(); // 선택한 메뉴의 개수

        setMenuName(menuId, holder);// 메뉴 이름
        setContentNameList(selectedOptionList, holder);// 선택한 옵션 목록
        NumberFormat numberFormat = NumberFormat.getInstance();
        String priceFormat = numberFormat.format(price);
        holder.menuPriceTV.setText(priceFormat + "원");// 가격
        holder.amountTV.setText(amount + "개");// 개수

        if (order.getAmount() == 1) {
            holder.decreaseBtn.setImageResource(R.drawable.icon_minus_gray);
        } else {
            holder.decreaseBtn.setImageResource(R.drawable.icon_minus);
        }

        // 결제 완료되었다면 메뉴 변경 불가
        if (paymentStatus == 1) {
            holder.deleteBtn.setVisibility(View.GONE);

            holder.decreaseBtn.setImageResource(R.drawable.icon_minus_gray);
            holder.decreaseBtn.setEnabled(false);

            holder.increaseBtn.setImageResource(R.drawable.icon_plus_gray);
            holder.increaseBtn.setEnabled(false);
        }

        // 메뉴 개수 수정
        holder.decreaseBtn.setOnClickListener(view -> {
            if (order.getAmount() != 1) {
                int menuAmount = amount - 1;
                int menuPrice = price - (price / amount);

                order.setAmount(menuAmount);
                order.setTotalPrice(menuPrice);

                modifyAmount(order);
                notifyItemChanged(position);

                RecruitOrderListActivity.totalPrice -= (order.getTotalPrice() / order.getAmount());
                String totalPrice = numberFormat.format(RecruitOrderListActivity.totalPrice);
                RecruitOrderListActivity.totalPriceTV.setText(totalPrice + "원");

                if (order.getAmount() == 1) {
                    holder.decreaseBtn.setImageResource(R.drawable.icon_minus_gray);
                } else {
                    holder.decreaseBtn.setImageResource(R.drawable.icon_minus);
                }
            }
        });

        holder.increaseBtn.setOnClickListener(view -> {
            int menuAmount = amount + 1;
            int menuPrice = price + (price / amount);

            order.setAmount(menuAmount);
            order.setTotalPrice(menuPrice);

            modifyAmount(order);
            notifyItemChanged(position);

            RecruitOrderListActivity.totalPrice += (order.getTotalPrice() / order.getAmount());
            String totalPrice = numberFormat.format(RecruitOrderListActivity.totalPrice);
            RecruitOrderListActivity.totalPriceTV.setText(totalPrice + "원");

            if (order.getAmount() == 1) {
                holder.decreaseBtn.setImageResource(R.drawable.icon_minus_gray);
            } else {
                holder.decreaseBtn.setImageResource(R.drawable.icon_minus);
            }
        });

        // 메뉴 삭제
        holder.deleteBtn.setOnClickListener(view -> deleteOrder(order.getParticipantOrderId(), order.getTotalPrice(), position));
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
        LinearLayout amountLayout;
        ImageButton decreaseBtn;
        ImageButton increaseBtn;
        ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menuNameTV = itemView.findViewById(R.id.menuNameTV);
            optionListTV = itemView.findViewById(R.id.optionListTV);
            menuPriceTV = itemView.findViewById(R.id.menuPriceTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            amountLayout = itemView.findViewById(R.id.amountLayout);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
            increaseBtn = itemView.findViewById(R.id.increaseBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    // 메뉴이름
    private void setMenuName(int menuId, RecruitOrderListAdapter.ViewHolder holder) {
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
    private void setContentNameList(String contentNameList, @NonNull RecruitOrderListAdapter.ViewHolder holder) {
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

    // 개수 수정
    private void modifyAmount(ParticipantOrderVO order) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.modifyAmount(order)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ParticipantOrderVO> call, @NonNull Response<ParticipantOrderVO> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<ParticipantOrderVO> call, @NonNull Throwable t) {
                    }
                });
    }

    // 삭제
    private void deleteOrder(int participantOrderId, int orderPrice, int position) {
        retrofitService = new RetrofitService();
        recruitApi = retrofitService.getRetrofit().create(RecruitApi.class);

        recruitApi.deleteOrder(participantOrderId)
                .enqueue(new Callback<>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        RecruitOrderListActivity.totalPrice -= orderPrice;

                        NumberFormat numberFormat = NumberFormat.getInstance();
                        String totalPrice = numberFormat.format(RecruitOrderListActivity.totalPrice);
                        RecruitOrderListActivity.totalPriceTV.setText(totalPrice + "원");
                        orderList.remove(position);
                        notifyDataSetChanged();

                        // 삭제 후 장바구니가 비어있다면 레이아웃 변경
                        if (orderList.size() == 0) {
                            RecruitOrderListActivity.orderListLayout.setVisibility(View.GONE);
                            RecruitOrderListActivity.emptyLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }
}