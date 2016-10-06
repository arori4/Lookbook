package com.arori4.lookbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by game on 04-Oct-16.
 */

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private BaseActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mActivity = (BaseActivity) getActivity();
        if (mActivity == null){
            Log.e(TAG, "Could not find correct base activity");
        }


        //Button behaviors
        LinearLayout closetButton = (LinearLayout) view.findViewById(R.id.home_closet_button);
        if (closetButton != null){
            closetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.switchTab(1);
                }
            });
        }
        LinearLayout stylistButton = (LinearLayout) view.findViewById(R.id.home_stylist_button);
        if (stylistButton != null){
            stylistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.switchTab(2);
                }
            });
        }
        LinearLayout lookbookButton = (LinearLayout) view.findViewById(R.id.home_lookbook_button);
        if (lookbookButton != null){
            lookbookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.switchTab(3);
                }
            });
        }
        LinearLayout laundryButton = (LinearLayout) view.findViewById(R.id.home_laundry_button);
        if (laundryButton != null){
            laundryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.switchTab(4);
                }
            });
        }

        return view;
    }
}
