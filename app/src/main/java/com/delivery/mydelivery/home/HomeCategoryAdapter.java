package com.delivery.mydelivery.home;

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
import com.delivery.mydelivery.store.StoreActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 카테고리 어댑터
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private List<CategoryVO> categoryList; // 카테고리 리스트
    private int[] categoryImgList; // 카테고리 이미지 리스트
    Context context; // context

    // 데이터 추가
    @SuppressLint("NotifyDataSetChanged")
    public void setCategoryList(List<CategoryVO> categoryList, int[] categoryImgList) {
        this.categoryList = categoryList;
        this.categoryImgList = categoryImgList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(categoryList.get(position), categoryImgList[position]);
    }

    @Override
    public int getItemCount() {
        if (categoryList != null) {
            return categoryList.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_category, parent, false);
        context = view.getContext();

        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView categoryIV;
        TextView categoryTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // xml변수 초기화
            categoryIV = itemView.findViewById(R.id.categoryIV);
            categoryTV = itemView.findViewById(R.id.categoryTV);

            // 매장리스트 이동
            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                changeView(position);
            });
        }

        // 카테고리 이미지, 이름 지정
        void onBind(CategoryVO category, int categoryImg) {
            Glide.with(itemView).load(categoryImg).placeholder(R.drawable.ic_launcher_background).into(categoryIV);
            categoryTV.setText(category.getCategoryName());
        }
    }

    // 매장 리스트 이동 메소드
    private void changeView(int categoryPosition) {
        Intent intent = new Intent(context, StoreActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("categoryPosition", categoryPosition);
        context.startActivity(intent);
    }

}
