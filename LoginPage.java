package com.example.bicycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {
    private EditText edloginusername,edloginpassword;
    private Button btnlogin, btnregister;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login Page");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        edloginpassword = findViewById(R.id.ed_loginpassword);
        edloginusername = findViewById(R.id.ed_loginusername);
        btnlogin = findViewById(R.id.btn_loginbutton);
        btnregister = findViewById(R.id.byn_registerbutton);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2bikerregisterpage = new Intent(LoginPage.this, BikerRegisterPage.class);
                startActivity(intent2bikerregisterpage);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernamelogin = edloginusername.getText().toString().trim();
                String passwordlogin = edloginpassword.getText().toString().trim();


                if (usernamelogin.isEmpty()) {
                    edloginpassword.setError("Email is required!");
                    edloginusername.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(usernamelogin).matches()) {
                    edloginusername.setError("Please provide a valid email!");
                    edloginusername.requestFocus();
                    return;
                }
                if (passwordlogin.isEmpty()) {
                    edloginpassword.setError("Password is required!");
                    edloginpassword.requestFocus();
                    return;
                }
                if (passwordlogin.length() < 6) {
                    edloginpassword.setError("Password cannot less than 6 character!");
                    edloginpassword.requestFocus();
                    return;
                }

                progressDialog.show();

                mAuth.signInWithEmailAndPassword(usernamelogin, passwordlogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user.isEmailVerified()) {
                                //get user email and is from auth
                                checkUserType();
                                finish();
                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(getApplicationContext(), "Please check your email to verify your Maybike account!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private void checkUserType() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String accountType = ""+ds.child("accountType").getValue();
                    //if user is biker
                    if (accountType.equals("Biker")){
                        progressDialog.dismiss();
                        Intent intentToLocarionAllow = new Intent(LoginPage.this,BikerOptionPage.class);
                        startActivity(intentToLocarionAllow);
                        finish();
                    }
                    //if user is admin
                    else {
                        progressDialog.dismiss();
                        Intent intentToAdmin = new Intent(LoginPage.this,AdminProfileOptionPage.class);
                        startActivity(intentToAdmin);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }}
