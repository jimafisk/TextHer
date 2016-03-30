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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
                Toast.makeText(MainActivity.this, "You are currently located at: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();


                System.out.println("Has not run try yet");
                // Run Async
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    // Get street address from latitude and longitude coordinates
                    Geocoder geocoder;
                    List<Address> addresses;
                    String address = null, city = null, state = null, country = null, postalCode = null, knownName = null;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        country = addresses.get(0).getCountryName();
                        postalCode = addresses.get(0).getPostalCode();
                        knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String origin = address + city + state + postalCode;
                    Cursor result = textHerDb.getLastLocation();
                    String destination = new String();
                    for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
                        // The Cursor is now set to the right position
                        destination = result.getString(0);
                    }
                    String duration = null;

                    try {
                        String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + URLEncoder.encode(origin, "UTF-8") + "&destination=" + URLEncoder.encode(destination, "UTF-8") + "&sensor=false";
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
                        //String duration = durationObject.getString("text");
                        duration = durationObject.getString("text");
                        Toast.makeText(MainActivity.this, "It will take you " + duration + " to get to " + destination + " from " + origin, Toast.LENGTH_LONG).show();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Calendar cal = Calendar.getInstance();
                    long startTime = System.currentTimeMillis(); // Process start time
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Calculating elapse time
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    // Incrementing the calendar time based on elapsed time
                    cal.setTimeInMillis(cal.getTime().getTime() + elapsedTime);
                    Toast.makeText(MainActivity.this, "Current time is: " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();

                    Cursor resultTime = textHerDb.getLastMeetTime();
                    String meetTime = new String();
                    for (resultTime.moveToFirst(); !resultTime.isAfterLast(); resultTime.moveToNext()) {
                        // The Cursor is now set to the right position
                        meetTime = resultTime.getString(0);
                    }
                    Toast.makeText(MainActivity.this, "Meet time is: " + meetTime, Toast.LENGTH_LONG).show();


                    //String test = "15 hour 10 min";
                    String splitStr = duration;
                    String[] splitDur = splitStr.split(" ");
                    //long additionalDuration = 0;
                    int durDays = 0;
                    int durHours = 0;
                    int durMinutes = 0;
                    for(int i = 0; i < splitDur.length; i+=2) {
                        switch(splitDur[i+1].toLowerCase()) {
                            case "day":
                            case "days":
                                //additionalDuration += Integer.parseInt(testArray[i]) * (1000 * 60 * 60 * 24);
                                durDays = Integer.parseInt(splitDur[i]);
                                break;
                            case "hour":
                            case "hours":
                                //additionalDuration += Integer.parseInt(testArray[i]) * (1000 * 60 * 60);
                                durHours = Integer.parseInt(splitDur[i]);
                                break;
                            case "minute":
                            case "minutes":
                            case "min":
                            case "mins":
                                //additionalDuration += Integer.parseInt(testArray[i]) * (1000*60);
                                durMinutes = Integer.parseInt(splitDur[i]);
                                break;
                            default:
                                System.out.println("Could not parse unit: \""+splitDur[i+1]+"\"");
                        }
                    }
                    //Date futureDate = new Date(new Date().getTime() + additionalDuration);
                    //Toast.makeText(MainActivity.this, "Futuredate: " + futureDate, Toast.LENGTH_LONG).show();
                    //Toast.makeText(MainActivity.this, "Futuredate: " + additionalDuration, Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Travel = " + durDays + " days, " + durHours + " hours, " + durMinutes + " minutes", Toast.LENGTH_LONG).show();

                    //Integer x = Integer.valueOf(durMinutes);
                    //Integer y = Integer.valueOf(durHours);
                    //cal.add(Calendar.MINUTE, x);
                    //cal.add(Calendar.HOUR_OF_DAY, y)
                    cal.add(Calendar.MINUTE, durMinutes);
                    cal.add(Calendar.HOUR_OF_DAY, durHours);
                    //cal.add(Calendar.MINUTE, 10);
                    //cal.add(Calendar.HOUR_OF_DAY, 1);
                    Toast.makeText(MainActivity.this, "Current time + travel is: " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();

                    String[] splitMeet = meetTime.split(":");
                    int meetHours = 0;
                    int meetMinutes = 0;
                    meetHours = Integer.parseInt(splitMeet[0]);
                    meetMinutes = Integer.parseInt(splitMeet[1]);
                    if (meetHours < cal.get(Calendar.HOUR_OF_DAY)) {
                        // You're going to be late, so send text
                        Toast.makeText(MainActivity.this, "You're clearly late... send text!", Toast.LENGTH_LONG).show();

                    } else if (meetHours == cal.get(Calendar.HOUR_OF_DAY)) {
                        // hours are the same so check mins
                        if (meetMinutes <= cal.get(Calendar.MINUTE)) {
                            Toast.makeText(MainActivity.this, "You're late... texting", Toast.LENGTH_LONG).show();
                            sendSMS("617-231-1234", "Sorry I'm running late! Apparently you're not supposed to ");
                            /*
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "413-233-7273"));
                            intent.putExtra("sms_body", message);
                            startActivity(intent);
                            */
                        } else {
                            Toast.makeText(MainActivity.this, "You're ok for a few more mins", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "You're ok for now", Toast.LENGTH_LONG).show();
                    }

                }
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

    // Helper method to send text messages
    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

}
