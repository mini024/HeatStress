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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.TextView;
import com.example.jessicamcavazoserhard.heatstress.BlurBuilder;
import com.example.jessicamcavazoserhard.heatstress.Helper;
import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.TrackGPS;
import com.example.jessicamcavazoserhard.heatstress.adapter.WeatherCardAdapter;
import com.example.jessicamcavazoserhard.heatstress.model.WeatherCard;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, AdapterView.OnItemClickListener {

    //MARK - Elements of View
    ImageButton btGo;
    AutoCompleteTextView etLocation;
    TextView tvCurrentRisk;
    TextView tvCurrentHumidity;
    TextView tvCurrentTemperature;
    TextView tvMaxHumidity;
    TextView tvMaxTemp;
    TextView tvMaxRisk;
    View vMax;
    SeekBar sbCall;
    TextView tvLocation;
    CheckBox cbCurrentLocation;
    View FilterView;
    ImageButton btShowLocation;
    private RecyclerView recyclerView;
    private WeatherCardAdapter adapter;
    private ArrayList<WeatherCard> listData;
    ArrayAdapter<String> adapterAutoComplete;


    //MARK - Variables

    private TrackGPS gps;
    String Location;
    boolean internet;
    boolean Checked;
    int sbprogress;
    int taps = -1;
    int RiskType;
    private double lLat, lLong;
    String[] Cities = new String[]{
            "Monterrey, Mexico"
    };

    //MARK: Implementing helper
    Helper instance = Helper.getInstance();

    //Max temps and humidity
    PriorityQueue<Integer> pqRisk;
    Map mapTemps, mapHums, mapType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listData = getData();

        recyclerView = (RecyclerView) findViewById(R.id.rec_view_main);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WeatherCardAdapter(listData, this);
        recyclerView.setAdapter(adapter);

        btGo = (ImageButton) findViewById(R.id.imageButton_risk);
        btGo.setOnClickListener(this);

        tvCurrentHumidity = (TextView) findViewById(R.id.textView_humidityValue);
        tvCurrentTemperature = (TextView) findViewById(R.id.textView_temperatureValue);
        tvCurrentRisk = (TextView) findViewById(R.id.textView_risk);
        tvMaxHumidity = (TextView) findViewById(R.id.textView_humidityMax);
        tvMaxTemp = (TextView) findViewById(R.id.textView_temperatureMax);
        tvMaxRisk = (TextView) findViewById(R.id.tv_main_max_risk);
        vMax = findViewById(R.id.view_main_max);

        btShowLocation = (ImageButton) findViewById(R.id.imageButton_showLocation);
        btShowLocation.setOnClickListener(this);

        tvLocation = (TextView) findViewById(R.id.textView_location);

        isInternetAvailable();

        //MARK: Checkbox
        cbCurrentLocation = (CheckBox) findViewById(R.id.checkBox2);
        cbCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                taps = 0;
                Checked = isChecked;
                if(isChecked && isInternetAvailable()){
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etLocation.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    new RetrieveWeatherForLocation().execute();
                } else if (!isInternetAvailable()) {
                    internet = false;
                    new AlertDialog.Builder(MainActivity.this).setTitle("Internet Connection").setMessage("Please check your internet connection").setNeutralButton("Close", null).show();
                }
            }
        });

        //MARK:Location
        gps = new TrackGPS(MainActivity.this);
        lLong = gps.getLongitude();
        lLat = gps .getLatitude();

        //MARK: Blurry background
        FilterView = findViewById(R.id.activity_main);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap blurredBitmap = BlurBuilder.blur( MainActivity.this, icon);
        FilterView.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );

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

        //AutoComplete Text View
        adapterAutoComplete = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Cities);
        adapterAutoComplete.setNotifyOnChange(true);
        etLocation = (AutoCompleteTextView) findViewById(R.id.editText_address);
        etLocation.setAdapter(adapterAutoComplete);
        etLocation.setOnKeyListener(this);
        etLocation.setThreshold(4);
        etLocation.addTextChangedListener(textWatcher);
        etLocation.setOnItemClickListener(this);
        etLocation.setOnClickListener(this);
    }

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
                    //ShowLocation();
                } else {
                    internet = false;
                    new AlertDialog.Builder(MainActivity.this).setTitle("Internet Connection").setMessage("Please check your internet connection").setNeutralButton("Close", null).show();
                }
                break;
            case R.id.editText_address:
                taps = taps + 1;
                if (taps > 1){
                    etLocation.setText("");
                    taps = 0;
                } else {
                    etLocation.setCursorVisible(true);
                    Toast.makeText(getApplicationContext(), "Tap again on address to clear ", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etLocation.dismissDropDown();
        FilterView.findFocus();
        if (isInternetAvailable()){
            //MARK: Get Weather
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(etLocation.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            new RetrieveWeatherForLocation().execute();
            //HideLocation();
        } else {
            new AlertDialog.Builder(MainActivity.this).setTitle("Internet Connection").setMessage("Please check your internet connection").setNeutralButton("Close", null).show();
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

    //MARK: Detect enter to hide keyboard and get data.
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    taps = 0;
                    etLocation.dismissDropDown();
                    if (isInternetAvailable()){
                        //MARK: Get Weather
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(etLocation.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        new RetrieveWeatherForLocation().execute();
                        //HideLocation();
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

    //MARK: Text Watcher, detect if text changed.
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count ==  4 || start == 4) {
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
            cbCurrentLocation.setChecked(false);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            etLocation.setCursorVisible(false);
        }
    };

    //MARK: Get false Data while getting real data
    ArrayList<WeatherCard> getData(){
        ArrayList<WeatherCard> dummyData = new ArrayList<>();
        WeatherCard dummy = new WeatherCard("11:00 AM"," ",0,0,"green");
        dummyData.add(dummy);
        dummy = new WeatherCard("12:00 PM","Snowy",0,0,"green");
        dummyData.add(dummy);
        dummy = new WeatherCard("1:00 PM"," ",0,0, "yellow");
        dummyData.add(dummy);
        dummy = new WeatherCard("2:00 PM"," ",0,0, "orange");
        dummyData.add(dummy);
        dummy = new WeatherCard("3:00 PM"," ",0,0, "red");
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

    static class PQsort implements Comparator<Integer> {

        public int compare(Integer one, Integer two) {
            return two - one;
        }
    }

    //MARK: Get data of cards from JSON response
    void setData(JSONArray dataT){
        listData.clear();
        JSONObject obj;

        try {
            obj = (JSONObject) dataT.get(0);
            Log.d("Humedad","HUMEDAD EN SAN FRANCISCO : " + obj.getJSONObject("FCTTIME").getString("hour"));
        } catch  (JSONException e){
            Log.e("ERROR", e.getMessage(), e);
            Toast.makeText(getApplicationContext(), "Coundn't set data in cards, check connection", Toast.LENGTH_SHORT).show();
            return;
        }

        PQsort pqs = new PQsort();
        mapHums=new HashMap();
        mapTemps=new HashMap();
        mapType = new HashMap();
        pqRisk=new PriorityQueue<Integer>(12,pqs);

        int temp,hum;
        for (int i = 0; i< 12; i++){
            //Add temperature and humidity to its respective priority queues
            temp=instance.getJSONInt(obj, dataT, "english", i, "temp");
            hum=instance.getJSONInt(obj, dataT, "humidity", i, " ");
            double heatIndex = instance.getRisk(temp,Integer.toString(hum));
            Log.d("RISK",String.valueOf(i)+" "+String.valueOf((int)heatIndex));

            mapHums.put((int)heatIndex,String.valueOf(hum));
            mapTemps.put((int)heatIndex,String.valueOf(temp));
            pqRisk.add((int)heatIndex);

            if (temp <=80 ||  heatIndex <=90){
                WeatherCard dummy = new WeatherCard(instance.getJSONString(obj, dataT, "hour", i, "FCTTIME") + ":00", instance.getJSONString(obj, dataT,"condition", i, " "), temp, hum, "green");
                listData.add(dummy);
                mapType.put((int)heatIndex,1);
                Log.d("TYPE_C",String.valueOf((int)heatIndex)+" 1");
            }else if (heatIndex >= 91 && heatIndex <=103){
                WeatherCard dummy = new WeatherCard(instance.getJSONString(obj, dataT,"hour", i, "FCTTIME") + ":00", instance.getJSONString(obj, dataT,"condition", i, " "), temp, hum, "yellow");
                listData.add(dummy);
                mapType.put((int)heatIndex,2);
                Log.d("TYPE_C",String.valueOf((int)heatIndex)+" 2");
            } else if (heatIndex >= 104 && heatIndex <=125){
                WeatherCard dummy = new WeatherCard(instance.getJSONString(obj, dataT,"hour", i, "FCTTIME") + ":00",instance.getJSONString(obj, dataT,"condition", i, " "), temp, hum, "orange");
                listData.add(dummy);
                mapType.put((int)heatIndex,3);
                Log.d("TYPE_C",String.valueOf((int)heatIndex)+" 3");
            }else {
                WeatherCard dummy = new WeatherCard(instance.getJSONString(obj, dataT,"hour", i, "FCTTIME") + ":00", instance.getJSONString(obj, dataT,"condition", i, " "), temp, hum, "red");
                listData.add(dummy);
                mapType.put((int)heatIndex,4);
                Log.d("TYPE_C",String.valueOf((int)heatIndex)+" 4");
            }
        }

        adapter.notifyDataSetChanged();
        setMaxs();
    }

    private void setMaxs() {

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


    //MARK - API Implementation
    // IMPLEMENTATION ASYNCTASK FOR WEATHER API REQUEST
    // KEY - 25d0f02c485109f2
    class RetrieveWeatherForLocation extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            Location = etLocation.getText().toString();
        }

        protected String doInBackground(Void... urls) {

            Log.d("Conexion","String transformed: " + lLat + lLong);

            // Do some validation here
            String state="";
            if (!Checked) {
                if (Location.contains(",")){
                    state = Location.substring(Location.indexOf(","));
                } else {
                    state = " ";
                }

                Location = Location.replace(state, "");
                state = state.replace(", ", "");
                state = state.replaceAll(" ", "_");
                state = state.replaceAll("\\s+", "_");
                Location = Location.replaceAll("\\s+", "_");
                Location = Location.replaceAll(" ", "_");

                Log.d("Conexion", "String transformed: " + Location);
            }

            try {
                if (internet) {
                    URL url;
                    if (Checked){
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/"+ lLat +","+ lLong +".json");
                    } else if (instance.states.get(state) != null) {
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/" + instance.states.get(state) + "/" + Location + ".json");
                    } else if (Location.equals("Monterrey")){
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/25.87,-100.20.json");
                    } else {
                        url = new URL("http://api.wunderground.com/api/25d0f02c485109f2/conditions/hourly/q/" + state + "/" + Location + ".json");
                    }

                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setConnectTimeout(5000);
                        Log.d("Connection", "Retrieving weather from: " + url);
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } catch (Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                        new AlertDialog.Builder(MainActivity.this).setTitle("Location Error").setMessage("No information for location").setNeutralButton("Close", null).show();

                        return null;
                    } finally {
                        urlConnection.disconnect();
                    }
                } else {
                    return null;
                }
            }catch (java.net.SocketTimeoutException e){
                new AlertDialog.Builder(MainActivity.this).setTitle("Connection Error").setMessage("Check your network settings").setNeutralButton("Close", null).show();
                return null;
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                new AlertDialog.Builder(MainActivity.this).setTitle("Location Error").setMessage("No information for location").setNeutralButton("Close", null).show();
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);

            try {
                String humidity;
                String temperature;
                String currentLocation;
                if (internet){
                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                    if(object.has("error")){
                        new AlertDialog.Builder(MainActivity.this).setTitle("Location Error").setMessage("No information for location").setNeutralButton("Close", null).show();
                        return;
                    } else{
                        if (Checked){
                            Location = object.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
                            etLocation.setText(Location);
                            etLocation.dismissDropDown();
                            tvLocation.setText(Location);
                            //HideLocation();
                        }
                        humidity = object.getJSONObject("current_observation").getString("relative_humidity");
                        temperature = object.getJSONObject("current_observation").getString("temp_f");
                        if (temperature.contains(".")){
                            temperature = temperature.substring(0,temperature.indexOf("."));
                        }
                    }

                    tvCurrentTemperature.setText(temperature + " ºF");
                    tvCurrentHumidity.setText(humidity);

                    double itemperature = Double.parseDouble(temperature);
                    double heatIndex = instance.getRisk(Double.parseDouble(temperature), humidity);

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

                    JSONArray dataTi = object.getJSONArray("hourly_forecast");
                    setData(dataTi);
                }

            } catch (JSONException e){
                Log.e("ERROR", e.getMessage(), e);
                new AlertDialog.Builder(MainActivity.this).setTitle("Location Error").setMessage("No information for location").setNeutralButton("Close", null).show();
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
            // Do some validation her
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
                //new AlertDialog.Builder(MainActivity.this).setTitle("Autocomplete Error").setMessage(e.getMessage()).setNeutralButton("Close", null).show();
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
}
