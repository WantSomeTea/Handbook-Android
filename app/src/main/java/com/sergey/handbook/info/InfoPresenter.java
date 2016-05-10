package com.sergey.handbook.info;

/**
 * Created by Sergey.
 */
public class InfoPresenter {
    private final InfoView view;
    private final InfoService service;

    public InfoPresenter(InfoView view, InfoService service) {
        this.view = view;
        this.service = service;
    }

    public void setTextToView() {
        String phoneNumber = service.getPhoneNumber();
        String version = service.getVersion();
        view.setTextToView(phoneNumber, version);
    }
}
