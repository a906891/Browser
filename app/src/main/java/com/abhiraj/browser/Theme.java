package com.abhiraj.browser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Theme extends Activity implements View.OnClickListener {

    public static final String SHARED_PREFS = "sharedPrefs";

    public static final int RESULT_FAV_HIS = 0;
    private static final int RESULT_DEFAULT = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Button thm1 = findViewById(R.id.thm1);
        thm1.setOnClickListener(this);
        Button thm2 = findViewById(R.id.thm2);
        thm2.setOnClickListener(this);
        Button thm3 = findViewById(R.id.thm3);
        thm3.setOnClickListener(this);
        Button thm4 = findViewById(R.id.thm4);
        thm4.setOnClickListener(this);
        Button thm5 = findViewById(R.id.thm5);
        thm5.setOnClickListener(this);
        Button thm6 = findViewById(R.id.thm6);
        thm6.setOnClickListener(this);
        Button thm7 = findViewById(R.id.thm7);
        thm7.setOnClickListener(this);
        Button thm8 = findViewById(R.id.thm8);
        thm8.setOnClickListener(this);
        Button thm9 = findViewById(R.id.thm9);
        thm9.setOnClickListener(this);
        Button thm10 = findViewById(R.id.thm10);
        thm10.setOnClickListener(this);
        Button thm11 = findViewById(R.id.thm11);
        thm11.setOnClickListener(this);
        Button thm12 = findViewById(R.id.thm12);
        thm12.setOnClickListener(this);
        Button thm13 = findViewById(R.id.thm13);
        thm13.setOnClickListener(this);
        Button thm14 = findViewById(R.id.thm14);
        thm14.setOnClickListener(this);


        setResult(RESULT_DEFAULT);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.thm1:
                sendTheme(1);
                break;
            case R.id.thm2:
                sendTheme(2);
                break;
            case R.id.thm3:
                sendTheme(3);
                break;
            case R.id.thm4:
                sendTheme(4);
                break;
            case R.id.thm5:
                sendTheme(5);
                break;
            case R.id.thm6:
                sendTheme(6);
                break;
            case R.id.thm7:
                sendTheme(7);
                break;
            case R.id.thm8:
                sendTheme(8);
                break;
            case R.id.thm9:
                sendTheme(9);
                break;
            case R.id.thm10:
                sendTheme(10);
                break;
            case R.id.thm11:
                sendTheme(11);
                break;
            case R.id.thm12:
                sendTheme(12);
                break;
            case R.id.thm13:
                sendTheme(13);
                break;
            case R.id.thm14:
                sendTheme(14);
                break;
        }
    }

    private void sendTheme(int number) {
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("number", String.valueOf(number));
        editor.apply();


        finish();   //ultra high level code
//        startActivity(new Intent  (Theme.this,OriginalMainActivity.class));
    }




}
