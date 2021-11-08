package com.example.couchpotatosplan.setting;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.couchpotatosplan.R;
import com.example.couchpotatosplan.myday.FragmentDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Calendar;


public class SettingAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private Button theme_btn;
    private BottomNavigationView bottomNavigation;
    private Button switch_btn;
    private Button custom_btn;
    private Switch alarm_btn;
    private Boolean on_click = false;    //alarm_btn이 on 인 상태인지 off인 상태인지

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_fragment);

        theme_btn = findViewById(R.id.theme_btn);
        bottomNavigation = findViewById(R.id.bottomNavi);
        alarm_btn = findViewById(R.id.alarm_btn);
        custom_btn = findViewById(R.id.custom_btn);

        addEventAction();

    }

    private void addEventAction(){
        theme_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(SettingAlarm.this);
                dlg.setTitle("테마");                         //제목
                final String[] versionArray = new String[] {"기본 테마", "단델리온", "썬키스드 코랄", "블러싱 브라이드", "트와일라잇 퍼플", "애프터 미드나잇"};

                dlg.setSingleChoiceItems(versionArray, 0, new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if(versionArray[which] == "단델리온"){
                            bottomNavigation.setBackgroundColor(R.color.dandelion);
                        }
                        else if(versionArray[which] == "썬키스드 코랄"){
                            bottomNavigation.setBackgroundColor(R.color.sunKissedCoral);
                        }
                        else if(versionArray[which] == "블러싱 브라이드"){
                            bottomNavigation.setBackgroundColor(R.color.blushingBride);
                        }
                        else if(versionArray[which] == "트와일라잇 퍼플"){
                            bottomNavigation.setBackgroundColor(R.color.twilightPurple);
                        }
                        else if(versionArray[which] == "애프터 미드나잇"){
                            bottomNavigation.setBackgroundColor(R.color.afterMidnight);
                        }
                        else if(versionArray[which] == "기본 테마"){
                            bottomNavigation.setBackgroundColor(R.color.basic);
                        }


                    }
                });
                //버튼 클릭시 동작
                dlg.show();
            }
        });

        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(on_click == false) {
                    custom_btn.setEnabled(true);
                    custom_btn.setTextColor(Color.BLACK);
                    on_click = true;
                }
                else{
                    custom_btn.setEnabled(false);
                    custom_btn.setTextColor(Color.GRAY);
                    on_click = false;
                    cancelAlarm();
                }
            }
        });

        custom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }


    private void updateTimeText(Calendar c){
        String timeText = "Alarm set for : ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        //mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if(c.before((Calendar.getInstance()))){
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1*60*1000 ,  pendingIntent);

    }


    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        //mTextView.setText("Alarm canceled");
    }


}