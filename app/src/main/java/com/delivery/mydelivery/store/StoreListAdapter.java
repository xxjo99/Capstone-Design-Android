package com.delivery.mydelivery.store;

import android.annotation.SuppressLint;
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
@SuppressLint("SetTextI18n")
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
            view = layoutInflater.inflate(R.layout.item_store_store_list, viewGroup, false); // view에 item_store_list.xml 지정
        }

        // xml 변수 초기화
        ImageView storeImageView = view.findViewById(R.id.storeIV);
        TextView storeNameTV = view.findViewById(R.id.storeNameTV);
        TextView storeDeliveryPriceTV = view.findViewById(R.id.storeDeliveryPriceTV);
        TextView storeInfoTV = view.findViewById(R.id.storeInfoTV);
        TextView deliveryTimeTV = view.findViewById(R.id.deliveryTimeTV);

        // storeList의 position에 위치한 값 가져옴
        StoreVO store = storeList.get(position);

        String text = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png";
        // store의 지정된 데이터들을 뷰에 세팅
        Glide.with(context).load(/*store.getStoreImageUrl()*/ text).placeholder(R.drawable.ic_launcher_background).override(100, 100).into(storeImageView);
        storeNameTV.setText(store.getStoreName());
        String storeDeliveryPrice = "최소주문 " + store.getMinimumDeliveryPrice() + "원, 배달팁 " + store.getDeliveryTip() + "원";
        storeDeliveryPriceTV.setText(storeDeliveryPrice);
        storeInfoTV.setText(store.getStoreInfo());
        deliveryTimeTV.setText(store.getDeliveryTime() + "분");

        // 매장 클릭 이벤트
        view.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, MenuListActivity.class);
            intent.putExtra("storeId", store.getStoreId());
            intent.putExtra("participantType", "등록자"); // 등록자 or 참가자인지 확인
            intent.putExtra("recruitId", -1);
            context.startActivity(intent);
        });

        return view;
    }
}
