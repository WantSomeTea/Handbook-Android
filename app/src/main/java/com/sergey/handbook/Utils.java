package com.sergey.handbook;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Sergey.
 */
public class Utils {
    private static ArrayList<Employee> contactsList;
    public String transformNumber(String phoneNumber) {
        try {
            phoneNumber = phoneNumber.replaceAll("\\D", "");
            StringBuilder stringBuilder = new StringBuilder(phoneNumber);
            stringBuilder.setCharAt(0, '7');
            phoneNumber = stringBuilder.toString();
        } catch (StringIndexOutOfBoundsException e) {
            phoneNumber = "";
            e.printStackTrace();
        }

        return phoneNumber;
    }

    public static ArrayList<Employee> getContactsList() {
        return contactsList;
    }

    public static void setContactsList(ArrayList<Employee> contactsList) {
        Utils.contactsList = contactsList;
    }

    public static SSLContext getSSLContext(Context context) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        InputStream caInput;
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        AssetManager assetManager = context.getAssets();
        caInput = new BufferedInputStream(assetManager.open("2_factory-handbook.ru.crt"));
        Certificate ca = null;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            caInput.close();
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        //HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        return sslContext;
    }
}
