package com.forecast.weather.weatherforecast.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forecast.weather.weatherforecast.R;
import com.forecast.weather.weatherforecast.models.Weather;
import com.forecast.weather.weatherforecast.models.WeatherViewHolder;
import com.forecast.weather.weatherforecast.utils.UnitConvertor;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;



public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    private List<Weather> itemList;
    private Context context;

    public WeatherRecyclerAdapter(Context context, List<Weather> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder customViewHolder, int i) {
        Weather weatherItem = itemList.get(i);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        // Temperature
        float temperature = UnitConvertor.convertTemperature(Float.parseFloat(weatherItem.getTemperature()), sp);
        if (sp.getBoolean("temperatureInteger", false)) {
            temperature = Math.round(temperature);
        }


        TimeZone tz = TimeZone.getDefault();
        String defaultDateFormat = context.getResources().getStringArray(R.array.dateFormatsValues)[0];
        String dateFormat = sp.getString("dateFormat", defaultDateFormat);
        if ("custom".equals(dateFormat)) {
            dateFormat = sp.getString("dateFormatCustom", defaultDateFormat);
        }
        String dateString;
        try {
            SimpleDateFormat resultFormat = new SimpleDateFormat(dateFormat);
            resultFormat.setTimeZone(tz);
            dateString = resultFormat.format(weatherItem.getDate());
        } catch (IllegalArgumentException e) {
            dateString = context.getResources().getString(R.string.error_dateFormat);
        }

        if (sp.getBoolean("differentiateDaysByTint", false)) {
            Date now = new Date();
            /* Unfortunately, the getColor() that takes a theme (the next commented line) is Android 6.0 only, so we have to do it manually
             * customViewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.attr.colorTintedBackground, context.getTheme())); */
            int color;
            if (weatherItem.getNumDaysFrom(now) > 1) {
                TypedArray ta = context.obtainStyledAttributes(new int[]{R.color.colorTintedBackground, R.color.colorTintedBackground});
                if (weatherItem.getNumDaysFrom(now) % 2 == 1) {
                    color = ta.getColor(0, context.getResources().getColor(R.color.colorTintedBackground));
                } else {
                    /* We must explicitly set things back, because RecyclerView seems to reuse views and
                     * without restoring back the "normal" color, just about everything gets tinted if we
                     * scroll a couple of times! */
                    color = ta.getColor(1, context.getResources().getColor(R.color.colorBackground));
                }
                ta.recycle();
                customViewHolder.itemView.setBackgroundColor(color);
            }
        }

        customViewHolder.itemDate.setText(dateString);
        if (sp.getBoolean("displayDecimalZeroes", false)) {
            customViewHolder.itemTemperature.setText(new DecimalFormat("#.0").format(temperature) + " " + sp.getString("unit", "°C"));
        } else {
            customViewHolder.itemTemperature.setText(new DecimalFormat("#.#").format(temperature) + " " + sp.getString("unit", "°C"));
        }
        }

    @Override
    public int getItemCount() {
        return (null != itemList ? itemList.size() : 0);
    }
}
