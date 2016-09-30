package com.arori4.lookbook.Closet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.arori4.lookbook.BaseActivity;
import com.arori4.lookbook.IClosetApplication;
import com.arori4.lookbook.R;

public class UpdateClothingActivity extends BaseActivity {

    private Spinner category;
    private Spinner weather;
    private Spinner occasion;
    private Spinner color;

    private CheckBox worn;
    private CheckBox shared;
    private CheckBox lost;

    private Button doneButton;
    private Button deleteButton;

    private EditText notes;

    private ImageButton addClothingPreview;
    private Bitmap currentBitmap;


    private Closet mCurrCloset = IClosetApplication.getAccount().getCloset();
    String id;
    private Clothing mCurrClothing;

    private AlertDialog imagePreviewDialog;

    SharedPreferences mPrefs;

    Gson gson;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_clothing);
        setToolbar((Toolbar) findViewById(R.id.toolbar));

        //get passed in clothing id to get correct clothing object from fragment_closet
        id = (String) getIntent().getSerializableExtra("clothing_id");
        System.err.println("RENU: finding clothing with id in update clothing: " + id);
        mCurrClothing = mCurrCloset.findClothingByID(id);

        //Setup spinners
        String[] cat_array = new String[]{"Select", Clothing.ACCESSORY, Clothing.TOP, Clothing.BOTTOM, Clothing.SHOE, Clothing.BODY, Clothing.HAT, Clothing.JACKET};
        category = initSpinner(R.id.Category, cat_array);
        category.setSelection(getIndex(category, mCurrClothing.getCategory()));


        String[] weat_array = new String[]{"Select", "Snow", "Rain", "Cold", "Cool", "Warm", "Hot", "Select All"};
        weather = initSpinner(R.id.Weather, weat_array);
        weather.setSelection(getIndex(weather, mCurrClothing.getWeather()));

        String[] occ_array = new String[]{"Select", "Casual", "Work", "Semi-formal", "Formal", "Fitness", "Party", "Business"};
        occasion = initSpinner(R.id.Occasion, occ_array);
        occasion.setSelection(getIndex(occasion, mCurrClothing.getOccasion()));

        //eventually make this colored squares
        String[] col_array = new String[]{"Select", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "Black", "White", "Gray"};
        color = initSpinner(R.id.Color, col_array);
        color.setSelection(getIndex(color, mCurrClothing.getColor()));

        //get edit text notes and check boxes
        notes = (EditText) findViewById(R.id.Notes);
        if (notes != null) {
            notes.setText(mCurrClothing.getNotes(), TextView.BufferType.EDITABLE);
        }

        worn = (CheckBox) findViewById(R.id.Worn);
        if (mCurrClothing.isWorn())
            worn.setChecked(true);

        shared = (CheckBox) findViewById(R.id.Shared);
        if (mCurrClothing.isShared())
            shared.setChecked(true);

        lost = (CheckBox) findViewById(R.id.Lost);
        if (mCurrClothing.isLost())
            lost.setChecked(true);

        //set the image preview
        addClothingPreview = (ImageButton) findViewById(R.id.add_clothing_image_preview);
        if (addClothingPreview != null) {
            addClothingPreview.setImageBitmap(mCurrClothing.getBitmap());
        }
        //final String id = (String) getIntent().getSerializableExtra("photo_id");

        //done button
        doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get all selections
                String selected_category = category.getSelectedItem().toString();
                String selected_weather = weather.getSelectedItem().toString();
                String selected_occasion = occasion.getSelectedItem().toString();
                String selected_color = color.getSelectedItem().toString();
                String input_notes = notes.getText().toString();
                //check whether it is a valid condition
                boolean validSelections = validateClothingAttributes(selected_category, selected_weather, selected_occasion, selected_color);

                //receive the checkmarks
                boolean isWorn = false;
                if (worn.isChecked())
                    isWorn = true;
                boolean isShared = false;
                if (shared.isChecked())
                    isShared = true;
                boolean isLost = false;
                if (lost.isChecked())
                    isLost = true;

                //create new clothing object - set to currClothing and add to fragment_closet
                if (validSelections) {

                    //update currClothing
                    mCurrClothing.setCategory(selected_category);
                    mCurrClothing.setWeather(selected_weather);
                    mCurrClothing.setOccasion(selected_occasion);
                    mCurrClothing.setColor(selected_color);
                    mCurrClothing.setWorn(isWorn);
                    mCurrClothing.setShared(isShared);
                    mCurrClothing.setLost(isLost);
                    mCurrClothing.setNotes(input_notes);

                    //Receive preferences
                    mPrefs = getSharedPreferences("com.arori4.fragment_lookbook", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    gson = new Gson();

                    //temp set bitmap to null, so doesnt bitmap store to json (reset later)
                    currentBitmap=mCurrClothing.getBitmap();
                    mCurrClothing.setBitmap(null);

                    // Store clothing object
                    String clothing = gson.toJson(mCurrClothing); //we are also storing the bitmap as a full thing here. this is a problem.
                    prefsEditor.putString(mCurrClothing.getId(), clothing);
                    prefsEditor.apply();

                    // Store id list
                    String id_list = gson.toJson(mCurrCloset.getIdList());
                    prefsEditor.putString("id_list", id_list); //TODO Tyler check this implementation, maybe we need to have a string array of ids
                    prefsEditor.apply();

                    //Now reset the bitmap
                    mCurrClothing.setBitmap(currentBitmap);

                    goBack();
                }
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * We will not create a new intent. Finishing this should go back to fragment_closet.
     */
    protected void goBack() {
        this.finish();
    }

    //creates dropdowns given a string and spinner object
    protected Spinner initSpinner(int resource, String[] arr) {
        Spinner sp = (Spinner) findViewById(resource);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        if (sp != null) {
            sp.setAdapter(adapter);
        } else {
            System.err.println("SP is null in UpdateClothingActivity.java");
        }

        return sp;

    }


    /**
     * Validates whether the selections are not "Select"
     *
     * @param cat
     * @param weath
     * @param occ
     * @param col
     * @return
     */
    protected boolean validateClothingAttributes(String cat, String weath, String occ, String col) {
        if (cat.equals("Select") || weath.equals("Select") || occ.equals("Select") || col.equals("Select")) {
            Toast newToast = Toast.makeText(this, "Invalid attributes", Toast.LENGTH_SHORT);
            newToast.show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Get the layout and set into the alertDialog
        LayoutInflater inflater = getLayoutInflater();
        View previewLayout = inflater.inflate(R.layout.add_clothing_image_dialog, null);
        AlertDialog.Builder previewBuilder = new AlertDialog.Builder(this);
        previewBuilder.setTitle("Preview");
        previewBuilder.setView(previewLayout);

        //Set the imageView
        ImageView previewView = (ImageView) previewLayout.findViewById(R.id.add_clothing_image_dialog_image_view);
        previewView.setImageBitmap(mCurrClothing.getBitmap());

        //Set the button
        previewBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        imagePreviewDialog = previewBuilder.create();
    }

    public void showImagePreview(View view) {
        imagePreviewDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "UpdateClothing Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.arori4.fragment_lookbook/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /*
    //return list for color spinner
    public ArrayList<ListItem> getAllList() {

        ArrayList<ListItem> allList = new ArrayList<ListItem>();

        //"Select","Red", "Orange", "Yellow", "Green", "Blue","Purple", "Pink","Brown", "Black","White","Gray"

        ListItem item = new ListItem();
        item.setData("Select", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Red", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Orange", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Yellow", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Green", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Blue", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Purple", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Pink", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Brown", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Black", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("White", R.drawable.white);
        allList.add(item);

        item = new ListItem();
        item.setData("Gray", R.drawable.white);
        allList.add(item);


        for (int i = 0; i < 10000; i++) {
            item = new ListItem();
            item.setData("Test " + i, R.drawable.white);
            allList.add(item);
        }

        return allList;
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_clothing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //class to hold color image and string
    public class ListItem {
        String name;
        int logo;

        public void setData(String name, int logo) {
            this.name = name;
            this.logo = logo;
        }
    }

    //adaptor to display images in spinner
    public class MyAdapter extends ArrayAdapter<ListItem> {

        LayoutInflater inflater;
        ArrayList<ListItem> objects;
        ViewHolder holder = null;

        public MyAdapter(Context mContext, int textViewResourceId, ArrayList<ListItem> objects) {
            super(mContext, textViewResourceId, objects);
            inflater = ((Activity) mContext).getLayoutInflater();
            this.objects = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            ListItem listItem = objects.get(position);
            View row = convertView;

            if (null == row) {
                holder = new ViewHolder();
                row = inflater.inflate(R.layout.row, parent, false);
                holder.name = (TextView) row.findViewById(R.id.name);
                holder.imgThumb = (ImageView) row.findViewById(R.id.imgThumb);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            holder.name.setText(listItem.name);
            holder.imgThumb.setBackgroundResource(listItem.logo);

            return row;
        }

        static class ViewHolder {
            TextView name;
            ImageView imgThumb;
        }
    } */
}



