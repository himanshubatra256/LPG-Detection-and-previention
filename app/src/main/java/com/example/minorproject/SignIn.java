package com.example.minorproject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class SignIn extends AppCompatActivity {
    private static final int RC_SIGN_IN =123 ;
    EditText phonee,passworde;
    Button btnsignin;
    String pid;
    private String mPass,mPhn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        phonee = findViewById(R.id.phonee);
        passworde = findViewById(R.id.passworde);
        btnsignin = findViewById(R.id.btnsignin);

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gpassword1 = passworde.getText().toString().trim();
                final String gphone1=phonee.getText().toString().trim();
                if (TextUtils.isEmpty(gphone1)) {
                    phonee.setError("Enter Phone Number");
                    phonee.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(gpassword1)) {
                    passworde.setError("Enter Password");
                    passworde.requestFocus();
                    return;
                }
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                pid=phonee.getText().toString();
                mDialog.setMessage("Please wait......");
                mDialog.show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_user = database.getReference("User").child(phonee.getText().toString());
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            mDialog.dismiss();
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if(map.get("password")!=null){
                                mPass = map.get("password").toString();
                            }
                            if (mPass.equals(passworde.getText().toString())) {
                                Intent intent1 = new Intent(SignIn.this,HomeActivity.class);
                                intent1.putExtra("phnno",phonee.getText().toString());
                                startActivity(intent1);
                            } else {
                                Toast.makeText(SignIn.this, "Sign in failed !!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist !!!", Toast.LENGTH_SHORT).show();
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

