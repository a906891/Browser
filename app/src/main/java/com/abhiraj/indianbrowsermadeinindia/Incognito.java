package com.abhiraj.indianbrowsermadeinindia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import java.util.Timer;
import java.util.TimerTask;

public class Incognito extends AppCompatActivity {

    private static final int RESULT_DEFAULT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incognito);

        run();


    }
    private void run()
    {
        new Timer().schedule(new TimerTask(){
            public void run() {

                setResult(RESULT_DEFAULT);

            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}