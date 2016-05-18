package com.sergey.handbook.register;

/**
 * Created by Sergey.
 */
interface RegisterView {
    String getPhoneNumber();

    void showPhoneNumberError(int resId);
}
