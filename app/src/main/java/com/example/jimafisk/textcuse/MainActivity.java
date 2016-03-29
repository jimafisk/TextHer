package com.example.jimafisk.textcuse;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper textHerDb;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textHerDb = new DatabaseHelper(this);

        Cursor result = textHerDb.getContacts();
        if (result.getCount() == 0) {

        }
        // Create empty lists for Contact Names and IDs
        List<String> allContacts = new ArrayList<String>();
        List<String> allContactIDs = new ArrayList<String>();
        // Add Contact Names from db to allContacts and Contact IDs from db to allContactIDs
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
            // The Cursor is now set to the right position
            allContacts.add(result.getString(0));
            allContactIDs.add(result.getString(1));
        }
        // Get the Spinner (select list of contacts) element
        Spinner spinner = (Spinner) findViewById(R.id.contactSaved);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Create an empty list of categories that will hold the Contact Name and ID via tags
        List<StringWithTag> categories = new ArrayList<StringWithTag>();
        // Check if the Spinner (select list of Contacts) is empty
        if (allContacts.size() < 1) {
            // if the list of Contact Names from the db is empty, display some help text to the user asking them to create a contact
            TextView contactRequired = (TextView) findViewById(R.id.contactRequired);
            contactRequired.setVisibility(View.VISIBLE);
        } else {
            // if the list of Contact Names is NOT empty, add the name and it's associated ID tag (primary key)
            for (int i = 0; i < allContacts.size(); i++) {
                // Run StringWithTag helper method below
                categories.add(new StringWithTag(allContacts.get(i), allContactIDs.get(i)));
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<StringWithTag> dataAdapter = new ArrayAdapter<StringWithTag>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // Add Font Awesome
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);

        // GPS

        final TextView testTextView = (TextView) findViewById(R.id.excuseAuto);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //testTextView.append("\n hey!!!! " + location.getLatitude() + " " + location.getLongitude());
                Toast.makeText(MainActivity.this, "You are currently located at: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();

                System.out.println("Has not run try yet");
                // Run Async
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=false";
                        StringBuilder response = new StringBuilder();

                        URL url = new URL(stringUrl);
                        HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                            String strLine = null;
                            while ((strLine = input.readLine()) != null) {
                                response.append(strLine);
                            }
                            input.close();
                        }
                        String responseText = response.toString();
                        System.out.println(responseText);
                        JSONObject jsonObject = new JSONObject(responseText);
                        // routesArray contains ALL routes
                        JSONArray routesArray = jsonObject.getJSONArray("routes");
                        // Grab the first route
                        JSONObject route = routesArray.getJSONObject(0);
                        // Take all legs from the route
                        JSONArray legs = route.getJSONArray("legs");
                        // Grab first leg
                        JSONObject leg = legs.getJSONObject(0);
                        JSONObject durationObject = leg.getJSONObject("duration");
                        String duration = durationObject.getString("text");
                        Toast.makeText(MainActivity.this, "It will take you " + duration + " to get to your destination", Toast.LENGTH_LONG).show();
                        /*
                        JSONObject jsonObject = new JSONObject(responseText);
                        JSONArray routeObject = jsonObject.getJSONArray("routes");
                        JSONArray legsObject = routeObject.getJSONArray(2);
                        JSONObject durationObject = legsObject.getJSONObject(1);
                        String duration = durationObject.getString("text");
                        Toast.makeText(MainActivity.this, "It will take you " + duration + " mins to get to your destination", Toast.LENGTH_LONG).show();
                        System.out.println(duration);
                        */
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }




                /*
                Location meetLocation = new Location("point A");
                meetLocation.setLatitude(79.422006);
                meetLocation.setLongitude(-89.084095);
                float distance = location.distanceTo(meetLocation);
                Toast.makeText(MainActivity.this, "You are " + distance + " meters away from where you need to be", Toast.LENGTH_LONG).show();
                */
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
            return;
        } else {
            configureInterval();
        }

    }

    private void configureInterval() {
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    configureInterval();
                return;
        }
    }
    // End GPS

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
        // Get user inputted location text
        EditText Location = (EditText) findViewById(R.id.editLocation);
        // Get ID tag (primary key) associated with selected Contact
        Spinner contactSpinner = (Spinner) findViewById(R.id.contactSaved);
        Integer pos = contactSpinner.getSelectedItemPosition();
        StringWithTag s = (StringWithTag) contactSpinner.getItemAtPosition(pos);
        Object tag = s.tag;
        int contactID = Integer.valueOf((String) tag);
        // Get user selected meeting time
        TimePicker MeetTime = (TimePicker) findViewById(R.id.timePickerMeetTime);
        String Excuse = "Test_Excuse";
        //Button buttonResponses;
        boolean isInserted = textHerDb.insertResponse(
                Location.getText().toString(),
                contactID,
                MeetTime.getHour() + ":" + MeetTime.getMinute(),
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

    // Helper method to allow IDs to be added to Spinner select list items
    public class StringWithTag {
        public String string;
        public Object tag;

        public StringWithTag(String stringPart, Object tagPart) {
            string = stringPart;
            tag = tagPart;
        }

        @Override
        public String toString() {
            return string;
        }
    }


}
