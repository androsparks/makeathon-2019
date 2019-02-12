package com.selectmakeathon.app.util;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class FormUtil {

    public static Boolean emailValid(TextInputLayout et) {
        CharSequence email = et.getEditText().getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static Boolean isEmpty(TextInputLayout et) {
        CharSequence str = et.getEditText().getText().toString();
        return TextUtils.isEmpty(str);
    }

    public static Boolean phoneValid(TextInputLayout et) {
        CharSequence phone = et.getEditText().getText().toString();
        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());
    }

    public static Boolean websiteValid(TextInputLayout et) {
        CharSequence website = et.getEditText().getText().toString();
        return (!TextUtils.isEmpty(website) && Patterns.WEB_URL.matcher(website).matches());
    }

}
