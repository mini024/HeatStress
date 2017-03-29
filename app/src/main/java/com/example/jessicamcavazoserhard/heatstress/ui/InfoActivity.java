package com.example.jessicamcavazoserhard.heatstress.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.jessicamcavazoserhard.heatstress.R;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton ibMoreInfo;
    SeekBar sbCall;
    int sbprogress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ibMoreInfo = (ImageButton) findViewById(R.id.image_MoreInfo);
        ibMoreInfo.setOnClickListener(this);

        sbCall = (SeekBar) findViewById(R.id.seek_Call911);
        sbCall.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                if (sbprogress > 93){
                    //Making Call
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:8186938092"));
                    startActivity(i);

                } else {
                    sbCall.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                sbprogress = progress;
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sbCall.setProgress(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sbCall.setProgress(0);
        //Restore state here
    }

    @Override
    public void onClick(View v){

        if (v.getId() == R.id.image_MoreInfo){

            Intent MoreInfo = new Intent(InfoActivity.this, MoreInfoActivity.class);
            startActivity(MoreInfo);
        }
    }


}

