package com.sergey.handbook.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.sergey.handbook.Preferences;
import com.sergey.handbook.R;
import com.sergey.handbook.Utils;
import com.sergey.handbook.main.MainActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sergey.
 */
class RegisterService {
    private Context context;

    RegisterService(Context context) {
        this.context = context;
    }

    boolean isPhoneNumberCorrect(String phoneNumber) {
        boolean isPhoneNumberCorrect = false;

        if (phoneNumber.length() == 11 && phoneNumber.startsWith("79")) isPhoneNumberCorrect = true;

        return isPhoneNumberCorrect;
    }

    boolean isPhoneNumberInDB(final String phoneNumber) throws IOException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean isNumberInDB = false;
                OkHttpClient client = new OkHttpClient().setSslSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());
                Request request = new Request.Builder()
                        .url(context.getString(R.string.server_name) + context.getString(R.string.check_phone_urlpath, phoneNumber))
                        .build();

                Response response = client.newCall(request).execute();
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    isNumberInDB = true;
                } else {
                    Log.d(RegisterService.this.getClass().getSimpleName(), String.valueOf(response.code()));
                }
                return isNumberInDB;
            }
        };
        Future<Boolean> future = executor.submit(callable);
        executor.shutdown();
        return future.get();
    }

    private void writeSettings(final String phoneNumber) {
        final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient().setSslSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory());
                    Request request = new Request.Builder()
                            .url(context.getString(R.string.server_name) + context.getString(R.string.register_path, phoneNumber))
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject requests = jsonObject.getJSONObject("requests");
                        String getPhoneBookRequest = requests.getString("employees");
                        String key = jsonObject.getString("key");

                        SharedPreferences preferences = context.getSharedPreferences(Preferences.getPreferences.APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Preferences.getPreferences.GET_PHONEBOOK_PATH, getPhoneBookRequest);
                        editor.putString(Preferences.getPreferences.PHONE_NUMBER, phoneNumber);
                        editor.putString(Preferences.getPreferences.KEY, key);
                        editor.apply();
                    }
                    response.body().close();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        asyncTask.execute();
    }

    void getDigitsBuilder(final String userPhoneNumber) {
        AuthCallback authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                writeSettings(userPhoneNumber);

                SharedPreferences sharedPreferences = context.getSharedPreferences(Preferences.getPreferences.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Preferences.getPreferences.IS_FIRST_LAUNCH, false);
                editor.apply();

                context.startActivity(new Intent(context, MainActivity.class));
            }

            @Override
            public void failure(DigitsException exception) {
                Toast.makeText(context, "Не правильно набран номер", Toast.LENGTH_LONG).show();
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        };
        DigitsAuthConfig.Builder builder = new DigitsAuthConfig.Builder()
                .withAuthCallBack(authCallback)
                .withPhoneNumber("+" + new Utils().transformNumber(userPhoneNumber))
                .withThemeResId(R.style.CustomDigitsTheme);
        Digits.authenticate(builder.build());
    }
}
