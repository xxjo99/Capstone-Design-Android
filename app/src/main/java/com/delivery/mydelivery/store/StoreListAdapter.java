package com.delivery.mydelivery.store;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.menu.MenuListActivity;

import java.util.List;

// 매장 리스트 어댑터
public class StoreListAdapter extends BaseAdapter {

    Context context;
    List<StoreVO> storeList;

    public StoreListAdapter(Context context, List<StoreVO> storeList) {
        this.context = context;
        this.storeList = storeList;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        // 뷰가 비어있다면 뷰 세팅, 매장 리스트 아이템 추가
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // LayoutInflater 설정
            view = layoutInflater.inflate(R.layout.item_store_list, viewGroup, false); // view에 item_store_list.xml 지정
        }

        // 매장의 이미지, 이름, 최소주문금액, 배달팁 xml 변수 초기화
        ImageView storeImageView = view.findViewById(R.id.storeIV);
        TextView storeNameTV = view.findViewById(R.id.storeNameTV);
        TextView minimumDeliveryPriceTV = view.findViewById(R.id.minimumDeliveryPriceTV);
        TextView deliveryTipTV = view.findViewById(R.id.deliveryTipTV);

        // storeList의 position에 위치한 값 가져옴
        StoreVO store = storeList.get(position);

        // store의 지정된 데이터들을 뷰에 세팅
        Glide.with(context).load(store.getStoreImageUrl()).placeholder(R.drawable.ic_launcher_background).override(100, 100).into(storeImageView);
        storeNameTV.setText(store.getStoreName());
        minimumDeliveryPriceTV.setText(store.getMinimumDeliveryPrice() + "");
        deliveryTipTV.setText(store.getDeliveryTip());

        // 매장 클릭 이벤트
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MenuListActivity.class);
                intent.putExtra("storeId", store.getStoreId());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
