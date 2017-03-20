package com.example.jessicamcavazoserhard.heatstress.model;

/**
 * Created by Sabba on 3/19/2017.
 */

public class WeatherCard {

    private String sHour, sWeather;
    private int iTemperature, iHumidity;

    public WeatherCard(){
        this.sHour="";
        this.sWeather="";
        this.iTemperature=0;
        this.iHumidity=0;
    }

    public WeatherCard (String sHour, String sWeather, int iTemperature, int iHumidity){
        this.sHour=sHour;
        this.sWeather=sWeather;
        this.iTemperature=iTemperature;
        this.iHumidity=iHumidity;
    }

    public String getsHour() {
        return sHour;
    }

    public void setsHour(String sHour) {
        this.sHour = sHour;
    }

    public String getsWeather() {
        return sWeather;
    }

    public void setsWeather(String sWeather) {
        this.sWeather = sWeather;
    }

    public int getiTemperature() {
        return iTemperature;
    }

    public void setiTemperature(int iTemperature) {
        this.iTemperature = iTemperature;
    }

    public int getiHumidity() {
        return iHumidity;
    }

    public void setiHumidity(int iHumidity) {
        this.iHumidity = iHumidity;
    }
}
