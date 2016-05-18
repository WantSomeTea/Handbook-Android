package com.sergey.handbook.contact_info;

/**
 * Created by Sergey.
 */
class ContactInfoPresenter {
    private final ContactInfoView view;
    private final ContactInfoService service;

    ContactInfoPresenter(ContactInfoView view, ContactInfoService service) {
        this.view = view;
        this.service = service;
    }

    void setDataToActivity() {
        view.setDataToTextViews(service.getName(),
                service.getCompanyName(),
                service.getJobName(),
                service.getDepartmentName(),
                service.getPhoneNumber(),
                service.getWorkNumber(),
                service.getEmail());
        view.setEmailOnClickListener(service.getEmailOnClickListener());
        view.setMobileCallOnClickListener(service.getMobileCallOnClickListener());
    }
}
