package com.gurmeet.corvac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class userfirstdosedata extends AppCompatActivity {
Button create_first_pdf;
TextView name_txt,aadhar_txt,gender_txt,dob_txt,agegroup_txt,state_txt,district_txt,hospital_txt,doctor_txt,status_txt,slot_date_txt,vaccine_txt;
Bitmap bmp,bmp2,scalebmp,scalebmp2,scalebmp3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userfirstdosedata);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        name_txt=findViewById(R.id.textView18);
        aadhar_txt=findViewById(R.id.textView19);
        gender_txt=findViewById(R.id.textView20);
        agegroup_txt=findViewById(R.id.textView21);
        dob_txt=findViewById(R.id.textView22);
        state_txt=findViewById(R.id.textView23);
        district_txt=findViewById(R.id.textView24);
        hospital_txt=findViewById(R.id.textView25);
        status_txt=findViewById(R.id.textView27);
        slot_date_txt=findViewById(R.id.textView26);
        doctor_txt=findViewById(R.id.textView28);
        create_first_pdf=findViewById(R.id.button12);
        vaccine_txt=findViewById(R.id.textView30);
        bmp= BitmapFactory.decodeResource(getResources(),R.drawable.unnamed);
        bmp2= BitmapFactory.decodeResource(getResources(),R.drawable.modi);
        scalebmp=Bitmap.createScaledBitmap(bmp,512,287,false);
        scalebmp2=Bitmap.createScaledBitmap(bmp,1000,500,false);
        scalebmp3=Bitmap.createScaledBitmap(bmp2,1200,600,false);

        ActivityCompat.requestPermissions(userfirstdosedata.this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(userfirstdosedata.this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);




        SharedPreferences preferences=getSharedPreferences("myPrefs",MODE_PRIVATE);
        String checkstatus=preferences.getString("checkstatus","");
        String user_aadhar=preferences.getString("user_aadhar","");

        final String[] aadharnumber = new String[1];





        create_first_pdf.setEnabled(false);
        create_first_pdf.setVisibility(View.INVISIBLE);

//        if(checkstatus=="Booked")
//        {
//            status_txt.setText("Booked");
//            doctor_txt.setEnabled(false);
//            doctor_txt.setVisibility(View.INVISIBLE);
//        }
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("userdetails");
        dref.child(user_aadhar).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                name_txt.setText(Objects.requireNonNull(capitalize(snapshot.child("fullname").getValue().toString())));
                aadhar_txt.setText(Objects.requireNonNull(snapshot.child("aadharnumber").getValue().toString()));
                aadharnumber[0]=snapshot.child("aadharnumber").getValue().toString();
                gender_txt.setText(Objects.requireNonNull(capitalize(snapshot.child("gender").getValue().toString())));
                dob_txt.setText(Objects.requireNonNull(snapshot.child("dob").getValue()).toString());
                state_txt.setText(Objects.requireNonNull(capitalize(snapshot.child("vaccine1state").getValue().toString())));
                district_txt.setText(Objects.requireNonNull(capitalize(snapshot.child("vaccine1district").getValue().toString())));
                slot_date_txt.setText(Objects.requireNonNull(snapshot.child("vaccine1date").getValue()).toString());
                agegroup_txt.setText(Objects.requireNonNull(snapshot.child("agegroup").getValue()).toString());
                vaccine_txt.setText(Objects.requireNonNull(snapshot.child("vaccine").getValue()).toString());
                hospital_txt.setText(Objects.requireNonNull(capitalize(snapshot.child("vaccine1hospital").getValue().toString())));

                if(((String) Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString()).equals("Booked"))
                {
                    status_txt.setTextColor(Color.rgb(255,192,12));
                    status_txt.setText("Booked");
                }
                if(((String) Objects.requireNonNull(snapshot.child("vaccine1status").getValue()).toString()).equals("Done"))
                {
                    doctor_txt.setText(Objects.requireNonNull(snapshot.child("vaccine1doctor").getValue()).toString());
                    status_txt.setTextColor(Color.GREEN);
                    status_txt.setText("Done");
                    create_first_pdf.setEnabled(true);
                    create_first_pdf.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        create_first_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                String[] str=slot_date_txt.getText().toString().split("/");
                Calendar calendar=Calendar.getInstance();
                calendar.set(Integer.parseInt(str[2]),Integer.parseInt(str[1]),Integer.parseInt(str[0]));
                calendar.add(Calendar.DAY_OF_YEAR,35);
                Date c2=calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR,21);
                Date c3=calendar.getTime();


                String second_dose = df.format(c2)+" to "+df.format(c3);

                PdfDocument myPdfDocument=new PdfDocument();
                Paint mypaint=new Paint();
                Paint mypaint1=new Paint();
                Paint titlePaint=new Paint();


                PdfDocument.PageInfo myPageInfo=new PdfDocument.PageInfo.Builder(1200,2010,1).create();
                PdfDocument.Page myPage1=myPdfDocument.startPage(myPageInfo);
                Canvas canvas=myPage1.getCanvas();
                canvas.drawBitmap(scalebmp,340,0,mypaint);

                Paint mypaint3 = new Paint();
                Canvas canvas2=myPage1.getCanvas();
                canvas2.drawBitmap(scalebmp3,0,1400,mypaint3);


                //Bitmap bgr = BitmapFactory.decodeResource(getResources(),R.drawable.unnamed);
                Paint transparentpainthack = new Paint();
                transparentpainthack.setAlpha(15);
                canvas.drawBitmap(scalebmp2, 100, 700, transparentpainthack);


                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                titlePaint.setTextSize(65);
                canvas.drawText(" India Covid-19 Vaccination Program",600,350,titlePaint);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setColor(Color.parseColor("#006DCC"));
                titlePaint.setTextSize(45);
                canvas.drawText("Covid-19 Vaccine Certificate for Vaccine Dose-1",600,420,titlePaint);

                mypaint1.setTextAlign(Paint.Align.LEFT);
                mypaint1.setTextSize(35);
                mypaint1.setColor(Color.parseColor("#006DCC"));
                mypaint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                mypaint1.setFlags(Paint. UNDERLINE_TEXT_FLAG);
                canvas.drawText("Benificiary Details :",110,510,mypaint1);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                mypaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                canvas.drawText("Benificiary Name",110,570,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(name_txt.getText().toString(),600,570,mypaint);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Date of Birth",110,620,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(dob_txt.getText().toString(),600,620,mypaint);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Age Group",110,670,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(agegroup_txt.getText().toString(),600,670,mypaint);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Gender",110,720,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(gender_txt.getText().toString(),600,720,mypaint);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Aadhar Number",110,770,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(aadhar_txt.getText().toString(),600,770,mypaint);






                mypaint1.setTextAlign(Paint.Align.LEFT);
                mypaint1.setTextSize(35);
                mypaint1.setColor(Color.parseColor("#006DCC"));
                mypaint1.setFlags(Paint. UNDERLINE_TEXT_FLAG);
                canvas.drawText("Vaccination Details",110,870,mypaint1);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Vaccine Name",110,930,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(vaccine_txt.getText().toString().trim(),600,930,mypaint);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Date of Dose",110,980,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(slot_date_txt.getText().toString().trim(),600,980,mypaint);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Next due Date",110,1030,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(second_dose,600,1030,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Vaccinated By",110,1080,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(doctor_txt.getText().toString().trim(),600,1080,mypaint);


                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText("Vaccinated at",110,1130,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(hospital_txt.getText().toString().trim()+","+district_txt.getText().toString(),600,1130,mypaint);

                mypaint.setTextAlign(Paint.Align.LEFT);
                mypaint.setTextSize(25);
                mypaint.setColor(Color.BLACK);
                canvas.drawText(state_txt.getText().toString(),600,1150,mypaint);

                mypaint.setTextAlign(Paint.Align.CENTER);
                mypaint.setTextSize(50);
                mypaint.setColor(Color.parseColor("#006DCC"));
                canvas.drawText("Please Register for Dose 2!",600,1280,mypaint);









                myPdfDocument.finishPage(myPage1);
                String path = Environment.getExternalStorageDirectory() + "/CovacPDF";

                File dir = new File(path);
                if(!dir.exists())
                    dir.mkdirs();

                Log.d("PDFCreator", "PDF Path: " + path);

                String filename=aadharnumber[0]+"_Vaccine-1"+".pdf";

                File file = new File(dir, filename);


                // File file= new File(Environment.getExternalStorageDirectory(),str+".pdf");
                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                    viewPdf(filename,"CovacPDF");

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                myPdfDocument.close();

            }
        });

    }
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + directory + "/" + file);
        Uri path = FileProvider.getUriForFile(userfirstdosedata.this, BuildConfig.APPLICATION_ID + ".provider",pdfFile);


        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        if (pdfFile.exists()) {

            pdfIntent.setDataAndType(path,"application/pdf");
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Intent.createChooser(pdfIntent, "Open File Using..."));

        }
    }
}