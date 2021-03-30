package com.example.t_t;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Add_category extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    ImageView image;
    TextView url,title;
    Button imageBtn,SaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        image = findViewById(R.id.category_image);
        url = findViewById(R.id.editTextTextPersonName10);
        title = findViewById(R.id.editTextTextPersonName7);
        imageBtn = findViewById(R.id.button3);
        SaveBtn = findViewById(R.id.save_button);
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUrl = url.getText().toString();
                imageUrl.replace("http","https");
                String name = title.getText().toString().trim();
                FirebaseUser firebaseUser = fAuth.getCurrentUser();
                String uid = firebaseUser.getUid();
                DocumentReference documentReference = db.collection("users").document(uid).collection("Categories").document(name);
                Map<String,Object> category = new HashMap<>();
                category.put("Name",name);
                category.put("ImageUrl",imageUrl);
                documentReference.set(category).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Add_category.this,"Success",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Add_category.this,DashboardMainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_category.this,"Error ! couldn't add  "+name+"."+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUrl = url.getText().toString();
                Picasso.get().load(imageUrl).into(image);
            }
        });
    }
}