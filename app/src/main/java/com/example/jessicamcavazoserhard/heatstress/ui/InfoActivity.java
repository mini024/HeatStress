package com.example.jessicamcavazoserhard.heatstress.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.jessicamcavazoserhard.heatstress.R;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton ibMoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ibMoreInfo = (ImageButton) findViewById(R.id.image_MoreInfo);
        ibMoreInfo.setOnClickListener(this);

    }


    @Override
    public void onClick(View v){

        if (v.getId() == R.id.image_MoreInfo){

            Intent MoreInfo = new Intent(InfoActivity.this, MoreInfoActivity.class);
            startActivity(MoreInfo);
        }
    }


}

