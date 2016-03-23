package com.example.jimafisk.texther;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jimafisk on 3/21/16.
 */
public class ContactModal extends Activity {

    DatabaseHelper textHerDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_modal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.6));

        textHerDb = new DatabaseHelper(this);
    }

    public void onClickSaveContact (View view) {

        EditText Name = (EditText) findViewById(R.id.contactName);
        EditText Phone = (EditText) findViewById(R.id.contactPhone);

        boolean isInserted = textHerDb.insertContact(
                Name.getText().toString(),
                Phone.getText().toString()
        );

        if (isInserted == true) {
            Toast.makeText(ContactModal.this, "Contact has been saved!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ContactModal.this, MainActivity.class));
        } else {
            Toast.makeText(ContactModal.this, "TextHer could not save " + Name.getText().toString() + " at this time.", Toast.LENGTH_LONG).show();
        }
    }
}
