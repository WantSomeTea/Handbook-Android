package com.sergey.handbook.contact_info;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Toast;

import com.sergey.handbook.Employee;
import com.sergey.handbook.Permissions;


/**
 * Created by Sergey.
 */
class ContactInfoService {
    private final Context context;
    private String name;
    private String companyName;
    private String jobName;
    private String departmentName;
    private String phoneNumber;
    private String workNumber;
    private String email;
    private Boolean isPermissionsGranted = false;

    ContactInfoService(Context context) {
        this.context = context;
    }


    void getDataFromIntent(Intent intent) {
        Employee employee = intent.getParcelableExtra("employee");
        name = employee.getName();
        companyName = employee.getCompanyName();
        jobName = employee.getJobName();
        departmentName = employee.getDepartmentName();
        phoneNumber = employee.getPhoneNumber();
        workNumber = employee.getWorkNumber();
        email = employee.getEmail();
    }

    String getName() {
        return name;
    }

    String getCompanyName() {
        return companyName;
    }

    String getJobName() {
        return jobName;
    }

    String getDepartmentName() {
        return departmentName;
    }

    String getPhoneNumber() {
        if (phoneNumber != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                phoneNumber = "+" + PhoneNumberUtils.formatNumber(phoneNumber, "RU");
            } else {
                phoneNumber = "+" + PhoneNumberUtils.formatNumber(phoneNumber);
            }
        }
        return phoneNumber;
    }

    String getWorkNumber() {
        return workNumber;
    }

    String getEmail() {
        return email;
    }

    private Boolean getPermissionsGranted() {
        return isPermissionsGranted;
    }

    void setPermissionsGranted(Boolean permissionsGranted) {
        isPermissionsGranted = permissionsGranted;
    }

    View.OnClickListener getMobileCallOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:+" + phoneNumber));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},
                                    Permissions.getPermissionCode.PERMISSION_PHONE_CALL);
                            if (getPermissionsGranted()) {
                                context.startActivity(intent);
                            }
                        } else {
                            context.startActivity(intent);
                        }
                    } else {
                        context.startActivity(intent);
                    }
                }
            }
        };
    }

    View.OnClickListener getEmailOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                try {
                    context.startActivity(Intent.createChooser(intent, "Отправить email..."));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Не установлено ни одного email клиента", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
    }
}
