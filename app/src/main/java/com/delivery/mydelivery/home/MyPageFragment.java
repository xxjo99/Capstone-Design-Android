package com.delivery.mydelivery.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delivery.mydelivery.MainActivity;
import com.delivery.mydelivery.R;
import com.delivery.mydelivery.myInfo.ModifyPwActivity;
import com.delivery.mydelivery.myInfo.MyInfoActivity;
import com.delivery.mydelivery.myInfo.OrderHistoryActivity;
import com.delivery.mydelivery.point.PointActivity;
import com.delivery.mydelivery.point.PointHistoryActivity;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.retrofit.RetrofitService;
import com.delivery.mydelivery.user.UserApi;
import com.delivery.mydelivery.user.UserVO;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 내정보 프래그먼트
public class MyPageFragment extends Fragment {

    // 닉네임, 포인트
    TextView userNameTV;
    TextView pointTV;

    LinearLayout addPointLayout;
    LinearLayout pointHistoryLayout;
    LinearLayout orderHistoryLayout;
    LinearLayout myInfoLayout;
    LinearLayout modifyPwLayout;
    LinearLayout logoutLayout;

    // view, context
    View view;
    Context context;

    // 레트로핏, api
    RetrofitService retrofitService;
    UserApi userApi;

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
        pointTV = view.findViewById(R.id.pointTV);

        addPointLayout = view.findViewById(R.id.addPointLayout);
        pointHistoryLayout = view.findViewById(R.id.pointHistoryLayout);
        orderHistoryLayout = view.findViewById(R.id.orderHistoryLayout);
        myInfoLayout = view.findViewById(R.id.myInfoLayout);
        modifyPwLayout = view.findViewById(R.id.modifyPwLayout);
        logoutLayout = view.findViewById(R.id.logoutLayout);

        setUserInfo(user.getUserId()); // 유저정보 추가

        // 포인트 충전 페이지
        addPointLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PointActivity.class);
            startActivity(intent);
        });

        // 포인트 이용내역 페이지 이동
        pointHistoryLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PointHistoryActivity.class);
            startActivity(intent);
        });

        // 주문내역 액티비티 이동
        orderHistoryLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
            startActivity(intent);
        });

        // 내정보 페이지 이동
        myInfoLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MyInfoActivity.class);
            startActivity(intent);
        });

        // 비밀번호 변경 페이지 이동
        modifyPwLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ModifyPwActivity.class);
            startActivity(intent);
        });

        // 토큰 삭제 후 로그아웃
        logoutLayout.setOnClickListener(view -> {
            deleteToken(user.getUserId());
            PreferenceManager.logout(context);

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    // 유저정보 추가
    private void setUserInfo(int userId) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.getUser(userId)
                .enqueue(new Callback<>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<UserVO> call, @NonNull Response<UserVO> response) {
                        UserVO user = response.body();

                        userNameTV.setText(Objects.requireNonNull(user).getName());

                        NumberFormat numberFormat = NumberFormat.getInstance();
                        String point = numberFormat.format(user.getPoint());
                        pointTV.setText(point);
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserVO> call, @NonNull Throwable t) {

                    }
                });
    }

    // 토큰 삭제
    private void deleteToken(int userId) {
        retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.deleteToken(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
    }

}
