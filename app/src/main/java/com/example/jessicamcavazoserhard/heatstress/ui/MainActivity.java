package com.example.jessicamcavazoserhard.heatstress.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.adapter.WeatherCardAdapter;
import com.example.jessicamcavazoserhard.heatstress.model.WeatherCard;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btGo;
    SeekBar sbCall;
    int sbprogress;
    private RecyclerView recyclerView;
    private WeatherCardAdapter adapter;
    private ArrayList<WeatherCard> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listData = getData();

        recyclerView = (RecyclerView) findViewById(R.id.rec_view_main);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new WeatherCardAdapter(listData,this);
        recyclerView.setAdapter(adapter);

        btGo = (ImageButton) findViewById(R.id.imageButton_risk);
        btGo.setOnClickListener(this);

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
    public void onClick(View v) {
        Intent i = new Intent(this , InfoActivity.class);
        startActivity(i);
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

    ArrayList<WeatherCard> getData(){
        ArrayList<WeatherCard> dummyData = new ArrayList<>();
        WeatherCard dummy = new WeatherCard("11:00 AM","Sunny",80,20);
        dummyData.add(dummy);
        dummy = new WeatherCard("12:00 PM","Sunny",80,20);
        dummyData.add(dummy);
        dummy = new WeatherCard("1:00 PM","Sunny",80,20);
        dummyData.add(dummy);
        dummy = new WeatherCard("2:00 PM","PartCloudy",70,30);
        dummyData.add(dummy);
        dummy = new WeatherCard("3:00 PM","PartCloudy",70,30);
        dummyData.add(dummy);
        dummy = new WeatherCard("4:00 PM","PartCloudy",70,50);
        dummyData.add(dummy);
        dummy = new WeatherCard("5:00 PM","Cloudy",60,60);
        dummyData.add(dummy);
        dummy = new WeatherCard("6:00 PM","Cloudy",60,60);
        dummyData.add(dummy);
        dummy = new WeatherCard("7:00 PM","Rainy",50,100);
        dummyData.add(dummy);
        dummy = new WeatherCard("8:00 PM","Rainy",50,100);
        dummyData.add(dummy);
        dummy = new WeatherCard("9:00 PM","Windy",50,90);
        dummyData.add(dummy);
        dummy = new WeatherCard("10:00 PM","Snowy",0,10);
        dummyData.add(dummy);

        return dummyData ;
    }

}
