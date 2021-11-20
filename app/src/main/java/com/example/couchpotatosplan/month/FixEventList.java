package com.example.couchpotatosplan.month;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FixEventList {

    public static ArrayList<FixEvent> fixeventsList = new ArrayList<>();

    public static ArrayList<FixEvent> eventsForDate(String date)
    {
        ArrayList<FixEvent> events = new ArrayList<>();

        for(FixEvent event : fixeventsList)
        {
            events.add(event);
        }

        return events;
    }

}
