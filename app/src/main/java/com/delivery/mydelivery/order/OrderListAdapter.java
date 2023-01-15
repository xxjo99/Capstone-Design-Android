package com.delivery.mydelivery.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.menu.MenuApi;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.store.StoreApi;
import com.delivery.mydelivery.store.StoreVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 장바구니 어댑터
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<OrderVO> orderList; // 주문 리스트
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
        View view = inflater.inflate(R.layout.item_order_list, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        OrderVO order = orderList.get(position);

        int storeId = order.getStoreId();
        int menuId = order.getMenuId();
        String selectOptionList = order.getSelectOption();
        int menuPrice = order.getTotalPrice();
        int amount = order.getAmount();

        // 매장이름, 선택한 옵션, 가격, 개수 세팅
        setStoreName(storeId); // 매장 이름
        setMenuName(menuId, holder); // 메뉴 이름
        if (!selectOptionList.equals("")) {
            setContentNameList(selectOptionList, holder);// 선택 옵션 리스트
        } else {
            holder.optionListTV.setVisibility(View.GONE);
        }
        holder.menuPriceTV.setText(menuPrice + ""); // 메뉴 총 가격
        holder.amountTV.setText(amount + ""); // 메뉴 개수
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
        TextView optionListTV;
        TextView menuPriceTV;
        TextView amountTV;
        Button decreaseBtn;
        Button increaseBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menuNameTV = itemView.findViewById(R.id.menuNameTV);
            optionListTV = itemView.findViewById(R.id.optionListTV);
            menuPriceTV = itemView.findViewById(R.id.menuPriceTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
            increaseBtn = itemView.findViewById(R.id.increaseBtn);
        }
    }

    // 매장이름 지정
    private void setStoreName(int storeId) {
        retrofitService = new RetrofitService();
        storeApi = retrofitService.getRetrofit().create(StoreApi.class);

        storeApi.getStore(storeId)
                .enqueue(new Callback<StoreVO>() {
                    @Override
                    public void onResponse(Call<StoreVO> call, Response<StoreVO> response) {
                        StoreVO store = response.body();
                        OrderListActivity.storeNameTV.setText(store.getStoreName());
                    }

                    @Override
                    public void onFailure(Call<StoreVO> call, Throwable t) {
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
                    public void onResponse(Call<String> call, Response<String> response) {
                        String menuName = response.body();
                        holder.menuNameTV.setText(menuName);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
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
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> contentNameResult = response.body();

                        holder.optionListTV.setText("");
                        for (int i = 0; i < contentNameResult.size(); i++) {

                            if (i == contentNameResult.size() - 1) {
                                holder.optionListTV.append(contentNameResult.get(i));
                            } else {
                                holder.optionListTV.append(contentNameResult.get(i) + ", ");
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                    }
                });
    }
}