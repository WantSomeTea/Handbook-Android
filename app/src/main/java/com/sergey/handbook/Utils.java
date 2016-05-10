package com.sergey.handbook;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sergey.
 */
public class Utils {
    private static ArrayList<HashMap<String, String>> contactsList;
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

    public static ArrayList<HashMap<String, String>> getContactsList() {
        return contactsList;
    }

    public static void setContactsList(ArrayList<HashMap<String, String>> contactsList) {
        Utils.contactsList = contactsList;
    }
}
