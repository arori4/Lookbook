package com.arori4.lookbook.Closet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.Toast;
import com.arori4.lookbook.BaseActivity;
import com.arori4.lookbook.IClosetApplication;
import com.arori4.lookbook.R;
import com.google.gson.Gson;

/**
 * Created by Christopher Cabreros on 05-May-16.
 * Adds the clothing into the database
 */
public class AddClothingActivity extends BaseActivity {

    private Spinner categorySpinner;
    private Spinner weatherSpinner;
    private Spinner occasionSpinner;
    private Spinner colorSpinner;

    private CheckBox wornBox;
    private CheckBox sharedBox;
    private CheckBox lostBox;

    private Button doneButton;

    private EditText notesEditText;

    private ImageButton addClothingPreview;
    private Bitmap currentBitmap;

    private Clothing mCurrClothing;
    private Closet mCurrCloset = IClosetApplication.getAccount().getCloset();

    private AlertDialog imagePreviewDialog;

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_clothing);
        setToolbar((Toolbar) findViewById(R.id.toolbar));

        //Setup spinners
        String[] cat_array = new String[]{"Select", Clothing.ACCESSORY, Clothing.TOP, Clothing.BOTTOM, Clothing.SHOE, Clothing.BODY, Clothing.HAT, Clothing.JACKET};
        categorySpinner = initSpinner(R.id.Category, cat_array);
        String[] weat_array = new String[]{"Select", "Snow", "Rain", "Cold", "Cool", "Warm", "Hot", "Select All"};
        weatherSpinner = initSpinner(R.id.Weather, weat_array);
        String[] occ_array = new String[]{"Select", "Casual", "Work", "Semi-formal", "Formal", "Fitness", "Party", "Business"};
        occasionSpinner = initSpinner(R.id.Occasion, occ_array);

        //eventually make this colored squares
        String[] col_array = new String[]{"Select", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "Brown", "Black", "White", "Gray"};
        colorSpinner = initSpinner(R.id.Color, col_array);

        //get edit text notesEditText and check boxes
        notesEditText = (EditText) findViewById(R.id.Notes);
        wornBox = (CheckBox) findViewById(R.id.Worn);
        sharedBox = (CheckBox) findViewById(R.id.Shared);
        lostBox = (CheckBox) findViewById(R.id.Lost);

        //set the image preview
        final String id = (String) getIntent().getSerializableExtra("photo_id");

        //scale down first bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize  = 8; //make the image 1/4 the size
        Bitmap firstBitmap = BitmapFactory.decodeFile(id, options);

        //Rotate the bitmap and remove first bitmap
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        currentBitmap = Bitmap.createBitmap(firstBitmap, 0, 0,
                firstBitmap.getWidth(), firstBitmap.getHeight(), matrix, true);
        firstBitmap.recycle();

        //Get the image preview
        addClothingPreview = (ImageButton) findViewById(R.id.add_clothing_image_preview);
        if (addClothingPreview != null) {
            addClothingPreview.setImageBitmap(currentBitmap);
        }

        //done button
        doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get all selections
                String selected_category = categorySpinner.getSelectedItem().toString();
                String selected_weather = weatherSpinner.getSelectedItem().toString();
                String selected_occasion = occasionSpinner.getSelectedItem().toString();
                String selected_color = colorSpinner.getSelectedItem().toString();
                String input_notes = notesEditText.getText().toString();
                //check whether it is a valid condition
                boolean validSelections = validateClothingAttributes(selected_category, selected_weather, selected_occasion, selected_color);

                //receive the checkmarks
                boolean isWorn = false;
                if (wornBox.isChecked())
                    isWorn = true;
                boolean isShared = false;
                if (sharedBox.isChecked())
                    isShared = true;
                boolean isLost = false;
                if (lostBox.isChecked())
                    isLost = true;

                //create new clothing object - set to currClothing and add to fragment_closet
                if (validSelections) {

                    //Create the clothing
                    mCurrClothing = new Clothing(selected_category, selected_color, selected_weather, selected_occasion, input_notes,
                            isWorn, isShared, isLost, id);
                    System.err.println("RENU: ID of Clothing added is "  + id);
                    mCurrCloset.addClothing(mCurrClothing);

                    // Store id and data in clothing
                    mCurrCloset.addId(mCurrClothing.getId());
                    //DO NOT SET BITMAP YET

                    //Receive preference editor
                    mPrefs = getSharedPreferences("com.arori4.fragment_lookbook", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();

                    // Store clothing object
                    String clothing = gson.toJson(mCurrClothing);
                    prefsEditor.putString(mCurrClothing.getId(), clothing);
                    // Store id list
                    String id_list = gson.toJson(mCurrCloset.getIdList());
                    prefsEditor.putString(IClosetApplication.PREFERENCE_CLOTHING_ID, id_list);
                    //Apply changes
                    prefsEditor.apply();

                    //Now set the bitmap
                    mCurrClothing.setBitmap(currentBitmap);
                    goBackToCloset();
                }
            }
        });
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
        previewView.setImageBitmap(currentBitmap);

        //Set the button
        previewBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        imagePreviewDialog = previewBuilder.create();
    }


    /**
     * Validates whether the selections are not "Select"
     * @param cat - category
     * @param weath - weather
     * @param occ - occasion
     * @param col - color
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


    /**
     * We will not create a new intent. Finishing this should go back to fragment_closet.
     */
    protected void goBackToCloset() {
        this.finish();
    }

    //creates dropdowns given a string array and spinner object
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
            System.err.println("SP is null in AddClothingActivity.java");
        }

        return sp;

    }

    public void goToCloset(View view) {
        finish();
    }

    public void addClothingCamera(View view) {
    }

    public void addClothingTags(View view) {
    }

    public void addClothingNext(View view) {
    }

    public void retakePicture(View view) {
        //TODO: remove picture, go to camera, retrieve bitmap from camera
        //TODO: delete old picture if it was saved
    }

    public void addClothing(View view) {
        //TODO: take the values from all the spinners and add the clothing to the fragment_closet
    }

    public void showImagePreview(View view) {
        imagePreviewDialog.show();
    }


    /**
     * Clears the attributes when the clear button is selected
     * @param view - deprecated
     */
    public void clearAttributes(View view) {
        categorySpinner.setSelection(0);
        colorSpinner.setSelection(0);
        weatherSpinner.setSelection(0);
        occasionSpinner.setSelection(0);

        lostBox.setSelected(false);
        sharedBox.setSelected(false);
        wornBox.setSelected(false);

        notesEditText.setText("");
    }
}
