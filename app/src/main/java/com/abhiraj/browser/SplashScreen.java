package com.abhiraj.browser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        run();


    }
    private void run()
    {
        new Timer().schedule(new TimerTask(){
            public void run() {

               startActivity(new Intent(SplashScreen.this,MainActivity.class));
            }
        }, 2400);
    }

}
