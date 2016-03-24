package com.example.jimafisk.textcuse;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
        getWindow().setLayout((int) (width*.8), (int) (height*.65));

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
    }

    public void onClickDeleteContact (View view) {

        EditText Name = (EditText) findViewById(R.id.contactName);
        EditText Phone = (EditText) findViewById(R.id.contactPhone);

        boolean isInserted = textHerDb.insertContact(
                Name.getText().toString(),
                Phone.getText().toString()
        );

        if (isInserted == true) {
            Toast.makeText(ContactModalDelete.this, "Contact has been saved!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ContactModalDelete.this, MainActivity.class));
        } else {
            Toast.makeText(ContactModalDelete.this, "TextHer could not save " + Name.getText().toString() + " at this time.", Toast.LENGTH_LONG).show();
        }
    }
}
