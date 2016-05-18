package com.sergey.handbook.info;

/**
 * Created by Sergey.
 */
class InfoPresenter {
    private final InfoView view;
    private final InfoService service;

    InfoPresenter(InfoView view, InfoService service) {
        this.view = view;
        this.service = service;
    }

    void setTextToView() {
        String phoneNumber = service.getPhoneNumber();
        String version = service.getVersion();
        view.setTextToView(phoneNumber, version);
    }
}
