package com.example.couchpotatosplan.weekly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.myday.MyDayEvent;

import java.util.List;

public class WeeklyEventAdapter extends ArrayAdapter<MyDayEvent> {
    public WeeklyEventAdapter(@NonNull Context context, List<MyDayEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        MyDayEvent event = getItem(position);

        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView time_tv = view.findViewById(R.id.time_tv);
        TextView content_tv = view.findViewById(R.id.content_tv);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        String time = Integer.toString(event.getTime()) + ":00";
        String content = event.getContent();
        Boolean check = event.getCheck();

        time_tv.setText(time);
        content_tv.setText(content);
        checkBox.setChecked(check);

        return view;
    }


}
