package com.gurmeet.corvac;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class login extends AppCompatActivity {

    TextInputEditText email_edttxt,password_edttxt;
    Button login_btn;
    TextView signup_txt,forgotpassword;
    SharedPreferences preferences;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        email_edttxt=findViewById(R.id.username);
        password_edttxt=findViewById(R.id.password);
        login_btn=findViewById(R.id.buttonLogin);
        signup_txt=findViewById(R.id.signUpText);
        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress);
        forgotpassword=findViewById(R.id.textView46);
        preferences=getSharedPreferences("corvacpref",MODE_PRIVATE);

        String email_prefsaved,password_prefsaved;

        email_prefsaved=preferences.getString("email","");
        password_prefsaved=preferences.getString("password","");



        final String[] email = new String[1];
        final String[] password = new String[1];

        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(),userhomescreen.class);
            startActivity(mainActivity);
            finish();
        }



        login_btn.setOnClickListener(v -> {
            email[0] =email_edttxt.getText().toString().trim();
            password[0] =password_edttxt.getText().toString().trim();
            if(!email[0].equals("")&&!password[0].equals(""))
            {
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email[0], password[0]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(Objects.requireNonNull(fAuth.getCurrentUser()).isEmailVerified())
                            {
                                savePrefsData();



                                String[] token={""};
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if(task.isComplete()){
                                            token[0] = task.getResult();
                                            Log.e("AppConstants", "onComplete: new Token got: "+token[0] );
                                            String[] str =email[0].split("@");
                                            String[] str1 =str[1].split("\\.");
                                            String user_emailcheck=str[0]+str1[0]+str1[1];
                                            DatabaseReference dref2=FirebaseDatabase.getInstance().getReference("aadharemail");
                                            dref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                    String email_aadhar=snapshot.child(user_emailcheck).getValue().toString();
                                                    DatabaseReference dref=FirebaseDatabase.getInstance().getReference("userdetails");
                                                    DatabaseReference cur_usre=dref.child(email_aadhar);


                                                    cur_usre.child("token").setValue(token[0]);


                                                    cur_usre.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                            SharedPreferences preferences=getSharedPreferences("myPrefs",MODE_PRIVATE);
                                                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=preferences.edit();
                                                            editor.putString("user_name", Objects.requireNonNull(snapshot.child("fullname").getValue()).toString());
                                                            editor.putString("user_phonenumber", Objects.requireNonNull(snapshot.child("phonenumber").getValue()).toString());
                                                            editor.putString("user_aadhar", Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString());
                                                            editor.putString("user_email", Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                                                            editor.apply();
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            //Toast.makeText(getApplicationContext(), Objects.requireNonNull(snapshot.child("aadharnumber").getValue()).toString(), Toast.LENGTH_SHORT).show();

                                                            // Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), userhomescreen.class));
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                        }
                                                    });


                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            });


                                        }
                                    }
                                });


                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(login.this,"Please verify yout email",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(login.this,"All fields are required",Toast.LENGTH_SHORT).show();
            }
        });

        signup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(login.this,Signup.class);
                startActivity(intent);
            }
        });


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email1,password1;
                email1 = String.valueOf(email_edttxt.getText());

                if (TextUtils.isEmpty(email1)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                if (!email1.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);

                    int c1=-1,c2=-1,count1=0;
                    for(int i=0;i<email1.length();i++)
                    {
                        if(email1.charAt(i)=='@')
                        {
                            c1=i;
                            count1++;
                        }
                        if(email1.charAt(i)=='.')
                        {
                            c2=i;
                        }
                    }
                    if(c1!=-1&&c2!=-1&&count1==1&&c1<c2)
                    {
                        String[] str =email1.split("@");
                        String[] str1 =str[1].split("\\.");
                        String user_emailcheck=str[0]+str1[0]+str1[1];
                        FirebaseDatabase busdb=FirebaseDatabase.getInstance();

                        final DatabaseReference node= busdb.getReference("userdetails");
                        final DatabaseReference emailcheck=node.child(user_emailcheck);
                        emailcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    if(email1.equals((String) snapshot.child("email").getValue()))
                                    {
                                        fAuth.sendPasswordResetEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(login.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(login.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                                }

                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });                                }
                                    else
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(login.this,"User not found, Please Signup! ",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(login.this,"User not found, Please Signup! ",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(login.this,"enter valid email-id",Toast.LENGTH_SHORT).show();
                    }


                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(login.this,"email cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("logedin", true);
        editor.apply();
    }



    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("logedin", false);
        return isIntroActivityOpnendBefore;
    }
}