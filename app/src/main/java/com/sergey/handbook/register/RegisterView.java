package com.sergey.handbook.register;

/**
 * Created by Sergey.
 */
public interface RegisterView {
    String getPhoneNumber();

    void showPhoneNumberError(int resId);
}
