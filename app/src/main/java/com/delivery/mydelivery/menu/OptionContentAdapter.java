package com.delivery.mydelivery.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.mydelivery.R;

import java.util.List;

public class OptionContentAdapter extends RecyclerView.Adapter<OptionContentAdapter.ViewHolder> {

    private List<OptionContentVO> optionContentList; // 옵션 내용 리스트
    Context context;

    // 생성자
    public OptionContentAdapter(List<OptionContentVO> optionContentList, Context context) {
        this.optionContentList = optionContentList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_menu_option_content, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull OptionContentAdapter.ViewHolder holder, int position) {
        OptionContentVO optionContent = optionContentList.get(position);

        holder.optionCheckBox.setText(optionContent.getOptionContentName());
        holder.optionPriceTV.setText(optionContent.getOptionPrice() + "");

        // 옵션 선택 이벤트
        holder.optionCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()) {
                    Toast.makeText(context, optionContent.getOptionContentName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (optionContentList != null) {
            return optionContentList.size();
        } else {
            return -1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox optionCheckBox;
        TextView optionPriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            optionCheckBox = itemView.findViewById(R.id.optionCheckBox);
            optionPriceTV = itemView.findViewById(R.id.optionPrice);
        }

    }

}
