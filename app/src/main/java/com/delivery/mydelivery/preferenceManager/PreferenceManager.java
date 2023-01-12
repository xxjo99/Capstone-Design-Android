package com.delivery.mydelivery.preferenceManager;

import android.content.Context;
import android.content.SharedPreferences;

// 로그인정보를 담음
public class PreferenceManager {

    private static final String PREFERENCES_NAME = "userInfo"; // sharedPreferences 이름

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    // 로그인 정보 저장
    public static void setLoginInfo(Context context, String userInfoJson) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("userInfo", userInfoJson.toString());

        editor.apply();
    }

    public static String getLoginInfo(Context context) {
        SharedPreferences prefs = getPreferences(context);
        String loginInfo = prefs.getString("userInfo", "");
        return loginInfo;
    }

    public static void logout(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

}
