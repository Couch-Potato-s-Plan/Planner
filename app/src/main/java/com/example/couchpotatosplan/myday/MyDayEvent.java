package com.example.couchpotatosplan.myday;

import com.google.firebase.database.IgnoreExtraProperties;

import java.time.LocalDate;
import java.util.ArrayList;

@IgnoreExtraProperties
public class MyDayEvent {
    private String date;
    private int time;
    private String content;

    public MyDayEvent() {

    }

    public MyDayEvent(String date, int time, String content)
    {
        this.date = date;
        this.time = time;
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
