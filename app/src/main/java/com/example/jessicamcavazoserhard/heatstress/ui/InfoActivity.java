/**
 Heat Stress is an Android health app.
 Copyright (C) 2017  Heat Stress Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.jessicamcavazoserhard.heatstress.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jessicamcavazoserhard.heatstress.BlurBuilder;
import com.example.jessicamcavazoserhard.heatstress.R;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton ibMoreInfo;
    SeekBar sbCall;
    TextView tvDo, tvDONT;
    int sbprogress = 0;
    int RiskType;
    View FilterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle i = getIntent().getExtras();
        RiskType = i.getInt("Risk");

        tvDo = (TextView)findViewById(R.id.text_DoRecomendations);
        tvDONT = (TextView)findViewById(R.id.text_DontRecomendations);
        ibMoreInfo = (ImageButton) findViewById(R.id.image_MoreInfo);
        ibMoreInfo.setOnClickListener(this);
        FilterView = findViewById(R.id.activity_info);


        SetRecomendations();

        //Blurry background
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.background);

        Bitmap blurredBitmap = BlurBuilder.blur(InfoActivity.this, icon);

        FilterView.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );

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

    public void SetRecomendations(){

        switch (RiskType){

            case 1:
                tvDo.setText(R.string.minimal_DO_REQ);
                tvDONT.setText(R.string.minimal_DONT_REQ);
                break;

            case 2:
                tvDo.setText(R.string.medium_DO_REQ);
                tvDONT.setText(R.string.medium_DONT_REQ);
                break;

            case 3:
                tvDo.setText(R.string.high_DO_REQ);
                tvDONT.setText(R.string.high_DONT_REQ);
                break;

            case 4:
                tvDo.setText(R.string.very_high_DO_REQ);
                tvDONT.setText(R.string.very_high_DONT_REQ);

        }

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

