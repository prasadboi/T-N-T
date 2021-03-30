package com.example.t_t;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.Random;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Register extends AppCompatActivity {
    EditText mUsername, mEmail, mPassword, mProfession,mPhone;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    Button mRegisterBtn;
    FirebaseFirestore fstore;
    String userId;
    List<String> titles;
    List<String> categoryImages;
    String phone;
    String sEmail;
    String sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.editTextTextPersonName3);
        mEmail = findViewById(R.id.editTextTextPersonName2);
        mPassword = findViewById(R.id.editTextTextPersonName4);
        mPhone = findViewById(R.id.editTextTextPersonName11);
        mProfession = findViewById(R.id.editTextTextPersonName6);
        mRegisterBtn = findViewById(R.id.button2);
        mLoginBtn = findViewById(R.id.textView3);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        fstore = FirebaseFirestore.getInstance();
        final String TAG = DashboardMainActivity.class.getSimpleName();
        titles = new ArrayList<>();
        titles.add("Groceries");
        titles.add("Maintenance");
        titles.add("Kitchen Appliances");
        titles.add("Events");
        categoryImages = new ArrayList<>();
        categoryImages.add("R.drawable.groceries");
        categoryImages.add("R.drawable.maintenance");
        categoryImages.add("R.drawable.kitchen");
        categoryImages.add("R.drawable.calendar");


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String username = mUsername.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String profession = mProfession.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(phone)) {
                    mPhone.setError("Phone Number is required");
                    return;
                }
                int p = phone.length();
                if(p!=13){
                    mPhone.setError("Phone number should be of 10 digits");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Username is required");
                    return;
                }

                if (TextUtils.isEmpty(profession)) {
                    mProfession.setError("Profession is required");
                    return;
                }

                if (password.length() < 8) {
                    mPassword.setError("Password should be atleast 8 characters long");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            userId = firebaseUser.getUid();
                            Map<String,Object> dat = new HashMap<>();
                            dat.put("Username",username);
                            dat.put("Email",email);
                            dat.put("Phone",phone);
                            dat.put("Profession",profession);


                            fstore.collection("users").document(userId).set(dat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_LONG);
                                }
                            });
                            Map<String, Object> data = new HashMap<>();
                            data.put("Name", "Groceries");
                            data.put("ImageUrl", "https://png.pngtree.com/png-clipart/20190520/original/pngtree-supermarket-the-mall-grocery-store-cartoon-png-image_3857252.jpg");
                            Map<String,Object> dat1 = new HashMap<>();
                            dat1.put("Name","Maintainance");
                            dat1.put("ImageUrl","https://png.pngtree.com/png-clipart/20190516/original/pngtree-wrench-and-screwdriver-technical-repair-service-logo.-png-image_3623523.jpg");
                            Map<String,Object> dat2 = new HashMap<>();
                            dat2.put("Name","Kitchen Appliances");
                            dat2.put("ImageUrl","https://png.pngtree.com/png-clipart/20190516/original/pngtree-vector-cartoon-modern-kitchen-interior-background-png-image_3576214.jpg");

                            fstore.collection("users").document(userId).collection("Categories").document("Groceries")
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

                            fstore.collection("users").document(userId).collection("Categories").document("Maintainance")
                                    .set(dat1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                            fstore.collection("users").document(userId).collection("Categories").document("Kitchen Appliances")
                                    .set(dat2)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

                            Intent intent = new Intent(Register.this,email_verification.class);

                            //intent.putExtra("username", username);
                            intent.putExtra("email", email);
                            intent.putExtra("phone",phone);
                            //intent.putExtra("profession", profession);
                            startActivity(intent);
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(Register.this, "You are already registered with this email.Please Login", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

}