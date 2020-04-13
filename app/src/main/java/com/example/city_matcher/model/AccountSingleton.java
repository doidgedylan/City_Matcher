package com.example.city_matcher.model;

import java.util.HashMap;

public class AccountSingleton {
    private static AccountSingleton single_instance = null;
    private HashMap<String, String> accountIDs;

    private AccountSingleton() {
        accountIDs = new HashMap<>();
    }

    public static AccountSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new AccountSingleton();
        }
        return single_instance;
    }

    public String getID(String email) {
        return accountIDs.get(email);
    }

    public void addAccount(String email, String id) {
        accountIDs.put(email, id);
    }

    public void removeAccount(String email) { accountIDs.remove(email); }

    public HashMap getMap() {return accountIDs;}
}
