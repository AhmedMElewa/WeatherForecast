package com.forecast.weather.weatherforecast.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.forecast.weather.weatherforecast.R;


public class WeatherViewHolder extends RecyclerView.ViewHolder {
    public TextView itemDate;
    public TextView itemTemperature;
    public View lineView;

    public WeatherViewHolder(View view) {
        super(view);
        this.itemDate = (TextView) view.findViewById(R.id.itemDate);
        this.itemTemperature = (TextView) view.findViewById(R.id.itemTemperature);
        this.lineView = view.findViewById(R.id.lineView);
    }
}