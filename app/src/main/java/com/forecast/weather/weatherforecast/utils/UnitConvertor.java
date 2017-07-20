package com.forecast.weather.weatherforecast.utils;

import android.content.SharedPreferences;

import java.util.Locale;

public class UnitConvertor {
    public static float convertTemperature(float temperature, SharedPreferences sp) {
            return UnitConvertor.kelvinToCelsius(temperature);
    }

    public static float kelvinToCelsius(float kelvinTemp) {
        return kelvinTemp - 273.15f;
    }





}
