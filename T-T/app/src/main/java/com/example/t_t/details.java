package com.example.t_t;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class details extends AppCompatActivity {
    TextView name,date,time,desp;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name = findViewById(R.id.textView7);
        time = findViewById(R.id.textView9);
        date = findViewById(R.id.textView8);
        desp = findViewById(R.id.textView10);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        Intent intent = getIntent();
        String title = intent.getStringExtra("Event");
        firebaseFirestore.collection("users").document(uid).collection("To_Do_List").document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> dat = new HashMap<>();
                    dat = document.getData();
                    String task1 = document.get("Task").toString();
                    String date1 = document.get("Date").toString();
                    String time1 = document.get("Time").toString();
                    String description = document.get("Description").toString();
                    name.setText(task1);
                    desp.setText(description);
                    time.setText(time1);
                    date.setText(date1);
                }
                else{
                    Toast.makeText(details.this,"Error! "+task.getException(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}