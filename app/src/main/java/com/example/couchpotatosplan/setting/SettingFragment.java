package com.example.couchpotatosplan.setting;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.app.AlarmManager;
import com.example.couchpotatosplan.utils.Alarm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.couchpotatosplan.MainActivity;
import com.example.couchpotatosplan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;


//설정화면..
public class SettingFragment extends Fragment {

    private Button theme_btn;
    private BottomNavigationView bottomNavigation;
    private AlarmManager alarmManager;
    private Switch alarm_switch;
    private Alarm alarm;
    private TimePickerDialog dialog;
    private DatabaseReference mDatabase;
    private Button custom_btn;
    private String theme_num = "0";
    private boolean isAlarmSet = false;
    private View bar;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        theme_btn = view.findViewById(R.id.theme_btn);
        bottomNavigation = view.findViewById(R.id.bottomNavi);
        alarm_switch = view.findViewById(R.id.alarm_switch);
        custom_btn = view.findViewById(R.id.custom_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bar = view.findViewById(R.id.view4);

        addEventAction();
        return view;
    }

    private void addEventAction() {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    if(snapshot.child("theme").getValue() != null) {
                        theme_num = snapshot.child("theme").getValue().toString();
                    }
                    if(snapshot.child("alarm").getValue() != null) {
                        alarm = snapshot.child("alarm").getValue(Alarm.class);
                        if(alarm.isChecked()) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                            calendar.set(Calendar.MINUTE, alarm.getMinute());
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);

                            if(calendar.before(Calendar.getInstance())) {
                                calendar.add(Calendar.DATE, 1);
                            }

                            intent = new Intent(getContext(), Alarm.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                        if (alarm.isChecked()) {
                            alarm_switch.setChecked(true);
                            custom_btn.setEnabled(true);
                            custom_btn.setTextColor(Color.BLACK);
                            alarm.setChecked(true);
                            mDatabase.child("alarm").setValue(alarm);
                        } else {
                            alarm_switch.setChecked(false);
                            custom_btn.setEnabled(false);
                            custom_btn.setTextColor(Color.GRAY);
                            alarm.setChecked(false);
                            mDatabase.child("alarm").setValue(alarm);
                            if(isAlarmSet) {
                                destroyNotification();
                                unregist();
                            }
                            isAlarmSet = false;
                        }
                    } else {
                        alarm = new Alarm(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), false);
                    }

                    int theme = Integer.parseInt(theme_num);
                    switch (theme) {
                        case 0:
                            bar.setBackgroundColor(Color.rgb(143, 186, 216));
                            break;
                        case 1:
                            bar.setBackgroundColor(Color.rgb(255, 211, 26));
                            break;
                        case 2:
                            bar.setBackgroundColor(Color.rgb(234, 102, 118));
                            break;
                        case 3:
                            bar.setBackgroundColor(Color.rgb(248, 213, 224));
                            break;
                        case 4:
                            bar.setBackgroundColor(Color.rgb(102, 100, 139));
                            break;
                        case 5:
                            bar.setBackgroundColor(Color.rgb(48, 52, 63));
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        theme_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
                dlg.setTitle("테마");

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.changeTheme(theme_num);
                        mDatabase.child("theme").setValue(theme_num);
                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final String[] Theme = new String[]{"Basic", "Yellow", "Coral", "BabyPink", "Purple", "Midnight"};

                dlg.setSingleChoiceItems(Theme, Integer.parseInt(theme_num), new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (Theme[which]) {
                            case "Basic":
                                theme_num = "0";
                                break;
                            case "Yellow":
                                theme_num = "1";
                                break;
                            case "Coral":
                                theme_num = "2";
                                break;
                            case "BabyPink":
                                theme_num = "3";
                                break;
                            case "Purple":
                                theme_num = "4";
                                break;
                            case "Midnight":
                                theme_num = "5";
                                break;
                        }
                    }
                });
                dlg.show();
            }
        });

        alarm_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!alarm.isChecked()) {
                    custom_btn.setEnabled(true);
                    custom_btn.setTextColor(Color.BLACK);
                    alarm.setChecked(true);
                    mDatabase.child("alarm").setValue(alarm);
                } else {
                    custom_btn.setEnabled(false);
                    custom_btn.setTextColor(Color.GRAY);
                    alarm.setChecked(false);
                    mDatabase.child("alarm").setValue(alarm);
                    if(isAlarmSet) {
                        destroyNotification();
                        unregist();
                    }
                    isAlarmSet = false;
                }
            }
        });

        custom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, listener, alarm.getHour(), alarm.getMinute(), false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setTitle("알람 시간 설정");
                dialog.show();
            }
        });

    }

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            isAlarmSet = true;
            alarm = new Alarm(hourOfDay, minute, alarm.isChecked());

            mDatabase.child("alarm").setValue(alarm);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if(calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }

            intent = new Intent(getContext(), Alarm.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    };


    public void unregist() {
        intent = new Intent(getContext(), Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    void destroyNotification() {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


}