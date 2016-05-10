package com.sergey.handbook.main;

import android.os.AsyncTask;
import android.view.View;

import com.sergey.handbook.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sergey.
 */
public class MainPresenter {
    private final MainView view;
    private final MainService service;
    private ContactsAdapter contactsAdapter;

    MainPresenter(MainView view, MainService service) {
        this.view = view;
        this.service = service;
    }

    public void getContactsList(final Boolean isSwipeRefresh) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    contactsAdapter = service.getContactsAdapter(isSwipeRefresh);
                } catch (ExecutionException | InterruptedException | JSONException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(!isSwipeRefresh) {
                    view.setProgressBarVisibility(View.GONE);
                }
                if (contactsAdapter != null) {
                    view.setAdapterToListView(contactsAdapter);
                    view.setListOnItemClickListener(service.getListOnItemClickListener());
                } else {
                    view.showAlertDialog(R.string.network_error_text);
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!isSwipeRefresh) {
                    view.setProgressBarVisibility(View.VISIBLE);
                }
            }
        };
        asyncTask.execute();
    }

}
