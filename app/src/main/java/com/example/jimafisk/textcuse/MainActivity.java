package com.example.jimafisk.textcuse;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper textHerDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textHerDb = new DatabaseHelper(this);

        Cursor result = textHerDb.getContacts();
        if(result.getCount() == 0) {

        }
        // Create empty list
        List<String> allContacts = new ArrayList<String>();
        // Add Contacts from db to list
        for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            // The Cursor is now set to the right position
            allContacts.add(result.getString(0));
        }
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.contactSaved);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        if (allContacts.size() < 1) {
            TextView contactRequired = (TextView) findViewById(R.id.contactRequired);
            contactRequired.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < allContacts.size(); i++) {
                categories.add(allContacts.get(i));
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // Add Font Awesome
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);
        //Button buttonAddContact = (Button) findViewById(R.id.buttonAddContact);
        //Button buttonDeleteContact = (Button) findViewById(R.id.buttonDeleteContact);
        //buttonDeleteContact.setTypeface(FontManager.getTypeface(FontManager.FONTAWESOME));

    }

    public void onClickCustom (View view) {
        EditText excuseCustom = (EditText) findViewById(R.id.excuseCustom);
        Spinner excuseSaved = (Spinner) findViewById(R.id.excuseSaved);
        TextView excuseAuto = (TextView) findViewById(R.id.excuseAuto);
        excuseCustom.setVisibility(View.VISIBLE);
        excuseSaved.setVisibility(View.GONE);
        excuseAuto.setVisibility(View.GONE);
    }
    public void onClickSaved (View view) {
        EditText excuseCustom = (EditText) findViewById(R.id.excuseCustom);
        Spinner excuseSaved = (Spinner) findViewById(R.id.excuseSaved);
        TextView excuseAuto = (TextView) findViewById(R.id.excuseAuto);
        excuseCustom.setVisibility(View.GONE);
        excuseSaved.setVisibility(View.VISIBLE);
        excuseAuto.setVisibility(View.GONE);
    }
    public void onClickAuto (View view) {
        EditText excuseCustom = (EditText) findViewById(R.id.excuseCustom);
        Spinner excuseSaved = (Spinner) findViewById(R.id.excuseSaved);
        TextView excuseAuto = (TextView) findViewById(R.id.excuseAuto);
        excuseCustom.setVisibility(View.GONE);
        excuseSaved.setVisibility(View.GONE);
        excuseAuto.setVisibility(View.VISIBLE);
    }
    public void onClickSaveResponses (View view) {
        EditText Location = (EditText) findViewById(R.id.editLocation);
        String Contact = "Test_Contact";
        TimePicker MeetTime = (TimePicker) findViewById(R.id.timePickerMeetTime);
        String Excuse = "Test_Excuse";
        Button buttonResponses;
        boolean isInserted = textHerDb.insertResponse(
                Location.getText().toString(),
                Contact,
                //MeetTime.getCurrentHour().toString() + ":" + MeetTime.getCurrentMinute().toString(),
                MeetTime.getHour() + ":" + MeetTime.getMinute(),
                //"FAke time",
                Excuse
        );

        if (isInserted == true) {
            Toast.makeText(MainActivity.this, "TextHer has been set!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Your TextHer Could NOT be saved at this time!", Toast.LENGTH_LONG).show();
        }

    }
    public void onClickAddContact (View view) {
        startActivity(new Intent(MainActivity.this, ContactModal.class));
    }

    public void onClickDeleteContact (View view) {
        startActivity(new Intent(MainActivity.this, ContactModalDelete.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
