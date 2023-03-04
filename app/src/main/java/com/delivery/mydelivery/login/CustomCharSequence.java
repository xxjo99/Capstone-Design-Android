package com.delivery.mydelivery.login;

import androidx.annotation.NonNull;

public class CustomCharSequence implements CharSequence {

    private final CharSequence source;

    public CustomCharSequence(CharSequence source) {
        this.source = source;
    }

    @Override
    public int length() {
        return source.length();
    }

    @Override
    public char charAt(int index) {
        return '*';
    }

    @NonNull
    @Override
    public CharSequence subSequence(int startIndex, int endIndex) {
        return source.subSequence(startIndex, endIndex);
    }
}