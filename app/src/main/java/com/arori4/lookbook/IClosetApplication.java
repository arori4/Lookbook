package com.arori4.lookbook;

import android.app.Application;

import com.arori4.lookbook.Accounts.Account;

/**
 * Created by Christopher Cabreros on 01-Jun-16.
 */
public class IClosetApplication extends Application {

    public static final String PREFERENCE_CLOTHING_ID = "id_list";
    public static final String PREFERENCE_LOOKBOOK_ID = "id_lookbook";

    private static IClosetApplication singleton;
    private static Account mAccount;

    private static int counter;

    public static IClosetApplication getInstance() {
        return singleton;
    }

    public static Account getAccount(){
        return mAccount;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        mAccount = new Account("AUTHORITY");

        System.err.println("ON CREATE" + (counter++));
    }

}
