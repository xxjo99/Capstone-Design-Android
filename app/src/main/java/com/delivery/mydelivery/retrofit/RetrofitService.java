package com.delivery.mydelivery.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 레트로핏 설정
public class RetrofitService {

    private Retrofit retrofit; // 레트로핏

    // 생성자, 객체 생성시 init() 메소드 불러옴
    public RetrofitService() {
        init();
    }

    // 레트로핏 설정
    public void init() {
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://1.254.120.139:50000") // 주소
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    // 레트로핏 반환부분
    public Retrofit getRetrofit() {
        return retrofit;
    }

}
