package com.delivery.mydelivery.store;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StoreViewPagerAdapter extends FragmentStateAdapter {

    public StoreViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0 : // 고기
                return new StoreListMeatFragment();

            case 1: // 도시락
                return new StoreListLunchBoxFragment();

            case 2: // 돈까스, 회, 일식
                return new StoreListJapaneseFragment();

            case 3: // 백반, 죽, 국수
                return new StoreListBaekbanFragment();

            case 4: // 분식
                return new StoreListSnackFragment();

            case 5: // 아시안
                return new StoreListAsianFragment();

            case 6: // 야식
                return new StoreListMidnightSnackFragment();

            case 7: // 양식
                return new StoreListWesternFragment();

            case 8: // 족발, 보쌈
                return new StoreListJokbalBossamFragment();

            case 9: // 중식
                return new StoreListChineseFragment();

            case 10: // 찜, 탕, 찌개
                return new StoreListStewFragment();

            case 11: // 치킨
                return new StoreListChickenFragment();

            case 12: // 카페, 디저트
                return new StoreListDessertFragment();

            case 13: // 패스트 푸드
                return new StoreListFastFoodFragment();

            case 14: // 피자
                return new StoreListPizzaFragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 15;
    }
}