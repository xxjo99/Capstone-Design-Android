package com.delivery.mydelivery.register;

import androidx.annotation.NonNull;

import java.io.Serializable;

// 회원 정보
public class UserVO implements Serializable {

    private int userId; // 사용자 식별자
    private String email; // 사용자 이메일
    private String pw; // 사용자 비밀번호
    private String name; // 사용자 이름
    private String birth; // 사용자 생일
    private String phoneNum; // 사용자 휴대폰 번호

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserVO{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", birth='" + birth + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
