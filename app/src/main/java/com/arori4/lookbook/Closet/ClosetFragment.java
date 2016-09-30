package com.arori4.lookbook.Closet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arori4.lookbook.IClosetApplication;
import com.arori4.lookbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Cabreros on 05-May-16.
 * Defines the fragment that displays the fragment_closet.
 */
public class ClosetFragment extends Fragment {

    private ListView mClosetListView;
    private ViewGroup mClosetParentLayout;
    private int mClosetListViewIndex;
    private Closet mCurrentCloset = IClosetApplication.getAccount().getCloset();

    //Lists
    private List<List<Clothing>> listOfLists = new ArrayList<>();

    private List<Clothing> accessoryList = new ArrayList<>();
    private List<Clothing> topList = new ArrayList<>();
    private List<Clothing> bottomList = new ArrayList<>();
    private List<Clothing> shoeList = new ArrayList<>();
    private List<Clothing> bodyList = new ArrayList<>();
    private List<Clothing> hatList = new ArrayList<>();
    private List<Clothing> jacketList = new ArrayList<>();

    Activity mContext = getActivity();
    View mCurrentView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mCurrentView =  inflater.inflate(R.layout.fragment_closet, container, false);

        //Find all the views
        mClosetParentLayout = (ViewGroup) mCurrentView.findViewById(R.id.closet_vertical_linear_layout);
        mClosetListView = (ListView) mCurrentView.findViewById(R.id.closet_list_view);
        mClosetListViewIndex = mClosetParentLayout.indexOfChild(mClosetListView);

        //Create Floating Action Bar Viewer and intent
        FloatingActionButton myFab = (FloatingActionButton) mCurrentView.findViewById(R.id.closet_fab);
        if (myFab != null) {
            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Create a new dialog
                    final Dialog add_clothing_dialog = new Dialog(mContext);
                    add_clothing_dialog.setContentView(R.layout.add_clothing_type);
                    add_clothing_dialog.setTitle("Select Clothing Type");

                    //Add in action listeners for all the buttons
                    ImageButton addAccessoryButton = (ImageButton) add_clothing_dialog.findViewById(R.id.add_accessory_button);
                    if (addAccessoryButton != null) {
                        addAccessoryButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CameraActivity.class);
                                intent.putExtra(Clothing.EXTRA_TYPE_STRING, Clothing.ACCESSORY);
                                startActivity(intent);
                                add_clothing_dialog.dismiss();
                                showCameraToast();
                            }
                        });
                    }
                    ImageButton addShirtButton = (ImageButton) add_clothing_dialog.findViewById(R.id.add_shirt_button);
                    if (addShirtButton != null) {
                        addShirtButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CameraActivity.class);
                                intent.putExtra(Clothing.EXTRA_TYPE_STRING, Clothing.TOP);
                                startActivity(intent);
                                add_clothing_dialog.dismiss();
                                showCameraToast();
                            }
                        });
                    }
                    ImageButton addPantsButton = (ImageButton) add_clothing_dialog.findViewById(R.id.add_pants_button);
                    if (addPantsButton != null) {
                        addPantsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CameraActivity.class);
                                intent.putExtra(Clothing.EXTRA_TYPE_STRING, Clothing.BOTTOM);
                                startActivity(intent);
                                add_clothing_dialog.dismiss();
                                showCameraToast();
                            }
                        });
                    }
                    ImageButton addShoeButton = (ImageButton) add_clothing_dialog.findViewById(R.id.add_shoe_button);
                    if (addShoeButton != null) {
                        addShoeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CameraActivity.class);
                                intent.putExtra(Clothing.EXTRA_TYPE_STRING, Clothing.SHOE);
                                startActivity(intent);
                                add_clothing_dialog.dismiss();
                                showCameraToast();
                            }
                        });
                    }
                    ImageButton addHatButton = (ImageButton) add_clothing_dialog.findViewById(R.id.add_hat_button);
                    if (addHatButton != null) {
                        addHatButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CameraActivity.class);
                                intent.putExtra(Clothing.EXTRA_TYPE_STRING, Clothing.HAT);
                                startActivity(intent);
                                add_clothing_dialog.dismiss();
                                showCameraToast();
                            }
                        });
                    }
                    ImageButton addDressButton = (ImageButton) add_clothing_dialog.findViewById(R.id.add_dress_button);
                    if (addDressButton != null) {
                        addDressButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CameraActivity.class);
                                intent.putExtra(Clothing.EXTRA_TYPE_STRING, Clothing.BODY);
                                startActivity(intent);
                                add_clothing_dialog.dismiss();
                                showCameraToast();
                            }
                        });
                    }
                    ImageButton addJacketButton = (ImageButton) add_clothing_dialog.findViewById(R.id.add_jacket_button);
                    if (addJacketButton != null) {
                        addJacketButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, CameraActivity.class);
                                intent.putExtra(Clothing.EXTRA_TYPE_STRING, Clothing.JACKET);
                                startActivity(intent);
                                add_clothing_dialog.dismiss();
                                showCameraToast();
                            }
                        });
                    }

                    add_clothing_dialog.show();
                }


            });
        }

        return mCurrentView;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = getActivity();
        updateClosetView();
    }

    private void updateClosetView(){
        //update fragment_closet
        mCurrentCloset = IClosetApplication.getAccount().getCloset();
        System.out.println("CLOSET SIZE: " + mCurrentCloset.getList().size());

        //Clear the lists and refill them
        accessoryList.clear();
        accessoryList = mCurrentCloset.filter(new PreferenceList(false, Clothing.ACCESSORY, null, null, null, null, null, null));
        topList.clear();
        topList = mCurrentCloset.filter(new PreferenceList(false, Clothing.TOP, null, null, null, null, null, null));
        bottomList.clear();
        bottomList = mCurrentCloset.filter(new PreferenceList(false, Clothing.BOTTOM, null, null, null, null, null, null));
        shoeList.clear();
        shoeList = mCurrentCloset.filter(new PreferenceList(false, Clothing.SHOE, null, null, null, null, null, null));
        bodyList.clear();
        bodyList = mCurrentCloset.filter(new PreferenceList(false, Clothing.BODY, null, null, null, null, null, null));
        hatList.clear();
        hatList = mCurrentCloset.filter(new PreferenceList(false, Clothing.HAT, null, null, null, null, null, null));
        jacketList.clear();
        jacketList = mCurrentCloset.filter(new PreferenceList(false, Clothing.JACKET, null, null, null, null, null, null));

        //Add all of these lists into a single list of lists, if it is a size large enough
        listOfLists.clear();
        if (!accessoryList.isEmpty()) {
            listOfLists.add(accessoryList);
        }
        if (!topList.isEmpty()) {
            listOfLists.add(topList);
        }
        if (!bottomList.isEmpty()) {
            listOfLists.add(bottomList);
        }
        if (!shoeList.isEmpty()) {
            listOfLists.add(shoeList);
        }
        if (!bodyList.isEmpty()) {
            listOfLists.add(bodyList);
        }
        if (!hatList.isEmpty()) {
            listOfLists.add(hatList);
        }
        if (!jacketList.isEmpty()) {
            listOfLists.add(jacketList);
        }

        //check if we have any clothing
        if (mCurrentCloset.getList().isEmpty() || listOfLists.isEmpty()) {
            //replace the linear layout with the no_elements_layout
            mClosetParentLayout.removeViewAt(mClosetListViewIndex);
            View noElementsLayout = mContext.getLayoutInflater().inflate(
                    R.layout.no_elements_layout, mClosetParentLayout, false);
            mClosetParentLayout.addView(noElementsLayout, mClosetListViewIndex);

            //change the text of the no_elements_layout
            TextView noElementsTextView = (TextView) mCurrentView.findViewById(R.id.no_elements_default_text);
            if (noElementsTextView != null) {
                noElementsTextView.setText(R.string.closet_no_elements_text);
            }
        }
        else { //refresh the amount of clothing we have
            //add stuff to the fragment_closet list view
            ClosetCategoryAdapter adapter = new ClosetCategoryAdapter(mContext, listOfLists);
            if (mClosetListView != null) {
                mClosetListView.setAdapter(adapter);
            }

            //add the linear layout back in
            mClosetParentLayout.removeViewAt(mClosetListViewIndex);
            mClosetParentLayout.addView(mClosetListView, mClosetListViewIndex);
        }
    }

    /**
     * ClosetCategoryAdapter that represents the large list views
     * Only does this for a specific view
     */
    private class ClosetCategoryAdapter extends ArrayAdapter<List<Clothing>> {

        /**
         * Constructor
         *
         * @param context       - mContext of this activity
         * @param clothingLists - list of clothing lists
         */
        public ClosetCategoryAdapter(Context context, List<List<Clothing>> clothingLists) {
            super(context, R.layout.closet_category, clothingLists);
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            //Get the view to inflate
            View categoryView = inflater.inflate(R.layout.closet_category, parent, false);

            //Get the linear layout
            final LinearLayout linearLayout = (LinearLayout) categoryView.findViewById(R.id.closet_category_clothing_slider_layout);
            //add stuff to the linearLayout
            final List<Clothing> currentList = getItem(position);

            //Get each fragment_closet category
            LinearLayout categoryLeftBox = (LinearLayout) categoryView.findViewById(R.id.closet_category_left_layout);
            ImageView categoryViewImage = (ImageView) categoryView.findViewById(R.id.closet_category_picture);
            TextView categoryTextView = (TextView) categoryView.findViewById(R.id.closet_category_text);
            PreferenceList categoryPreferenceList = null;

            //Set the picture and the text for the fragment_closet Category
            if (!currentList.isEmpty() && currentList.get(0).getCategory().equals(Clothing.HAT)) {
                categoryViewImage.setImageResource(R.drawable.cap);
                categoryTextView.setText("Hat");
                categoryPreferenceList = new PreferenceList(false, Clothing.HAT, null, null, null, null, null, null);
            } else if (!currentList.isEmpty() && currentList.get(0).getCategory().equals(Clothing.ACCESSORY)) {
                //set image to accessory
                categoryViewImage.setImageResource(R.drawable.accessory);
                categoryTextView.setText("Accessory");
                categoryPreferenceList = new PreferenceList(false, Clothing.ACCESSORY, null, null, null, null, null, null);
            } else if (!currentList.isEmpty() && currentList.get(0).getCategory().equals(Clothing.BODY)) {
                //set image to body
                categoryViewImage.setImageResource(R.drawable.dress);
                categoryTextView.setText("Body");
                categoryPreferenceList = new PreferenceList(false, Clothing.BODY, null, null, null, null, null, null);
            } else if (!currentList.isEmpty() && currentList.get(0).getCategory().equals(Clothing.BOTTOM)) {
                //set image to bottom
                categoryViewImage.setImageResource(R.drawable.bag_pants);
                categoryTextView.setText("Bottom");
                categoryPreferenceList = new PreferenceList(false, Clothing.BOTTOM, null, null, null, null, null, null);
            } else if (!currentList.isEmpty() && currentList.get(0).getCategory().equals(Clothing.JACKET)) {
                //set image to jacket
                categoryViewImage.setImageResource(R.drawable.nylon_jacket);
                categoryTextView.setText("Jacket");
                categoryPreferenceList = new PreferenceList(false, Clothing.JACKET, null, null, null, null, null, null);
            } else if (!currentList.isEmpty() && currentList.get(0).getCategory().equals(Clothing.SHOE)) {
                //set image to shoes
                categoryViewImage.setImageResource(R.drawable.sneaker);
                categoryTextView.setText("Shoes");
                categoryPreferenceList = new PreferenceList(false, Clothing.SHOE, null, null, null, null, null, null);
            } else if (!currentList.isEmpty() && currentList.get(0).getCategory().equals(Clothing.TOP)) {
                //set image to top
                categoryViewImage.setImageResource(R.drawable.top);
                categoryTextView.setText("Top");
                categoryPreferenceList = new PreferenceList(false, Clothing.TOP, null, null, null, null, null, null);
            }
            final PreferenceList clickPreference = categoryPreferenceList;

            //Set the pictures to each have an on click listener
            categoryLeftBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ViewClothingByCatActivity.class);
                    intent.putExtra(PreferenceList.EXTRA_STRING, clickPreference);
                    intent.putExtra(ViewClothingByCatActivity.CAME_FROM_CLOSET_STRING, true);
                    startActivity(intent);
                }
            });

            for (int index = 0; index < currentList.size(); index++) {
                //Get the correct clothing
                final Clothing currentClothing = currentList.get(index);

                //get clothing bitmap and the appropriate view
                Bitmap currentBitmap = currentClothing.getBitmap();

                //get the view and add a click listener to go to the correct view
                ClickableFrameLayout clothingFrame = (ClickableFrameLayout) inflater.inflate
                        (R.layout.closet_category_clothing_image, linearLayout, false);
                clothingFrame.setClickable(true);
                clothingFrame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ViewClothingActivity.class);
                        intent.putExtra("clothing_id", currentClothing.getId());
                        mContext.startActivity(intent);
                    }
                });

                //set the image. set to grey if it is worn
                ImageView imageView = (ImageView) clothingFrame.findViewById(R.id.clothing_image_view);
                imageView.setImageBitmap(currentBitmap);
                if (currentClothing.isWorn()){
                    //Create a color matrix to set the bitmap to grey
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    imageView.setColorFilter(filter);
                }
                //add this to the linearLayout
                linearLayout.addView(clothingFrame);
            }

            return categoryView;
        }

    }


    private void showCameraToast(){
        Toast newToast = Toast.makeText(mContext, "Follow the guidelines and take a picture.", Toast.LENGTH_SHORT);
        newToast.show();
    }

}//end class ClosetFragment
