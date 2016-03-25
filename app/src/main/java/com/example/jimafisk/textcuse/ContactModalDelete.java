package com.example.jimafisk.textcuse;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimafisk on 3/21/16.
 */
public class ContactModalDelete extends Activity {

    DatabaseHelper textHerDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_modal_delete);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.9), (int) (height*.65));

        textHerDb = new DatabaseHelper(this);

        Cursor result = textHerDb.getContacts();
        if(result.getCount() == 0) {

        }
        // Create empty list
        List<String> allContacts = new ArrayList<String>();
        List<Integer> allContactIDs = new ArrayList<Integer>();
        // Add Contacts from db to list
        for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            // The Cursor is now set to the right position
            allContacts.add(result.getString(0));
            allContactIDs.add(result.getInt(1));
        }
        LinearLayout ll = (LinearLayout) findViewById(R.id.checkboxGroup);
        ll.setOrientation(LinearLayout.VERTICAL);
        if (allContacts.size() < 1) {
            Toast.makeText(ContactModalDelete.this, "There are no contacts to delete", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < allContacts.size(); i++) {
                //categories.add(allContacts.get(i));
                CheckBox cb = new CheckBox(getApplicationContext());
                cb.setText(allContacts.get(i));
                cb.setId(allContactIDs.get(i));
                ll.addView(cb);
            }
        }

        // Add Font Awesome
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);

    }

    public void onClickDeleteContact (View view) {

        LinearLayout container = (LinearLayout)findViewById(R.id.checkboxGroup);
        List<Integer> contactDeleteIDs = new ArrayList<Integer>();;
        for (int i = 0; i < container.getChildCount(); i++){
            // Assign each child to a general View variable
            View v = container.getChildAt(i);
            // Check if child is actually a CheckBox
            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    contactDeleteIDs.add(cb.getId());
                }
            }
        }


        boolean isInserted = textHerDb.removeContacts(contactDeleteIDs);

        if (isInserted == true) {
            Toast.makeText(ContactModalDelete.this, "Contacts have been deleted!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ContactModalDelete.this, MainActivity.class));
        } else {
            Toast.makeText(ContactModalDelete.this, "No contacts to delete", Toast.LENGTH_LONG).show();
        }

    }

    public void buttonCloseModal (View view) {
        startActivity(new Intent(ContactModalDelete.this, MainActivity.class));
    }
}
