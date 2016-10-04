package com.arori4.lookbook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arori4.lookbook.Accounts.SettingsActivity;
import com.arori4.lookbook.Closet.ClosetFragment;
import com.arori4.lookbook.Closet.LaundryFragment;
import com.arori4.lookbook.Lookbook.LookbookFragment;
import com.arori4.lookbook.Lookbook.OutfitGenFragment;

/**
 * Home activity
 */
public class BaseActivity extends AppCompatActivity {

    //Variables
    private CharSequence mTitles[] = {"Home", "Closet", "Outfit\nCreator", "Lookbook", "Laundry"};
    private boolean mBackButtonPressed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Setup the pager and the adapter
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), mTitles);
        ViewPager toolbarPager = (ViewPager) findViewById(R.id.home_pager);
        if (toolbarPager != null) {
            toolbarPager.setAdapter(homePagerAdapter);
        }

        //Setup tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(toolbarPager);
        }

        //Setup toolbar
        Toolbar homeToolbar = (Toolbar) findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(homeToolbar);

    }



    @Override
    protected void onResume() {
        super.onResume();
        mBackButtonPressed = false;
    }

    /**
     * Class that helps fill the lower toolbar dynamically.
     */
    public class HomePagerAdapter extends FragmentStatePagerAdapter {

        private CharSequence mTitles[]; //stores the titles
        private int mNumTabs; // stores the number of tabs

        /**
         * Constructor
         *
         * @param fm     - fragment manager from parent activity
         * @param titles - a sequence of titles.
         */
        public HomePagerAdapter(FragmentManager fm, CharSequence[] titles) {

            super(fm);

            mTitles = titles;
            mNumTabs = titles.length;

            if (titles.length <= 2) {
                throw new IllegalArgumentException("Titles does not have enough titles");
            }
        }


        /**
         * Gets the item at the current position
         *
         * @param position - position to get the fragment at
         * @return - fragment that corresponds to each position
         */
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new ClosetFragment();
                case 2:
                    return new OutfitGenFragment();
                case 3:
                    return new LookbookFragment();
                case 4:
                    return new LaundryFragment();
                default:
                    return new Fragment();
            }
        }


        /**
         * Gets the page title at the position
         *
         * @param position - position to get the title at
         * @return - title at specific position
         */
        @Override
        public CharSequence getPageTitle(int position) {

            if (position >= mNumTabs) {
                throw new IndexOutOfBoundsException("HomePagerAdapter method getPageTitle has " +
                        "position out of bounds.");
            }
            return mTitles[position];
        }


        /**
         * Returns the amount of tabs that exist
         *
         * @return - number of tabs that exist
         */
        @Override
        public int getCount() {
            return mNumTabs;
        }
    }//end inner class


    @Override
    /**
     * Defines closing the app through the fragment_home activity
     */
    public void onBackPressed() {
        //leave app if backButton was pressed twice
        if (!mBackButtonPressed) {
            mBackButtonPressed = true;
            Toast newToast = Toast.makeText(this, "Press the back button again to leave.",
                    Toast.LENGTH_SHORT);
            newToast.show();
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_about:
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_about);
                dialog.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}

