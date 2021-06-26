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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;

public class userbookingshow extends AppCompatActivity {
TextView vaccine1txt,vaccine2txt;
Button firstdose_btn,seconddose_btn,cancel_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userbookingshow);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        vaccine1txt=findViewById(R.id.textView5);
        vaccine2txt=findViewById(R.id.textView6);
        firstdose_btn=findViewById(R.id.button10);
        seconddose_btn=findViewById(R.id.button11);
        cancel_btn=findViewById(R.id.button15);


        DatabaseReference dref5= FirebaseDatabase.getInstance().getReference("userdetails");
        SharedPreferences preferences=getSharedPreferences("myPrefs",MODE_PRIVATE);
        String user_aadhar=preferences.getString("user_aadhar","");
        final String[] vaccine1date = {""};
        final String[] vaccine2date = {""};
        final String[] vaccine1h_key = {""};
        final String[] vaccine2h_key = {""};
        final String[] vaccine1state = {""};
        final String[] vaccine2state = {""};
        final String[] vaccine1district = {""};
        final String[] vaccine2district = {""};
        final String[] vaccine1name = {""};
        final String[] vaccine2name = {""};

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
                        vaccine1h_key[0]=snapshot.child("vaccine1hospitalkey").getValue().toString();
                        vaccine1state[0]=snapshot.child("vaccine1state").getValue().toString();
                        vaccine1district[0]=snapshot.child("vaccine1district").getValue().toString();
                        vaccine1date[0] =snapshot.child("vaccine1date").getValue().toString();
                        vaccine1name[0]=snapshot.child("vaccine").getValue().toString();
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
                                    vaccine2h_key[0]=snapshot.child("vaccine2hospitalkey").getValue().toString();
                                    vaccine2state[0]=snapshot.child("vaccine2state").getValue().toString();
                                    vaccine2district[0]=snapshot.child("vaccine2district").getValue().toString();
                                    vaccine2date[0] =snapshot.child("vaccine2date").getValue().toString();
                                    vaccine2name[0]=snapshot.child("vaccine").getValue().toString();
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

        firstdose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vaccine1txt.getText().toString().equals("Booked")||vaccine1txt.getText().toString().equals("Done"))
                {
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor1=preferences.edit();
                    if(vaccine1txt.getText().toString().equals("Booked"))
                    editor1.putString("checkstatus",vaccine1txt.getText().toString());
                    if(vaccine1txt.getText().toString().equals("Done"))
                    editor1.putString("checkstatus",vaccine1txt.getText().toString());
                    editor1.apply();
                    Intent intent=new Intent(userbookingshow.this,userfirstdosedata.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(userbookingshow.this,"No Boking Data for Second Dose!",Toast.LENGTH_SHORT).show();
                }
            }
        });
       seconddose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vaccine2txt.getText().toString().equals("Booked")||vaccine2txt.getText().toString().equals("Done"))
                {
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor1=preferences.edit();
                    if(vaccine2txt.getText().toString().equals("Booked"))
                        editor1.putString("checkstatus",vaccine2txt.getText().toString());
                    if(vaccine2txt.getText().toString().equals("Done"))
                        editor1.putString("checkstatus",vaccine2txt.getText().toString());
                    editor1.apply();
                    Intent intent=new Intent(userbookingshow.this,userseconddosedata.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(userbookingshow.this,"No Boking Data for Second Dose!",Toast.LENGTH_SHORT).show();
                }
            }
        });

       cancel_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(vaccine1txt.getText().toString().equals("Booked")||vaccine2txt.getText().toString().equals("Booked"))
               {

                   String number;
                   String date;
                   String state;
                   String district;
                   String h_key;
                   final String[] vaccine = new String[1];
                   if(vaccine1txt.getText().toString().equals("Booked"))
                   {
                       number="1";
                       date=vaccine1date[0];
                       state=vaccine1state[0];
                       district=vaccine1district[0];
                       vaccine[0] =vaccine1name[0];
                       h_key=vaccine1h_key[0];
                   }
                   else {
                       number="2";
                       date=vaccine2date[0];
                       state=vaccine2state[0];
                       district=vaccine2district[0];
                       vaccine[0] =vaccine2name[0];
                       h_key=vaccine2h_key[0];
                   }
                   String[] str=date.split("/");
                   date=str[0]+str[1]+str[2];
                   final AlertDialog.Builder builder = new AlertDialog.Builder(userbookingshow.this);
                   builder.setCancelable(true);
                   builder.setTitle("Cancel Booking");
                   builder.setMessage("Booking for vaccine Dose-"+number+" on "+date+" will be Cancelled!");

                   String finalDate = date;
                   builder.setPositiveButton("Confirm",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   if(vaccine[0].equals("Covaxin"))
                                   {
                                       vaccine[0] ="covaxin";
                                   }
                                   else if(vaccine[0].equals("Covishield"))
                                   {
                                       vaccine[0] ="covishield";
                                   }
                                   else if(vaccine[0].equals("Sputnik V"))
                                   {
                                       vaccine[0] ="sputnik";
                                   }

                                   DatabaseReference dref=FirebaseDatabase.getInstance().getReference("userdetails");
                                   dref.child(user_aadhar).child("vaccine"+number+"state").removeValue();
                                   dref.child(user_aadhar).child("vaccine"+number+"district").removeValue();
                                   dref.child(user_aadhar).child("vaccine"+number+"hospital").removeValue();
                                   dref.child(user_aadhar).child("vaccine"+number+"hospitalkey").removeValue();
                                   dref.child(user_aadhar).child("vaccine"+number+"status").removeValue();
                                   dref.child(user_aadhar).child("vaccine"+number+"date").removeValue();

                                   if(number.equals("1"))
                                   {
                                       vaccine1txt.setText("Not Booked Yet");
                                   }
                                   else
                                   {
                                       vaccine2txt.setText("Not Booked Yet");
                                   }

                                   DatabaseReference dref2=FirebaseDatabase.getInstance().getReference("vaccinebookingdata");
                                   dref2.child(finalDate).child(state).child(district).child(h_key).child(vaccine[0]).child(user_aadhar).removeValue();

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
               else
               {
                   Toast.makeText(userbookingshow.this,"You Have no Cancelable Booking!",Toast.LENGTH_LONG).show();
               }
           }
       });

    }
    public void onBackPressed()
    {
        Intent intent=new Intent(userbookingshow.this,userhomescreen.class);
        startActivity(intent);
    }
}