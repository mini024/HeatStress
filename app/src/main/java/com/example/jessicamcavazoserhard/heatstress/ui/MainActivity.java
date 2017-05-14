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
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    boolean internet;
    int sbprogress;
    int RiskType;
    View FilterView;

    final double c1=16.923,c2=0.185212,c3=5.37941,c4=-0.100254,c5=0.00941695,c6=0.00728898,c7=0.000345372,c8=-0.000814971,c9=0.0000102102,c10=-0.000038646,c11=0.0000291583,c12=0.00000142721,c13=0.000000197483,c14=-0.0000000218429,c15=0.000000000843296,c16=-0.0000000000481975;

    int colorSunny, colorCloudy, colorRainy, colorWindy, colorSnowy, colorPCloudy;

    //Max temps and humidity
    PriorityQueue<Integer> pqRisk;
    Map mapTemps, mapHums, mapType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //MARK: CREATE COLORS
        colorSunny = Color.parseColor("#5bE1BB22");
        colorRainy = Color.parseColor("#5b528DD4");
        colorCloudy = Color.parseColor("#5b6D6C6A");
        colorWindy = Color.parseColor("#5bDA7736");
        colorSnowy = Color.parseColor("#FFFFFF");
        colorPCloudy = Color.parseColor("#5b749BCA");


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
        FilterView = findViewById(R.id.filterView);
        btGo.setOnClickListener(this);

        btShowLocation = (ImageButton) findViewById(R.id.imageButton_showLocation);
        btShowLocation.setOnClickListener(this);

        tvLocation = (TextView) findViewById(R.id.textView_location);

        isInternetAvailable();

        cbCurrentLocation = (CheckBox) findViewById(R.id.checkBox2);
        cbCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && isInternetAvailable()){
                    new RetrieveLocation().execute();
                } else {
                    internet = false;
                    new AlertDialog.Builder(MainActivity.this).setTitle("Internet Connection").setMessage("Please check your internet connection").setNeutralButton("Close", null).show();
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
                    i.setData(Uri.parse("tel:8180200922"));
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
                i.putExtra("Risk",RiskType);
                startActivity(i);
                break;
            case R.id.imageButton_showLocation:
                if (isInternetAvailable()){
                    ShowLocation();
                } else {
                    internet = false;
                    new AlertDialog.Builder(MainActivity.this).setTitle("Internet Connection").setMessage("Please check your internet connection").setNeutralButton("Close", null).show();
                }
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
                    if (isInternetAvailable()){
                        //MARK: Get Weather
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(etLocation.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        new RetrieveWeatherForLocation().execute();
                        HideLocation();
                    } else {
                        new AlertDialog.Builder(MainActivity.this).setTitle("Internet Connection").setMessage("Please check your internet connection").setNeutralButton("Close", null).show();
                    }
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
        WeatherCard dummy = new WeatherCard("11:00 AM"," ",0,0,"green");
        dummyData.add(dummy);
        dummy = new WeatherCard("12:00 PM"," ",0,0,"green");
        dummyData.add(dummy);
        dummy = new WeatherCard("1:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("2:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("3:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("4:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("5:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("6:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("7:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("8:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("9:00 PM"," ",0,0, "green");
        dummyData.add(dummy);
        dummy = new WeatherCard("10:00 PM"," ",0,0, "green");
        dummyData.add(dummy);

        return dummyData ;
    }


    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if( netInfo != null && netInfo.isConnectedOrConnecting()){
            internet=true;
            return true;
        } else {
            internet=false;
            return false;
        }
    }

    double getRisk (double temperature, String humidity){

        humidity = humidity.replaceAll("%", "");
        int Rhumidity = Integer.parseInt(humidity);

        double heatIndex = c1 + c2*temperature +c3*Rhumidity + c4*temperature*Rhumidity + c5*(Math.pow(temperature,2)) + c6*(Math.pow(Rhumidity,2)) + c7*(Math.pow(temperature,2))*Rhumidity + c8*temperature*(Math.pow(Rhumidity,2)) + c9*(Math.pow(temperature,2))*(Math.pow(Rhumidity,2)) + c10*(Math.pow(temperature,3)) + c11*(Math.pow(Rhumidity,3)) + c12*(Math.pow(temperature,3))*Rhumidity + c13*temperature*(Math.pow(Rhumidity,3)) + c14*(Math.pow(temperature,3))*(Math.pow(Rhumidity,2)) + c15*(Math.pow(temperature,2))*(Math.pow(Rhumidity,3)) + c15*(Math.pow(temperature,3))*(Math.pow(Rhumidity,3));
        return heatIndex;
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
        mapHums=new HashMap();
        mapTemps=new HashMap();
        mapType = new HashMap();
        pqRisk=new PriorityQueue<Integer>(12,pqs);
        //PriorityQueue pqRisk = new PriorityQueue<Double>(12,pqs);

        int temp,hum;
        for (int i = 0; i< 12; i++){
            //Add temperature and humidity to its respective priority queues
            temp=getJSONInt("english", i, "temp");
            hum=getJSONInt("humidity", i, " ");
            double heatIndex = getRisk(temp,Integer.toString(hum));
            Log.d("RISK",String.valueOf(i)+" "+String.valueOf((int)heatIndex));

            mapHums.put((int)heatIndex,String.valueOf(hum));
            mapTemps.put((int)heatIndex,String.valueOf(temp));
            pqRisk.add((int)heatIndex);

            if (temp <=80 ||  heatIndex <=90){
                WeatherCard dummy = new WeatherCard(getJSONString("hour", i, "FCTTIME") + ":00", getJSONString("condition", i, " "), temp, hum, "green");
                listData.add(dummy);
                mapType.put((int)heatIndex,1);
                Log.d("TYPE_C",String.valueOf((int)heatIndex)+" 1");
            }else if (heatIndex >= 91 && heatIndex <=103){
                WeatherCard dummy = new WeatherCard(getJSONString("hour", i, "FCTTIME") + ":00", getJSONString("condition", i, " "), temp, hum, "yellow");
                listData.add(dummy);
                mapType.put((int)heatIndex,2);
                Log.d("TYPE_C",String.valueOf((int)heatIndex)+" 2");
            } else if (heatIndex >= 104 && heatIndex <=125){
                WeatherCard dummy = new WeatherCard(getJSONString("hour", i, "FCTTIME") + ":00", getJSONString("condition", i, " "), temp, hum, "orange");
                listData.add(dummy);
                mapType.put((int)heatIndex,3);
                Log.d("TYPE_C",String.valueOf((int)heatIndex)+" 3");
            }else {
                WeatherCard dummy = new WeatherCard(getJSONString("hour", i, "FCTTIME") + ":00", getJSONString("condition", i, " "), temp, hum, "red");
                listData.add(dummy);
                mapType.put((int)heatIndex,4);
                Log.d("TYPE_C",String.valueOf((int)heatIndex)+" 4");
            }
        }

        adapter.notifyDataSetChanged();
        setMaxs();
    }

    private void setMaxs() {

        //double heatIndex = getRisk(Double.parseDouble(String.valueOf(pqTemp.peek())),String.valueOf(pqHum.peek()+"%"));

        int heatIndex = getMaxIndex();
        Log.d("HEATINDEX",String.valueOf(heatIndex));
        tvMaxTemp.setText(mapTemps.get(heatIndex) + "°F");
        tvMaxHumidity.setText(mapHums.get(heatIndex) + "%");
        if ((int)mapType.get(heatIndex)==1){
            Log.d("RISK","MIN");
            tvMaxRisk.setText("Minimal Risk");

        } else if ((int)mapType.get(heatIndex)==2) {
            Log.d("RISK","MED");
            tvMaxRisk.setText("Medium Risk");

        }else if ((int)mapType.get(heatIndex)==3){
            Log.d("RISK","HIGH");
            tvMaxRisk.setText("High Risk");

        }else if ((int)mapType.get(heatIndex)==4){
            Log.d("RISK","EXT");
            tvMaxRisk.setText("Extreme Risk");

        }
    }

    private int getMaxIndex() {
        int iMaxType=0;
        int iMaxIndex=0;

        for(int c=0; c<12; c++){
            Log.d("MAX_TYPS",String.valueOf((int)mapType.get(pqRisk.peek())));
            if ((int)mapType.get(pqRisk.peek())>iMaxType){
                iMaxType=(int)mapType.get(pqRisk.peek());
                iMaxIndex=pqRisk.poll();
                Log.d("MAX_I",String.valueOf(iMaxIndex));
                Log.d("MAX_T",String.valueOf(iMaxType));
            }else{
                pqRisk.poll();
            }
        }
        return iMaxIndex;
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
            state = state.replaceAll(" ", "_");
            state = state.replaceAll("\\s+", "_");
            Location = Location.replaceAll("\\s+", "_");
            Location = Location.replaceAll(" ", "_");

            Log.d("Conexion","String transformed: " + Location);

            try {
                if (internet) {
                    URL url;
                    if (states.get(state) != null) {
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/" + states.get(state) + "/" + Location + ".json");
                    } else if (Location.equals("Monterrey")){
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/25.87,-100.20.json");
                    }else {
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
                new AlertDialog.Builder(MainActivity.this).setTitle("Location Error").setMessage(e.getMessage()).setNeutralButton("Close", null).show();

                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);

            try {
                if (internet){
                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                    if(!object.has("current_observation")){
                        Toast.makeText(getApplicationContext(), "No information available \n for this location", Toast.LENGTH_SHORT).show();
                    } else{
                        humidity = object.getJSONObject("current_observation").getString("relative_humidity");
                    }

                    Log.d("Query", object.toString());

                    temperature = object.getJSONObject("current_observation").getString("temp_f");
                    Log.d("Humedad","HUMEDAD EN SAN FRANCISCO : " + humidity);

                    String weather = object.getJSONObject("current_observation").getString("weather");

                    switch (weather){
                        case "Rainy": FilterView.setBackgroundColor(colorRainy);
                            break;
                        case "Clear": FilterView.setBackgroundColor(colorSunny);
                            break;
                        case "Partly Cloudy": FilterView.setBackgroundColor(colorPCloudy);
                            break;
                        case "Mostly Cloudy": FilterView.setBackgroundColor(colorCloudy);
                            break;
                        case "Cloudy": FilterView.setBackgroundColor(colorCloudy);
                            break;
                        case "Windy": FilterView.setBackgroundColor(colorWindy);
                            break;
                        case "Snowy": FilterView.setBackgroundColor(colorSnowy);
                            break;
                    }

                    tvCurrentTemperature.setText(temperature + " ºF");

                    tvCurrentHumidity.setText(humidity);

                    double itemperature = Double.parseDouble(temperature);
                    double heatIndex = getRisk(Double.parseDouble(temperature), humidity);

                    if (itemperature <=80 ||  heatIndex <=90){
                        btGo.setImageResource(R.drawable.minimal_risk);
                        tvCurrentRisk.setText("Minimal Risk");
                        RiskType =1;
                    } else if (heatIndex >= 91 && heatIndex <=103) {
                        btGo.setImageResource(R.drawable.medium_risk);
                        tvCurrentRisk.setText("Medium Risk");
                        RiskType = 2;
                    }else if (heatIndex >= 104 && heatIndex <=125){
                        btGo.setImageResource(R.drawable.high_risk);
                        tvCurrentRisk.setText("High Risk");
                        RiskType =3;
                    }else if (heatIndex >= 126){
                        btGo.setImageResource(R.drawable.very_high_risk);
                        tvCurrentRisk.setText("Extreme Risk");
                        RiskType=4;
                    }

                    dataTime = (JSONArray) object.getJSONArray("hourly_forecast");

                    setData();
                }

            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
                new AlertDialog.Builder(MainActivity.this).setTitle("Location Error").setMessage(e.getMessage()).setNeutralButton("Close", null).show();
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
            Location = Location.replaceAll(" ", "%20");
            Log.d("Conexion","String transformed: " + Location);

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
                new AlertDialog.Builder(MainActivity.this).setTitle("Autocomplete Error").setMessage(e.getMessage()).setNeutralButton("Close", null).show();
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);

            try {
                if (internet) {
                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                    JSONArray results = object.getJSONArray("RESULTS");

                    //Arrays.fill( Cities, results.length() );
                    Cities = new String[results.length()];

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject CityObject = results.getJSONObject(i);
                        String city = CityObject.getString("name");
                        Cities[i] = city;
                    }

                    //Update Adapter
                    adapterAutoComplete = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_dropdown_item_1line, Cities);

                    etLocation.setAdapter(adapterAutoComplete);
                    adapterAutoComplete.notifyDataSetChanged();
                }

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
                if (internet){
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
                if (internet){
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
                            sState=object2.getString("short_name");
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
                        etLocation.setText(sCity+", "+country);
                    }
                }
            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
            }

        }
    }

}
