package com.example.minorproject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class myBackgroundProcess extends BroadcastReceiver {
    private DatabaseReference mDatabase,mDatabase1,mDatabase3,mDatabase4;
    long diff,diffm;
    static String Nodate,phn,Userphn;
    static Date Cudate,Ndate;
    String leakstatus,actiontaken,phnno,name;
    Context context;
    private static final int PERMISSION_SEND_SMS = 123;
    @Override

    public void onReceive(final Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String Userphn=sharedPref.getString("PhoneNumber","Not Available");
        Log.d("Phone","Phone"+Userphn);
        assert Userphn != null;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(Userphn);
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("User").child(Userphn).child("GasLeakage");
        mDatabase4 = FirebaseDatabase.getInstance().getReference().child("User").child(Userphn).child("FirstNotification");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    assert map != null;
                    leakstatus = map.get("GasLeakage").toString();
                    actiontaken = map.get("ActionTaken").toString();
                    name = map.get("name").toString();
                    phnno = map.get("emergencyPhone").toString();
                }
                    Log.d("Leak Status","Leak Status"+leakstatus);
                    Log.d("Action taken","Action Taken"+actiontaken);

                    if (leakstatus.equals("1") && actiontaken.equals("0"))
                    {
                        Intent intent1 = new Intent(context, SignIn.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent1,0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notify")
                                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                .setContentTitle("Gas Leakage Detection System")
                                .setContentText("Gas is Leaking "+Calendar.getInstance().getTime().toString())
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(200,builder.build());
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                    Nodate=map.get("FirstNotification").toString();

                                    if(Nodate.equals("0") && actiontaken.equals("0")){
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH-mm-ss");
                                        Date date = new Date();
                                        String strDate = dateFormat.format(date);
                                        mDatabase4.setValue(strDate);
                                        String time = strDate.split("\\s")[1].split("\\.")[0];
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
                                        try {
                                            Ndate=sdf.parse(time);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("Date", "Nodate " + Nodate);
                                        Calendar cal = Calendar.getInstance();
                                        Cudate = cal.getTime();

                                        Log.d("Date", "Cudate " + Cudate);
                                        diff=Ndate.getTime()-Cudate.getTime();
                                        diffm=(diff / (1000 * 60)) % 60;
                                        if(diffm<0){
                                            diffm=-(diffm);
                                        }
                                        Log.d("Date", "Diff " + diffm);
                                    }
                                    else
                                    {
                                        try {
                                            String time = Nodate.split("\\s")[1].split("\\.")[0];
                                            SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
                                            Ndate=sdf.parse(time);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("Date", "Ndate " + Ndate);
                                        Log.d("Date", "Nodate " + Nodate);
                                        Calendar cal = Calendar.getInstance();
                                        Cudate = cal.getTime();

                                        Log.d("Date", "Cudate " + Cudate);
                                        diff=Ndate.getTime()-Cudate.getTime();
                                        diffm=(diff / (1000 * 60)) % 60;
                                        if(diffm<0){
                                            diffm=-(diffm);
                                        }
                                        Log.d("Date", "Diff " + diffm);
                                        if(diffm==5 && actiontaken.equals("0"))
                                        {
                                            String msg="In your neighbour :- "+name+" house gas is leaking";
                                            try {
                                                Toast.makeText(context,"Emergency"+phnno,Toast.LENGTH_LONG).show();
                                                SmsManager smsManager=SmsManager.getDefault();
                                                smsManager.sendTextMessage(phnno,null,msg,null,null);
                                                Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
                                            }catch (Exception e)
                                            {
                                                Toast.makeText(context,"Message not sent",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                            }}

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else if(leakstatus.equals("1") && actiontaken.equals("1"))
                    {
                        mDatabase1.setValue("0");
                        mDatabase4.setValue("0");
                        Intent intent2 = new Intent(context, SignIn.class);;
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent2,0);

                        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context,"notify")
                                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                .setContentTitle("Gas Leakage Detection System")
                                .setContentText("Regulator is Turned OFF "+Calendar.getInstance().getTime().toString())
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(200,builder1.build());
                    }
                    else if(leakstatus.equals("0") && actiontaken.equals("0"))
                    {
                        Log.d("Msg", "Regulator is Turned On");
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}




