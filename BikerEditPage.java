package com.example.bicycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BikerEditPage extends AppCompatActivity {

    private EditText edusernamebiker, ednamebiker, edemailbiker, edphonebiker, edpasswordbiker;
    private Button btnsavebiker;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String username, name, email, phone, password;
    private DrawerLayout drawerLayouteditbiker;
    private NavigationView navigationVieweditbiker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biker_edit_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Biker Edit Page");

        edusernamebiker = findViewById(R.id.editText_usernamebiker);
        ednamebiker = findViewById(R.id.editText_namebiker);
        edphonebiker = findViewById(R.id.editText_phonebiker);
        edemailbiker = findViewById(R.id.editText_emailbiker);
        edpasswordbiker = findViewById(R.id.editText_passwordbiker);
        btnsavebiker = findViewById(R.id.button_savebiker);
        drawerLayouteditbiker = findViewById(R.id.DrawerLayoutbikeredit);
        navigationVieweditbiker = findViewById(R.id.Navigationviewbikeredit);

        navigationVieweditbiker.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profilebiker :
                        startActivity(new Intent(getApplicationContext(),BikerOptionPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(BikerEditPage.this,"Going to Profile",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.rentbiker:
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerEditPage.this,"Rent the Bike",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.returnbiker :
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerEditPage.this,"Return the Bike",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.reportbiker :
                         startActivity(new Intent(getApplicationContext(),BikerReportPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(BikerEditPage.this,"Make a Report",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.paymentbiker :
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerEditPage.this,"Payment...",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.logoutbiker :
                        Intent intent2logout = new Intent(BikerEditPage.this,MainActivity.class);
                        startActivity(intent2logout);
                        Toast.makeText(BikerEditPage.this,"Signing Out....",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;
            }
        });

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth = FirebaseAuth.getInstance();
        checkUser();

        btnsavebiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBikerProfile();
            }
        });

    }

    private void insertBikerProfile(){
        username = edusernamebiker.getText().toString().trim();
        name = ednamebiker.getText().toString().trim();
        email = edemailbiker.getText().toString().trim();
        phone = edphonebiker.getText().toString().trim();
        password = edpasswordbiker.getText().toString().trim();
        updateBikerProfile();
    }

    private void updateBikerProfile(){
        progressDialog.setMessage("Updating ...");
        progressDialog.show();

        //setup data to update
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", "" +username);
        hashMap.put("name",""+name);
        hashMap.put("email",""+email);
        hashMap.put("phone",""+phone);
        hashMap.put("password",""+password);

        //save to database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(mAuth.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Update Success",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void checkUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intentToMain = new Intent(BikerEditPage.this, MainActivity.class);
            startActivity(intentToMain);
            finish();
        } else {
            loadBikerProfile();
        }
    }

    private void loadBikerProfile() {
        //load user profile and set to view
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String accountType = "" + ds.child("accountType").getValue();
                    String username = "" + ds.child("username").getValue();
                    String name = "" + ds.child("name").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String email = "" + ds.child("email").getValue();
                    String password = "" + ds.child("password").getValue();
                   // String uid = "" + ds.child("uid").getValue();

                    edusernamebiker.setText(username);
                    ednamebiker.setText(name);
                    edemailbiker.setText(email);
                    edphonebiker.setText(phone);
                    edpasswordbiker.setText(password);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}