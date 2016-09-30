package com.arori4.lookbook.Lookbook;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Cabreros on 26-May-16.
 * This is a custom layout I created for our uses, like LinearLayout or FrameLayout.
 * This will be used for the outfit to correctly display shirts and pants.
 *
 * If you are interested in how I developed this, I recommend you read the articles at
 * http://javatechig.com/android/how-to-create-custom-layout-in-android-by-extending-viewgroup-class
 * https://arpitonline.com/2012/07/01/creating-custom-layouts-for-android/
 *
 * For LinearLayout and adding in an adapter, look at this StackOverflow question
 * http://stackoverflow.com/questions/14550309/creating-an-adapter-to-a-customview
 */

public class ClothingStackLayout extends ViewGroup {

    private static final float SIZE_MULTIPLIER = 0.85f;
    private static final float SHIFT_MULTIPLIER = 0.3f;
    private static final float SHIFT_RECENTER_MULTIPLIER = SHIFT_MULTIPLIER / 2.0f;
    private static final int CHILD_LEFT_COORDINATE = 0;
    private static final int CHILD_TOP_COORDINATE = 1;
    private static final int CHILD_RIGHT_COORDINATE = 2;
    private static final int CHILD_BOTTOM_COORDINATE = 3;

    private Adapter mAdapter;
    private SparseArray<List<View>> mTypedViewsCache = new SparseArray<>();
    private final DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            refreshViewsFromAdapter();
        }

        @Override
        public void onInvalidated() {
            removeAllViews();
        }
    };

    public ClothingStackLayout(Context context) {
        super(context);
    }

    public ClothingStackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClothingStackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets the adapter
     * @param adapter - adapter to set
     */
    public void setAdapter(Adapter adapter){
        //if we have a current adapter, remove the current observer
        if (mAdapter != null){
            mAdapter.unregisterDataSetObserver(mObserver);
        }

        //add new adapter
        mAdapter = adapter;
        //set a data set observer if the new adapter is not null
        if (mAdapter != null){
            mAdapter.registerDataSetObserver(mObserver);
        }

        //Initialize full views
        initViewsFromAdapter();
    }

    /**
     * Initialize all the views from the adapter
     */
    protected void initViewsFromAdapter(){
        //Clear the caches
        mTypedViewsCache.clear();
        removeAllViews();

        //nullptr check
        if (mAdapter != null){
            for (int index = 0; index < mAdapter.getCount(); index++){
                View view = mAdapter.getView(index, null, this);
                //We have a map for all the types we can add
                addViewToMap(mAdapter.getItemViewType(index), view, mTypedViewsCache);
                addView(view, index);
            }
        }
    }


    /**
     * When adapter changes, refresh the views
     */
    protected void refreshViewsFromAdapter(){
        //Cache the old map and create a new map
        SparseArray<List<View>> typedViewsCacheCopy = mTypedViewsCache;
        mTypedViewsCache = new SparseArray<>();
        removeAllViews();

        //Loop through all the views
        for (int index = 0; index < mAdapter.getCount(); index++){
            int type = mAdapter.getItemViewType(index);

            //The recycling happens here. ShiftCachedViewsOfType will recycle the views
            View convertView = shiftCachedViewOfType(type, typedViewsCacheCopy);
            convertView = mAdapter.getView(index, convertView, this);

            //Add the view to the map
            addViewToMap(type, convertView, mTypedViewsCache);

            //Finally, add the view back to the adapter
            addView(convertView, index);
        }
    }

    /**
     * Adds any views to the map
     * @param type - type of view to add
     * @param view - view to add
     * @param typedViewsCache - cache of views to add to
     */
    private static void addViewToMap(int type, View view, SparseArray<List<View>> typedViewsCache){
        List<View> singleTypedViews = typedViewsCache.get(type);
        //If the type of view is null, it is new and we have to create the list
        if (singleTypedViews == null){
            singleTypedViews = new ArrayList<>();
            typedViewsCache.put(type, singleTypedViews);
        }

        //Finally, add the view to the appropriate list
        singleTypedViews.add(view);
    }

    /**
     * Shifts the cached view of type.
     * This is where the recycling happens
     * @param type - type of view to move
     * @param typedViewsCache - views cache to edit
     * @return - the view that was removed.
     */
    private static View shiftCachedViewOfType(int type, SparseArray<List<View>> typedViewsCache) {
        List<View> singleTypeViews = typedViewsCache.get(type);
        if(singleTypeViews != null) {
            if(singleTypeViews.size() > 0) {
                return singleTypeViews.remove(0);
            }
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        if (getChildCount() > 0){
            childWidth = (int) (getWidth() * SIZE_MULTIPLIER);
            childHeight = (int) (getHeight() * SIZE_MULTIPLIER);
            System.out.println("HOOOO " + childWidth + " " + childHeight);
        }

        //Find the center coordinates
        final int centerX = (int) (this.getX() + this.getWidth() / 2);
        final int centerY = (int) (this.getY() + this.getHeight() / 2);

        //Place the child at the correct center spot
        final int childLeft = centerX - childWidth / 2;
        final int childTop = centerY - childHeight / 2;
        final int childRight = childLeft + childWidth;
        final int childBottom = childTop + childHeight;

        int[][] coordinates = new int[count][4];

        //Set the coordinates for each child
        for (int outer = 0; outer < count; outer++) {
            coordinates[outer][CHILD_LEFT_COORDINATE] = childLeft;
            coordinates[outer][CHILD_TOP_COORDINATE] = childTop;
            coordinates[outer][CHILD_RIGHT_COORDINATE] = childRight;
            coordinates[outer][CHILD_BOTTOM_COORDINATE] = childBottom;

            //move the other children back
            for (int inner = 0; inner < outer; inner++) {
                coordinates[outer][CHILD_LEFT_COORDINATE] -= childWidth * SHIFT_MULTIPLIER;
                coordinates[outer][CHILD_TOP_COORDINATE] -= childHeight * SHIFT_MULTIPLIER;
                coordinates[outer][CHILD_RIGHT_COORDINATE] -= childWidth * SHIFT_MULTIPLIER;
                coordinates[outer][CHILD_BOTTOM_COORDINATE] -= childHeight * SHIFT_MULTIPLIER;
            }
        }

        //Set the calculated coordinates for all of the children
        for (int index = 0; index < count; index++) {
            //Center the coordinates
            coordinates[index][CHILD_LEFT_COORDINATE] += childWidth * SHIFT_RECENTER_MULTIPLIER;
            coordinates[index][CHILD_TOP_COORDINATE] += childHeight * SHIFT_RECENTER_MULTIPLIER;
            coordinates[index][CHILD_RIGHT_COORDINATE] += childWidth * SHIFT_RECENTER_MULTIPLIER;
            coordinates[index][CHILD_BOTTOM_COORDINATE] += childHeight * SHIFT_RECENTER_MULTIPLIER;

            View child = getChildAt(index);

            if (child.getVisibility() == GONE) {
                return;
            }

            //Set the child coordinate layouts
            child.layout(coordinates[index][CHILD_LEFT_COORDINATE],
                    coordinates[index][CHILD_TOP_COORDINATE],
                    coordinates[index][CHILD_RIGHT_COORDINATE],
                    coordinates[index][CHILD_BOTTOM_COORDINATE]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //we need to measure the view first
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //For each of the children, assign them a size
        for (int index = 0; index < getChildCount(); index++){
            int childWidth = MeasureSpec.makeMeasureSpec((int) (getWidth() * SIZE_MULTIPLIER), MeasureSpec.EXACTLY);
            int childHeight = MeasureSpec.makeMeasureSpec((int) (getHeight() * SIZE_MULTIPLIER), MeasureSpec.EXACTLY);
            measureChild(getChildAt(index), childWidth, childHeight);


            System.out.println("eoighu " + childWidth + " " + childHeight);
        }
    }

}//end class ClothingStackLayout