package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delivery.mydelivery.MainActivity;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.myInfo.ModifyPwActivity;
import com.delivery.mydelivery.myInfo.MyInfoActivity;
import com.delivery.mydelivery.point.PointActivity;
import com.delivery.mydelivery.point.PointHistoryActivity;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

// 내정보 프래그먼트
public class MyPageFragment extends Fragment {

    // 닉네임, 로그아웃
    TextView userNameTV;
    Button logoutBtn;

    // 포인트
    TextView pointTV;
    Button addPointBtn;
    Button pointHistoryBtn;

    // 개인정보
    TextView myInfoTV;
    TextView modifyPwTV;

    // view, context
    View view;
    Context context;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragment_home_my_page, container, false); // view 설정
        assert container != null;
        context = container.getContext(); // context 초기화

        // 사용자 정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 초기화
        userNameTV = view.findViewById(R.id.userNameTV);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        userNameTV.setText(user.getName()); // 사용자 닉네임

        // 로그아웃
        logoutBtn.setOnClickListener(view -> {
            PreferenceManager.logout(context);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // 초기화
        pointTV = view.findViewById(R.id.pointTV);
        addPointBtn = view.findViewById(R.id.addPointBtn);
        pointHistoryBtn = view.findViewById(R.id.pointHistoryBtn);

        pointTV.setText(user.getPoint() + "P");// 보유 포인트

        // 충전페이지 이동
        addPointBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PointActivity.class);
            startActivity(intent);
        });

        // 포인트 이용내역 페이지 이동
        pointHistoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PointHistoryActivity.class);
            startActivity(intent);
        });

        // 초기화
        myInfoTV = view.findViewById(R.id.myInfoTV);
        modifyPwTV = view.findViewById(R.id.modifyPwTV);

        // 내정보 페이지 이동
        myInfoTV.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MyInfoActivity.class);
            startActivity(intent);
        });

        // 비밀번호 변경 페이지 이동
        modifyPwTV.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ModifyPwActivity.class);
            startActivity(intent);
        });

        return view;
    }

}
