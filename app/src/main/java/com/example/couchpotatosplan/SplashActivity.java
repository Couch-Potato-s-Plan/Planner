package com.example.couchpotatosplan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceStare){
        super.onCreate(savedInstanceStare);


        setContentView((R.layout.splash_activity));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), com.example.couchpotatosplan.MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
