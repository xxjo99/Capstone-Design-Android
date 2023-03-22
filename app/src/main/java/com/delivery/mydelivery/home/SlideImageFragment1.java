package com.delivery.mydelivery.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delivery.mydelivery.R;

import androidx.fragment.app.Fragment;

public class SlideImageFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_home_banner1, container, false);
    }

}
