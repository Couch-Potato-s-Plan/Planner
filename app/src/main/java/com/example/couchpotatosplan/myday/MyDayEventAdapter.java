package com.example.couchpotatosplan.myday;

import android.content.Context;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.couchpotatosplan.R;

import java.util.List;

public class MyDayEventAdapter extends ArrayAdapter<MyDayEvent> {
    public MyDayEventAdapter(@NonNull Context context, List<MyDayEvent> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent)
    {
        MyDayEvent event = getItem(position);

        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.myday_event_cell, parent, false);

        TextView time_tv = view.findViewById(R.id.time_tv);
        TextView content_tv = view.findViewById(R.id.content_tv);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        String time = Integer.toString(event.getTime()) + ":00";
        String content = event.getContent();
        Boolean check = event.getCheck();

//        checkBox.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if(check == true) {
//                    checkBox.setChecked(false);
//                } else{
//                    checkBox.setChecked(true);
//                }
//            }
//        });

        time_tv.setText(time);
        content_tv.setText(content);
        checkBox.setChecked(check);

        return view;
    }


}
