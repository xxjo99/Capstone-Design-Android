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
            case 0 : // 한식
                return new StoreListKoreanFragment();

            case 1: // 일식
                return new StoreListJapaneseFragment();

            case 2: // 중식
                return new StoreListChineseFragment();

            case 3: // 양식
                return new StoreListWesternFragment();

            case 4: // 고기
                return new StoreListMeatFragment();

            case 5: // 치킨
                return new StoreListChickenFragment();

            case 6: // 햄버거
                return new StoreListHamburgerFragment();

            case 7: // 피자
                return new StoreListPizzaFragment();

            case 8: // 아시안
                return new StoreListAsianFragment();

            case 9: // 분식
                return new StoreListSnackFragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}