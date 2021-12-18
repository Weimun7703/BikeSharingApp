package com.example.bicycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BikerViewPage extends AppCompatActivity {

    private EditText edusernamebiker, ednamebiker, edemailbiker,edphonebiker, edpasswordbiker;
    private FirebaseAuth mAuth;
    private String username,name,email,phone,password;
    private DrawerLayout drawerLayoutviewbiker;
    private NavigationView navigationViewviewbiker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biker_view_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Biker View Page");

        edusernamebiker = findViewById(R.id.editText_usernamebiker);
        ednamebiker = findViewById(R.id.editText_namebiker);
        edemailbiker = findViewById(R.id.editText_emailbiker);
        edphonebiker = findViewById(R.id.editText_phonebiker);
        edpasswordbiker = findViewById(R.id.editText_passwordbiker);
        mAuth = FirebaseAuth.getInstance();
        drawerLayoutviewbiker = findViewById(R.id.DrawerLayoutbikerview);
        navigationViewviewbiker = findViewById(R.id.NavigationViewBiker);
        navigationViewviewbiker.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profilebiker :
                        startActivity(new Intent(getApplicationContext(),BikerOptionPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(BikerViewPage.this,"Going to Profile",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.rentbiker:
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerViewPage.this,"Rent the Bike",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.returnbiker :
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerViewPage.this,"Return the Bike",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.reportbiker :
                        startActivity(new Intent(getApplicationContext(),BikerReportPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(BikerViewPage.this,"Make a Report",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.paymentbiker :
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerViewPage.this,"Payment...",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.logoutbiker :
                        Intent intent2logout = new Intent(BikerViewPage.this,MainActivity.class);
                        startActivity(intent2logout);
                        Toast.makeText(BikerViewPage.this,"Signing Out....",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;

            }

        });


        checkUser();
    }

    private void checkUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            Intent intentToMain = new Intent(BikerViewPage.this,MainActivity.class);
            startActivity(intentToMain);
            finish();
        }
        else{
            loadBikerProfile();
        }
    }

    private void loadBikerProfile() {
        //load user profile and set to view
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String accountType = "" + ds.child("accountType").getValue();
                    String username = "" + ds.child("username").getValue();
                    String name = "" + ds.child("name").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String email = "" + ds.child("email").getValue();
                    String password = "" + ds.child("password").getValue();
                  //  String uid = "" + ds.child("uid").getValue();

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