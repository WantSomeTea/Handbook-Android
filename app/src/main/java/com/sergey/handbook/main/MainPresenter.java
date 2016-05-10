package com.sergey.handbook.main;

import android.os.AsyncTask;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.sergey.handbook.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
            ArrayList<HashMap<String, String>> contactsList;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    contactsList = service.getContactsList(isSwipeRefresh);
                    contactsAdapter = service.getContactsAdapter(contactsList);
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
                if (contactsAdapter != null && contactsList != null) {
                    view.setAdapterToListView(contactsAdapter);
                    view.setListOnItemClickListener(service.getListOnItemClickListener(contactsList));
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

    public void getSearchViewOnQueryTextListener() {
        view.setSearchViewOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    ArrayList<HashMap<String, String>> contactsArrayList = service.getContactsList(false);
                    ArrayList<HashMap<String, String>> tempContactsArrayList = service.getTempContactsArrayList(contactsArrayList, query);
                    contactsAdapter = service.getContactsAdapter(tempContactsArrayList);
                    view.setAdapterToListView(contactsAdapter);
                    view.setListOnItemClickListener(service.getListOnItemClickListener(contactsArrayList));
                } catch (InterruptedException | ExecutionException | JSONException | IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    ArrayList<HashMap<String, String>> contactsArrayList = service.getContactsList(false);
                    ArrayList<HashMap<String, String>> tempContactsArrayList = service.getTempContactsArrayList(contactsArrayList, newText);
                    contactsAdapter = service.getContactsAdapter(tempContactsArrayList);
                    view.setAdapterToListView(contactsAdapter);
                    view.setListOnItemClickListener(service.getListOnItemClickListener(contactsArrayList));
                } catch (InterruptedException | ExecutionException | JSONException | IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    public void getSearchViewOnSuggestionListener() {
        view.setSearchViewOnSuggestionListener(service.getOnSuggestionListener());
    }
}
