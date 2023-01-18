package com.delivery.mydelivery.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.home.CategoryVO;

import java.util.List;

// 카테고리 어댑터
public class StoreCategoryAdapter extends RecyclerView.Adapter<StoreCategoryAdapter.ViewHolder> {

    private List<CategoryVO> categoryList; // 카테고리 리스트
    Context context; // context

    // 클릭 이벤트 구현
    private OnItemClickListener clickListener = null;

    public interface OnItemClickListener {
        void onItemClicked(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // 카테고리 리스트에 데이터 추가
    @SuppressLint("NotifyDataSetChanged")
    public void setCategoryList(List<CategoryVO> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StoreCategoryAdapter.ViewHolder holder, int position) {
        holder.onBind(categoryList.get(position));
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
    public StoreCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_category, parent, false);
        context = view.getContext();

        return new StoreCategoryAdapter.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTV = itemView.findViewById(R.id.categoryTV);

            // 클릭 이벤트
            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();

                if (clickListener != null) {
                    clickListener.onItemClicked(view, position);
                }
            });
        }

        // 카테고리 텍스트 추가
        void onBind(CategoryVO category) {
            categoryTV.setText(category.getCategoryName());
        }
    }

}