package com.example.t_t;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainScreen extends AppCompatActivity {
    EditText mPhone;
    Button mSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mPhone = findViewById(R.id.editTextTextPersonName9);

        mSend = findViewById(R.id.button4);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mPhone.getText().toString().trim();
                if(TextUtils.isEmpty(mobile)) {
                mPhone.setError("Phone Number is required");
                return;
                }
                int p = mobile.length();
                if(p!=13){
                    mPhone.setError("Phone number should be of 10 digits");
                    return;
                }
                Intent intent = new Intent(MainScreen.this,Register.class);
                intent.putExtra("phone",mobile);
                startActivity(intent);


            }
        });


    }
}