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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BikerReportPage extends AppCompatActivity {
    private EditText edbikenumber, edfeedback;
    private Button btnsubmit,btnviewownreport;
    private NavigationView navigationViewreportbiker;
    private FirebaseAuth mAuth;
    String bicyclenumber, feedbackbike,adminID;
    private DrawerLayout drawerLayoutreportbiker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biker_report_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Biker Report Page");

        edbikenumber = findViewById(R.id.editText_BikeNumberUsed);
        edfeedback = findViewById(R.id.editText_FeedbackBikerFill);
        btnsubmit = findViewById(R.id.button_SubmitReport);
        btnviewownreport = findViewById(R.id.button_ViewOwnReport);
        drawerLayoutreportbiker = findViewById(R.id.DrawerLayoutBikerReport);
        navigationViewreportbiker = findViewById(R.id.NavigationBikerReport);
        mAuth = FirebaseAuth.getInstance();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitFeedback();
                
            }

        });

        btnviewownreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Intent intent2viewownreport = new Intent(BikerReportPage.this, BikerViewOwnReport.class);
             //   startActivity(intent2viewownreport);
                Toast.makeText(getApplicationContext(), "Viewing Your Reporting List", Toast.LENGTH_LONG).show();

            }
        });

        navigationViewreportbiker.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profilebiker:
                        startActivity(new Intent(getApplicationContext(), BikerOptionPage.class));
                      //  overridePendingTransition(0, 0);
                        Toast.makeText(BikerReportPage.this, "Going to Profile", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.rentbiker:
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerReportPage.this, "Rent the Bike", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.returnbiker:
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(BikerReportPage.this, "Return the Bike", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.reportbiker:
                       startActivity(new Intent(getApplicationContext(), BikerReportPage.class));
                        overridePendingTransition(0, 0);
                        Toast.makeText(BikerReportPage.this, "Make a Report", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.paymentbiker:
                        startActivity(new Intent(getApplicationContext(),BikerPaymentPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(BikerReportPage.this, "Payment...", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.logoutbiker:
                        Intent intent2logout = new Intent(BikerReportPage.this, MainActivity.class);
                        startActivity(intent2logout);
                        Toast.makeText(BikerReportPage.this, "Signing Out....", Toast.LENGTH_SHORT).show();
                        return true;
                }

                return true;
            }
        });
    }

    private void SubmitFeedback() {

        if (edbikenumber.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter the bike number!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edbikenumber.length() < 4) {
            Toast.makeText(getApplicationContext(), "Please enter 4 digit number only!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edfeedback.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter feedback!", Toast.LENGTH_SHORT).show();
            return;
        }

        SaveFeedbackData();


    }

    private void SaveFeedbackData() {
        final String timestamp = "" + System.currentTimeMillis();
        bicyclenumber = edbikenumber.getText().toString().trim();
        feedbackbike = edfeedback.getText().toString().trim();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("draftID", "" + timestamp);
        hashMap.put("adminID", "" + adminID);
        hashMap.put("bicycleID",""+timestamp);
        hashMap.put("bicyclenumber", "" + bicyclenumber);
        hashMap.put("feedbackbike", "" + feedbackbike);
        hashMap.put("uid", mAuth.getUid());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(mAuth.getUid()).child("Draft").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Draft Saved!", Toast.LENGTH_SHORT).show();
                Intent intentdonereport = new Intent(BikerReportPage.this, BikerOptionPage.class);
                startActivity(intentdonereport);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Unable to Save Draft!", Toast.LENGTH_SHORT).show();

            }
        });


    }
}