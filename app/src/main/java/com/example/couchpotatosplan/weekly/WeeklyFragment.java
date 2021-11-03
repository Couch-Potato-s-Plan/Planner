package com.example.couchpotatosplan.weekly;

import static com.example.couchpotatosplan.weekly.CalendarUtils.daysInWeekArray;
import static com.example.couchpotatosplan.weekly.CalendarUtils.formattedDate;
import static com.example.couchpotatosplan.weekly.CalendarUtils.monthDayFromDate;
import static com.example.couchpotatosplan.weekly.CalendarUtils.monthYearFromDate;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.myday.MyDayEventList;
import com.example.couchpotatosplan.myday.MyDayEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeeklyFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private TextView monthDayText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private DatePickerDialog dialog;
    private DatabaseReference mDatabase;
    private WeeklyEventAdapter adapter;
    private long postNum;

    Button previous_btn;
    Button next_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.weekly_fragment, container, false);

        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyDayEventList.eventsList.clear();
                if(snapshot.exists())
                    postNum = (snapshot.child("event").getChildrenCount());
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for(int i = 1; i <= postNum; i++) {
                        MyDayEvent post = dataSnapshot.child(String.valueOf(i)).getValue(MyDayEvent.class);
                        MyDayEventList.eventsList.add(post);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        previous_btn = (Button)view.findViewById(R.id.previous_btn);
        next_btn = (Button)view.findViewById(R.id.next_btn);

        previousWeekAction();
        nextWeekAction();

        CalendarUtils.selectedDate = LocalDate.now();
        initWidgets(view);
        setWeekView();

        return view;
    }

    private void initWidgets(View view)
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        monthDayText = view.findViewById(R.id.monthDayTV);
        eventListView = view.findViewById(R.id.eventListView);

        monthYearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DatePickerDialog(getContext(), R.style.MyDatePickerStyle, listener, CalendarUtils.selectedDate.getYear(), CalendarUtils.selectedDate.getMonth().getValue(), CalendarUtils.selectedDate.getDayOfMonth());
                dialog.show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            CalendarUtils.selectedDate = LocalDate.of(year, month, dayOfMonth);
            setWeekView();
        }
    };

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        monthDayText.setText(monthDayFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }


    public void previousWeekAction()
    {
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });
    }

    public void nextWeekAction()
    {
        next_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater()
    {
        ArrayList<MyDayEvent> dailyEvents = MyDayEventList.eventsForDate(formattedDate(CalendarUtils.selectedDate));
        adapter = new WeeklyEventAdapter(getActivity().getApplicationContext(), dailyEvents);
        eventListView.setAdapter(adapter);
    }
}