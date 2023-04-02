package com.delivery.mydelivery.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.NumberFormat;
import java.util.List;

// 카테고리 어댑터
@SuppressLint("SetTextI18n")
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    private List<MenuVO> menuList; // 메뉴 리스트
    Context context; // context

    // 메뉴 리스트에 데이터 추가
    @SuppressLint("NotifyDataSetChanged")
    public void setMenuList(List<MenuVO> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(menuList.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (menuList != null) {
            return menuList.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public MenuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_menu, parent, false);
        context = view.getContext();

        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView menuIV;
        TextView menuNameTV;
        TextView menuPriceTV;
        View divisionLineView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // xml변수 초기화
            menuIV = itemView.findViewById(R.id.menuIV);
            menuNameTV = itemView.findViewById(R.id.menuNameTV);
            menuPriceTV = itemView.findViewById(R.id.menuPriceTV);
            divisionLineView = itemView.findViewById(R.id.divisionLineView);

            menuIV.setClipToOutline(true);
            // 클릭 이벤트
            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();

                // 필요한 정보를 담아 옵션 리스트로 이동,
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, OptionActivity.class);

                    MenuVO menu = menuList.get(position);

                    int storeId = menu.getStoreId();
                    int menuId = menu.getMenuId();
                    String menuImageUrl = menu.getMenuPicUrl();
                    String menuName = menu.getMenuName();
                    int menuPrice = menu.getMenuPrice();

                    intent.putExtra("storeId", storeId);
                    intent.putExtra("menuId", menuId);
                    intent.putExtra("menuImageUrl", menuImageUrl);
                    intent.putExtra("menuName", menuName);
                    intent.putExtra("menuPrice", menuPrice);
                    intent.putExtra("participantType", MenuListActivity.participantType);
                    intent.putExtra("recruitId", MenuListActivity.recruitId);

                    context.startActivity(intent);
                }
            });
        }

        // 메뉴 정보 출력
        void onBind(MenuVO menu, int position) {
            if (menu.getMenuPicUrl() == null) {
                menuIV.setVisibility(View.GONE);
            } else {
                Glide.with(itemView).load(menu.getMenuPicUrl()).placeholder(R.drawable.ic_launcher_background).into(menuIV);
            }
            menuNameTV.setText(menu.getMenuName());

            NumberFormat numberFormat = NumberFormat.getInstance();
            String menuPrice = numberFormat.format(menu.getMenuPrice());
            menuPriceTV.setText(menuPrice + "원");

            if (position == getItemCount() - 1) {
                divisionLineView.setVisibility(View.GONE);
            }
        }
    }

}
