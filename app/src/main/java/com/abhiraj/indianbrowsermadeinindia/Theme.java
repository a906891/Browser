package com.abhiraj.indianbrowsermadeinindia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



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
        }
    }

    private void sendTheme(int number) {
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("number", String.valueOf(number));
        editor.apply();
        Toast.makeText(this, "Number is saved " + number, Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(Theme.this,OriginalMainActivity.class));
    }




}
