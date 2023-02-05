package com.delivery.mydelivery.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// 뷰페이저 어댑터
public class SearchViewPagerAdapter extends FragmentStateAdapter {

    // 프래그먼트
    Fragment searchResultStore;
    Fragment searchResultRecruit;

    public SearchViewPagerAdapter(@NonNull Fragment fragment, Fragment searchResultStore, Fragment searchResultRecruit) {
        super(fragment);
        this.searchResultStore = searchResultStore;
        this.searchResultRecruit = searchResultRecruit;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return searchResultStore;
        } else {
            return searchResultRecruit;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
