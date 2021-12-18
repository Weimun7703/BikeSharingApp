package com.example.bicycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminViewPage extends AppCompatActivity {
    private EditText edusernameadmin, ednameadmin, edemailadmin,edphoneadmin, edpasswordadmin;
    private FirebaseAuth mAuth;
    private String username,name,email,phone,password;
    private DrawerLayout drawerLayout;
    private NavigationView navigationViewadminview;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Admin View Page");

        edusernameadmin = findViewById(R.id.editText_usernameadmin);
        ednameadmin = findViewById(R.id.editText_nameadmin);
        edemailadmin = findViewById(R.id.editText_emailadmin);
        edphoneadmin = findViewById(R.id.editText_phoneadmin);
        edpasswordadmin = findViewById(R.id.editText_passwordadmin);
        textView = findViewById(R.id.textView_myprofile);

        drawerLayout = findViewById(R.id.DrawerLayoutadminview);
        navigationViewadminview = findViewById(R.id.NavigationViewAdmin);
        navigationViewadminview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profileadmin :
                        startActivity(new Intent(getApplicationContext(),AdminProfileOptionPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(AdminViewPage.this,"Going to Profile",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.reportadmin :
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(AdminViewPage.this,"View The Report",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.logoutadmin :
                        Intent intentlogout = new Intent(AdminViewPage.this,MainActivity.class);
                        startActivity(intentlogout);
                        Toast.makeText(AdminViewPage.this,"Signing Out....",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;

            }

        });


        mAuth = FirebaseAuth.getInstance();
        checkUser();
    }
        private void checkUser() {
            FirebaseUser user = mAuth.getCurrentUser();
            if(user == null){
                Intent intentToMain = new Intent(AdminViewPage.this,MainActivity.class);
                startActivity(intentToMain);
                finish();
            }
            else{
                loadAdminProfile();
            }
        }

        private void loadAdminProfile() {
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

                        edusernameadmin.setText(username);
                        ednameadmin.setText(name);
                        edemailadmin.setText(email);
                        edphoneadmin.setText(phone);
                        edpasswordadmin.setText(password);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }