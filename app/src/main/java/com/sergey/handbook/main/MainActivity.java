package com.sergey.handbook.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.sergey.handbook.R;
import com.sergey.handbook.info.InfoActivity;

public class MainActivity extends AppCompatActivity implements MainView {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    ProgressBar progressBar;
    SearchView searchView;
    MainPresenter presenter;
    MainService mainService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_srl);
        listView = (ListView) findViewById(R.id.main_list_view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getContactsList(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        mainService = new MainService(this);
        presenter = new MainPresenter(this, mainService);
        presenter.getContactsList(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconifiedByDefault(true);

        presenter.getSearchViewOnQueryTextListener();
        presenter.getSearchViewOnSuggestionListener();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, InfoActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setAdapterToListView(ContactsAdapter contactsAdapter) {
        if (listView != null) {
            listView.setAdapter(contactsAdapter);
        }
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void showAlertDialog(int text) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.network_connection_error_title)
                .setMessage(getString(text))
                .setPositiveButton(R.string.ok_button, null)
                .create();
        alertDialog.show();
    }

    @Override
    public void setListOnItemClickListener(AdapterView.OnItemClickListener listOnItemClickListener) {
        listView.setOnItemClickListener(listOnItemClickListener);
    }

    @Override
    public void setSearchViewOnQueryTextListener(SearchView.OnQueryTextListener onQueryTextListener) {
        searchView.setOnQueryTextListener(onQueryTextListener);
    }

    @Override
    public void setSearchViewOnSuggestionListener(SearchView.OnSuggestionListener onSuggestionListener) {
        searchView.setOnSuggestionListener(onSuggestionListener);
    }

}
