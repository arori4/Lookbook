package com.arori4.lookbook.Closet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arori4.lookbook.Accounts.Account;
import com.arori4.lookbook.BaseActivity;
import com.arori4.lookbook.IClosetApplication;
import com.arori4.lookbook.R;

import java.util.List;

/**
 * Defines the activity when user decides to view clothing by category.
 */
public class ViewClothingByCatActivity extends BaseActivity {

    public static final String CAME_FROM_CLOSET_STRING = "yes it did come from the closet";

    private ViewGroup mCategoryParentLayout;
    private GridView mCategoryGridView;
    private int mCategoryGridViewIndex;

    private Account mCurrentAccount = IClosetApplication.getAccount();
    private Closet mCurrentCloset = mCurrentAccount.getCloset();
    private List<Clothing> mDisplayList;

    private boolean mCameFromCloset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_clothing_by_cat);
        setToolbar((Toolbar) findViewById(R.id.toolbar));

        //Find all the views
        mCategoryParentLayout = (ViewGroup) findViewById(R.id.category_parent_layout);
        mCategoryGridView = (GridView) findViewById(R.id.category_grid_view);
        mCategoryGridViewIndex = mCategoryParentLayout.indexOfChild(mCategoryGridView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Create essential references. this is needed for onItemClick
        final Activity currentActivity = this;
        Intent previousIntent = getIntent();

        //Get the activity it came from
        mCameFromCloset = previousIntent.getBooleanExtra(CAME_FROM_CLOSET_STRING, true);

        //Get the extra preference
        PreferenceList getPreference;
        //Check if it actually exists
        if (previousIntent.hasExtra(PreferenceList.EXTRA_STRING)) {
            getPreference = (PreferenceList) previousIntent.
                    getSerializableExtra(PreferenceList.EXTRA_STRING);
        } else {
            getPreference = new PreferenceList(false, null, null, null, null, null, null, null);
        }
        ;

        //Get the correct list
        mDisplayList = mCurrentCloset.filter(getPreference);

        //Do something depending on whether the list is empty or not
        if (mDisplayList != null && !mDisplayList.isEmpty()) {
            //create new adapter
            GridImageAdapter categoryGridViewAdapter = new GridImageAdapter(this, mDisplayList);
            mCategoryGridView.setAdapter(categoryGridViewAdapter);

            //catch any clicks
            mCategoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Two different actions based on the activity it came from
                    if (mCameFromCloset) {
                        Intent intent = new Intent(currentActivity, ViewClothingActivity.class);
                        intent.putExtra("Clothing", mDisplayList.get(position));
                        currentActivity.finish();
                        currentActivity.startActivity(intent);
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(Clothing.EXTRA_STRING, mDisplayList.get(position).hashCode());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
            });
        }
        else{ //null or is empty
            //replace the linear layout with the no_elements_layout
            mCategoryParentLayout.removeViewAt(mCategoryGridViewIndex);
            View noElementsLayout = getLayoutInflater().inflate(
                    R.layout.no_elements_layout, mCategoryParentLayout, false);
            mCategoryParentLayout.addView(noElementsLayout, mCategoryGridViewIndex);

            //change the text of the no_elements_layout
            TextView noElementsTextView = (TextView) findViewById(R.id.no_elements_default_text);
            if (noElementsTextView != null) {
                noElementsTextView.setText(R.string.view_clothing_by_category_no_matches);
            }
        }
    }


    /**
     * Image Adapter class for clothing
     */
    private class GridImageAdapter extends ArrayAdapter<Clothing> {

        public GridImageAdapter(Context context, List<Clothing> clothingList) {
            super(context, R.layout.closet_preference_clothing_image, clothingList);
        }

        @Override
        /**
         * convert view is a reference to other reusable views
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            //Recycling views
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                //Get the correct view to inflate
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.closet_preference_clothing_image, parent, false);
            }

            //Get the desired bitmap
            Clothing currentClothing = getItem(position);
            Bitmap currentBitmap = currentClothing.getBitmap();

            //Set the image view to have the bitmap
            ImageView imageView = (ImageView) view.findViewById(R.id.clothing_image_view);
            imageView.setImageBitmap(currentBitmap);

            return view;
        }


    }

}//end class viewByCategoryActivity
