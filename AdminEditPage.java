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

public class AdminEditPage extends AppCompatActivity {

    private EditText edusernameadmin, ednameadmin, edemailadmin, edphoneadmin, edpasswordadmin;
    private Button btnsaveadmin;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String username, name, email, phone, password;
    private DrawerLayout drawerLayoutadminedit;
    private NavigationView navigationViewadminedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Admin Edit Page");

        edusernameadmin = findViewById(R.id.editText_usernameadmin);
        ednameadmin = findViewById(R.id.editText_nameadmin);
        edphoneadmin = findViewById(R.id.editText_phoneadmin);
        edemailadmin = findViewById(R.id.editText_emailadmin);
        edpasswordadmin = findViewById(R.id.editText_passwordadmin);
        btnsaveadmin = findViewById(R.id.button_saveadmin);
        drawerLayoutadminedit = findViewById(R.id.DrawerLayoutadminedit);
        navigationViewadminedit = findViewById(R.id.NavigationEditAdmin);

        navigationViewadminedit.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profileadmin :
                        startActivity(new Intent(getApplicationContext(),AdminProfileOptionPage.class));
                        overridePendingTransition(0,0);
                        Toast.makeText(AdminEditPage.this,"Going to Profile",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.reportadmin :
                        //  startActivity(new Intent(getApplicationContext(),XXXX.class));
                        //overridePendingTransition(0,0);
                        Toast.makeText(AdminEditPage.this,"View The Report",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.logoutadmin :
                        Intent intentlogout = new Intent(AdminEditPage.this,MainActivity.class);
                        startActivity(intentlogout);
                        Toast.makeText(AdminEditPage.this,"Signing Out....",Toast.LENGTH_SHORT).show();
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

        btnsaveadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAdminProfile();
            }
        });

    }
    private void insertAdminProfile(){
        username = edusernameadmin.getText().toString().trim();
        name = ednameadmin.getText().toString().trim();
        email = edemailadmin.getText().toString().trim();
        phone = edphoneadmin.getText().toString().trim();
        password = edpasswordadmin.getText().toString().trim();
        updateAdminProfile();
    }

    private void updateAdminProfile(){
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
            Intent intentToMain = new Intent(AdminEditPage.this, MainActivity.class);
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