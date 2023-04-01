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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 카테고리 어댑터
@SuppressLint("SetTextI18n")
public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    private final List<OptionVO> optionList; // 옵션 리스트

    private List<OptionContentVO> optionContentList; // 옵션 내용 리스트
    Context context; // context

    OptionContentAdapter optionContentAdapter; // 옵션 내용 어댑터

    // 최소, 최대 선택 개수
    public static int minimumSelection;
    public static int maximumSelection;

    // 생성자
    public OptionAdapter(List<OptionVO> optionList, Context context) {
        this.optionList = optionList;
        this.context = context;
    }

    // 화면생성
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_menu_option, parent, false);
        return new ViewHolder(view);
    }

    // 데이터 셋팅
    @Override
    public void onBindViewHolder(@NonNull OptionAdapter.ViewHolder holder, int position) {
        OptionVO option = optionList.get(position);
        
        OptionActivity.selectedOptionFlagList.add(false); // 옵션 선택 flag 추가

        String optionName = option.getOptionName(); // 옵션 이름
        minimumSelection = option.getMinimumSelection(); // 최소 선택 개수
        maximumSelection = option.getMaximumSelection(); // 최대 선택 개수

        // 최소, 최대 선택 개수에 따른 텍스트와 타입 설정
        if (minimumSelection == 0 && maximumSelection != 0) {
            holder.optionNameTV.setText(optionName + " (최대 " + maximumSelection + "개)");
        } else if (minimumSelection == 1 && maximumSelection == 1) {
            holder.optionNameTV.setText(optionName + " (필수)");
        } else if (minimumSelection != 0 && maximumSelection == 0 || minimumSelection == maximumSelection) {
            holder.optionNameTV.setText(optionName + " (최소 " + minimumSelection + "개 선택)");
        } else {
            holder.optionNameTV.setText(optionName + " (최소 " + minimumSelection + "개, 최대 " + maximumSelection + "개 선택)");
        }

        // 옵션 내용 리사이클러뷰 생성, 설정
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.optionContentRecyclerView.setLayoutManager(layoutManager);
        holder.optionContentRecyclerView.setHasFixedSize(true);

        // 데이터 추가, 어댑터 적용
        int menuOptionId = option.getMenuOptionId();
        setOptionContent(menuOptionId, minimumSelection, maximumSelection, position, holder);
    }

    @Override
    public int getItemCount() {
        if (optionList != null) {
            return optionList.size();
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView optionNameTV;
        RecyclerView optionContentRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            optionNameTV = itemView.findViewById(R.id.optionNameTV);
            optionContentRecyclerView = itemView.findViewById(R.id.optionContentRecyclerView);
        }

    }

    // 옵션 내용 가져오는 api, 어댑터 생성
    private void setOptionContent(int menuOptionId, int minimumSelection, int maximumSelection, int optionPosition, @NonNull OptionAdapter.ViewHolder holder) {
        RetrofitService retrofitService = new RetrofitService();
        MenuApi api = retrofitService.getRetrofit().create(MenuApi.class);

        optionContentList = new ArrayList<>();

        api.getMenuOptionContentList(menuOptionId)
                .enqueue(new Callback<List<OptionContentVO>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<OptionContentVO>> call, @NonNull Response<List<OptionContentVO>> response) {
                        optionContentList = response.body();
                        optionContentAdapter = new OptionContentAdapter(optionContentList, minimumSelection, maximumSelection, optionPosition, context);
                        holder.optionContentRecyclerView.setAdapter(optionContentAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<OptionContentVO>> call, @NonNull Throwable t) {

                    }
                });
    }

}
