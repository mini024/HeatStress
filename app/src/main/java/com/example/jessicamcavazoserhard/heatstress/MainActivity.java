package com.example.jessicamcavazoserhard.heatstress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btGo = (ImageButton) findViewById(R.id.imageButton_risk);
        btGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this , InfoActivity.class);
        startActivity(i);
    }
}
