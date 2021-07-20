package com.example.minorproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.minorproject.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    EditText phonee1,passworde1,name1,email,emergency;
    Button btnsignup;
    String Phone=" ";
    static SignUp INSTANCE;
    private DatabaseReference leak,action,firstnotify,mDatabase3;
    private DatabaseReference signUpUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name1=findViewById(R.id.name1);
        email=findViewById(R.id.email);
        phonee1=findViewById(R.id.phonee1);
        emergency=findViewById(R.id.emergency);
        passworde1=findViewById(R.id.passworde1);
        btnsignup=findViewById(R.id.btnsignup);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gmaile = email.getText().toString().trim();
                String gemergency = emergency.getText().toString().trim();
                String gpassword = passworde1.getText().toString().trim();
                String gphone=phonee1.getText().toString().trim();
                String gname=name1.getText().toString().trim();
                if (TextUtils.isEmpty(gmaile)) {
                    email.setError("Enter Email Address");
                    email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(gname)) {
                    name1.setError("Enter Full Name");
                    name1.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(gemergency)) {
                    name1.setError("Enter Emergency Number");
                    name1.requestFocus();
                    return;
                }
                if(gemergency.equals(gphone))
                {
                    name1.setError("Enter a different number");
                    name1.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(gphone)) {
                    phonee1.setError("Enter Phone Number");
                    phonee1.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(gpassword)) {
                    passworde1.setError("Enter Password");
                    passworde1.requestFocus();
                    return;
                }
                if (gpassword.length() < 6) {
                    passworde1.setError("Password too short,Enter minimum 6 characters!");
                    passworde1.requestFocus();
                    return;
                }
                final ProgressDialog mDialog=new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please wait......");
                mDialog.show();
                signUpUser=FirebaseDatabase.getInstance().getReference().child("User");
                signUpUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(phonee1.getText().toString()).exists()){
                            mDialog.dismiss();
                            Intent intent=new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            mDialog.dismiss();
                            User user=new User(email.getText().toString(),name1.getText().toString(),passworde1.getText().toString(),emergency.getText().toString());
                            signUpUser.child(phonee1.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                            leak= FirebaseDatabase.getInstance().getReference().child("User").child(phonee1.getText().toString()).child("GasLeakage");
                            leak.setValue("0");
                            action = FirebaseDatabase.getInstance().getReference().child("User").child(phonee1.getText().toString()).child("ActionTaken");
                            action.setValue("0");
                            firstnotify = FirebaseDatabase.getInstance().getReference().child("User").child(phonee1.getText().toString()).child("FirstNotification");
                            firstnotify.setValue("0");
                            Phone=phonee1.getText().toString();
                            Intent intent=new Intent(SignUp.this,SignIn.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
