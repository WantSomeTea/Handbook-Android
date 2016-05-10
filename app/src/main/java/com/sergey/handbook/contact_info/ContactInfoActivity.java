package com.sergey.handbook.contact_info;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sergey.handbook.Permissions;
import com.sergey.handbook.R;

public class ContactInfoActivity extends AppCompatActivity implements ContactInfoView {
    private TextView name;
    private TextView companyName;
    private TextView jobName;
    private TextView departmentName;
    private TextView phoneNumber;
    private TextView workNumber;
    private TextView email;
    private Button mobileCall;

    private ContactInfoService service;
    private ContactInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name = (TextView) findViewById(R.id.textView_contactName);
        companyName = (TextView) findViewById(R.id.textView_companyName);
        jobName = (TextView) findViewById(R.id.textView_jobName);
        departmentName = (TextView) findViewById(R.id.textView_departmentName);
        phoneNumber = (TextView) findViewById(R.id.textView_phoneNumber);
        workNumber = (TextView) findViewById(R.id.textView_workNumber);
        email = (TextView) findViewById(R.id.textView_email);
        mobileCall = (Button) findViewById(R.id.button_mobileCall);

        service = new ContactInfoService(this);
        presenter = new ContactInfoPresenter(this, service);

        service.getDataFromIntent(getIntent());
        presenter.setDataToActivity();
    }


    @Override
    public void setDataToTextViews(String name, String companyName, String jobName, String departmentName, String phoneNumber, String workNumber, String email) {
        if (name != null) {
            this.name.setText(name);
        } else {
            this.name.setVisibility(View.INVISIBLE);
        }
        if (companyName != null) {
            this.companyName.setText(companyName);
        } else {
            this.companyName.setVisibility(View.INVISIBLE);
        }
        if (jobName != null) {
            this.jobName.setText(jobName);
        } else {
            this.jobName.setVisibility(View.INVISIBLE);
        }
        if (departmentName != null) {
            this.departmentName.setText(departmentName);
        } else {
            this.departmentName.setVisibility(View.INVISIBLE);
        }
        if (phoneNumber != null) {
            this.phoneNumber.setText(phoneNumber);
        } else {
            this.phoneNumber.setVisibility(View.INVISIBLE);
            mobileCall.setVisibility(View.INVISIBLE);
        }
        if (workNumber != null) {
            this.workNumber.setText(workNumber);
        } else {
            this.workNumber.setVisibility(View.INVISIBLE);
        }
        if (email != null) {
            SpannableString emailString = new SpannableString(email);
            emailString.setSpan(new UnderlineSpan(), 0, emailString.length(), 0);
            this.email.setText(emailString);
        } else {
            this.email.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setMobileCallOnClickListener(View.OnClickListener mobileCallOnClickListener) {
        mobileCall.setOnClickListener(mobileCallOnClickListener);
    }

    @Override
    public void setEmailOnClickListener(View.OnClickListener emailOnClickListener) {
        email.setOnClickListener(emailOnClickListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Permissions.getPermissionCode.PERMISSION_PHONE_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    service.setPermissionsGranted(true);
                }
                break;
        }
    }
}
