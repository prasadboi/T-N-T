package com.example.t_t;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {
    EditText otpNumberOne,getOtpNumberTwo,getOtpNumberThree,getOtpNumberFour,getOtpNumberFive,otpNumberSix;
    Button verifyPhone;
    TextView resendOTP;
    Boolean otpValid = true;
    FirebaseAuth firebaseAuth;
    PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider.ForceResendingToken token;
    String verificationId;
    String  phone;
    String email;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        Intent data = getIntent();
        phone = data.getStringExtra("phone");


        firebaseAuth = FirebaseAuth.getInstance();

        otpNumberOne = findViewById(R.id.OTP_one1);
        getOtpNumberTwo = findViewById(R.id.OTP_two2);
        getOtpNumberThree = findViewById(R.id.OTP_three3);
        getOtpNumberFour = findViewById(R.id.OTP_four4);
        getOtpNumberFive = findViewById(R.id.OTP_five5);
        otpNumberSix = findViewById(R.id.OTP_six6);
        verifyPhone = findViewById(R.id.Verifybtn);
        resendOTP = findViewById(R.id.Gen_OTP_After_60);

        verifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateField(otpNumberOne);
                validateField(getOtpNumberTwo);
                validateField(getOtpNumberThree);
                validateField(getOtpNumberFour);
                validateField(getOtpNumberFive);
                validateField(otpNumberSix);

                if(otpValid){
                    // send otp to the user
                    String otp = otpNumberOne.getText().toString()+getOtpNumberTwo.getText().toString()+getOtpNumberThree.getText().toString()+getOtpNumberFour.getText().toString()+
                            getOtpNumberFive.getText().toString()+otpNumberSix.getText().toString();

                     PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);

                    verifyAuthentication(credential);

                }
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationId = s;
                token = forceResendingToken;
                resendOTP.setVisibility(View.GONE);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendOTP.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                verifyAuthentication(phoneAuthCredential);
                resendOTP.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneVerification.this, "OTP Verification Failed."+e.getMessage()
                        , Toast.LENGTH_SHORT).show();
            }
        };

        sendOTP(phone);


        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTP(phone);
            }
        });

    }

    public void sendOTP(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void resendOTP(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)           // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    public void validateField(EditText field){
        if(field.getText().toString().isEmpty()){
            field.setError("Required");
            otpValid = false;
        }else {
            otpValid = true;
        }
    }

    public void verifyAuthentication(PhoneAuthCredential credential){
        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Toast.makeText(PhoneVerification.this, "Acccount Created and Linked.", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(PhoneVerification.this,DashboardMainActivity.class));
            }
        });
    }
}