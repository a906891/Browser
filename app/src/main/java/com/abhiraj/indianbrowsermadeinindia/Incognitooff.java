package com.abhiraj.indianbrowsermadeinindia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import java.util.Timer;
import java.util.TimerTask;

public class Incognitooff extends AppCompatActivity {

    private static final int RESULT_DEFAULT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incognitooff);

        new Timer().schedule(new TimerTask(){
            public void run() {

                setResult(RESULT_DEFAULT);
                finish();

            }
        }, 2000);
    }
}