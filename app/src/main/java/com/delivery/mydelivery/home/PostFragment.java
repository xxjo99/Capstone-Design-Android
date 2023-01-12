package com.delivery.mydelivery.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delivery.mydelivery.R;

public class PostFragment extends Fragment {

    View view; // 해당 view에 지정한 프래그먼트 추가

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_post, container, false); // view 초기화

        return view;
    }
}
