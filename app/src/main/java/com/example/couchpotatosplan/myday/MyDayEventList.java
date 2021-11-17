package com.example.couchpotatosplan.myday;

import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.couchpotatosplan.weekly.CalendarUtils;
import com.example.couchpotatosplan.weekly.WeeklyEventAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@IgnoreExtraProperties
public class MyDayEventList {
    public static ArrayList<MyDayEvent> eventsList = new ArrayList<>();
    private static DatabaseReference mDatabase;
    public static boolean isTimeTableFooled()
    {
        boolean p = false;
        ArrayList<MyDayEvent> events = new ArrayList<>();
        for(MyDayEvent event : eventsList)
        {
            if(event.getDate().equals(formattedDate(LocalDate.now())))
                events.add(event);
        }
        for(int i = 0 ; i < 24 ; i ++)
        {
            p = false;
            for(MyDayEvent event : events)
            {
                if(event.isPiled(i))
                {
                    p = true;
                }
            }
            if(!p)
            {
                return false;
            }
        }
        return true;
    }

    public static void reroll(String date) {
/*
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("event").child("1").child("date").setValue("노민솔 다이어트해");
*/
        for(MyDayEvent event : eventsList)
        {
            Log.d("MyLog", date);
            if(event.getDate().equals(date)) {
                Log.d("MyLog", "here");
                Log.d("MyLog", event.getTime() + event.getContent());
            }
        }

        ArrayList<MyDayEvent> events = new ArrayList<>();
        for(MyDayEvent event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }
        if(events.isEmpty())
        {
            return;
        }
        ArrayList<MyDayEvent> newevents = new ArrayList<>();
        for(MyDayEvent event : events)
        {
            int startTime = 0;
            boolean ok = true;
            if(newevents.isEmpty())
            {
                startTime = FragmentDialog.RandomNum();
            }
            else {
                while (ok) {
                    ok = false;
                    startTime = FragmentDialog.RandomNum();
                    for (MyDayEvent e : newevents) {
                        if (e.isPiled(startTime)) {
                            ok = true;
                        }
                    }
                }
            }
            event.setTime(startTime);
            newevents.add(event);
        }
        return;
    }
    public static ArrayList<MyDayEvent> eventsForDate(String date)
    {
        ArrayList<MyDayEvent> events = new ArrayList<>();

        for(MyDayEvent event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }
        Collections.sort(events, new Comparator<MyDayEvent>() {
            @Override
            public int compare(MyDayEvent o1, MyDayEvent o2) {
                if( o1.getTime() > o2.getTime())
                {
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        return events;
    }

    public static void sort()
    {}

}

