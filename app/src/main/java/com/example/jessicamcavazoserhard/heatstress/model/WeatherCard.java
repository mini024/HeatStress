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
