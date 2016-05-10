package com.sergey.handbook.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import com.sergey.handbook.Preferences;
import com.sergey.handbook.R;
import com.sergey.handbook.Utils;
import com.sergey.handbook.contact_info.ContactInfoActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sergey.
 */
public class MainService {
    private Context context;
    private SharedPreferences sharedPreferences;

    public MainService(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Preferences
                .getPreferences.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public ContactsAdapter getContactsAdapter(Boolean isSwipeRefresh)
            throws InterruptedException, ExecutionException, JSONException, IOException {
        ArrayList<HashMap<String, String>> contactsList;
        contactsList = Utils.getContactsList();
        if (contactsList == null || isSwipeRefresh) {
            contactsList = getContactsList();
            Utils.setContactsList(contactsList);
        }
        ContactsAdapter contactsAdapter = null;
        if (contactsList != null && contactsList.size() != 0) {
            Comparator<HashMap<String, String>> comparator = new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
                    return lhs.get("name").compareTo(rhs.get("name"));
                }
            };
            Collections.sort(contactsList, comparator);
            contactsAdapter = new ContactsAdapter(context,
                    contactsList,
                    R.layout.contacts_row,
                    new String[] {"name", "workNumber"},
                    new int[] {R.id.CONTACTS_NAME_CELL, R.id.textView_PBX});
        }
        return contactsAdapter;
    }

    public ArrayList<HashMap<String, String>> getContactsList()
            throws ExecutionException, InterruptedException, IOException, JSONException {
        ArrayList<HashMap<String, String>> contactsArrayList = new ArrayList<>();
        String phoneNumber = sharedPreferences.getString(Preferences
                .getPreferences.PHONE_NUMBER, "");
        String key = sharedPreferences.getString(Preferences.getPreferences.KEY, "");
        String phonebookPath = sharedPreferences.getString(Preferences
                .getPreferences.GET_PHONEBOOK_PATH, "");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(context.getString(R.string.server_name)
                        + phonebookPath + "?" + context.getString(R.string.phonebook_params, phoneNumber, key))
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == HttpURLConnection.HTTP_OK) {
            JSONArray jsonArray = new JSONArray(response.body().string());
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                hashMap.put("name", jsonObject.getString("name"));
                hashMap.put("phoneNumber", jsonObject.getString("phoneNumber"));
                hashMap.put("workNumber", jsonObject.getString("workNumber"));
                hashMap.put("email", jsonObject.getString("email"));
                hashMap.put("additionalNumbs", jsonObject.getString("additionalNumbs"));
                hashMap.put("companyName", jsonObject.getString("companyName"));
                hashMap.put("jobName", jsonObject.getString("jobName"));
                hashMap.put("departmentName", jsonObject.getString("departmentName"));
                contactsArrayList.add(hashMap);
            }
        } else {
            contactsArrayList = null;
        }
        response.body().close();
        return contactsArrayList;
    }

    public AdapterView.OnItemClickListener getListOnItemClickListener() {
        final ArrayList<HashMap<String, String>> list = Utils.getContactsList();
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = list.get(position);

                Intent intent = new Intent(context, ContactInfoActivity.class);
                intent.putExtra("name", map.get("name"));
                intent.putExtra("phoneNumber", map.get("phoneNumber"));
                intent.putExtra("workNumber", map.get("workNumber"));
                intent.putExtra("email", map.get("email"));
                intent.putExtra("additionalNumbs", map.get("additionalNumbs"));
                intent.putExtra("companyName", map.get("companyName"));
                intent.putExtra("jobName", map.get("jobName"));
                intent.putExtra("departmentName", map.get("departmentName"));

                context.startActivity(intent);
            }
        };
    }
}
