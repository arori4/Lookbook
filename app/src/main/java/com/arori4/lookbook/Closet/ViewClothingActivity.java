package com.arori4.lookbook.Closet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.arori4.lookbook.BaseActivity;
import com.arori4.lookbook.IClosetApplication;
import com.arori4.lookbook.R;

public class ViewClothingActivity extends BaseActivity {
    Context context = this;
    Button delete_button;
    Button update_button;

    private Closet currCloset = IClosetApplication.getAccount().getCloset();
    private Clothing currentClothing;

    SharedPreferences mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_clothing);
        setToolbar((Toolbar) findViewById(R.id.toolbar));

        // get preferences
        mPrefs = getSharedPreferences("com.arori4.fragment_lookbook", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        //get intent extra
        String id = (String) getIntent().getSerializableExtra("clothing_id");
        currentClothing = currCloset.findClothingByID(id);

        //get the picture from the view, and set the picture
        ImageView clothingView = (ImageView) findViewById(R.id.view_clothing_picture);
        if (clothingView != null) {
            clothingView.setImageBitmap(currentClothing.getBitmap());
        }

        // get the appropriate text views
        TextView categoryTextView = (TextView) findViewById(R.id.closet_item_category_entry);
        TextView colorTextView = (TextView) findViewById(R.id.closet_item_color_entry);
        TextView weatherTextView = (TextView) findViewById(R.id.closet_item_weather_entry);
        TextView occasionTextView = (TextView) findViewById(R.id.closet_item_occasion_entry);
        TextView notesTextView = (TextView) findViewById(R.id.closet_item_notes_entry);

        // set the appropriate text views
        categoryTextView.setText(currentClothing.getCategory());
        colorTextView.setText(currentClothing.getColor());
        weatherTextView.setText(currentClothing.getWeather());
        occasionTextView.setText(currentClothing.getOccasion());
        notesTextView.setText(currentClothing.getNotes());

        // find checkboxes
        CheckBox wornCB = (CheckBox) findViewById(R.id.cb_worn);
        CheckBox sharedCB = (CheckBox) findViewById(R.id.cb_shared);
        CheckBox lostCB = (CheckBox) findViewById(R.id.cb_lost);

        // set checkboxes
        if (currentClothing.isWorn()) {
            wornCB.setChecked(true);
        }
        if (currentClothing.isShared()) {
            sharedCB.setChecked(true);
        }
        if (currentClothing.isLost()) {
            lostCB.setChecked(true);
        }

        // respond to user checking boxes
        if (wornCB.isChecked()) {
            currentClothing.setWorn(true);
        }
        if (sharedCB.isChecked()) {
            currentClothing.setShared(true);
        }
        if (lostCB.isChecked()) {
            currentClothing.setLost(true);
        }

        // update button
        Button update_button = (Button) findViewById(R.id.update);
        if (update_button != null) {
            update_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateClothingActivity.class);
                    intent.putExtra("clothing_id", currentClothing.getId());
                    startActivity(intent);
                }
            });
        }

        // delete button
        Button delete_button = (Button) findViewById(R.id.delete);
        if (delete_button != null) {
            delete_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteClothing();
                }
            });
        }//end of button


    } //end of initView method


    public void deleteClothing() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewClothingActivity.this);
        builder.setTitle("Delete Clothing?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currCloset.removeClothing(currentClothing);

                Intent intent = new Intent(context, ClosetFragment.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
