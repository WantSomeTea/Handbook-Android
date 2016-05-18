package com.sergey.handbook.register;

import com.sergey.handbook.R;
import com.sergey.handbook.Utils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sergey.
 */
class RegisterPresenter {
    private RegisterView view;
    private RegisterService service;
    private Utils utils = new Utils();

    RegisterPresenter(RegisterView view, RegisterService service) {
        this.view = view;
        this.service = service;
    }

    void onRegisterClicked() {
        try {
            String userPhoneNumber = view.getPhoneNumber();

            userPhoneNumber = utils.transformNumber(userPhoneNumber);

            if (userPhoneNumber.isEmpty()) {
                view.showPhoneNumberError(R.string.phonenumber_empty_error);
            } else if (!service.isPhoneNumberCorrect(userPhoneNumber)) {
                view.showPhoneNumberError(R.string.phonenumber_incorrect_error);
            } else if (!service.isPhoneNumberInDB(userPhoneNumber)) {
                view.showPhoneNumberError(R.string.phonenumber_notin_DB);
            } else {
                service.getDigitsBuilder(userPhoneNumber);
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            view.showPhoneNumberError(R.string.connection_error);
            e.printStackTrace();
        }
    }
}
