package com.arori4.lookbook.Lookbook;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.arori4.lookbook.Accounts.Account;
import com.arori4.lookbook.Closet.Closet;
import com.arori4.lookbook.Closet.Clothing;
import com.arori4.lookbook.Closet.PreferenceList;
import com.arori4.lookbook.Closet.ViewClothingByCatActivity;
import com.arori4.lookbook.IClosetApplication;
import com.arori4.lookbook.R;

import java.util.ArrayList;

/**
 * Created by Christopher Cabreros on 05-May-16.
 * Creates the outfit
 */
public class OutfitGenFragment extends Fragment {

    private static final int CHOOSE_CLOTHING_REQUEST = 1;

    private Account mAccount = IClosetApplication.getAccount();
    private Lookbook mLookbook = mAccount.getLookbook();
    private Closet mCloset = mAccount.getCloset();

    private ImageButton mShoesButton;
    private LinearLayout mAccessoriesLayout;
    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private OutfitGenLinearAdapter mAccessoriesAdapter;
    private OutfitGenLinearAdapter mTopAdapter;
    private OutfitGenLinearAdapter mBottomAdapter;
    private LinearLayout.LayoutParams mAccessoriesParameters;
    private LinearLayout.LayoutParams mTopParameters;
    private LinearLayout.LayoutParams mBottomParameters;

    //Outfit and tracking variable to prevent duplicates
    private boolean mAddedOutfitAlready;
    private boolean mOutfitGeneratedAlready;
    private Outfit mCurrentOutfit = new Outfit();

    // spinners
    private Spinner weather;
    private Spinner occasion;
    private Spinner color;

    private View mCurrentView;
    Activity mContext = getActivity();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mCurrentView =  inflater.inflate(R.layout.fragment_outfit_gen, container, false);

        //Get the required views
        mShoesButton = (ImageButton) mCurrentView.findViewById(R.id.outfit_gen_shoes_button);
        mAccessoriesLayout = (LinearLayout) mCurrentView.findViewById(R.id.outfit_gen_accessories_layout);
        mTopLayout = (LinearLayout) mCurrentView.findViewById(R.id.outfit_gen_top_layout);
        mBottomLayout = (LinearLayout) mCurrentView.findViewById(R.id.outfit_gen_bottom_layout);

        //Set the adapters
        mAccessoriesAdapter = new OutfitGenLinearAdapter(mContext, Clothing.ACCESSORY);
        mTopAdapter = new OutfitGenLinearAdapter(mContext, Clothing.TOP);
        mBottomAdapter = new OutfitGenLinearAdapter(mContext, Clothing.BOTTOM);

        return mCurrentView;
    }


    @Override
    public void onStart() {
        super.onStart();

        //Set the layout parameters
        //Calculate in post because we need to get the actual height post creation
        //otherwise the height is 0dp
        mAccessoriesParameters = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mAccessoriesLayout.getHeight());
        mAccessoriesLayout.post(new Runnable() {
            @Override
            public void run() {
                mAccessoriesParameters.height = mAccessoriesLayout.getHeight();
            }
        });
        mAccessoriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("kerr");
                setManualChoosingIntent(Clothing.ACCESSORY);
            }
        });

        mTopParameters = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mTopLayout.getHeight());
        mTopLayout.post(new Runnable() {
            @Override
            public void run() {
                mTopParameters.height = mTopLayout.getHeight();
            }
        });
        mTopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("khiet");
                setManualChoosingIntent(Clothing.TOP);
            }
        });

        mBottomParameters = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mBottomLayout.getHeight());
        mBottomLayout.post(new Runnable() {
            @Override
            public void run() {
                mBottomParameters.height = mBottomLayout.getHeight();
            }
        });
        mBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("kellie");
                setManualChoosingIntent(Clothing.BOTTOM);
            }
        });

        mShoesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("kurry");
                setManualChoosingIntent(Clothing.SHOE);
            }
        });
    }


    /**
     * Saves the outfit when the button is pressed
     *
     * @param view - deprecated
     */
    public void pressSaveOutfit(View view) {
        //Add the outfit only if it hasn't been added in already
        if (!mAddedOutfitAlready && mOutfitGeneratedAlready) {

            //Prompt for name with an AlertDialog
            LayoutInflater inflater = mContext.getLayoutInflater();
            AlertDialog.Builder nameDialog = new AlertDialog.Builder(mContext);
            View nameDialogView = inflater.inflate(R.layout.outfit_name_dialog, null);
            nameDialog.setView(nameDialogView);

            //Set title
            nameDialog.setTitle("Name your outfit");

            //Receive edit text
            final EditText nameText = (EditText) nameDialogView.findViewById(R.id.outfit_name_edit_text);

            //Set cancel and okay buttons
            nameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            nameDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveOutfit(nameText.getText().toString());
                }
            });

            //Create the dialog with the alert builder object
            AlertDialog dialog = nameDialog.create();
            dialog.show();

        }
        else if (mOutfitGeneratedAlready) {
            Toast newToast = Toast.makeText(mContext, R.string.already_saved_outfit_toast_text,
                    Toast.LENGTH_SHORT);
            newToast.show();
        }
        else {
            Toast newToast = Toast.makeText(mContext, R.string.not_created_outfit_toast_text,
                    Toast.LENGTH_SHORT);
            newToast.show();
        }
    }


    /**
     * Helper method to save outfit
     * Toast declaration must be made here, because it cannot be made inside an AlertDialogBuilder
     */
    private void saveOutfit(String name){
        //Give the default name if there is no name passed in
        if (name == null || name.length() == 0){
            name = "No Name";
        }

        //Set the name and add the outfit to the fragment_lookbook
        mCurrentOutfit.setName(name);
        //TODO: set weather and occasion
        mLookbook.addOutfit(mCurrentOutfit);

        //Write to preferences
        mCurrentOutfit.updateSerializedList();

        //Receive preference editor
        SharedPreferences mPrefs = mContext.getSharedPreferences("com.arori4.fragment_lookbook", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        //Prevent circular dependencies
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).serializeNulls().create();

        //Convert fragment_lookbook to gson
        String id_list = gson.toJson(mLookbook.createSerializedList());
        prefsEditor.putString(IClosetApplication.PREFERENCE_LOOKBOOK_ID, id_list);
        //Apply changes
        prefsEditor.apply();

        //Create the toast text
        Toast newToast = Toast.makeText(mContext, "Saved outfit to fragment_lookbook", Toast.LENGTH_SHORT);
        newToast.show();

        //Prevent us from saving a duplicate
        mAddedOutfitAlready = true;
    }



    /**
     * Generates a random outfit
     *
     * @param view - deprecated
     */
    public void generateOutfit(View view) {
        //Clear all of the views
        clearLayouts();

        if (mCloset.getList().isEmpty()) {
            Toast newToast = Toast.makeText(mContext, "No clothing in fragment_closet.", Toast.LENGTH_SHORT);
            newToast.show();
        }

        //create a random outfit
        mCurrentOutfit = mLookbook.generateRandomOutfit();

        //Add components of outfits to layouts
        if (mCurrentOutfit.getHat() != null) {
            mAccessoriesAdapter.add(mCurrentOutfit.getHat());
        }
        if (mCurrentOutfit.getFirstTop() != null) {
            mTopAdapter.addAll(mCurrentOutfit.getTops());
        }
        if (mCurrentOutfit.getFirstBottom() != null) {
            mBottomAdapter.addAll(mCurrentOutfit.getBottoms());
        }
        if (mCurrentOutfit.getShoes() != null) {
            mShoesButton.setImageBitmap(mCurrentOutfit.getShoes().getBitmap());
        }
        updateLayouts();

        mOutfitGeneratedAlready = true;
        mAddedOutfitAlready = false;
        Toast newToast = Toast.makeText(mContext, "Generated a random outfit", Toast.LENGTH_SHORT);
        newToast.show();
    }

    /* Generate an outfit with PreferenceList */
    public void generateOutfitPref(PreferenceList pref) {
        //Clear all of the views
        clearLayouts();

        //Generate a random outfit with a null preference
        if (pref == null || true) { // random all of the time because algorithm is buggy
            mCurrentOutfit = mLookbook.generateRandomOutfit();
            Toast newToast = Toast.makeText(mContext, "Generated an outfit", Toast.LENGTH_SHORT);
            newToast.show();
        }
        else {
            mCurrentOutfit = mLookbook.generateOutfit(pref);
            Toast newToast = Toast.makeText(mContext, "Generated an outfit with preferences.", Toast.LENGTH_SHORT);
            newToast.show();
        }

        //Add components of outfits to layouts
        if (mCurrentOutfit.getHat() != null) {
            mAccessoriesAdapter.add(mCurrentOutfit.getHat());
        }
        if (mCurrentOutfit.getFirstTop() != null) {
            mTopAdapter.addAll(mCurrentOutfit.getTops());
        }
        if (mCurrentOutfit.getFirstBottom() != null) {
            mBottomAdapter.addAll(mCurrentOutfit.getBottoms());
        }
        if (mCurrentOutfit.getShoes() != null) {
            mShoesButton.setImageBitmap(mCurrentOutfit.getShoes().getBitmap());
        }
        updateLayouts();

        mOutfitGeneratedAlready = true;
        mAddedOutfitAlready = false;

    }

    /* Handles the dialog box for the user to choose outfit parameters */
    public void chooseAttributes(View view) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.outfit_preferences, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Choose Attributes");
        alert.setView(alertLayout);

        // find views by ids
        final Spinner wSp = (Spinner) alertLayout.findViewById(R.id.weatherSpinner);
        final Spinner oSp = (Spinner) alertLayout.findViewById(R.id.occasionSpinner);
        final Spinner cSp = (Spinner) alertLayout.findViewById(R.id.colorSpinner);

        // spinners
        String[] weat_array = new String[]{"Select","Snow","Rain","Cold", "Cool","Warm","Hot","Select All"};
        initSpinner(alertLayout, weather, R.id.weatherSpinner, weat_array);

        String[] occ_array = new String[]{"Select","Casual", "Work", "Semi-formal","Formal", "Fitness","Party", "Business"};
        initSpinner(alertLayout, occasion, R.id.occasionSpinner, occ_array);

        //eventually make this colored squares
        String[] col_array = new String[]{"Select","Red", "Orange", "Yellow", "Green", "Blue","Purple", "Pink","Brown", "Black","White","Gray"};
        initSpinner(alertLayout, color, R.id.colorSpinner, col_array);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceList pref = getAttributes(wSp, oSp, cSp);
                generateOutfitPref(pref);
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /* Create a PreferenceList from user input */
    public PreferenceList getAttributes(Spinner spWeather, Spinner spOccasion, Spinner spColor) {
        String weatherPref = spWeather.getSelectedItem().toString();
        String occasionPref = spOccasion.getSelectedItem().toString();
        String colorPref = spColor.getSelectedItem().toString();

        ArrayList<String> occasionPrefArray = new ArrayList<String>();
        occasionPrefArray.add(occasionPref);

        //Boolean mWorn, String mCategory, String mColor, String mSize, List<String> mOccasion, String mStyle, String mWeather, String mSecondaryColor
        //return new PreferenceList(colorPref, occasionPref, weatherPref);
        return new PreferenceList(Boolean.FALSE, null, colorPref, null, occasionPrefArray, null, weatherPref, null);
    }

    public void setAccessory(View view) {
        setManualChoosingIntent(Clothing.ACCESSORY);
    }

    public void setTop(View view) {
        setManualChoosingIntent(Clothing.TOP);
    }

    public void setBottom(View view) {
        setManualChoosingIntent(Clothing.BOTTOM);
    }


    /**
     * Defines the linear layout in the
     */
    private class OutfitGenLinearAdapter extends ArrayAdapter<Clothing> {

        private PreferenceList mPreferenceList;

        public OutfitGenLinearAdapter(Context context, String clothingType) {
            super(context, R.layout.closet_category_clothing_image, new ArrayList<Clothing>());

            //we can't get the category from objects because sometimes the list will be of size 0
            //Set the picture and the text for the fragment_closet Category
            switch (clothingType) {
                case Clothing.HAT:
                    mPreferenceList = new PreferenceList(false, Clothing.HAT, null, null, null, null, null, null);
                    break;
                case Clothing.ACCESSORY:
                    mPreferenceList = new PreferenceList(false, Clothing.ACCESSORY, null, null, null, null, null, null);
                    break;
                case Clothing.BODY:
                    mPreferenceList = new PreferenceList(false, Clothing.BODY, null, null, null, null, null, null);
                    break;
                case Clothing.BOTTOM:
                    mPreferenceList = new PreferenceList(false, Clothing.BOTTOM, null, null, null, null, null, null);
                    break;
                case Clothing.JACKET:
                    mPreferenceList = new PreferenceList(false, Clothing.JACKET, null, null, null, null, null, null);
                    break;
                case Clothing.SHOE:
                    mPreferenceList = new PreferenceList(false, Clothing.SHOE, null, null, null, null, null, null);
                    break;
                case Clothing.TOP:
                    mPreferenceList = new PreferenceList(false, Clothing.TOP, null, null, null, null, null, null);
                    break;
            }

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //Recycle views
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.closet_category_clothing_image, null);
            } else {
                //Remove the previous parent
                parent.removeView(convertView);
            }

            //Add picture to view
            ImageView clothingImageView = (ImageView) convertView.findViewById(R.id.clothing_image_view);
            clothingImageView.setImageBitmap(getItem(position).getBitmap());

            //Add action listener to change the piece of clothing
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ViewClothingByCatActivity.class);
                    intent.putExtra(PreferenceList.EXTRA_STRING, mPreferenceList);
                    intent.putExtra(ViewClothingByCatActivity.CAME_FROM_CLOSET_STRING, false);
                    startActivityForResult(intent, CHOOSE_CLOTHING_REQUEST);
                }
            });

            return convertView;
        }
    }


    /**
     * Adds the clothing to the correct view, sets the correct adapter, and notifies stuff
     * @param clothing - clothing to add
     */
    private void addClothingToOutfit(Clothing clothing){
        //Add the clothing object to the appropriate list
        String clothingType = clothing.getCategory();
        switch (clothingType) {
            case Clothing.HAT:
                mCurrentOutfit.addAccessory(clothing);
                mAccessoriesAdapter.add(clothing);
                break;
            case Clothing.ACCESSORY:
                mCurrentOutfit.addAccessory(clothing);
                mAccessoriesAdapter.add(clothing);
                break;
            case Clothing.BODY:
                mCurrentOutfit.addTop(clothing);
                mTopAdapter.add(clothing);
                break;
            case Clothing.BOTTOM:
                mCurrentOutfit.addBottom(clothing);
                mBottomAdapter.add(clothing);
                break;
            case Clothing.JACKET:
                mCurrentOutfit.addTop(clothing);
                mTopAdapter.add(clothing);
                break;
            case Clothing.SHOE:
                mCurrentOutfit.setShoes(clothing);
                mShoesButton.setImageBitmap(clothing.getBitmap());
                break;
            case Clothing.TOP:
                mCurrentOutfit.addTop(clothing);
                mTopAdapter.add(clothing);
                break;
            default:
                System.err.println("Invalid clothing category " + clothingType);
                break;
        }
        //finally, update all the layouts
        updateLayouts();
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        //Handle adding, removing clothing
//        if (requestCode == CHOOSE_CLOTHING_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//
//                int receivedHashCode = data.getIntExtra(Clothing.EXTRA_STRING, 0);
//                Clothing clothing = mAccount.getCloset().findClothingByHash(receivedHashCode);
//                addClothingToOutfit(clothing);
//
//                //make sure that we can save the outfit
//                mOutfitGeneratedAlready = true;
//                mAddedOutfitAlready = false;
//
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                //nothing is done
//                System.out.println("Nothing was done");
//            }
//        }
//    }


    /**
     * Clears the three linear layouts, plus the shoes button
     */
    private void clearLayouts() {
        mAccessoriesAdapter.clear();
        mAccessoriesAdapter.notifyDataSetChanged();
        mAccessoriesLayout.removeAllViews();

        mTopAdapter.clear();
        mTopAdapter.notifyDataSetChanged();
        mTopLayout.removeAllViews();

        mBottomAdapter.clear();
        mBottomAdapter.notifyDataSetChanged();
        mBottomLayout.removeAllViews();

        mShoesButton.setImageResource(R.drawable.sneaker);
    }


    /**
     * Helper method to update each layout
     */
    private void updateLayouts() {
        //for each category, update the adapter, then update the layout
        updateSpecificLayout(Clothing.ACCESSORY, mAccessoriesAdapter, mAccessoriesLayout, mAccessoriesParameters);
        updateSpecificLayout(Clothing.TOP, mTopAdapter, mTopLayout, mTopParameters);
        updateSpecificLayout(Clothing.BOTTOM, mBottomAdapter, mBottomLayout, mBottomParameters);
    }


    /**
     * Updates the specific layout given with the items in the adapter
     *
     * @param adapter - adapter to receive views from
     * @param layout  - layout to place items into
     */
    private void updateSpecificLayout(String type, final ArrayAdapter<Clothing> adapter, LinearLayout layout, LinearLayout.LayoutParams params) {
        //prepare layout
        adapter.notifyDataSetChanged();
        layout.removeAllViews();

        //Add in an image only if the adapter has more than 0 objects
        if (adapter.getCount() > 0) {
            for (int index = 0; index < adapter.getCount(); index++) {
                View viewToAdd = adapter.getView(index, null, layout);
                layout.addView(viewToAdd, params);

                //Set the click function for this view
                final int finalIndex = index;
                viewToAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setManualChoosingIntent(adapter.getItem(finalIndex).getCategory());
                    }
                });
            }
        } else {
            //Add in stock image based on what the category is
            ImageView view = new ImageView(mContext);
            view.setClickable(true);
            if (type.equals(Clothing.ACCESSORY)) {
                view.setImageResource(R.drawable.cap);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setManualChoosingIntent(Clothing.ACCESSORY);
                    }
                });
            } else if (type.equals(Clothing.TOP)) {
                view.setImageResource(R.drawable.nylon_jacket);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setManualChoosingIntent(Clothing.TOP);
                    }
                });
            } else if (type.equals(Clothing.BOTTOM)) {
                view.setImageResource(R.drawable.pants);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setManualChoosingIntent(Clothing.BOTTOM);
                    }
                });
            } else {
                System.err.println("ERROR: TYPE " + type + " IS INVALID TYPE IN OUTFIT GEN ACTIVITY");
            }

            //Add the view into the layout
            layout.addView(view);
        }
    }


    //creates dropdowns given a string and spinner object
    protected void initSpinner (View view, Spinner sp, int resource, String[]arr){
        sp = (Spinner) view.findViewById(resource);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, arr);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        sp.setAdapter(adapter);

    }


    /**
     * Sets the choosing intent with the clothing type given
     * @param clothingType - type of clothing to give
     */
    private void setManualChoosingIntent(String clothingType){
        PreferenceList preference = new PreferenceList(false, clothingType, null, null, null, null, null, null);

        //Create intent and pass to it
        Intent intent = new Intent(mContext, ViewClothingByCatActivity.class);
        intent.putExtra(ViewClothingByCatActivity.CAME_FROM_CLOSET_STRING, false);
        intent.putExtra(PreferenceList.EXTRA_STRING, preference);
        startActivityForResult(intent, CHOOSE_CLOTHING_REQUEST);
    }



}//end class OutfitGenFragment
