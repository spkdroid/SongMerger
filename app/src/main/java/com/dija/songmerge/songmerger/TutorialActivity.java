package com.dija.songmerge.songmerger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TutorialActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

       tv = findViewById(R.id.tutorialMessage);
       tv.setText(R.string.tutorial);
    }

    public void onBackPressed()
    {
        this.finish();
    }


}
