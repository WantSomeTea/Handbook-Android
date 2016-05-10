package com.sergey.handbook.register;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;

import com.sergey.handbook.R;

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    private EditText phoneNumberView;
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterService registerService = new RegisterService(this);

        presenter = new RegisterPresenter(this, registerService);
        phoneNumberView = (EditText) findViewById(R.id.editText_register_phone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            phoneNumberView.addTextChangedListener(new PhoneNumberFormattingTextWatcher("RU"));
        } else {
            phoneNumberView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        }
    }

    public void registerButtonOnClick(View view) {
        presenter.onRegisterClicked();
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumberView.getText().toString();
    }

    @Override
    public void showPhoneNumberError(int resId) {
        phoneNumberView.setError(getString(resId));
    }
}
