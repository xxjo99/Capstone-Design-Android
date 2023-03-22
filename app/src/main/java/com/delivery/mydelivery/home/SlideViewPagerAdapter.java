package com.delivery.mydelivery.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SlideViewPagerAdapter extends FragmentStateAdapter {

    int pageCount;

    public SlideViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int pageCount) {
        super(fragmentActivity);
        this.pageCount = pageCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        switch (index) {
            case 0 :
                return new SlideImageFragment1();
            case 1 :
                return new SlideImageFragment2();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) {
        return position % pageCount;
    }

}
