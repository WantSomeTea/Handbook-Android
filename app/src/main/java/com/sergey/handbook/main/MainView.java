package com.sergey.handbook.main;

import android.widget.AdapterView;

/**
 * Created by Sergey.
 */
public interface MainView {

    void setAdapterToListView(ContactsAdapter contactsAdapter);

    void setProgressBarVisibility(int visibility);

    void showAlertDialog(int text);

    void setListOnItemClickListener(AdapterView.OnItemClickListener listOnItemClickListener);
}
