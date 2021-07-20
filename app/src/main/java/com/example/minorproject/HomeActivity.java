package com.example.minorproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {
private DatabaseReference mDatabase;
String leakstatus,actiontaken,phnno,userph;
TextView status1;
ImageView statusimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        status1=findViewById(R.id.status);
        statusimage=findViewById(R.id.statusimg);
        userph=getIntent().getStringExtra("phnno");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("PhoneNumber", userph);
        editor.apply();
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        Intent intent = new Intent(this, myBackgroundProcess.class);
        intent.setAction("BackgroundProcess");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 10, pendingIntent);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userph);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    leakstatus = snapshot.child("GasLeakage").getValue(String.class);
                    actiontaken = snapshot.child("ActionTaken").getValue(String.class);
                }
                Log.d("Leak Status", "Leak Status" + leakstatus);
                Log.d("Action taken", "Action Taken" + actiontaken);

                if (leakstatus.equals("1") && actiontaken.equals("0")) {
                    status1.setText("Gas is Leaking");
                    statusimage.setBackgroundResource(R.drawable.red1);
                }
                if (leakstatus.equals("0") && actiontaken.equals("1")) {
                    status1.setText("Regulator is Turned Off");
                    statusimage.setBackgroundResource(R.drawable.green1);
                }
                if (leakstatus.equals("0") && actiontaken.equals("0")) {
                    status1.setText("Regulator is Turned On");
                    statusimage.setBackgroundResource(R.drawable.green1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
