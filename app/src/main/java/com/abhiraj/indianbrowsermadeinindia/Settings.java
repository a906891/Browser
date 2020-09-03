package com.abhiraj.indianbrowsermadeinindia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;


public class Settings extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    int nightmode = 0;
    LinearLayout back;

    private static final int RESULT_DEFAULT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.background);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        nightmode = Integer.parseInt(sharedPreferences.getString("nightmode", "0"));

        if (nightmode == 1) {
            back.setBackgroundColor(Color.GRAY);
        } else {
            back.setBackgroundColor(Color.WHITE);
        }

        TextView setDefaultBrowserbtn = findViewById(R.id.setDefaultBrowserbtn);
        setDefaultBrowserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        TextView downloadSettingsbtn = findViewById(R.id.downloadSettingsbtn);
        downloadSettingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsDownload.class));
            }
        });


        TextView searchSetttingsbtn = findViewById(R.id.searchSetttingsbtn);
        searchSetttingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,SettingsSearch.class));
            }
        });


        TextView adBlockerbtn = findViewById(R.id.adBlockerbtn);
        adBlockerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsAdBlocker.class));
            }
        });


        TextView clearRecordsbtn = findViewById(R.id.clearRecordsbtn);
        clearRecordsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsClearRecord.class));
            }
        });


        TextView browsingSettingsbtn = findViewById(R.id.browsingSettingsbtn);
        browsingSettingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsBrowsing.class));
            }
        });


        TextView notificationSettingsbtn = findViewById(R.id.notificationSettingsbtn);
        notificationSettingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsNotification.class));
            }
        });


        TextView aboutUsbtn = findViewById(R.id.aboutUsbtn);
        aboutUsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsAboutUs.class));
            }
        });


        TextView changeLanguagebtn = findViewById(R.id.changeLanguagebtn);
        changeLanguagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, SettingsChnagelanguage.class));
            }
        });


        TextView resetToDefaultbtn = findViewById(R.id.resetToDefaultbtn);
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
                Toast.makeText(Settings.this, "Restart App To Reset", Toast.LENGTH_SHORT).show();

            }
        });

        setResult(RESULT_DEFAULT);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}