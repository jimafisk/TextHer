package com.example.jimafisk.texther;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper textHerDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textHerDb = new DatabaseHelper(this);
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
}
