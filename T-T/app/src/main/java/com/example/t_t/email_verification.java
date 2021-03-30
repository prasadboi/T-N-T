package com.example.t_t;




import android.content.Intent;
import android.os.Bundle;


import android.util.Log;


import android.view.View;


import android.widget.Button;


import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class email_verification extends AppCompatActivity {
    long Otp;
    String email;
    String phone;
    @Override


    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_email_verification);


    }


    public void sendMail(View view) {


        try


        {


            Intent intent = getIntent();
            email = intent.getStringExtra("email");
            phone = intent.getStringExtra("phone");
            LongOperation l=new LongOperation(email);

            Otp = l.passOTP();
            l.execute();  //sends the email in background
            Toast.makeText(this, "Email Sent ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(email_verification.this, verify_otp.class);
            intent1.putExtra("OTP_key",Otp);
            intent1.putExtra("phone",phone);
            startActivity(intent1);



        } catch (Exception e) {


            Log.e("SendMail", e.getMessage(), e);


        }


    }


}