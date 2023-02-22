package com.delivery.mydelivery.home;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.user.UserVO;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

public class SearchFragment extends Fragment {

    View view; // 해당 view에 지정한 프래그먼트 추가

    EditText searchET;

    // 레이아웃, 탭 레이아웃, 뷰페이저
    LinearLayout searchFragmentLayout;
    LinearLayout searchResultLayout;
    TabLayout searchTab;
    ViewPager2 searchResultViewPager;

    // 검색 결과 뷰
    Fragment searchResultStore;
    Fragment searchResultRecruit;

    Context context;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragment_home_search, container, false);
        assert container != null;
        context = container.getContext();

        // 사용자정보
        String loginInfo = PreferenceManager.getLoginInfo(context);
        Gson gson = new Gson();
        UserVO user = gson.fromJson(loginInfo, UserVO.class);

        // 초기화
        searchET = view.findViewById(R.id.searchET);

        // 레이아웃, 탭 레이아웃, 뷰페이저 초기화
        searchFragmentLayout = view.findViewById(R.id.searchFragmentLayout);
        searchResultLayout = view.findViewById(R.id.searchResultLayout);
        searchTab = view.findViewById(R.id.searchTab);
        searchResultViewPager = view.findViewById(R.id.searchResultViewPager);

        searchFragmentLayout.setOnClickListener(view -> hideKeyboard());

        // 검색결과 뷰 초기화
        searchResultStore = new SearchResultStore();
        searchResultRecruit = new SearchResultRecruit();

        // 검색
        searchET.setOnEditorActionListener((textView, actionId, keyEvent) -> {

            if (actionId == IME_ACTION_SEARCH) {
                InputMethodManager mInputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(searchET.getWindowToken(), 0);

                String keyword = searchET.getText().toString();

                SearchResultStore.searchOpenStore(keyword, user.getSchool());
                SearchResultRecruit.searchRecruitResult(keyword, user.getSchool());

                searchResultLayout.setVisibility(View.VISIBLE);
            }
            return true;
        });

        // 어댑터 설정
        searchResultViewPager.setAdapter(new SearchViewPagerAdapter(this, searchResultStore, searchResultRecruit));
        searchResultViewPager.setCurrentItem(0);
        searchResultViewPager.setOffscreenPageLimit(1);

        // 탭과 뷰페이저 연결
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(searchTab, searchResultViewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("매장");
            } else {
                tab.setText("등록글");
            }
        });
        tabLayoutMediator.attach();

        return view;
    }

    // 키보드 외부를 누르면 내려감
    private void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
