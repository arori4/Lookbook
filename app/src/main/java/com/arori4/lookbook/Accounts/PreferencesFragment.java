package com.arori4.lookbook.Accounts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.arori4.lookbook.HomeActivity;
import com.arori4.lookbook.R;


/**
 * Created by Christina on 5/16/2016.
 */
public class PreferencesFragment extends PreferenceFragment
{
    protected SharedPreferences.OnSharedPreferenceChangeListener mListener;
    private String[] themesList = {"App Theme", "Eco", "Dynamic", "Monochrome" };

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("theme"))
                {
                    System.out.println("theme changed");
                    getActivity().recreate();

                    // recreate the task stack
                    android.support.v4.app.TaskStackBuilder.create(getActivity())
                            //ComponentName prev = this.getCallingActivity();
                            .addNextIntent(new Intent(getActivity(), HomeActivity.class))
                            .addNextIntent(getActivity().getIntent())
                            .startActivities();

                    return;
                }

                getActivity().finish();
                final Intent intent = getActivity().getIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | android.support.v4.content.IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
        super.onPause();
    }

}
