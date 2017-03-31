package com.example.jessicamcavazoserhard.heatstress.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.adapter.WeatherCardAdapter;
import com.example.jessicamcavazoserhard.heatstress.model.WeatherCard;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton btGo;
    EditText etLocation;
    TextView tvCurrentHumidity;
    TextView tvCurrentTemperature;
    private RecyclerView recyclerView;
    private WeatherCardAdapter adapter;
    private ArrayList<WeatherCard> listData;

    String Location;
    String humidity;
    String temperature;

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
        etLocation = (EditText) findViewById(R.id.editText_address);

        tvCurrentHumidity = (TextView) findViewById(R.id.textView_humidityValue);
        tvCurrentTemperature = (TextView) findViewById(R.id.textView_temperatureValue);

        btGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this , InfoActivity.class);
        startActivity(i);
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

    // IMPLEMENTATION ASYNCTASK FOR WEATHER API REQUEST
    // KEY - 25d0f02c485109f2

    class RetrieveWeatherForLocation extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {

            Location = etLocation.getText().toString();
        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            Location = Location.replaceAll("\\s+", "_");
            Log.d("Conecion","String trnsformed: " + Location);

            try {
                URL url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/CA/"+Location+".json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("Conecion","Retrieving weather from: " + url);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);

            try {

                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                humidity = object.getJSONObject("current_observation").getString("relative_humidity");
                temperature = object.getJSONObject("current_observation").getString("temp_f");
                Log.d("Humedad","HUMEDAD EN SAN FRANCISCO : " + humidity);

                tvCurrentTemperature.setText(temperature + " ºF");
                tvCurrentHumidity.setText(humidity + " %");


            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
            }

        }
    }


}
