package com.abhiraj.indianbrowsermadeinindia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class Feedback extends AppCompatActivity {

    private static final int RESULT_DEFAULT = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setResult(RESULT_DEFAULT);

    }

    @Override
    public void onBackPressed() {

        finish();
    }
}