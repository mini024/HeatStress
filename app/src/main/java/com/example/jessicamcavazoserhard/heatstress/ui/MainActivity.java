package com.example.jessicamcavazoserhard.heatstress.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jessicamcavazoserhard.heatstress.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btGo = (Button) findViewById(R.id.button_go);
        btGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this , MoreInfoActivity.class);
        startActivity(i);
    }
}
