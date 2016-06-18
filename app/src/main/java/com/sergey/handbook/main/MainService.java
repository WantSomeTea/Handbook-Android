package com.sergey.handbook.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;

import com.sergey.handbook.Employee;
import com.sergey.handbook.Preferences;
import com.sergey.handbook.R;
import com.sergey.handbook.SuggestionProvider;
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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * Created by Sergey.
 */
class MainService {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SearchRecentSuggestions suggestions;

    MainService(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Preferences
                .getPreferences.APP_PREFERENCES, Context.MODE_PRIVATE);
        suggestions = new SearchRecentSuggestions(context,
                SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
    }

    ArrayList<Employee> getContactsList(Boolean isSwipeRefresh) {
        ArrayList<Employee> contactsList = null;
        try {
            contactsList = Utils.getContactsList();
            if (contactsList == null || isSwipeRefresh) {
                contactsList = getContactsListFromAPI();
                Utils.setContactsList(contactsList);
            }
        } catch (InterruptedException | ExecutionException | JSONException | IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return contactsList;
    }

    ContactsAdapter getContactsAdapter(ArrayList<Employee> contactsList)
            throws InterruptedException, ExecutionException, JSONException, IOException {

        ContactsAdapter contactsAdapter = null;
        if (contactsList != null && contactsList.size() != 0) {
            Comparator<Employee> comparator = new Comparator<Employee>() {
                @Override
                public int compare(Employee lhs, Employee rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            };
            Collections.sort(contactsList, comparator);
            contactsAdapter = new ContactsAdapter(context,
                    R.layout.contacts_row,
                    contactsList);
        }
        return contactsAdapter;
    }

    private ArrayList<Employee> getContactsListFromAPI()
            throws ExecutionException, InterruptedException, IOException, JSONException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        ArrayList<Employee> contactsArrayList = new ArrayList<>();
        String phoneNumber = sharedPreferences.getString(Preferences
                .getPreferences.PHONE_NUMBER, "");
        String key = sharedPreferences.getString(Preferences.getPreferences.KEY, "");
        String phonebookPath = sharedPreferences.getString(Preferences
                .getPreferences.GET_PHONEBOOK_PATH, "");

        OkHttpClient client = new OkHttpClient().setSslSocketFactory(Utils.getSSLContext(context).getSocketFactory());
        Request request = new Request.Builder()
                .url(context.getString(R.string.server_name)
                        + phonebookPath + "?" + context.getString(R.string.phonebook_params, phoneNumber, key))
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == HttpURLConnection.HTTP_OK) {
            JSONArray jsonArray = new JSONArray(response.body().string());
            for (int i = 0; i < jsonArray.length(); i++) {
                Employee employee = new Employee();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                employee.setName(jsonObject.getString("name"));
                employee.setPhoneNumber(jsonObject.getString("phoneNumber"));
                employee.setWorkNumber(jsonObject.getString("workNumber"));
                employee.setEmail(jsonObject.getString("email"));
                employee.setCompanyName(jsonObject.getString("companyName"));
                employee.setJobName(jsonObject.getString("jobName"));
                employee.setDepartmentName(jsonObject.getString("departmentName"));
                contactsArrayList.add(employee);
            }
        } else {
            contactsArrayList = null;
        }
        response.body().close();
        return contactsArrayList;
    }

    AdapterView.OnItemClickListener getListOnItemClickListener(final ArrayList<Employee> list) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee employee = list.get(position);

                suggestions.saveRecentQuery(employee.getName(), null);

                context.startActivity(getContactIntent(employee));
            }
        };
    }

    private Intent getContactIntent(Employee employee) {
        Intent intent = new Intent(context, ContactInfoActivity.class);
        intent.putExtra("employee", employee);

        return intent;
    }

    ArrayList<Employee> getTempContactsArrayList(ArrayList<Employee> contactsArrayList, String query) {
        ArrayList<Employee> temp = new ArrayList<>();
        int queryLength = query.length();
        temp.clear();
        if (contactsArrayList != null && contactsArrayList.size() != 0) {
            for (int i = 0; i < contactsArrayList.size(); i++) {
                if (queryLength <= contactsArrayList.get(i).getName().length() ||
                        queryLength <= contactsArrayList.get(i).getWorkNumber().length()) {
                    Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
                    if (pattern.matcher(contactsArrayList.get(i).getName()).find() ||
                            pattern.matcher(contactsArrayList.get(i).getWorkNumber()).find()) {
                        temp.add(contactsArrayList.get(i));
                    }
                }
            }
        }

        return temp;
    }

    void onSuggestionEvent(ArrayList<Employee> contactsList, int position, SearchView searchView) {
        if (contactsList != null && contactsList.size() != 0) {
            Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
            String feedName = cursor.getString(3);
            for (Employee employee : contactsList) {
                String name = employee.getName();
                if (name.equals(feedName)) {
                    suggestions.saveRecentQuery(name, null);

                    context.startActivity(getContactIntent(employee));
                }
            }
        }
    }
}
