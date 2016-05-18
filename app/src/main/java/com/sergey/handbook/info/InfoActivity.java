package com.sergey.handbook.info;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.sergey.handbook.R;

public class InfoActivity extends AppCompatActivity implements InfoView{
    private InfoPresenter presenter;
    private InfoService service;
    private TextView phoneNumberText;
    private TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        phoneNumberText = (TextView) findViewById(R.id.textView_info_phoneNumber);
        versionText = (TextView) findViewById(R.id.textView_info_version);

        service = new InfoService(this);
        presenter = new InfoPresenter(this, service);

        presenter.setTextToView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTextToView(String phoneNumber, String version) {
        phoneNumberText.setText(phoneNumber);
        versionText.setText(version);
    }
}
