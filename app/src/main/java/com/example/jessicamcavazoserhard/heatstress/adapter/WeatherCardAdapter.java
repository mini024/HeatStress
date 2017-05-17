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

package com.example.jessicamcavazoserhard.heatstress.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessicamcavazoserhard.heatstress.R;
import com.example.jessicamcavazoserhard.heatstress.model.WeatherCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sabba on 3/19/2017.
 */

public class WeatherCardAdapter extends RecyclerView.Adapter<WeatherCardAdapter.WeatherCardHolder>{

    private List<WeatherCard> listData;
    private LayoutInflater inflater;

    int card_green, card_red, card_orange, card_yellow;

    public WeatherCardAdapter(List<WeatherCard> listData, Context c){
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public WeatherCardAdapter.WeatherCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hourly_wether_card, parent, false);
        return new WeatherCardHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherCardHolder holder, int position) {
        WeatherCard item = listData.get(position);
        holder.hour.setText(item.getsHour());
        holder.temp.setText(String.valueOf(item.getiTemperature())+" °F");
        holder.hum.setText(String.valueOf(item.getiHumidity())+" %");
        switch (item.getsWeather()){
            case "Rainy": holder.weather_icon.setImageResource(R.drawable.rain);
                break;
            case "Clear": holder.weather_icon.setImageResource(R.drawable.sun);
                break;
            case "Partly Cloudy": holder.weather_icon.setImageResource(R.drawable.sun_cloud);
                break;
            case "Mostly Cloudy": holder.weather_icon.setImageResource(R.drawable.sun_cloud);
                break;
            case "Cloudy": holder.weather_icon.setImageResource(R.drawable.cloud);
                break;
            case "Windy": holder.weather_icon.setImageResource(R.drawable.wind);
                break;
            case "Snowy": holder.weather_icon.setImageResource(R.drawable.snow);
                break;
        }

        switch (item.getColor()){
            case "red":
                holder.card.setBackgroundColor(card_red);
                break;
            case "orange":
                holder.card.setBackgroundColor(card_orange);
                break;
            case "yellow":
                holder.card.setBackgroundColor(card_yellow);
                break;
            default:
                holder.card.setBackgroundColor(card_green);
                break;
        }

        holder.temp_icon.setImageResource(R.drawable.temp);
        holder.hum_icon.setImageResource(R.drawable.drops);
    }

    public void setListData(ArrayList<WeatherCard> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class WeatherCardHolder extends RecyclerView.ViewHolder{

        ImageView weather_icon, temp_icon, hum_icon;
        TextView hour, temp, hum;
        View container;
        RelativeLayout card;

        public WeatherCardHolder(View itemView) {
            super(itemView);
            weather_icon = (ImageView)itemView.findViewById(R.id.iv_weather_card);
            temp_icon = (ImageView)itemView.findViewById(R.id.iv_temp_card);
            hum_icon = (ImageView)itemView.findViewById(R.id.iv_hum_card);

            hour = (TextView)itemView.findViewById(R.id.tv_hour_card);
            temp = (TextView) itemView.findViewById(R.id.tv_temp_card);
            hum = (TextView) itemView.findViewById(R.id.tv_hum_card);

            card = (RelativeLayout)itemView.findViewById(R.id.card);

            container = (View)itemView.findViewById(R.id.cont_item_root);

            card_green = Color.parseColor("#5CB04F");
            card_orange = Color.parseColor("#F59323");
            card_red = Color.parseColor("#E35349");
            card_yellow = Color.parseColor("#FED849");

        }

    }

}
