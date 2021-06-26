package com.gurmeet.corvac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

public class adminbookingdata extends AppCompatActivity {
    TextView total_booking,total_completed,total_covaxin,covaxin_completed,total_covishield,covishield_completed,total_sputnik,sputnik_completed;
 TextInputEditText enter_date;
 Button check_data;
    DatePickerDialog.OnDateSetListener setListener,setListener2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminbookingdata);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        total_booking=findViewById(R.id.textView53);
        total_completed=findViewById(R.id.textView54);
        total_covaxin=findViewById(R.id.textView55);
        covaxin_completed=findViewById(R.id.textView56);
        total_covishield=findViewById(R.id.textView57);
        covishield_completed=findViewById(R.id.textView58);
        total_sputnik=findViewById(R.id.textView59);
        sputnik_completed=findViewById(R.id.textView60);
        enter_date=findViewById(R.id.fullname);
        check_data=findViewById(R.id.button13);

        final Calendar[] calendar = {Calendar.getInstance()};
        int year= calendar[0].get(Calendar.YEAR);
        int day= calendar[0].get(Calendar.DAY_OF_MONTH);
        int month= calendar[0].get(Calendar.MONTH);



        enter_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog=new DatePickerDialog(adminbookingdata.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }

        });
        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date="";
                if(dayOfMonth<10)
                {
                    if(month<10)
                    {
                        date= "0"+dayOfMonth+"/0"+month+"/"+year;
                    }
                    else
                    {
                        date= "0"+dayOfMonth+"/"+month+"/"+year;
                    }
                }
                else
                {
                    if(month<10)
                    {
                        date= dayOfMonth+"/0"+month+"/"+year;
                    }
                    else
                    {
                        date= dayOfMonth+"/"+month+"/"+year;
                    }
                }

               enter_date.setText(date);
            }
        };




        check_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences=getSharedPreferences("myPrefs",MODE_PRIVATE);

                String entered_date=enter_date.getText().toString().trim();
                if(!entered_date.equals(""))
                {
                    String[] str=entered_date.split("/");
                    String check_date=str[0]+str[1]+str[2];

                    String admin_state=preferences.getString("admin_state","");
                    String admin_district=preferences.getString("admin_district","");
                    String admin_hospital_key=preferences.getString("admin_hospital_key","");
                    //Toast.makeText(adminbookingdata.this,, Toast.LENGTH_SHORT).show();
                    DatabaseReference dref= FirebaseDatabase.getInstance().getReference("vaccinebookingdata");
                    dref.child(check_date).child(admin_state).child(admin_district).child(admin_hospital_key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            long size1=snapshot.child("covaxin").getChildrenCount();
                            long size2=snapshot.child("covishield").getChildrenCount();
                            long size3=snapshot.child("sputnik").getChildrenCount();
                            long size1complete=0,size2complete=0,size3complete=0;
                            for(DataSnapshot mdata:snapshot.child("covaxin").getChildren())
                            {
                                if(mdata.getValue().toString().equals("Done"))
                                {
                                    size1complete++;
                                }
                            }
                            for(DataSnapshot mdata:snapshot.child("covishield").getChildren())
                            {
                                if(mdata.getValue().toString().equals("Done"))
                                {
                                    size2complete++;
                                }
                            }
                            for(DataSnapshot mdata:snapshot.child("sputnik").getChildren())
                            {
                                if(mdata.getValue().toString().equals("Done"))
                                {
                                    size3complete++;
                                }
                            }
                            total_booking.setText(String.valueOf(size1+size2+size3));
                            total_completed.setText(String.valueOf(size1complete+size2complete+size3complete));
                            total_covaxin.setText(String.valueOf(size1));
                            covaxin_completed.setText(String.valueOf(size1complete));
                            total_covishield.setText(String.valueOf(size2));
                            covishield_completed.setText(String.valueOf(size2complete));
                            total_sputnik.setText(String.valueOf(size3));
                            sputnik_completed.setText(String.valueOf(size3complete));
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
                else
                {

                }

            }
        });

    }
}