package com.example.bicycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminProfileOptionPage extends AppCompatActivity {
    private Button btnviewadmin, btneditadmin;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile_option_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Admin Option Page");
        mAuth = FirebaseAuth.getInstance();


        btneditadmin = findViewById(R.id.btn_editadmin);
        btnviewadmin = findViewById(R.id.btn_viewadmin);
        drawerLayout = findViewById(R.id.DrawerLayout);
        navigationView = findViewById(R.id.NavigationViewAdmin);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profileadmin :
                        startActivity(new Intent(getApplicationContext(),AdminProfileOptionPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(AdminProfileOptionPage.this,"Going to Profile",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.reportadmin :
                     //   startActivity(new Intent(getApplicationContext(),AdminReportListPage.class));
                     //   overridePendingTransition(0,0);
                        Toast.makeText(AdminProfileOptionPage.this,"View The Report",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.logoutadmin :
                      Intent intentlogout = new Intent(AdminProfileOptionPage.this,MainActivity.class);
                      startActivity(intentlogout);
                        Toast.makeText(AdminProfileOptionPage.this,"Signing Out....",Toast.LENGTH_SHORT).show();
                        return true;
                }
            return true;
            }
        });

        btneditadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2editadminpage = new Intent(AdminProfileOptionPage.this,AdminEditPage.class);
                startActivity(intent2editadminpage);

            }
        });

        btnviewadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2viewadminpage = new Intent(AdminProfileOptionPage.this,AdminViewPage.class);
                startActivity(intent2viewadminpage);
            }
        });


    }
}