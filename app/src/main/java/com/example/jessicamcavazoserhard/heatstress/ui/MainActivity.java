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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
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

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    private RecyclerView recyclerView;
    private WeatherCardAdapter adapter;
    private ArrayList<WeatherCard> listData;

    ImageButton btGo;
    AutoCompleteTextView etLocation;
    TextView tvCurrentRisk;
    TextView tvCurrentHumidity;
    TextView tvCurrentTemperature;
    JSONArray dataTime;
    String Location;
    String humidity;
    String temperature;
    JSONObject x;
    SeekBar sbCall;
    ImageButton btShowLocation;
    TextView tvLocation;
    CheckBox cbCurrentLocation;
    int sbprogress;

    final double c1=16.923,c2=0.185212,c3=5.37941,c4=-0.100254,c5=0.00941695,c6=0.00728898,c7=0.000345372,c8=-0.000814971,c9=0.0000102102,c10=-0.000038646,c11=0.0000291583,c12=0.00000142721,c13=0.000000197483,c14=-0.0000000218429,c15=0.000000000843296,c16=-0.0000000000481975;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listData = getData();

        recyclerView = (RecyclerView) findViewById(R.id.rec_view_main);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new WeatherCardAdapter(listData, this);
        recyclerView.setAdapter(adapter);

        btGo = (ImageButton) findViewById(R.id.imageButton_risk);

        tvCurrentHumidity = (TextView) findViewById(R.id.textView_humidityValue);
        tvCurrentTemperature = (TextView) findViewById(R.id.textView_temperatureValue);
        tvCurrentRisk = (TextView) findViewById(R.id.textView_risk);

        btGo.setOnClickListener(this);

        btShowLocation = (ImageButton) findViewById(R.id.imageButton_showLocation);
        btShowLocation.setOnClickListener(this);

        tvLocation = (TextView) findViewById(R.id.textView_location);

        cbCurrentLocation = (CheckBox) findViewById(R.id.checkBox2);

        //MARK: MAKING CALL
        sbCall = (SeekBar) findViewById(R.id.seek_Call911);
        sbCall.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                if (sbprogress > 93) {
                    //Making Call
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:8186938092"));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
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
        switch (v.getId()){
            case R.id.imageButton_risk:
                Intent i = new Intent(this , InfoActivity.class);
                startActivity(i);
                break;
            case R.id.imageButton_showLocation:
                ShowLocation();
                break;
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

    void HideLocation() {
        etLocation.setVisibility(View.GONE);
        cbCurrentLocation.setVisibility(View.GONE);
        btShowLocation.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.VISIBLE);
        tvLocation.setText(etLocation.getText());
    }

    void ShowLocation() {
        etLocation.setVisibility(View.VISIBLE);
        btShowLocation.setVisibility(View.GONE);
        cbCurrentLocation.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.GONE);
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
                    HideLocation();
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

    void calculateRisk (double temperature, String humidity ){

        humidity = humidity.replaceAll("%", "");
        int Rhumidity = Integer.parseInt(humidity);


        double heatIndex = c1 + c2*temperature +c3*Rhumidity + c4*temperature*Rhumidity + c5*(Math.pow(temperature,2)) + c6*(Math.pow(Rhumidity,2)) + c7*(Math.pow(temperature,2))*Rhumidity + c8*temperature*(Math.pow(Rhumidity,2)) + c9*(Math.pow(temperature,2))*(Math.pow(Rhumidity,2)) + c10*(Math.pow(temperature,3)) + c11*(Math.pow(Rhumidity,3)) + c12*(Math.pow(temperature,3))*Rhumidity + c13*temperature*(Math.pow(Rhumidity,3)) + c14*(Math.pow(temperature,3))*(Math.pow(Rhumidity,2)) + c15*(Math.pow(temperature,2))*(Math.pow(Rhumidity,3)) + c15*(Math.pow(temperature,3))*(Math.pow(Rhumidity,3));


        if (heatIndex >= 126){

            btGo.setImageResource(R.drawable.very_high_risk);
            tvCurrentRisk.setText("Extreme Risk");
        }

        if (heatIndex >= 104 && heatIndex <=125){
            btGo.setImageResource(R.drawable.high_risk);
            tvCurrentRisk.setText("High Risk");
        }

        if (heatIndex >= 91 && heatIndex <=103){

            btGo.setImageResource(R.drawable.medium_risk);
            tvCurrentRisk.setText("Medium Risk");

        }

        if (heatIndex <=90){

            btGo.setImageResource(R.drawable.minimal_risk);
            tvCurrentRisk.setText("Minimal Risk");

        }



    }

    //MARK: Get data of cards from JSON response
    void setData(){
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
            Log.d("Conexion","String transformed: " + Location);

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

                Log.d("Query", object.toString());

                temperature = object.getJSONObject("current_observation").getString("temp_f");
                Log.d("Humedad","HUMEDAD EN SAN FRANCISCO : " + humidity);

                tvCurrentTemperature.setText(temperature + " ºF");

                tvCurrentHumidity.setText(humidity);

                calculateRisk(Double.parseDouble(temperature), humidity);

                dataTime = (JSONArray) object.getJSONArray("hourly_forecast");

                setData();


            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
            }

        }
    }

}
