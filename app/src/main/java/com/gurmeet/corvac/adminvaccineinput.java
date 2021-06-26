package com.gurmeet.corvac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class adminvaccineinput extends AppCompatActivity {
TextInputEditText aadhar_edt;
TextView name_txt,gender_txt,dob_txt,age_group_txt,vaccine_txt,vaccine1date_txt,vaccine1hide,vaccine_count;
Button check_btn,vaccinate_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminvaccineinput);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        aadhar_edt=findViewById(R.id.aadharnumber);
        name_txt=findViewById(R.id.textView37);
        gender_txt=findViewById(R.id.textView38);
        dob_txt=findViewById(R.id.textView39);
        age_group_txt=findViewById(R.id.textView40);
        vaccine_txt=findViewById(R.id.textView41);
        vaccine_count=findViewById(R.id.textView42);
        vaccine1date_txt=findViewById(R.id.textView44);
        vaccine1hide=findViewById(R.id.textView43);

        check_btn=findViewById(R.id.button13);
        vaccinate_btn=findViewById(R.id.button14);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        String[] str1=formattedDate.split("/");
        String todaydate=str1[0]+str1[1]+str1[2];
        //Toast.makeText(adminvaccineinput.this,formattedDate,Toast.LENGTH_LONG).show();



        final String[] aadhar_enter = new String[1];
        vaccinate_btn.setEnabled(false);

        SharedPreferences preferences=getSharedPreferences("myPrefs",MODE_PRIVATE);
        String Doctor_name=preferences.getString("admin_name","");
        String hospital_state=preferences.getString("admin_state","");
        String hospital_district=preferences.getString("admin_district","");
        String hospital_name=preferences.getString("admin_hospital","");
        String hospital_key=preferences.getString("admin_hospital_key","");


        DatabaseReference dref=FirebaseDatabase.getInstance().getReference("vaccinebookingdata");


        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // dref.child(todaydate).child(hospital_state).child(hospital_district).child(hospital_key).

               aadhar_enter[0] = Objects.requireNonNull(aadhar_edt.getText()).toString().trim();
                if(!aadhar_enter[0].equals("")&& aadhar_enter[0].length()==12)
                {
                    DatabaseReference dref= FirebaseDatabase.getInstance().getReference("userdetails");
                    dref.child(aadhar_enter[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                if(snapshot.child("vaccine2status").exists())
                                {
                                    if(Objects.requireNonNull(snapshot.child("vaccine2status").getValue()).toString().equals("Booked")&&
                                            Objects.requireNonNull(snapshot.child("vaccine2date").getValue()).toString().equals(formattedDate)&& Objects.requireNonNull(snapshot.child("vaccine2hospital").getValue()).toString().equals(hospital_name))
                                    {
                                        vaccinate_btn.setEnabled(true);
                                        vaccine_count.setText("2");
                                        name_txt.setText(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                        gender_txt.setText(Objects.requireNonNull(snapshot.child("gender").getValue()).toString());
                                        dob_txt.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                                        age_group_txt.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                                        vaccine_txt.setText(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString());

                                            vaccine1date_txt.setText(Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());

                                    }
                                    else
                                    {
                                        Toast.makeText(adminvaccineinput.this,"No Booking Found on entered Aadhar Number!",Toast.LENGTH_LONG).show();
                                    }
                                }
                                else if(snapshot.child("vaccine1status").exists())
                                {
                                    if(Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString().equals("Booked")&&
                                            Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString().equals(formattedDate)&& Objects.requireNonNull(snapshot.child("vaccine1hospital").getValue()).toString().equals(hospital_name))
                                    {
                                        vaccinate_btn.setEnabled(true);
                                        vaccine_count.setText("1");
                                        name_txt.setText(Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                        gender_txt.setText(Objects.requireNonNull(snapshot.child("gender").getValue()).toString());
                                        dob_txt.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                                        age_group_txt.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                                        vaccine_txt.setText(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString());

                                            vaccine1date_txt.setVisibility(View.INVISIBLE);
                                            vaccine1hide.setVisibility(View.INVISIBLE);

                                    }
                                    else
                                    {
                                        Toast.makeText(adminvaccineinput.this,"No Booking Found on entered Aadhar Number!",Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(adminvaccineinput.this,"No Booking Found on entered Aadhar Number!",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(adminvaccineinput.this,"No Booking Found on entered Aadhar Number!",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(adminvaccineinput.this,"Please enter a valid Aadhar Number!",Toast.LENGTH_LONG).show();
                }
            }
        });
        vaccinate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(adminvaccineinput.this);

                builder.setCancelable(true);
                builder.setTitle("Vaccination");
                builder.setMessage("Are you Sure Vaccination is done!");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String vaccine_name=vaccine_txt.getText().toString();
                                if(vaccine_name.equals("Sputnik V"))
                                {
                                    vaccine_name="sputnik";
                                }
                                else if(vaccine_name.equals("Covaxin"))
                                {
                                    vaccine_name="covaxin";
                                }
                                else if(vaccine_name.equals("Covishield"))
                                {
                                    vaccine_name="covishield";
                                }
                                String v_count=vaccine_count.getText().toString().trim();
                                dref.child(todaydate).child(hospital_state).child(hospital_district).child(hospital_key).child(vaccine_name).child(aadhar_enter[0]).setValue("Done");
                                DatabaseReference dref2= FirebaseDatabase.getInstance().getReference("userdetails");
                                dref2.child(aadhar_enter[0]).child("vaccine"+v_count+"status").setValue("Done");
                                dref2.child(aadhar_enter[0]).child("vaccine"+v_count+"doctor").setValue(Doctor_name);



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



    }
}