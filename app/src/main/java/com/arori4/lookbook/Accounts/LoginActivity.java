package com.arori4.lookbook.Accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;

//import com.firebase.client.AuthData;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.arori4.lookbook.Closet.Closet;
import com.arori4.lookbook.Closet.Clothing;
import com.arori4.lookbook.BaseActivity;
import com.arori4.lookbook.IClosetApplication;
import com.arori4.lookbook.Lookbook.Lookbook;
import com.arori4.lookbook.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    // Storage variables
    SharedPreferences mPrefs;
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }


    public void runLogin(View view) {
        //Receive account singleton
        Account currentAccount = IClosetApplication.getAccount();

        //Load fragment_closet
        Closet currentCloset = currentAccount.getCloset();
        loadCloset(currentCloset);

        //Load Lookbook
        loadLookbookToAccount(currentAccount);


        //Start a new intent
        Intent intent = new Intent(this, BaseActivity.class);
        this.finish();
        startActivity(intent);
    }

    /**
     * Loads in the pictures and new other shit
     * @param clothingList
     * @param id
     * @throws IOException
     */
    private void loadPictures(List<Clothing> clothingList, List<String> id) throws IOException {
        System.err.println("Beginning to load in files and bitmaps");
        for (int i = 0; i < id.size(); i++) {

            System.err.println("ID at position " + i + "is " + id);

            if (id.get(i).contains(".jpg") || id.get(i).contains(".png")) {

                //scale down first bitmap
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize  = 8; //make the image 1/4 the size
                Bitmap firstBitmap = BitmapFactory.decodeFile(id.get(i), options);

                //Rotate the bitmap and remove first bitmap
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap currentBitmap = Bitmap.createBitmap(firstBitmap, 0, 0,
                        firstBitmap.getWidth(), firstBitmap.getHeight(), matrix, true);
                firstBitmap.recycle();

                //Recycle the bitmap to preserve memory
                firstBitmap.recycle();

                //Decode the clothing
                String json = mPrefs.getString(id.get(i), "");
                Clothing currClothing = gson.fromJson(json, Clothing.class);
                currClothing.setBitmap(currentBitmap);
                clothingList.add(currClothing);

                System.out.println("Loaded file " + i);
            }
        }

        System.err.println("Finished loading shit");
    }

    private void loadCloset(Closet currentCloset){

        //Receive preferences
        mPrefs  = getSharedPreferences("com.arori4.fragment_lookbook", Context.MODE_PRIVATE);

        //Get the list of IDs. Create a new one if it's not available
        String ids = mPrefs.getString(IClosetApplication.PREFERENCE_CLOTHING_ID, "");
        ArrayList<String> list_id = (ArrayList<String>) gson.fromJson(ids, List.class);
        if (list_id == null) {
            list_id = new ArrayList<>();
            System.err.println("Created a new id list in LoginActivity.java");
        }
        currentCloset.setIdList(list_id);

        //Load the pictures and clothing
        try {
            loadPictures(currentCloset.getList(), currentCloset.getIdList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadLookbookToAccount(Account currentAccount) {

        //Receive preferences
        mPrefs  = getSharedPreferences("com.arori4.fragment_lookbook", Context.MODE_PRIVATE);

        //Receive fragment_lookbook
        //http://stackoverflow.com/questions/5554217/google-gson-deserialize-listclass-object-generic-type/5554296#5554296
        String lookbookString = mPrefs.getString(IClosetApplication.PREFERENCE_LOOKBOOK_ID, "");
        Type currentType = new TypeToken<List<List<String>>>(){}.getType();
        List<List<String>> currentLookbookString = new Gson().fromJson(lookbookString, currentType);
        if (currentLookbookString == null) {
            currentLookbookString = new ArrayList<>();
            System.err.println("Created a new fragment_lookbook in LoginActivity.java");
        }

        Lookbook currentLookbook = currentAccount.getLookbook();
        currentLookbook.assignBelongingCloset(currentAccount.getCloset());
        currentLookbook.deserializeAllOutfits(currentLookbookString);
    }


}
