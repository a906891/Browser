package com.abhiraj.indianbrowsermadeinindia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button setDefaultBrowserbtn = findViewById(R.id.setDefaultBrowserbtn);
        setDefaultBrowserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        Button downloadSettingsbtn = findViewById(R.id.downloadSettingsbtn);
        downloadSettingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsDownload.class));
            }
        });


        Button searchSetttingsbtn = findViewById(R.id.searchSetttingsbtn);
        searchSetttingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,SettingsSearch.class));
            }
        });


        Button adBlockerbtn = findViewById(R.id.adBlockerbtn);
        adBlockerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsAdBlocker.class));
            }
        });


        Button clearRecordsbtn = findViewById(R.id.clearRecordsbtn);
        clearRecordsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsClearRecord.class));
            }
        });


        Button browsingSettingsbtn = findViewById(R.id.browsingSettingsbtn);
        browsingSettingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsBrowsing.class));
            }
        });


        Button notificationSettingsbtn = findViewById(R.id.notificationSettingsbtn);
        notificationSettingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsNotification.class));
            }
        });


        Button aboutUsbtn = findViewById(R.id.aboutUsbtn);
        aboutUsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsAboutUs.class));
            }
        });


        Button changeLanguagebtn = findViewById(R.id.changeLanguagebtn);
        changeLanguagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsChnagelanguage.class));
            }
        });


        Button resetToDefaultbtn = findViewById(R.id.resetToDefaultbtn);
        resetToDefaultbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //////change back do no show again in exit to default
                final SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("show", String.valueOf(0));
                editor1.apply();

                /////change back clear history to default
                final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ClearHistroy", String.valueOf(0));
                editor.apply();

                Toast.makeText(Settings.this, "Reset to Default", Toast.LENGTH_SHORT).show();
            }
        });

   }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}