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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BikerRegisterPage extends AppCompatActivity {
    private EditText edusername,edname,edemail,edphone,edpassword,edconfirmpassword;
    private Button btnregister;
    private TextView tvregisadmin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String username,name, email,phone,password, confirmpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biker_register_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Biker Register Page");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        edusername = findViewById(R.id.editText_username);
        edname = findViewById(R.id.editText_name);
        edemail = findViewById(R.id.editText_email);
        edphone = findViewById(R.id.editText_phone);
        edpassword = findViewById(R.id.editText_password);
        edconfirmpassword = findViewById(R.id.editText_confirmpassword);
        btnregister = findViewById(R.id.btn_bikerregister);
        tvregisadmin = findViewById(R.id.textView_regisadmin);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });


        tvregisadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoAdminRegister = new Intent(BikerRegisterPage.this,AdminRegisterPage.class);
                startActivity(intenttoAdminRegister);

            }
        });


    }

    private void inputData(){
        name = edname.getText().toString().trim();
        username = edusername.getText().toString().trim();
        phone = edphone.getText().toString().trim();
        email = edemail.getText().toString().trim();
        password = edpassword.getText().toString().trim();
        confirmpassword = edconfirmpassword.getText().toString().trim();

        //check data if empty
        if(edname.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Name is email!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            Toast.makeText(getApplicationContext(),"Username is email address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(),"Invalid email pattern!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edphone.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(),"Phone Number is empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6){
            Toast.makeText(getApplicationContext(),"Password must be at least 6 characters!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edconfirmpassword.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext()," Confirm Password is empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        createAccount();
        }

        private void createAccount(){
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        //create account and save into firebase
            mAuth.createUserWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //account created
                    saveAccountData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //failed to create account
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void saveAccountData(){
            progressDialog.setMessage("Saving Biker Info...");

            //set data to save
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+mAuth.getUid());
            hashMap.put("accountType","Biker");
            hashMap.put("name",""+name);
            hashMap.put("username",""+username);
            hashMap.put("phone",""+phone);
            hashMap.put("email",""+email);
            hashMap.put("password",""+password);
            hashMap.put("confirmpassword",""+confirmpassword);

            //save to database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(mAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Intent intenttoMainPage = new Intent(BikerRegisterPage.this,MainActivity.class);
                    startActivity(intenttoMainPage);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Intent intentToMain = new Intent(BikerRegisterPage.this,MainActivity.class);
                    startActivity(intentToMain);
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
