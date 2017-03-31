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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.WrapperListAdapter;

import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.adapter.WeatherCardAdapter;
import com.example.jessicamcavazoserhard.heatstress.model.WeatherCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    private RecyclerView recyclerView;
    private WeatherCardAdapter adapter;
    private ArrayList<WeatherCard> listData;

    ImageButton btGo;
    AutoCompleteTextView etLocation;
    TextView tvCurrentHumidity;
    TextView tvCurrentTemperature;
    JSONArray dataTime;
    String Location;
    String humidity;
    String temperature;
    JSONObject x;


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

        tvCurrentHumidity = (TextView) findViewById(R.id.textView_humidityValue);
        tvCurrentTemperature = (TextView) findViewById(R.id.textView_temperatureValue);

        btGo.setOnClickListener(this);


        //Auto Complete Text View
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        etLocation = (AutoCompleteTextView) findViewById(R.id.editText_address);
        etLocation.setAdapter(adapter);
        etLocation.setOnKeyListener(this);
    }

    //MARK: OnClick on button image(only button) go to InfoActivity
    @Override
    public void onClick(View v) {
        Intent i = new Intent(this , InfoActivity.class);
        startActivity(i);
    }

    //MARK: Detect enter to hide keyboard and get data.
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

    //MARK: Get false Data while getting real data
    ArrayList<WeatherCard> getData(){
        ArrayList<WeatherCard> dummyData = new ArrayList<>();
        WeatherCard dummy = new WeatherCard("11:00 AM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("12:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("1:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("2:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("3:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("4:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("5:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("6:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("7:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("8:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("9:00 PM"," ",0,0);
        dummyData.add(dummy);
        dummy = new WeatherCard("10:00 PM"," ",0,0);
        dummyData.add(dummy);

        return dummyData ;
    }

    void calculateRisk (int temperature, int humidity ){

        if (temperature < 32){
            btGo.setBackgroundResource(R.drawable.minimal_risk);
        }

        if (temperature > 108){
            btGo.setBackgroundResource(R.drawable.very_high_risk);
        }

        if ((temperature >= 32 && temperature <= 89) && (humidity >= 40 && humidity <= 45)){

            btGo.setBackgroundResource(R.drawable.minimal_risk);
        }



    }


    //MARK: Get data of cards from JSON response
    void getData2(){
        listData.clear();

        try {
            x = (JSONObject) dataTime.get(0);
            Log.d("Humedad","HUMEDAD EN SAN FRANCISCO : " + x.getJSONObject("FCTTIME").getString("hour"));
        } catch  (JSONException e){
            Log.e("ERROR", e.getMessage(), e);
        }

        for (int i = 0; i< 12; i++){
            WeatherCard dummy = new WeatherCard(getJSONString("hour", i, "FCTTIME") + ":00", getJSONString("condition", i, " "), getJSONInt("english", i, "temp"), getJSONInt("humidity", i, " "));
            listData.add(dummy);
        }

        adapter.notifyDataSetChanged();
    }

    String getJSONString(String key, int id, String keyObject){
        try {
            x = (JSONObject) dataTime.get(id);
            if (keyObject != " "){
                return x.getJSONObject(keyObject).getString(key);
            } else {
                return x.getString(key);
            }

        } catch  (JSONException e){
            Log.e("ERROR", e.getMessage(), e);
        }
        return " ";
    }

    int getJSONInt(String key, int id, String keyObject){
        try {
            x = (JSONObject) dataTime.get(id);
            if (keyObject != " "){
                return Integer.parseInt(x.getJSONObject(keyObject).getString(key));
            } else {
                return Integer.parseInt(x.getString(key));
            }

        } catch  (JSONException e){
            Log.e("ERROR", e.getMessage(), e);
        }

        return 0;
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

                temperature = object.getJSONObject("current_observation").getString("temp_f");
                Log.d("Humedad","HUMEDAD EN SAN FRANCISCO : " + humidity);

                tvCurrentTemperature.setText(temperature + " ºF");

                tvCurrentHumidity.setText(humidity);

                calculateRisk(Integer.parseInt(tvCurrentTemperature.getText().toString()), Integer.parseInt(tvCurrentHumidity.getText().toString()));

                dataTime = (JSONArray) object.getJSONArray("hourly_forecast");


                getData2();


            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
            }

        }
    }


}
