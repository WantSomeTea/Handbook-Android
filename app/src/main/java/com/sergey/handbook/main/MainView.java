package com.sergey.handbook.main;

import android.support.v7.widget.SearchView;
import android.widget.AdapterView;

/**
 * Created by Sergey.
 */
interface MainView {

    void setAdapterToListView(ContactsAdapter contactsAdapter);

    void setProgressBarVisibility(int visibility);

    void showAlertDialog(int text);

    void setListOnItemClickListener(AdapterView.OnItemClickListener listOnItemClickListener);

    void setSearchViewOnQueryTextListener(SearchView.OnQueryTextListener onQueryTextListener);

    void setSearchViewOnSuggestionListener(SearchView.OnSuggestionListener onSuggestionListener);

    SearchView getSearchView();
}
