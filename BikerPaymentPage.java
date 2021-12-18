package com.example.bicycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BikerPaymentPage extends AppCompatActivity {
    private EditText edcardname,edcardnumber,edexpiredate,edcvv;
    private RadioGroup radioGroup;
    private RadioButton rbcreadit,rbdebit;
    private Button btnpay;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biker_payment_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Biker Payment Page");

        edcardname = findViewById(R.id.editText_CardName);
        edcardnumber = findViewById(R.id.editText_CardNumber);
        edexpiredate = findViewById(R.id.editText_ExpiredDate);
        edcvv = findViewById(R.id.editText_CVV);
        radioGroup = findViewById(R.id.radioGroup_Card);
        rbcreadit = findViewById(R.id.radioButton_Credit);
        rbdebit = findViewById(R.id.radioButton_Debit);
        btnpay = findViewById(R.id.button_pay);

        mAuth = FirebaseAuth.getInstance();

        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCard();
            }
        });
    }

    private void checkCard(){
        if (edcardname.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter holder's name!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edcardnumber.length()<16){
            Toast.makeText(getApplicationContext(),"Please enter valid card number!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edcardnumber.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the card number!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edexpiredate.length()<5){
            Toast.makeText(getApplicationContext(),"Please enter valid expired date!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edexpiredate.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter expired date!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edcvv.length()<3){
            Toast.makeText(getApplicationContext(),"Please enter valid CVV!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (edcvv.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter CVV!",Toast.LENGTH_SHORT).show();
            return;
        }
        savePaymentData();

    }

    private void savePaymentData(){
        final String timestamp = ""+System.currentTimeMillis();
        String cardName = edcardname.getText().toString().trim();
        String cardNumber = edcardnumber.getText().toString().trim();
        String expiredDate = edexpiredate.getText().toString().trim();
        String cvv = edcvv.getText().toString().trim();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("paymentID", ""+timestamp);
        hashMap.put("cardName",""+cardName);
        hashMap.put("cardNumber",""+cardNumber);
        hashMap.put("expiredDate",""+expiredDate);
        hashMap.put("cvv",""+cvv);
       hashMap.put("uid",""+mAuth.getUid());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(mAuth.getUid()).child("Payment").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Payment Success!",Toast.LENGTH_SHORT).show();
                Intent intentdonepayment = new Intent(BikerPaymentPage.this,BikerOptionPage.class);
                startActivity(intentdonepayment);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}