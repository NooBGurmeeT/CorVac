package com.gurmeet.corvac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;

public class userhomescreen extends AppCompatActivity {
Button bookmyslot_btn,logout_btn;
TextView vaccine1txt,vaccine2txt;
ProgressBar progressBar;
Button my_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhomescreen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();


        bookmyslot_btn=findViewById(R.id.button);
        logout_btn=findViewById(R.id.button2);
        vaccine1txt=findViewById(R.id.textView5);
        vaccine2txt=findViewById(R.id.textView6);
        progressBar=findViewById(R.id.progress);
        my_booking=findViewById(R.id.button9);


        DatabaseReference dref5= FirebaseDatabase.getInstance().getReference("userdetails");
        SharedPreferences preferences=getSharedPreferences("myPrefs",MODE_PRIVATE);
        String user_aadhar=preferences.getString("user_aadhar","");
        //Toast.makeText(userhomescreen.this,user_aadhar, Toast.LENGTH_SHORT).show();
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=preferences.edit();

        dref5.child(user_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.child("vaccine1status").exists())
                {
                    vaccine1txt.setText("Not Booked Yet");
                    vaccine2txt.setText("Not Booked Yet");
                }
                else
                {
                    if(snapshot.child("vaccine1status").getValue().toString().equals("Booked"))
                    {
                        vaccine1txt.setText("Booked");
                        vaccine2txt.setText("Not Booked Yet");
                    }
                    else
                    {
                        if(snapshot.child("vaccine1status").getValue().toString().equals("Done"))
                        {
                            vaccine1txt.setText("Done");
                            if(!snapshot.child("vaccine2status").exists())
                            {
                                vaccine2txt.setText("Not Booked Yet");
                            }
                            else
                            {
                                if(snapshot.child("vaccine2status").getValue().toString().equals("Booked"))
                                {
                                    vaccine2txt.setText("Booked");
                                }
                                else
                                {
                                    if(snapshot.child("vaccine2status").getValue().toString().equals("Done"))
                                    {
                                        vaccine2txt.setText("Done");
                                    }
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        bookmyslot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                DatabaseReference dref5= FirebaseDatabase.getInstance().getReference("userdetails");
                SharedPreferences preferences=getSharedPreferences("myPrefs",MODE_PRIVATE);
                String user_aadhar=preferences.getString("user_aadhar","");
                //Toast.makeText(userhomescreen.this,user_aadhar, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor=preferences.edit();

                dref5.child(user_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(!snapshot.child("vaccine1status").exists())
                        {
                            editor.putString("vaccinenumber","vaccine1status");
                            editor.apply();
                            Intent intent=new Intent(getApplicationContext(),location.class);
                            startActivity(intent);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                        else
                        {
                            if(snapshot.child("vaccine1status").getValue().toString().equals("Booked"))
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(userhomescreen.this,"You have already booked for Vaccine Dose 1", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(snapshot.child("vaccine1status").getValue().toString().equals("Done"))
                                {
                                    if(!snapshot.child("vaccine2status").exists())
                                    {
                                        editor.putString("vaccine1date", Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                                        editor.putString("vaccinenumber","vaccine2status");
                                        editor.apply();
                                        Intent intent=new Intent(getApplicationContext(),location.class);
                                        startActivity(intent);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                    else
                                    {
                                        if(snapshot.child("vaccine2status").getValue().toString().equals("Booked"))
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(userhomescreen.this,"You have already booked for Vaccine Dose 2", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            if(snapshot.child("vaccine2status").getValue().toString().equals("Done"))
                                            {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(userhomescreen.this,"You have Been Vaccinated Twice!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(userhomescreen.this);

                builder.setCancelable(true);
                builder.setTitle("Log out");
                builder.setMessage("Are you Sure You want to logout!");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("logedin", false);
                                editor.apply();
                                Intent intent=new Intent(getApplicationContext(),adminuserlogin.class);
                                startActivity(intent);
                                finish();

                            }


                        });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });

        my_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vaccine1txt.getText().toString().equals("Not Booked Yet"))
                {
                    Toast.makeText(userhomescreen.this,"You Have Not Done Any Booking!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent=new Intent(userhomescreen.this,userbookingshow.class);
                    startActivity(intent);
                }
            }
        });

    }
    public  void  onBackPressed()
    {
       finishAffinity();
    }
}