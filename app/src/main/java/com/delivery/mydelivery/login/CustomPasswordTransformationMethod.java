package com.delivery.mydelivery.login;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class CustomPasswordTransformationMethod extends PasswordTransformationMethod {

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new CustomCharSequence(source);
    }
}