package com.gurmeet.corvac;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class adminsignup extends AppCompatActivity {
    TextInputEditText textInputEditTextFullname,textInputEditTextPhonenumber,textInputEditTextPassword,textInputEditTextEmail,textInputEditTextAadhar;
    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;
    SharedPreferences preferences;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminsignup);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();


        textInputEditTextFullname=findViewById(R.id.fullname);
        textInputEditTextPhonenumber=findViewById(R.id.phonenumber);
        textInputEditTextEmail=findViewById(R.id.email);
        textInputEditTextPassword=findViewById(R.id.password);
        buttonSignUp=findViewById((R.id.buttonSignUp));
        textViewLogin=findViewById(R.id.loginText);
        textInputEditTextAadhar=findViewById(R.id.aadharnumber);
        progressBar=findViewById(R.id.progress);
        fAuth=FirebaseAuth.getInstance();
        preferences=getSharedPreferences("corvacpref",MODE_PRIVATE);

        final String[] fullname = new String[1];
        final String[] email = new String[1];
        final String[] phonenumber = new String[1];
        final String[] password = new String[1];
        final String[] registration_no = new String[1];

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullname[0] = String.valueOf(textInputEditTextFullname.getText());
                phonenumber[0] = String.valueOf(textInputEditTextPhonenumber.getText());
                password[0] = String.valueOf(textInputEditTextPassword.getText());
                email[0] = String.valueOf(textInputEditTextEmail.getText());
                registration_no[0] =String.valueOf(textInputEditTextAadhar.getText());

                if (!fullname[0].equals("") && !phonenumber[0].equals("") && !password[0].equals("") && !email[0].equals("")&&!registration_no[0].equals(""))
                {
                    if(phonenumber[0].length()!=10)
                    {
                        Toast.makeText(adminsignup.this,"Enter valid Phone Number",Toast.LENGTH_SHORT).show();
                    }
                    else if(password[0].length()<6)
                    {
                        Toast.makeText(adminsignup.this,"Password lenght >=6",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        int c1=-1,c2=-1,count1=0;
                        for(int i=0;i<email[0].length();i++)
                        {
                            if(email[0].charAt(i)=='@')
                            {
                                c1=i;
                                count1++;
                            }
                            if(email[0].charAt(i)=='.')
                            {
                                c2=i;
                            }
                        }
                        if(c1!=-1&&c2!=-1&&count1==1&&c1<c2)
                        {
                            String[] str =email[0].split("@");
                            String[] str1 =str[1].split("\\.");
                            String admin_emailcheck=str[0]+str1[0]+str1[1];
                            final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference admindetail=database.child("admindetails");
                            //DatabaseReference user = userdetail.child(str[0]);

                            admindetail.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                                    if(snapshot.child(admin_emailcheck).exists())
                                    {
                                        Toast.makeText(getApplicationContext(), "Email Already Registered", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        progressBar.setVisibility(View.VISIBLE);
                                        fAuth.createUserWithEmailAndPassword(email[0], password[0]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    Objects.requireNonNull(fAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                adminsignupdataholder obj = new adminsignupdataholder(fullname[0], phonenumber[0], email[0],registration_no[0]);
                                                                FirebaseDatabase busdb = FirebaseDatabase.getInstance();
                                                                DatabaseReference node = busdb.getReference("admindetails");
                                                                node.child(admin_emailcheck).setValue(obj);





                                                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(getApplicationContext(), adminlogin.class));
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                finish();
                                                            }
                                                            else {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(getApplicationContext(), "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });

                                                } else {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(getApplicationContext(), "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                                }
                            });

                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(adminsignup.this,"Email format incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(adminsignup.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(adminsignup.this,adminlogin.class);
                startActivity(intent);
            }
        });
    }
}