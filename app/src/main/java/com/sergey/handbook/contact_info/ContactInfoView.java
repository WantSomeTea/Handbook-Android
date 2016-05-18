package com.sergey.handbook.contact_info;

import android.view.View;

/**
 * Created by Sergey.
 */
interface ContactInfoView {
    void setDataToTextViews(String name,
                            String companyName,
                            String jobName,
                            String departmentName,
                            String phoneNumber,
                            String workNumber,
                            String email);

    void setMobileCallOnClickListener(View.OnClickListener mobileCallOnClickListener);

    void setEmailOnClickListener(View.OnClickListener emailOnClickListener);
}
