package com.example.jimafisk.texther;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
