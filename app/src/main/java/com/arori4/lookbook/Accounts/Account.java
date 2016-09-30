package com.arori4.lookbook.Accounts;


import com.arori4.lookbook.Closet.Closet;
import com.arori4.lookbook.Lookbook.Lookbook;

/**
 * Created by Christopher Cabreros on 08-May-16.
 */
public class Account{

    private String mAuthToken;
    private Closet mCloset;
    private Lookbook mLookbook;

    //Looking for Account.currentAccountInstance? The declaration has been changed to
    //IClosetApplication.getAccount()

    public Account(String authToken) {
        mAuthToken = authToken;
        mCloset = new Closet();
        mLookbook = new Lookbook();
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public void setAuthToken(String authToken) {
        mAuthToken = authToken;
    }

    public Closet getCloset() {
        return mCloset;
    }

    public void setCloset(Closet closet) {
        mCloset = closet;
    }

    public Lookbook getLookbook() {
        return mLookbook;
    }

    public void setLookbook(Lookbook lookbook) {
        mLookbook = lookbook;
    }
}
