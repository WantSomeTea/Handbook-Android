package com.sergey.handbook.info;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.PhoneNumberUtils;

import com.sergey.handbook.Preferences;
import com.sergey.handbook.R;

/**
 * Created by Sergey.
 */
public class InfoService {
    private final Context context;
    private SharedPreferences sharedPreferences;

    public InfoService(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Preferences.getPreferences.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getPhoneNumber() {
        String phoneNumber;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            phoneNumber = context.getString(R.string.phone_number_text, "+" + PhoneNumberUtils.formatNumber(sharedPreferences.getString(Preferences.getPreferences.PHONE_NUMBER, ""), "RU"));
        } else {
            phoneNumber = context.getString(R.string.phone_number_text, PhoneNumberUtils.formatNumber(sharedPreferences.getString(Preferences.getPreferences.PHONE_NUMBER, "")));
        }
        return phoneNumber;
    }

    public String getVersion() {
        String version = "";
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = context.getString(R.string.version_text, version);
        return version;
    }
}
