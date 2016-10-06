package com.arori4.lookbook.Closet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arori4.lookbook.Accounts.Account;
import com.arori4.lookbook.IClosetApplication;
import com.arori4.lookbook.R;

import java.util.List;

/**
 * Defines the activity when user decides to view clothing by category.
 */
public class LaundryFragment extends Fragment {

    private ViewGroup mLaundryParentLayout;
    private GridView mLaundyGridView;
    private int mLaundryGridViewIndex;

    private List<Clothing> mLaundryList;
    private Account mCurrentAccount = IClosetApplication.getAccount();
    private Closet mCurrentCloset = mCurrentAccount.getCloset();

    Activity mContext;
    View mCurrentView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mCurrentView = inflater.inflate(R.layout.fragment_laundry, container, false);
        mContext = getActivity();

        //Find all the views
        mLaundryParentLayout = (ViewGroup) mCurrentView.findViewById(R.id.laundry_parent_layout);
        mLaundyGridView = (GridView) mCurrentView.findViewById(R.id.laundry_grid_view);
        mLaundryGridViewIndex = mLaundryParentLayout.indexOfChild(mLaundyGridView);

        return mCurrentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateGridLayout();
    }

    private void updateGridLayout(){

        //Get the correct list
        mLaundryList = mCurrentCloset.filter(new ClosetQuery(true, null, null, null, null, null, null, null));
        GridImageAdapter laundryGridViewAdapter = new GridImageAdapter(mContext, mLaundryList);
        GridView laundryGridView = (GridView) mCurrentView.findViewById(R.id.laundry_grid_view);

        if (laundryGridView != null && !mLaundryList.isEmpty()) {//requires nullptr check
            laundryGridView.setAdapter(laundryGridViewAdapter);
            //catch any clicks
            laundryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, ViewClothingActivity.class);
                    intent.putExtra("Clothing", mLaundryList.get(position));
                    mContext.finish();
                    mContext.startActivity(intent);

                }
            });
        }
        else{ //null or is empty
            //replace the linear layout with the no_elements_layout
            mLaundryParentLayout.removeViewAt(mLaundryGridViewIndex);
            View noElementsLayout = mContext.getLayoutInflater().inflate(
                    R.layout.no_elements_layout, mLaundryParentLayout, false);
            mLaundryParentLayout.addView(noElementsLayout, mLaundryGridViewIndex);

            //change the text of the no_elements_layout
            TextView noElementsTextView = (TextView) mCurrentView.findViewById(R.id.no_elements_default_text);
            if (noElementsTextView != null) {
                noElementsTextView.setText(R.string.no_laundry_text);
            }
        }
    }

    /**
     * Does fragment_laundry when the view is pressed
     * @param view - do nothing with this
     */
    public void doLaundry(View view) {

        //do nothing if the fragment_laundry list is empty
        if (mLaundryList.isEmpty()){
            Toast newToast = Toast.makeText(mContext, "There is no fragment_laundry to clean.",
                    Toast.LENGTH_SHORT);
            newToast.show();
        }
        else{
            for (int index = 0; index < mLaundryList.size(); index++){
                mLaundryList.get(index).setWorn(false);
            }

            //notify and clear by running onStart()
            updateGridLayout();

            //signify to user that we have done fragment_laundry
            Toast newToast = Toast.makeText(mContext, "Marked all clothing as clean (not worn).",
                    Toast.LENGTH_SHORT);
            newToast.show();
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
