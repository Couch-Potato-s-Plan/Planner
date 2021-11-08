package com.example.couchpotatosplan.myday;

import android.widget.CheckBox;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class MyDayEventList {
    public static ArrayList<MyDayEvent> eventsList = new ArrayList<>();

//    public static ArrayList<MyDayEvent> eventsForCheck(boolean check){
//        ArrayList<MyDayEvent> events = new ArrayList<>();
//        for(MyDayEvent event : eventsList){
//            if(event.getCheck().equals(check))
//                events.add(event);
//        }
//
//    }
    public static ArrayList<MyDayEvent> eventsForDate(String date)
    {
        ArrayList<MyDayEvent> events = new ArrayList<>();

        for(MyDayEvent event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }


}
