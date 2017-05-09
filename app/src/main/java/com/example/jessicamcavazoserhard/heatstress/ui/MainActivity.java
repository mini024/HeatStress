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
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.WrapperListAdapter;

import com.example.jessicamcavazoserhard.heatstress.GlobalData;
import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.adapter.WeatherCardAdapter;
import com.example.jessicamcavazoserhard.heatstress.model.WeatherCard;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    //Location variables
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double lLat, lLong;
    private String sCity, sState;
    //

    String[] Cities = new String[]{
            "Monterrey, Nuevo Leon", "San Francisco , California", "Los Angeles , California", "Germany", "Spain"
    };

    //MARK - States
    public static final Map<String, String> states;
    static {
        states = new HashMap<String, String>();
        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","QC");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");
    }

    private RecyclerView recyclerView;
    private WeatherCardAdapter adapter;
    private ArrayList<WeatherCard> listData;
    ArrayAdapter<String> adapterAutoComplete;

    //Instance of global
    GlobalData global;

    ImageButton btGo;
    AutoCompleteTextView etLocation;
    TextView tvCurrentRisk;
    TextView tvCurrentHumidity;
    TextView tvCurrentTemperature;
    TextView tvMaxHumidity;
    TextView tvMaxTemp;
    TextView tvMaxRisk;
    View vMax;
    JSONArray dataTime;
    String Location;
    String humidity;
    String temperature;
    JSONObject x;
    SeekBar sbCall;
    ImageButton btShowLocation;
    TextView tvLocation;
    CheckBox cbCurrentLocation;
    String country = "";
    boolean internet = true;
    int sbprogress;

    final double c1=16.923,c2=0.185212,c3=5.37941,c4=-0.100254,c5=0.00941695,c6=0.00728898,c7=0.000345372,c8=-0.000814971,c9=0.0000102102,c10=-0.000038646,c11=0.0000291583,c12=0.00000142721,c13=0.000000197483,c14=-0.0000000218429,c15=0.000000000843296,c16=-0.0000000000481975;

    //Max temps and humidity
    PriorityQueue<Integer> pqTemp;
    PriorityQueue<Integer> pqHum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

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
        tvMaxHumidity = (TextView) findViewById(R.id.textView_humidityMax);
        tvMaxTemp = (TextView) findViewById(R.id.textView_temperatureMax);
        tvMaxRisk = (TextView) findViewById(R.id.tv_main_max_risk);
        vMax = findViewById(R.id.view_main_max);
        btGo.setOnClickListener(this);

        btShowLocation = (ImageButton) findViewById(R.id.imageButton_showLocation);
        btShowLocation.setOnClickListener(this);

        tvLocation = (TextView) findViewById(R.id.textView_location);

        cbCurrentLocation = (CheckBox) findViewById(R.id.checkBox2);
        cbCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    new RetrieveLocation().execute();
                }
            }
        });

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

        if (!isInternetAvailable()){
            internet = false;
            new AlertDialog.Builder(this).setTitle("Internet Connection").setMessage("Please check your internet connection").setNeutralButton("Close", null).show();
        }

        //Auto Complete Text View
        adapterAutoComplete = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Cities);
        adapterAutoComplete.setNotifyOnChange(true);
        etLocation = (AutoCompleteTextView) findViewById(R.id.editText_address);
        etLocation.setAdapter(adapterAutoComplete);
        etLocation.setOnKeyListener(this);
        etLocation.setThreshold(3);
        etLocation.addTextChangedListener(textWatcher);
    }

    //MARK: Text Watcher, detect if text changed.
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (start ==  4) {
                if (!isInternetAvailable()){
                    internet = false;
                    new AlertDialog.Builder(MainActivity.this).setTitle("Internet Connection").setMessage("Please check your internet connection").setNeutralButton("Close", null).show();
                } else {
                    internet = true;
                    //MARK: Get Weather
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etLocation.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    new RetrieveAutoComplete().execute();
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //MARK: OnClick on button image and showLocation
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
                    //MARK: Get Weather
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

    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    void calculateRisk (double temperature, String humidity, TextView tv ){

        humidity = humidity.replaceAll("%", "");
        int Rhumidity = Integer.parseInt(humidity);


        double heatIndex = c1 + c2*temperature +c3*Rhumidity + c4*temperature*Rhumidity + c5*(Math.pow(temperature,2)) + c6*(Math.pow(Rhumidity,2)) + c7*(Math.pow(temperature,2))*Rhumidity + c8*temperature*(Math.pow(Rhumidity,2)) + c9*(Math.pow(temperature,2))*(Math.pow(Rhumidity,2)) + c10*(Math.pow(temperature,3)) + c11*(Math.pow(Rhumidity,3)) + c12*(Math.pow(temperature,3))*Rhumidity + c13*temperature*(Math.pow(Rhumidity,3)) + c14*(Math.pow(temperature,3))*(Math.pow(Rhumidity,2)) + c15*(Math.pow(temperature,2))*(Math.pow(Rhumidity,3)) + c15*(Math.pow(temperature,3))*(Math.pow(Rhumidity,3));

        if (heatIndex >= 126){

            btGo.setImageResource(R.drawable.very_high_risk);
            tv.setText("Extreme Risk");
        }

        if (heatIndex >= 104 && heatIndex <=125){
            btGo.setImageResource(R.drawable.high_risk);
            tv.setText("High Risk");
        }

        if (heatIndex >= 91 && heatIndex <=103){

            btGo.setImageResource(R.drawable.medium_risk);
            tv.setText("Medium Risk");

        }

        if (heatIndex <=90){

            btGo.setImageResource(R.drawable.minimal_risk);
            tv.setText("Minimal Risk");

        }

    }

    static class PQsort implements Comparator<Integer> {

        public int compare(Integer one, Integer two) {
            return two - one;
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

        PQsort pqs = new PQsort();
        pqHum=new PriorityQueue<Integer>(12,pqs);
        pqTemp=new PriorityQueue<Integer>(12,pqs);

        int temp,hum;
        for (int i = 0; i< 12; i++){
            //Add temperature and humidity to its respective priority queues
            temp=getJSONInt("english", i, "temp");
            pqTemp.add(temp);
            hum=getJSONInt("humidity", i, " ");
            pqHum.add(hum);
            WeatherCard dummy = new WeatherCard(getJSONString("hour", i, "FCTTIME") + ":00", getJSONString("condition", i, " "), temp, hum);
            listData.add(dummy);
        }

        adapter.notifyDataSetChanged();
        setMaxs();
    }

    private void setMaxs() {
        tvMaxTemp.setText(String.valueOf(pqTemp.peek()) + "°F");
        tvMaxHumidity.setText(String.valueOf(pqHum.peek()) + "%");
        calculateRisk(Double.parseDouble(String.valueOf(pqTemp.peek())),String.valueOf(pqHum.peek()+"%"),tvMaxRisk);
    }



    //MARK: Get data from json and change to data type
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
            String state = Location.substring(Location.indexOf(","));
            Location = Location.replace(state, "");
            state = state.replace(", ", "");
            Location = Location.replaceAll("\\s+", "_");
            Log.d("Conexion","String transformed: " + Location);

            try {
                if (internet) {
                    URL url;
                    if (country != "United States" && country != "") {
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/" + country + "/" + Location + ".json");
                    } else if (states.get(state) != null) {
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/" + states.get(state) + "/" + Location + ".json");
                    } else {
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/" + state + "/" + Location + ".json");
                    }
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    Log.d("Connecion", "Retrieving weather from: " + url);
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } finally {
                        urlConnection.disconnect();
                    }
                } else {
                    return null;
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

                calculateRisk(Double.parseDouble(temperature), humidity, tvCurrentRisk);

                dataTime = (JSONArray) object.getJSONArray("hourly_forecast");

                setData();


            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
            }

        }
    }


    //MARK - API Implementation for AutoComplete
    class RetrieveAutoComplete extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            Location = etLocation.getText().toString();
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here
            Location = Location.replaceAll("\\s+", "%20");
            Log.d("Conexion","String transformed: " + Location);

            //http://autocomplete.wunderground.com/aq?query=query
            try {
                if (internet){
                    URL url = new URL("http://autocomplete.wunderground.com/aq?query=" + Location);
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
                } else {
                    return null;
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

                JSONArray results = object.getJSONArray("RESULTS");

                //Arrays.fill( Cities, results.length() );
                Cities = new String[results.length()];

                for (int i = 0; i< results.length(); i++){
                    JSONObject CityObject = results.getJSONObject(i);
                    String city = CityObject.getString("name");
                    Cities[i] = city;
                }

                //Update Adapter
                adapterAutoComplete = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_dropdown_item_1line, Cities);

                etLocation.setAdapter(adapterAutoComplete);
                adapterAutoComplete.notifyDataSetChanged();

            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
            }

        }
    }

    //MARK: LOCATION
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(300000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        lLat = location.getLatitude();
        lLong = location.getLongitude();
        Log.d("MainAct",String.valueOf(lLat)+String.valueOf(lLong));
        //new RetrieveLocation().execute();
    }

    class RetrieveLocation extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lLat+","+lLong+"&key=AIzaSyClNTvnFptk7EH8p1RynvJ1km0a6MyFLdA");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("Connecion","Location from: " + url);
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
                JSONArray results = object.getJSONArray("results");
                JSONObject object1 = results.getJSONObject(0);
                JSONArray address_components = object1.getJSONArray("address_components");
                JSONObject object2;

                boolean bstate=false,bcity=false, bcountry = false;
                for(int i=0; i<address_components.length(); i++){
                    object2 = address_components.getJSONObject(i);
                    String type = object2.getString("types");
                    if(type.contains("administrative_area_level_1")){
                        sState=object2.getString("long_name");
                        Log.d("STATE",sState);
                        bstate=true;
                    }
                    if(type.contains("administrative_area_level_2")){
                        sCity=object2.getString("long_name");
                        Log.d("CITY",sCity);
                        bcity=true;
                    }
                    if(type.contains("country")){
                        country=object2.getString("long_name");
                        Log.d("COUNTRY",country);
                        bcountry = true;
                    }
                }
                if(bcity && bstate && bcountry){
                    etLocation.setText(sCity+", "+sState);
                }
            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
            }

        }
    }

}
