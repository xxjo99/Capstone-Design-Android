package com.delivery.mydelivery.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 카테고리 어댑터
@SuppressLint("SetTextI18n")
public class MenuCategoryListAdapter extends RecyclerView.Adapter<MenuCategoryListAdapter.ViewHolder> {

    private List<MenuCategoryVO> menuCategoryList; // 메뉴 카테고리 리스트
    Context context; // context

    RetrofitService retrofitService;
    MenuApi menuApi;

    // 메뉴 리스트에 데이터 추가
    @SuppressLint("NotifyDataSetChanged")
    public void setMenuCategoryList(List<MenuCategoryVO> menuCategoryList) {
        this.menuCategoryList = menuCategoryList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuCategoryVO menuCategoryVO = menuCategoryList.get(position);

        holder.menuCategoryNameTV.setText(menuCategoryVO.getMenuCategoryName());

        // 메뉴 리스트 추가
        // 리사이클러뷰 설정
        holder.menuListRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.menuListRecyclerView.setHasFixedSize(true);

        // 어댑터, 메뉴 추가
        holder.menuListAdapter = new MenuListAdapter();
        holder.menuListRecyclerView.setAdapter(holder.menuListAdapter);
        setMenuList(menuCategoryVO.getStoreId(), menuCategoryVO.getMenuCategoryId(), holder);
    }

    @Override
    public int getItemCount() {
        if (menuCategoryList != null) {
            return menuCategoryList.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public MenuCategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_category, parent, false);
        context = view.getContext();

        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView menuCategoryNameTV;
        RecyclerView menuListRecyclerView;

        MenuListAdapter menuListAdapter;
        List<MenuVO> menuList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // xml변수 초기화
            menuCategoryNameTV = itemView.findViewById(R.id.menuCategoryNameTV);
            menuListRecyclerView = itemView.findViewById(R.id.menuListRecyclerView);
        }
    }

    // 해당 카테고리의 메뉴 추가
    private void setMenuList(int storeId, int menuCategoryId, MenuCategoryListAdapter.ViewHolder holder) {
        retrofitService = new RetrofitService();
        menuApi = retrofitService.getRetrofit().create(MenuApi.class);

        menuApi.getMenuList(storeId, menuCategoryId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<MenuVO>> call, @NonNull Response<List<MenuVO>> response) {
                        holder.menuList = response.body();
                        holder.menuListAdapter.setMenuList(holder.menuList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<MenuVO>> call, @NonNull Throwable t) {

                    }
                });
    }

}
