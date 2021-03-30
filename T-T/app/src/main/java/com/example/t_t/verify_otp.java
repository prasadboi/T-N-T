package com.example.t_t;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class verify_otp extends AppCompatActivity {
    EditText OTP_one, OTP_two, OTP_three, OTP_four, OTP_five, OTP_six;
    Button verify_OTP;
    Boolean isOTPValid = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        OTP_one = findViewById(R.id.OTP_one);
        OTP_two = findViewById(R.id.OTP_two);
        OTP_three = findViewById(R.id.OTP_three);
        OTP_four = findViewById(R.id.OTP_four);
        OTP_five = findViewById(R.id.OTP_five);
        OTP_six = findViewById(R.id.OTP_six);

        Intent intent = getIntent();
        Long ID = intent.getLongExtra("OTP_key",0);
        String phone = intent.getStringExtra("phone");
        String verification_ID = ID.toString();
        // Verify OTP button
        verify_OTP = findViewById(R.id.OTP_Verification_Button);

        verify_OTP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Checking if all OTP boxes are filled
                isDataEmpty(OTP_one);
                isDataEmpty(OTP_two);
                isDataEmpty(OTP_three);
                isDataEmpty(OTP_four);
                isDataEmpty(OTP_five);
                isDataEmpty(OTP_six);

                if(isOTPValid){
                    // accept OTP and then check further
                    String OTP = OTP_one.getText().toString()
                            + OTP_two.getText().toString()
                            + OTP_three.getText().toString()
                            + OTP_four.getText().toString()
                            + OTP_five.getText().toString()
                            + OTP_six.getText().toString();

                    if (OTP.equals(verification_ID)){
                        Intent intent1 = new Intent(verify_otp.this, PhoneVerification.class);
                        intent1.putExtra("phone",phone);
                        startActivity(intent1);
                    }

                }
            }
        });

    }

    private void isDataEmpty(EditText field) {
        if(field.getText().toString().isEmpty()){
            isOTPValid = false;
            field.setError(field + "required");
        }
        else{
            isOTPValid = true;
        }
    }
}