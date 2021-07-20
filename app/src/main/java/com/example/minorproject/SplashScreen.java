package com.example.minorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    public static int sp=2000;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser= auth.getCurrentUser();
        if(currUser==null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in= new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(in);

                }
            },sp);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent inn = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(inn);
                    finish();
                }
            },sp);

        }
    }
}
