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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class BikerOptionPage extends AppCompatActivity {
    private LinearLayout linearLayout;
    private Button btnviewbiker, btneditbiker;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biker_option_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Biker Option Page");
        btneditbiker = findViewById(R.id.btn_editbiker);
        btnviewbiker = findViewById(R.id.btn_viewbiker);
        drawerLayout = findViewById(R.id.DrawerLayoutbikeroption);
        navigationView = findViewById(R.id.NavigationViewbikeroption);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profilebiker :
                        startActivity(new Intent(getApplicationContext(),BikerOptionPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(BikerOptionPage.this,"Going to Profile",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.rentbiker:
                  //        startActivity(new Intent(getApplicationContext(),UnlockBike.class));
                  //      overridePendingTransition(0,0);
                        Toast.makeText(BikerOptionPage.this,"Rent the Bike",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.returnbiker :
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerOptionPage.this,"Return the Bike",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.reportbiker :
                          startActivity(new Intent(getApplicationContext(),BikerReportPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(BikerOptionPage.this,"View the Report",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.paymentbiker :
                          startActivity(new Intent(getApplicationContext(),BikerPaymentPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(BikerOptionPage.this,"Payment...",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.logoutbiker :
                        Intent intent2logout = new Intent(BikerOptionPage.this,MainActivity.class);
                        startActivity(intent2logout);
                        Toast.makeText(BikerOptionPage.this,"Signing Out....",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return true;

            }

        });
        btneditbiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2editbikerpage = new Intent(BikerOptionPage.this,BikerEditPage.class);
                startActivity(intent2editbikerpage);

            }
        });

        btnviewbiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2viewbiker = new Intent(BikerOptionPage.this,BikerViewPage.class);
                startActivity(intent2viewbiker);
            }
        });

    }
}
