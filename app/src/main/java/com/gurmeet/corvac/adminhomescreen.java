package com.gurmeet.corvac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class adminhomescreen extends AppCompatActivity {
Button hospital_data,booking_data,vaccinate,send_notification;

Button demo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhomescreen);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        hospital_data=findViewById(R.id.button6);
        booking_data=findViewById(R.id.button5);
        vaccinate=findViewById(R.id.button7);

        send_notification=findViewById(R.id.button8);
        booking_data.setEnabled(false);
        vaccinate.setEnabled(false);
        send_notification.setEnabled(false);


        Date c = Calendar.getInstance().getTime();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        Date c2=calendar.getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        String[] str1=formattedDate.split("/");
        String todaydate=str1[0]+str1[1]+str1[2];
        String formattedDate2 = df.format(c2);
        String[] str2=formattedDate2.split("/");
        String tommorowdate=str2[0]+str2[1]+str2[2];

        SharedPreferences preferences=getSharedPreferences("myPrefs",MODE_PRIVATE);
        String admin_email=preferences.getString("adminemail","");

        final String[] admin_name = new String[1];
        final String[] admin_state = new String[1];
        final String[] admin_district = new String[1];
        final String[] admin_hospital = new String[1];
        final String[] admin_hospital_key = new String[1];

        DatabaseReference dref=FirebaseDatabase.getInstance().getReference("admindetails");
        dref.child(admin_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child("verified").exists())
                {
                    if(Objects.requireNonNull(snapshot.child("verified").getValue()).toString().equals("Done"))
                    {
                        booking_data.setEnabled(true);
                        vaccinate.setEnabled(true);
                        send_notification.setEnabled(true);
                        admin_name[0]=snapshot.child("fullname").getValue().toString();
                        admin_district[0] =snapshot.child("district").getValue().toString();
                        admin_state[0] =snapshot.child("state").getValue().toString();
                        admin_hospital[0] =snapshot.child("hospital_name").getValue().toString();
                        admin_hospital_key[0]=snapshot.child("hospital_key").getValue().toString();
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("admin_name",admin_name[0]);
                        editor.putString("admin_state",admin_state[0]);
                        editor.putString("admin_district",admin_district[0]);
                        editor.putString("admin_hospital",admin_hospital[0]);
                        editor.putString("admin_hospital_key",admin_hospital_key[0]);
                        editor.apply();
                    }
                    else if(snapshot.child("verified").getValue().toString().equals("Pending"))
                    {
                        Toast.makeText(adminhomescreen.this,"Your account is undergoing Verification!",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(adminhomescreen.this,"PLease enter your Hospital Data",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        hospital_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),hospitaldata.class);
                startActivity(intent);
            }
        });
        booking_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),adminbookingdata.class);
                startActivity(intent);
            }
        });
        vaccinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("admin_name",admin_name[0]);
                editor.putString("admin_state",admin_state[0]);
                editor.putString("admin_district",admin_district[0]);
                editor.putString("admin_hospital",admin_hospital[0]);
                editor.putString("admin_hospital_key",admin_hospital_key[0]);
                editor.apply();
                Intent intent =new Intent(adminhomescreen.this,adminvaccineinput.class);
                startActivity(intent);

            }
        });


//        ArrayList<String> list1=new ArrayList<String>();
//        ArrayList<String> list2=new ArrayList<String>();
//        ArrayList<String> list3=new ArrayList<String>();
        //list1.add(0,"fHcAoNs6T3uGvitSU350jm:APA91bGgLLIiOPyuuaIID-pWCKVsr9JVGrasPWV-R1RhrHtvNpeBiceZgJPMEWz0PMpK9TAZLi0CuRO6VVju6jbsEYTuGLUtcO3teVHydn31OktuY5zlkhsNzp309FlG6xYrGBnOsMMe");
        DatabaseReference dref2=FirebaseDatabase.getInstance().getReference("vaccinebookingdata");
        DatabaseReference dref3=FirebaseDatabase.getInstance().getReference("userdetails");
       // Toast.makeText(adminhomescreen.this,tommorowdate, Toast.LENGTH_SHORT).show();

         send_notification.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 list1.clear();
//                 list2.clear();
//                 list3.clear();

                 @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                 String str = sdf.format(new Date());
                 if(str.compareTo("17:00:00")>=0)
                 {
                     dref2.child(todaydate).child(admin_state[0]).child(admin_district[0]).child(admin_hospital_key[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                             if(snapshot.child("covaxin").exists())
                             {
                                 for(DataSnapshot mdata:snapshot.child("covaxin").getChildren())
                                 {
                                     if(Objects.requireNonNull(mdata.getValue()).toString().equals("Done"))
                                     {
                                         String key=mdata.getKey();
                                         assert key != null;
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     String vaccine_number= Objects.requireNonNull(snapshot.child("vaccinenumber").getValue()).toString();
                                                     dref3.child(key).child("vaccine"+vaccine_number+"status").setValue("Done");
                                                     if(!Objects.requireNonNull(snapshot.child("token").getValue()).toString().equals("offline"))
                                                     {
                                                         String title="Vaccine Booking";
                                                         String body="Dear "+ Objects.requireNonNull(snapshot.child("fullname").getValue()).toString()+"\n"+"You have been successfully vaccinated on"+formattedDate+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });


                                     }
                                     else if(Objects.requireNonNull(mdata.getValue()).toString().equals("Booked"))
                                     {
                                         String key=mdata.getKey();
                                         assert key != null;
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     String vaccine_number= Objects.requireNonNull(snapshot.child("vaccinenumber").getValue()).toString();
                                                     dref3.child(key).child("vaccine"+vaccine_number+"status").removeValue();;
                                                     dref3.child(key).child("vaccine"+vaccine_number+"state").removeValue();;
                                                     dref3.child(key).child("vaccine"+vaccine_number+"date").removeValue();;
                                                     dref3.child(key).child("vaccine"+vaccine_number+"district").removeValue();;
                                                     dref3.child(key).child("vaccine"+vaccine_number+"hospital").removeValue();;
                                                     if(!snapshot.child("token").getValue().toString().equals("offline"))
                                                     {
                                                         String title="Vaccine Booking";
                                                         String body="Dear "+snapshot.child("fullname").getValue().toString()+"\n"+"You have missed the vaccination slot on"+formattedDate+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }


                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
                                     }
                                 }
                             }
                             if(snapshot.child("covishield").exists())
                             {
                                 for(DataSnapshot mdata:snapshot.child("covishield").getChildren())
                                 {
                                     if(Objects.requireNonNull(mdata.getValue()).toString().equals("Done"))
                                     {
                                         String key=mdata.getKey();
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     String vaccine_number=snapshot.child("vaccinenumber").getValue().toString();
                                                     dref3.child(key).child("vaccine"+vaccine_number+"status").setValue("Done");
                                                     if(!snapshot.child("token").getValue().toString().equals("offline"))
                                                     {
                                                         String title="Vaccine Booking";
                                                         String body="Dear "+snapshot.child("fullname").getValue().toString()+"\n"+"You have been successfully vaccinated on"+formattedDate+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
                                     }
                                     else if(Objects.requireNonNull(mdata.getValue()).toString().equals("Booked"))
                                     {
                                         String key=mdata.getKey();
                                         assert key != null;
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     String vaccine_number= Objects.requireNonNull(snapshot.child("vaccinenumber").getValue()).toString();
                                                     dref3.child(key).child("vaccine"+vaccine_number+"status").removeValue();;
                                                     dref3.child(key).child("vaccine"+vaccine_number+"state").removeValue();;
                                                     dref3.child(key).child("vaccine"+vaccine_number+"date").removeValue();;
                                                     dref3.child(key).child("vaccine"+vaccine_number+"district").removeValue();;
                                                     dref3.child(key).child("vaccine"+vaccine_number+"hospital").removeValue();;
                                                     if(!Objects.requireNonNull(snapshot.child("token").getValue()).toString().equals("offline"))
                                                     {
                                                         String title="Vaccine Booking";
                                                         String body="Dear "+ Objects.requireNonNull(snapshot.child("fullname").getValue()).toString()+"\n"+"You have missed the vaccination slot on"+formattedDate+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
                                     }
                                 }
                             }
                             if(snapshot.child("sputnik").exists())
                             {
                                 for(DataSnapshot mdata:snapshot.child("sputnik").getChildren())
                                 {
                                     if(Objects.requireNonNull(mdata.getValue()).toString().equals("Done"))
                                     {
                                         String key=mdata.getKey();
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     String vaccine_number= Objects.requireNonNull(snapshot.child("vaccinenumber").getValue()).toString();
                                                     dref3.child(key).child("vaccine"+vaccine_number+"status").setValue("Done");
                                                     if(!snapshot.child("token").getValue().toString().equals("offline"))
                                                     {
                                                         String title="Vaccine Booking";
                                                         String body="Dear "+ Objects.requireNonNull(snapshot.child("fullname").getValue()).toString()+"\n"+"You have been successfully vaccinated on"+formattedDate+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
                                     }
                                     else if(Objects.requireNonNull(mdata.getValue()).toString().equals("Booked"))
                                     {
                                         String key=mdata.getKey();
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     if(!Objects.requireNonNull(snapshot.child("token").getValue()).toString().equals("offline"))
                                                     {

                                                         String vaccine_number= Objects.requireNonNull(snapshot.child("vaccinenumber").getValue()).toString();
                                                         dref3.child(key).child("vaccine"+vaccine_number+"status").removeValue();;
                                                         dref3.child(key).child("vaccine"+vaccine_number+"state").removeValue();;
                                                         dref3.child(key).child("vaccine"+vaccine_number+"date").removeValue();;
                                                         dref3.child(key).child("vaccine"+vaccine_number+"district").removeValue();;
                                                         dref3.child(key).child("vaccine"+vaccine_number+"hospital").removeValue();;
                                                         String title="Vaccine Booking";
                                                         String body="Dear "+ Objects.requireNonNull(snapshot.child("fullname").getValue()).toString()+"\n"+"You have missed the vaccination slot on"+formattedDate+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
                                     }
                                 }
                             }

                         }

                         @Override
                         public void onCancelled(@NonNull @NotNull DatabaseError error) {

                         }
                     });
                     dref2.child(tommorowdate).child(admin_state[0]).child(admin_district[0]).child(admin_hospital_key[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                             if(snapshot.child("covaxin").exists())
                             {
                                 for(DataSnapshot mdata:snapshot.child("covaxin").getChildren())
                                 {
                                     if(Objects.requireNonNull(mdata.getValue()).toString().equals("Booked"))
                                     {
                                         String key=mdata.getKey();
                                         assert key != null;
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     if(!Objects.requireNonNull(snapshot.child("token").getValue()).toString().equals("offline"))
                                                     {
                                                         String title="Vaccine Booking";
                                                         String body="Dear"+ Objects.requireNonNull(snapshot.child("fullname").getValue()).toString()+"\n"+"You have a vaccine booking on"+formattedDate2+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
                                     }
                                 }
                             }
                             if(snapshot.child("covishield").exists())
                             {
                                 for(DataSnapshot mdata:snapshot.child("covishield").getChildren())
                                 {
                                     if(Objects.requireNonNull(mdata.getValue()).toString().equals("Booked"))
                                     {
                                         String key=mdata.getKey();
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     if(!snapshot.child("token").getValue().toString().equals("offline"))
                                                     {
                                                         String title="Vaccine Booking";
                                                         String body="Dear"+ Objects.requireNonNull(snapshot.child("fullname").getValue()).toString()+"\n"+"You have a vaccine booking on"+formattedDate2+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
                                     }
                                 }
                             }
                             if(snapshot.child("sputnik").exists())
                             {
                                 for(DataSnapshot mdata:snapshot.child("sputnik").getChildren())
                                 {
                                     if(Objects.requireNonNull(mdata.getValue()).toString().equals("Booked"))
                                     {
                                         String key=mdata.getKey();
                                         assert key != null;
                                         dref3.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                 if(snapshot.exists())
                                                 {
                                                     if(!snapshot.child("token").getValue().toString().equals("offline"))
                                                     {
                                                         String title="Vaccine Booking";
                                                         String body="Dear"+snapshot.child("fullname").getValue().toString()+"\n"+"You have a vaccine booking on"+formattedDate2+"\n"+"at"+admin_hospital[0];
                                                         FcmNotificationsSender notificationsSender=new FcmNotificationsSender(snapshot.child("token").getValue().toString(),title,body,
                                                                 getApplicationContext(),adminhomescreen.this);
                                                         notificationsSender.SendNotifications();
                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                             }
                                         });
                                     }
                                 }
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull @NotNull DatabaseError error) {

                         }
                     });

                 }
                 else
                 {
                     Toast.makeText(adminhomescreen.this,"This button will be active after 17:00:00",Toast.LENGTH_LONG).show();
                 }

             }
         });



    }


}