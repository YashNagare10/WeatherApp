package com.example.weatherapp;

import static com.example.weatherapp.WeatherAppConstants.DATE_TIME_FORMAT;
import static com.example.weatherapp.WeatherAppConstants.DEFAULT_LOCALE;
import static com.example.weatherapp.WeatherAppConstants.DEGREE_CELCIUS;
import static com.example.weatherapp.WeatherAppConstants.PROTOCOL;
import static com.example.weatherapp.WeatherAppConstants.TIME_FORMAT;
import static com.example.weatherapp.WeatherAppConstants.WIND_SPEED_UNIT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<WeatherRecyclerViewModel> weatherRecyclerViewModelArrayList;

    public WeatherRecyclerViewAdapter(Context context, ArrayList<WeatherRecyclerViewModel> weatherRecyclerViewModelArrayList) {
        this.context = context;
        this.weatherRecyclerViewModelArrayList = weatherRecyclerViewModelArrayList;
    }

    @NonNull
    @Override
    public WeatherRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, /* attachToRoot= */ false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerViewAdapter.ViewHolder holder, int position) {
        WeatherRecyclerViewModel model = weatherRecyclerViewModelArrayList.get(position);
        holder.temperature.setText(model.getTemperature().concat(DEGREE_CELCIUS));
        Picasso.get().load(PROTOCOL.concat(model.getIcon())).into(holder.condition);
        holder.windSpeed.setText(model.getWindSpeed().concat(WIND_SPEED_UNIT));

        SimpleDateFormat input = new SimpleDateFormat(DATE_TIME_FORMAT, DEFAULT_LOCALE);
        SimpleDateFormat output = new SimpleDateFormat(TIME_FORMAT, DEFAULT_LOCALE);
        try {
            Date date = input.parse(model.getTime());
            if (date != null) {
                holder.time.setText(output.format(date));
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRecyclerViewModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView time;
        private final TextView temperature;
        private final TextView windSpeed;
        private final ImageView condition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textViewTime);
            temperature = itemView.findViewById(R.id.textViewTemperature);
            windSpeed = itemView.findViewById(R.id.textViewWindSpeed);
            condition = itemView.findViewById(R.id.imageViewCondition);
        }
    }
}
