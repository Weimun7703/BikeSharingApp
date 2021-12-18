package com.example.bicycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminRegisterPage extends AppCompatActivity {
    private EditText edadminusername, edadminname, edadminemail, edadminphone, edadminpassword, edadminconfirmpassword;
    private Button btnadminregister;
    private TextView tvregisbiker;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String adminusername, adminname, adminemail, adminphone, adminpassword, adminconfirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Admin Register Page");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        edadminusername = findViewById(R.id.editText_adminusername);
        edadminname = findViewById(R.id.editText_adminname);
        edadminemail = findViewById(R.id.editText_adminemail);
        edadminphone = findViewById(R.id.editText_adminphone);
        edadminpassword = findViewById(R.id.editText_adminpassword);
        edadminconfirmpassword = findViewById(R.id.editText_adminconfirmpassword);
        btnadminregister = findViewById(R.id.btn_adminregister);
        tvregisbiker = findViewById(R.id.textView_regisbiker);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnadminregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        tvregisbiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttoBikerRegisterPage = new Intent(AdminRegisterPage.this, BikerRegisterPage.class);
                startActivity(intenttoBikerRegisterPage);
            }
        });
    }

    private void inputData() {
        adminname = edadminname.getText().toString().trim();
        adminusername = edadminusername.getText().toString().trim();
        adminphone = edadminphone.getText().toString().trim();
        adminemail = edadminemail.getText().toString().trim();
        adminpassword = edadminpassword.getText().toString().trim();
        adminconfirmpassword = edadminconfirmpassword.getText().toString().trim();

        //check data if empty
        if (edadminname.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "The name is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(adminusername).matches()) {
            Toast.makeText(getApplicationContext(), "The username must be Email Address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edadminphone.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "The phone number is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(adminemail).matches()) {
            Toast.makeText(getApplicationContext(), "The email address is invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        if (adminpassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edadminconfirmpassword.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Confirm password is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        createAdminAccount();
    }

    private void createAdminAccount() {
        progressDialog.setMessage("Creating Account....");
        progressDialog.show();
        //create account and save into firebase
        mAuth.createUserWithEmailAndPassword(adminemail, adminpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAccountData() {
        progressDialog.setMessage("Saving Account Info...");
        //set data to save
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", "" + mAuth.getUid());
        hashMap.put("accountType", "Admin");
        hashMap.put("name", "" + adminname);
        hashMap.put("username", "" + adminusername);
        hashMap.put("phone", "" + adminphone);
        hashMap.put("email", "" + adminemail);
        hashMap.put("password", "" + adminpassword);
        hashMap.put("confirmpassword", "" + adminconfirmpassword);

        //save to database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(mAuth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Intent intentToMain = new Intent(AdminRegisterPage.this, MainActivity.class);
                startActivity(intentToMain);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Intent intentToMain = new Intent(AdminRegisterPage.this, MainActivity.class);
                startActivity(intentToMain);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

