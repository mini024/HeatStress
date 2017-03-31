package com.example.jessicamcavazoserhard.heatstress.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    ImageButton btGo;
    EditText etLocation;
    private RecyclerView recyclerView;
    private WeatherCardAdapter adapter;
    private ArrayList<WeatherCard> listData;
    String humidity;

    String Location;

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
        btGo.setOnClickListener(this);

        etLocation.setOnKeyListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent i = new Intent(this , InfoActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etLocation.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    new RetrieveWeatherForLocation().execute();
                    return true;
                default:
                    break;
            }
        }
        return false;
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

    //MARK - API Implementation
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
            Log.d("Conecion","String transformed: " + Location);

            try {
                URL url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/CA/"+Location+".json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("Connecion","Retrieving weather from: " + url);
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
                Log.d("Humedad","HUMEDAD EN SAN FRANCISCO : " + humidity);


            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }


}
